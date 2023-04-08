package com.opriscan.isw2projects.isw2datasetcreator.common_classes;

public class Couple <T, Q> {

    private T first ;
    private Q second ;

    public Couple(T first, Q second) {
        this.first = first ;
        this.second = second ;
    }

    public T getFirst() {
        return first;
    }

    public void setFirst(T first) {
        this.first = first;
    }

    public Q getSecond() {
        return second;
    }

    public void setSecond(Q second) {
        this.second = second;
    }
}
