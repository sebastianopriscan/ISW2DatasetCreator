package com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.entities;

import com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.common_classes.Couple;
import com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.exceptions.AnalyserNotFoundException;
import com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.exceptions.VersionClassExtractionException;

import java.util.ArrayList;
import java.util.List;

public class MasterQueryAnalyser {

    private final List<AttributeAnalyser> singleCommitAnalysers = new ArrayList<>() ;

    private final List<AttributeAnalyser> intraReleaseAnalysers = new ArrayList<>() ;

    private final List<AttributeAnalyser> interReleaseAnalysers = new ArrayList<>() ;

    public List<List<String>> analyseOutput(Project project) throws AnalyserNotFoundException, VersionClassExtractionException
    {
        List<List<String>> result = new ArrayList<>() ;

        List<Couple<String, String>> couples = new VersionClassExtractor().getClassesVersionCouples(project.getGitURL(), project.getBranchName(), project.getReleases()) ;

        List<String> versions = new ArrayList<>() ;
        List<String> names = new ArrayList<>() ;

        for (Couple<String, String> couple : couples) {
            versions.add(couple.getFirst()) ;
            names.add(couple.getSecond()) ;
        }

        result.add(versions) ;
        result.add(names) ;

        for(String attribute : project.getColumns())
        {
            AttributeAnalyser analyser = null;

            switch(attribute) { //Various Analyser implementations for the columns to evaluate
                case "Name" :
                    break;
                default:
                    throw new AnalyserNotFoundException("One of the analysers submitted has not been found in the classes") ;
            }

            result.add(analyser.extractAnalysis(project, couples)) ;
        }

        return result ;

    }
}
