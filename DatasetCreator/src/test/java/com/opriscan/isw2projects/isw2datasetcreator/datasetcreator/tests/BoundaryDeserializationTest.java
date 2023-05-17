package com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.tests;

import com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.beans.JIRAReleaseBean;
import com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.beans.ProjectBean;
import com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.beans.ProjectsBean;
import com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.boundaries.ProjectInputBoundary;
import com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.controllers.DatasetCreatorController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mockStatic;

@RunWith(MockitoJUnitRunner.class)
public class BoundaryDeserializationTest {

    private final ProjectsBean projects ;

    public BoundaryDeserializationTest() {
        ProjectBean project1 = new ProjectBean() ;

        project1.setBranchName("main");
        project1.setGitURL("github");
        project1.setColumns(List.of("Pippo", "Baudo"));

        JIRAReleaseBean oneDotZero = new JIRAReleaseBean() ;
        oneDotZero.setName("1.0") ;
        oneDotZero.setStart("2023-05-15");
        oneDotZero.setEnd("2023-05-17");

        project1.setReleases(List.of(oneDotZero));

        projects = new ProjectsBean() ;

        projects.setBeans(List.of(project1)) ;
    }

    @Test
    public void testInputDeserialization() {

        try (MockedStatic<DatasetCreatorController> mocked = mockStatic(DatasetCreatorController.class)) {
            DummyDatasetController myController = new DummyDatasetController() ;
            mocked.when(DatasetCreatorController::buildController).thenReturn(myController) ;

            new ProjectInputBoundary().getProjectsData("./src/test/resources/testInput.json") ;

            assertEquals(projects, myController.getProjects()) ;
        }
    }
}
