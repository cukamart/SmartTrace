package com.cukamartin.microservicestracing.model;

import java.util.ArrayList;
import java.util.List;

public class JsonTrace {
    private String dependencyGraph;
    private List<Node> nodes = new ArrayList<>();
    private List<Edge> edges = new ArrayList<>();

    public String getDependencyGraph() {
        return dependencyGraph;
    }

    public void setDependencyGraph(String dependencyGraph) {
        this.dependencyGraph = dependencyGraph;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }
}
