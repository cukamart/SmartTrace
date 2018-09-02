package com.cukamartin.microservicestracing.model;

import java.util.ArrayList;
import java.util.List;

public class TraceGraph {
    private String name;
    private String parent;
    private List<TraceGraph> children;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public List<TraceGraph> getChildren() {
        if (children == null) {
            children = new ArrayList<>();
        }
        return children;
    }

    public void setChildren(List<TraceGraph> children) {
        this.children = children;
    }
}
