package com.cukamartin.microservicestracing.model.enums;

public enum SortChosen {
    LONGEST("longest"), SHORTEST("shortest"), NEWEST("newest"), OLDEST("oldest");

    private final String value;

    SortChosen(String value) {
        this.value = value;
    }
}
