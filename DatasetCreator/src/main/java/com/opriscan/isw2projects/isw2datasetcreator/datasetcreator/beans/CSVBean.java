package com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.beans;

import java.util.List;

public class CSVBean {

    private String name ;

    private List<List<String>> payload ;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<List<String>> getPayload() {
        return payload;
    }

    public void setPayload(List<List<String>> payload) {
        this.payload = payload;
    }
}
