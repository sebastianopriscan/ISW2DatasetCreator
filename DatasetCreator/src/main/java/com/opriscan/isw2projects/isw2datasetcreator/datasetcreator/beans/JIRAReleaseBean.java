package com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.beans;

public class JIRAReleaseBean {

    private String name ;

    private String start ;

    private String end ;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj.getClass() != this.getClass()) {
            return false ;
        }

        JIRAReleaseBean cast = (JIRAReleaseBean) obj ;

        boolean condition1 = this.name.equals(cast.name) ;
        boolean condition2 = this.start.equals(cast.start) ;
        boolean condition3 = this.end.equals(cast.end) ;

        return condition1 && condition2 && condition3 ;
    }
}
