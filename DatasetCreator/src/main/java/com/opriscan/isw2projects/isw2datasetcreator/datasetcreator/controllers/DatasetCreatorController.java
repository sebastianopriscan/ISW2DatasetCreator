package com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.controllers;

import com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.beans.CSVBean;
import com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.beans.JIRAReleaseBean;
import com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.beans.ProjectBean;
import com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.beans.ProjectsBean;
import com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.boundaries.OutputCSVBoundary;
import com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.entities.JIRARelease;
import com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.entities.MasterQueryAnalyser;
import com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.entities.Project;
import com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.exceptions.AnalyserNotFoundException;
import com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.exceptions.CSVCreationException;
import com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.exceptions.CacheException;
import com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.exceptions.VersionClassExtractionException;
import com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.file_scrapers.CacheManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DatasetCreatorController {

    public static DatasetCreatorController buildController() {
        return new DatasetCreatorController() ;
    }

    public void startAnalysis(ProjectsBean projects) throws VersionClassExtractionException, AnalyserNotFoundException, CSVCreationException, CacheException {

        for (ProjectBean bean : projects.getBeans()) {
            Project project = new Project() ;

            project.setGitURL(bean.getGitURL());
            project.setBranchName(bean.getBranchName());
            project.setColumns(bean.getColumns());

            List<JIRARelease> releases = new ArrayList<>() ;

            for(JIRAReleaseBean releaseBean : bean.getReleases()) {
                JIRARelease release = new JIRARelease() ;
                release.setName(releaseBean.getName());
                release.setStart(LocalDate.parse(releaseBean.getStart(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                release.setEnd(LocalDate.parse(releaseBean.getEnd(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));

                releases.add(release) ;
            }

            project.setReleases(releases) ;

            MasterQueryAnalyser analyser = new MasterQueryAnalyser() ;

            CSVBean output = new CSVBean() ;
            output.setPayload(analyser.analyseOutput(project));
            output.setName(CacheManager.getInstance().extractRepoName(project.getGitURL()));

            new OutputCSVBoundary().generateCSV(output) ;
        }
    }
}
