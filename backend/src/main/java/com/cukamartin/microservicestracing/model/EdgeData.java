package com.cukamartin.microservicestracing.model;

public class EdgeData {
    private String source;
    private String target;
    private String label;
    private String faveColor;

    public EdgeData(String source, String target, String label, String faveColor) {
        this.source = source;
        this.target = target;
        this.label = label;
        this.faveColor = faveColor;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getFaveColor() {
        return faveColor;
    }

    public void setFaveColor(String faveColor) {
        this.faveColor = faveColor;
    }
}
