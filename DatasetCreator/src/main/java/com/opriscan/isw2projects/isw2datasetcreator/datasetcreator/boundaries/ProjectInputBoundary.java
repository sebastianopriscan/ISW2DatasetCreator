package com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.boundaries;

import com.google.gson.Gson;
import com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.beans.ProjectsBean;
import com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.controllers.DatasetCreatorController;
import com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.exceptions.AnalyserNotFoundException;
import com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.exceptions.CSVCreationException;
import com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.exceptions.CacheException;
import com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.exceptions.VersionClassExtractionException;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProjectInputBoundary {

    private static final Logger LOGGER = Logger.getLogger(ProjectInputBoundary.class.getSimpleName()) ;

    private String readInput(String inputFile) throws IOException {
        try (InputStream in = new BufferedInputStream(new FileInputStream(inputFile))){

            return new String(in.readAllBytes()) ;
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Error in opening file");
            throw e ;
        }
    }

    public void getProjectsData(String inputFile) {

        try {
            String json = readInput(inputFile) ;
            Gson gson = new Gson() ;

            ProjectsBean projects = gson.fromJson(json, ProjectsBean.class) ;

            DatasetCreatorController.buildController().startAnalysis(projects) ;

        } catch (IOException ignored) {

        } catch (VersionClassExtractionException e) {
            LOGGER.log(Level.SEVERE, "Unable to extract the classes' column : \n" + e.getMessage()) ;
        } catch (AnalyserNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Analyser requested in input not found") ;
        } catch (CSVCreationException e) {
            LOGGER.log(Level.SEVERE, "Unable to generate output CSV : \n" + e.getMessage()) ;
        } catch (CacheException e) {
            LOGGER.log(Level.SEVERE, "Error in converting project url to project name") ;
        }
    }
}
