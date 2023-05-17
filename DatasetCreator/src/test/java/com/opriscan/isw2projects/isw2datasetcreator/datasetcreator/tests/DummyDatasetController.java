package com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.tests;

import com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.beans.ProjectsBean;
import com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.controllers.DatasetCreatorController;

public class DummyDatasetController extends DatasetCreatorController {

    private ProjectsBean projects ;

    @Override
    public void startAnalysis(ProjectsBean project) {
        this.projects = project ;
    }

    public ProjectsBean getProjects() {
        return projects;
    }
}
