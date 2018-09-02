package com.cukamartin.microservicestracing.controller;

import com.cukamartin.microservicestracing.model.JsonTrace;
import com.cukamartin.microservicestracing.model.Trace;
import com.cukamartin.microservicestracing.model.TraceGroup;
import com.cukamartin.microservicestracing.model.enums.SortChosen;
import com.cukamartin.microservicestracing.service.api.TraceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/trace")
public class TraceController {

    @Autowired
    private TraceService traceService;

    @GetMapping("/all")
    public Iterable<Trace> getAllTraces() {
        return traceService.findAll();
    }

    @PostMapping
    public Trace insertTrace(@RequestBody Trace trace) {
        return traceService.save(trace);
    }

    @GetMapping("/uri")
    public List<TraceGroup> getTracesCorrelation(@RequestParam String service, @RequestParam String sort, @RequestParam Long from) {
        return traceService.findCorrelationGroups(service.toLowerCase(), SortChosen.valueOf(sort.toUpperCase()), from);
    }

    @GetMapping("/{correlationId:.+}")
    public JsonTrace getTracesByCorrelationId(@PathVariable String correlationId) {
        return traceService.findByCorrelationId(correlationId);
    }

    @PostMapping("/sort")
    public List<TraceGroup> sortTraceGroups(@RequestBody List<TraceGroup> traceGroups, @RequestParam String sort) {
        traceService.sortTraceGroup(traceGroups, SortChosen.valueOf(sort.toUpperCase()));
        return traceGroups;
    }

    @ExceptionHandler(IllegalStateException.class)
    public void handleIllegalStateException(Exception ex, HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.getOutputStream().write(ex.getMessage().getBytes());
    }
}
