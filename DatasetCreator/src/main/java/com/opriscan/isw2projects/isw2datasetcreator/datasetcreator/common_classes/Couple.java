package com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.common_classes;

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

    @Override
    public int hashCode() {
        return first.toString().concat(second.toString()).hashCode() ;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj.getClass() != this.getClass()) return false ;

        Couple<T, Q> cast = (Couple<T, Q>) obj ;

        return this.first.equals(cast.first) && this.second.equals(cast.second);
    }
}
