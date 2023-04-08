package com.opriscan.isw2projects.isw2datasetcreator.tests;

import com.opriscan.isw2projects.isw2datasetcreator.exceptions.CloningException;
import com.opriscan.isw2projects.isw2datasetcreator.file_scrapers.GitCloner;
import com.opriscan.isw2projects.isw2datasetcreator.tests.test_exceptions.FileDeletionException;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.fail;

public class GitCloneTest {

    private static final String CACHE_PATH = "./src/main/resources/.cache" ;

    private static final Logger LOGGER = Logger.getLogger("com.opriscan.isw2projects.isw2datasetcreator.tests.GitCloneTest") ;

    private static void deleteDirectory(File file) throws FileDeletionException {
        if(file.isDirectory())
        {
            File[] subDirs = file.listFiles() ;

            if (subDirs == null) throw new FileDeletionException() ;

            for(File cacheFile : subDirs)
            {
                deleteDirectory(cacheFile);
            }
        }

        if(!file.delete()) throw new FileDeletionException() ;
    }

    @BeforeClass
    public static void cleanCache() {
        File file = new File(CACHE_PATH) ;

        if(file.exists())
        {
            try {
                File[] dirs = file.listFiles() ;
                if(dirs == null) throw new FileDeletionException() ;
                for (File dir : dirs)
                {
                    deleteDirectory(dir);
                }

            } catch (FileDeletionException e)
            {
                LOGGER.log(Level.SEVERE, "Unable to delete cache recursively, check file permission and do it manually");
                fail();
            }
        } else {
            if(!file.mkdir()) fail() ;
        }

    }

    @Test
    public void testCloning() {
        try {
            GitCloner cloner = new GitCloner() ;
            cloner.cloneRepository("git@github.com:sebastianopriscan/Test28032023.git");

            File presenceFile = new File(CACHE_PATH + "/Test28032023") ;

            File[] files = presenceFile.listFiles() ;

            if (files == null) fail();

            boolean condition = false ;

            for (File file : files) {
                if(file.getName().equals("pom.xml")) condition = true ;
            }

            if(!condition) fail();

        } catch (CloningException e)
        {
            fail() ;
        }
    }
}
