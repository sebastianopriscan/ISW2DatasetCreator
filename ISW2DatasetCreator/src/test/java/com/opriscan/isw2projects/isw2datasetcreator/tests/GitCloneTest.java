package com.opriscan.isw2projects.isw2datasetcreator.tests;

import com.opriscan.isw2projects.isw2datasetcreator.exceptions.CacheException;
import com.opriscan.isw2projects.isw2datasetcreator.exceptions.CloningException;
import com.opriscan.isw2projects.isw2datasetcreator.file_scrapers.CacheManager;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

public class GitCloneTest {

    private static final String CACHE_PATH = "./src/main/resources/.cache" ;

    @Before
    public void cleanCache() {
        try {
            CacheManager.getInstance().cleanCache();
        } catch (CacheException e) {
            fail() ;
        }

    }

    @Test
    public void testCloning() {
        try {
            CacheManager cloner = CacheManager.getInstance() ;
            cloner.cloneRepository("https://github.com/sebastianopriscan/Test28032023.git");

            File presenceFile = new File(CACHE_PATH + "/Test28032023") ;

            File[] files = presenceFile.listFiles() ;

            if (files == null) fail();

            boolean condition = false ;

            for (File file : files) {
                if(file.getName().equals("pom.xml")) condition = true ;
            }

            if(!condition) fail();

        } catch (CacheException | CloningException e)
        {
            fail() ;
        }
    }

    @Test
    public void testCacheHits() {
        try {
            CacheManager cloner = CacheManager.getInstance() ;
            boolean result = cloner.cloneRepository("https://github.com/sebastianopriscan/Test28032023.git");

            if(!result) fail();

            result = cloner.cloneRepository("https://github.com/sebastianopriscan/Test28032023.git");

            assertFalse(result);

        } catch (CacheException | CloningException e)
        {
            fail() ;
        }
    }
}
