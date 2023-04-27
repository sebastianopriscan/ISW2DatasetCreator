package com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.entities;

import com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.exceptions.AnalyserNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class MasterQueryAnalyser {

    public List<List<String>> analyseOutput(String projectName, List<String> passedAttributes) throws AnalyserNotFoundException
    {
        List<List<String>> result = new ArrayList<>() ;

        for(String attribute : passedAttributes)
        {
            AttributeAnalyser analyser = null;

            switch(attribute) { //Various Analyser implementations for the columns to evaluate
                case "Name" :
                    break;
                default:
                    throw new AnalyserNotFoundException("One of the analysers submitted has not been found in the classes") ;
            }

            result.add(analyser.extractAnalysis(projectName)) ;
        }

        return result ;
    }
}
