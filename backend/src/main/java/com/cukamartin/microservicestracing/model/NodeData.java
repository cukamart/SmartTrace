package com.cukamartin.microservicestracing.model;

public class NodeData {
    private String id;
    private String name;
    private String faveColor;
    private String faveShape;

    public NodeData(String id, String name, String faveColor, String faveShape) {
        this.id = id;
        this.name = name;
        this.faveColor = faveColor;
        this.faveShape = faveShape;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFaveColor() {
        return faveColor;
    }

    public void setFaveColor(String faveColor) {
        this.faveColor = faveColor;
    }

    public String getFaveShape() {
        return faveShape;
    }

    public void setFaveShape(String faveShape) {
        this.faveShape = faveShape;
    }
}
