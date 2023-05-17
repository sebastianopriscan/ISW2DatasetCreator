package com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.tests;

import com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.boundaries.ProjectInputBoundary;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import static org.junit.Assert.assertTrue;

public class TestCSVCreation {

    @Test
    public void testCSVCreation() {

        new ProjectInputBoundary().getProjectsData("./src/test/resources/finalTestInput.json") ;

        assertTrue(confrontCSVs("./src/main/resources/TestRepo.csv", "./src/test/resources/TestRepoExpected.csv")) ;
    }

    public boolean confrontCSVs(String path1, String path2) {
        try (InputStream in1 = new FileInputStream(path1) ; InputStream in2 = new FileInputStream(path2)){

            String csv1 = new String(in1.readAllBytes()) ;
            String csv2 = new String(in2.readAllBytes()) ;

            Set<String> csvSet1 = Set.of(csv1.split("\n")) ;
            Set<String> csvSet2 = Set.of(csv2.split("\n")) ;

            return csvSet1.equals(csvSet2) ;

        } catch (IOException e) {
            return false ;
        }
    }

}
