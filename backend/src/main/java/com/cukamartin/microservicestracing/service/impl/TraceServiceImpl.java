package com.cukamartin.microservicestracing.service.impl;

import com.cukamartin.microservicestracing.model.*;
import com.cukamartin.microservicestracing.model.enums.InvocationDirection;
import com.cukamartin.microservicestracing.model.enums.SortChosen;
import com.cukamartin.microservicestracing.repository.TraceRepository;
import com.cukamartin.microservicestracing.service.api.TraceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class TraceServiceImpl implements TraceService {

    private static final Logger logger = LoggerFactory.getLogger(TraceServiceImpl.class);

    private static final String MW = "mw";
    private static final String URI = "uri";
    private static final String START_DATE = "startDate";
    private static final int PAGE_SIZE = 10000;
    private static final int LIMIT = 12;

    @Autowired
    private TraceRepository traceRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Trace save(Trace trace) {
        return traceRepository.save(trace);
    }

    @Override
    public void delete(Trace trace) {
        traceRepository.delete(trace);
    }

    @Override
    public Trace findOne(String id) {
        return traceRepository.findById(id).get();
    }

    @Override
    public Iterable<Trace> findAll() {
        return traceRepository.findAll();
    }

    @Override
    public JsonTrace findByCorrelationId(String correlationId) {
        List<Trace> traces = traceRepository.findByCorrelationIdOrderByStartDateAsc(correlationId, PageRequest.of(0, PAGE_SIZE)).getContent();

        if (traces.isEmpty()) {
            throw new IllegalStateException("No trace found for correlationId: " + correlationId); // todo prerobit vynimky aby sa spravne zobrazovali na FE...
        }

        JsonTrace jsonTrace = createModuleGraph(traces);
        String dependencyGraph = createDependencyGraph(traces);
        jsonTrace.setDependencyGraph(dependencyGraph);

        return jsonTrace;
    }

    @Override
    public List<TraceGroup> findCorrelationGroups(String uri, SortChosen sort, Long from) {
        validateFindByUriContainingParams(uri);

        SearchQuery searchQuery = createSearchQuery(uri, from, new Date().getTime());
        logger.info("Searching for traces containing {}", uri);
        List<Trace> traces = elasticsearchTemplate.queryForList(searchQuery, Trace.class);

        List<TraceGroup> traceGroups = new ArrayList<>();
        Map<String, String> correlations = groupTracesByCorrelationId(traces);

        logger.info("Grouping {} traces by correlation id", traces.size());
        for (Map.Entry<String, String> correlationMap : correlations.entrySet()) {
            traces = traceRepository.findByCorrelationIdOrderByStartDateAsc(correlationMap.getKey(), PageRequest.of(0, PAGE_SIZE)).getContent();
            traceGroups.add(buildTraceGroup(traces, correlationMap.getValue()));
        }

        logger.info("Traces merge to {} tracesGroup", traceGroups.size());
        sortTraceGroup(traceGroups, sort);
        return traceGroups;
    }

    private String createDependencyGraph(List<Trace> traces) {
        TraceGraph tg = new TraceGraph();
        Map<Integer, List<TraceGraph>> childrenMap = new HashMap<>();
        List<TraceGraph> children = new ArrayList<>();
        int depth = 0;
        String lastInbound = null;

        for (int i = 0; i < traces.size(); i++) {
            Trace trace = traces.get(i);

            if (i == 0) {
                lastInbound = traces.get(0).getUri();
                tg.setName(trace.getUri());
                continue;
            }

            TraceGraph child = new TraceGraph();
            child.setParent(lastInbound);
            child.setName(trace.getUri());
            children.add(child);

            if (trace.getDirection().equals(InvocationDirection.INBOUND)) {
                childrenMap.put(depth, children);
                children = new ArrayList<>();
                lastInbound = trace.getUri();
                depth++;
            }

        }

        childrenMap.put(depth, children);

        for (int i = depth; i > 0; i--) {
            List<TraceGraph> currentDepth = childrenMap.get(i);
            List<TraceGraph> previousDepth = childrenMap.get(i - 1);
            previousDepth.get(previousDepth.size() - 1).setChildren(currentDepth);
            childrenMap.put(i - 1, previousDepth);
        }

        tg.setChildren(childrenMap.get(0));

        try {
            return objectMapper.writeValueAsString(tg);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

    private JsonTrace createModuleGraph(List<Trace> traces) {
        List<Node> nodes = new ArrayList<>();
        List<Edge> edges = new ArrayList<>();
        Set<String> nodeSet = new HashSet<>();
        Node lastInbound = null;
        Map<String, Integer> edgeMap = new HashMap<>();

        int index = 0;
        for (Trace trace : traces) {
            String nodeId = determineNodeId(trace);

            // if it's new unique NODE add it to Set
            if (nodeSet.add(nodeId)) {
                Node node = createNode(nodeId);
                nodes.add(node);
            }

            // if it's first node automatically set it as INBOUND node and go to next trace
            if (index == 0) {
                index++;
                lastInbound = createNode(nodeId);
                continue;
            }

            // if there's more traces than LIMIT mark edges with number otherwise don't use label and show all edges
            if (traces.size() <= LIMIT) {
                Edge edge = createEdge(lastInbound.getData().getId(), nodeId, "");
                edges.add(edge);
            } else {
                String key = lastInbound.getData().getId() + "_" + nodeId; // create key e.g. smart_esig2 (source_target)
                edgeMap.put(key, edgeMap.containsKey(key) ? edgeMap.get(key) + 1 : 1); // compute how many calls are from the same (source -> target)
            }

            // if current node is INBOUND set it as INBOUND node for next iteration
            if (trace.getDirection().equals(InvocationDirection.INBOUND)) {
                lastInbound = createNode(nodeId);
            }
        }

        // if graph is too large create edges with labels how many call are the same (source -> target)
        if (traces.size() > LIMIT) {
            for (Map.Entry<String, Integer> entryMap : edgeMap.entrySet()) {
                String[] keys = entryMap.getKey().split("_");
                edges.add(createEdge(keys[0], keys[1], entryMap.getValue()+""));
            }
        }
        
        JsonTrace jsonTrace = new JsonTrace();
        jsonTrace.setEdges(edges);
        jsonTrace.setNodes(nodes);
        
        return jsonTrace;
    }

    private Edge createEdge(String source, String target, String label) {
        Edge edge = new Edge();
        EdgeData data = new EdgeData(source, target, label, randomColor());
        edge.setData(data);
        return edge;
    }

    private Node createNode(String nodeId) {
        Node node = new Node();
        NodeData data = new NodeData(nodeId, nodeId, randomColor(), "roundrectangle");
        node.setData(data);
        return node;
    }

    private String determineNodeId(Trace trace) {
        return (trace.getUri().contains(MW)) ? MW : trace.getNodeId().split("_")[0];
    }

    private String randomColor() {
        String letters = "0123456789ABCDEF";
        String color = "#";

        for (int i = 0; i < 6; i++) {
            color += letters.charAt((int) Math.floor(Math.random() * 16));
        }

        return color;
    }

    private TraceGroup buildTraceGroup(List<Trace> traces, String serviceName) {
        TraceGroup traceGroup = new TraceGroup();

        traceGroup.setCorrelationId(traces.get(0).getCorrelationId());
        traceGroup.setSpan(traces.size());
        traceGroup.setStartDate(traces.get(0).getStartDate());
        traceGroup.setTotalDuration(getTotalDuration(traces));
        traceGroup.setUri(serviceName);

        return traceGroup;
    }

    private long getTotalDuration(List<Trace> traces) {
        long totalDuration = 0;

        for (Trace trace : traces) {
            totalDuration += trace.getDuration();
        }
        return totalDuration;
    }

    private void validateFindByUriContainingParams(String uri) {
        if (StringUtils.isEmpty(uri)) {
            logger.error("Requested uri is null or empty !");
            throw new IllegalStateException("Requested uri can't be null nor empty.");
        }
    }

    private SearchQuery createSearchQuery(String uri, Long from, Long to) {
        List<String> tokens = Arrays.asList(uri.split("[^a-zA-Z0-9_\\.]+"));

        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        tokens.stream().forEach(token -> builder.must(QueryBuilders.termQuery(URI, token)));
        builder.must(QueryBuilders.rangeQuery(START_DATE).gte(from));
        builder.must(QueryBuilders.rangeQuery(START_DATE).lte(to));

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withFilter(builder)
                .withPageable(PageRequest.of(0, PAGE_SIZE))
                .build();

        return searchQuery;
    }

    private Map<String, String> groupTracesByCorrelationId(List<Trace> traces) {
        Map<String, String> correlation = new HashMap<>();

        traces.stream()
                .filter(trace -> !correlation.containsKey(trace.getCorrelationId()))
                .forEach(trace -> correlation.put(trace.getCorrelationId(), trace.getUri()));

        return correlation;
    }

    public void sortTraceGroup(List<TraceGroup> traces, SortChosen sort) {
        logger.info("sorting traces by {}", sort);
        Comparator<TraceGroup> durationComp = Comparator.comparing(TraceGroup::getTotalDuration);
        Comparator<TraceGroup> startDateComp = Comparator.comparing(TraceGroup::getStartDate);

        switch (sort) {
            case LONGEST:
                Collections.sort(traces, durationComp.reversed());
                break;
            case SHORTEST:
                Collections.sort(traces, durationComp);
                break;
            case NEWEST:
                Collections.sort(traces, startDateComp.reversed());
                break;
            case OLDEST:
                Collections.sort(traces, startDateComp);
                break;
        }
    }
}
