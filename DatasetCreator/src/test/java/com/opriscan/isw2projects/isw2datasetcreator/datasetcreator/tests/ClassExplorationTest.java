package com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.tests;

import com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.exceptions.CacheException;
import com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.exceptions.CloningException;
import com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.exceptions.ExtractionException;
import com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.file_scrapers.CacheManager;
import com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.file_scrapers.ClassesScanner;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ClassExplorationTest {

    @Before
    public void cleanCache() {
        try {
            CacheManager.getInstance().cleanCache();
            CacheManager.getInstance().cloneRepository("https://github.com/sebastianopriscan/Test28032023.git") ;
        } catch (CacheException | CloningException e) {
            fail() ;
        }
    }

    @Test
    public void testExploration() {
        List<String> files = new ArrayList<>() ;
        files.add("App.java") ;

        List<String> callResult = null;

        try {
            callResult = new ClassesScanner().extractClasses("Test28032023") ;
        } catch (ExtractionException e) {
            fail();
        }

        assertEquals(files, callResult);
    }
}
