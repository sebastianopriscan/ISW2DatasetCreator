package com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.entities;

import java.time.LocalDate;

public class JIRARelease {

    private String name ;

    private LocalDate start ;

    private LocalDate end ;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }
}
