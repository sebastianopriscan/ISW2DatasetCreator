package com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.entities;

import com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.common_classes.Couple;

import java.util.List;

public interface AttributeAnalyser {
    List<String> extractAnalysis(Project project, List<Couple<String, String>> files) ;
}
