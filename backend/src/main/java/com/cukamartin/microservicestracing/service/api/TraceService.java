package com.cukamartin.microservicestracing.service.api;

import com.cukamartin.microservicestracing.model.JsonTrace;
import com.cukamartin.microservicestracing.model.Trace;
import com.cukamartin.microservicestracing.model.TraceGroup;
import com.cukamartin.microservicestracing.model.enums.SortChosen;

import java.util.List;

public interface TraceService {

    Trace save(Trace trace);

    void delete(Trace trace);

    Trace findOne(String id);

    Iterable<Trace> findAll();

    JsonTrace findByCorrelationId(String correlationId);

    List<TraceGroup> findCorrelationGroups(String uri, SortChosen sort, Long from);

    void sortTraceGroup(List<TraceGroup> traces, SortChosen sort);
}
