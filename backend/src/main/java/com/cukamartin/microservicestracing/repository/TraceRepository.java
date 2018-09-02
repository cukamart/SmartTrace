package com.cukamartin.microservicestracing.repository;

import com.cukamartin.microservicestracing.model.Trace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface TraceRepository extends ElasticsearchRepository<Trace, String> {

    Page<Trace> findByCorrelationIdOrderByStartDateAsc(String correlationId, Pageable pageable);
    List<Trace> findByUriContaining(String uri);
}
