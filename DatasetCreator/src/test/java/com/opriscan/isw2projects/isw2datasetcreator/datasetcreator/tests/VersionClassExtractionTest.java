package com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.tests;

import com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.common_classes.Couple;
import com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.entities.JIRARelease;
import com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.entities.VersionClassExtractor;
import com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.exceptions.CacheException;
import com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.exceptions.CloningException;
import com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.exceptions.VersionClassExtractionException;
import com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.file_scrapers.CacheManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.time.LocalDate;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(Parameterized.class)
public class VersionClassExtractionTest {

    private final List<JIRARelease> releases ;

    private final Set<Couple<String, String>> expected ;

    private final String URL ;

    @Parameterized.Parameters
    public static Collection<Object[]> getTestParameters() {

        JIRARelease release1 = new JIRARelease() ;
        release1.setName("1.0") ;
        release1.setStart(LocalDate.of(2021, 10, 13));
        release1.setEnd(LocalDate.of(2023,4,30));

        JIRARelease release2 = new JIRARelease() ;
        release2.setName("2.0");
        release2.setStart(LocalDate.of(2023, 5,1));
        release2.setEnd(LocalDate.of(2023, 5, 11));

        List<JIRARelease> releaseList = List.of(release1, release2) ;

        Couple<String,String> entry1 = new Couple<>("1.0", "TestRepo/Hello.java") ;
        Couple<String,String> entry2 = new Couple<>("1.0", "TestRepo/Calculator/src/main/java/com/example/calculator/Calculator.java") ;
        Couple<String,String> entry3 = new Couple<>("1.0", "TestRepo/Calculator/src/main/java/com/example/calculator/HelloController.java") ;
        Couple<String,String> entry4 = new Couple<>("1.0", "TestRepo/Calculator/src/main/java/module-info.java") ;

        Couple<String,String> entry5 = new Couple<>("2.0", "TestRepo/Hello.java") ;
        Couple<String,String> entry6 = new Couple<>("2.0", "TestRepo/Calculator/src/main/java/com/example/calculator/Calculator.java") ;
        Couple<String,String> entry7 = new Couple<>("2.0", "TestRepo/Calculator/src/main/java/com/example/calculator/HelloController.java") ;
        Couple<String,String> entry8 = new Couple<>("2.0", "TestRepo/FolderB/Hi.java") ;
        Couple<String,String> entry9 = new Couple<>("2.0", "TestRepo/Calculator/src/main/java/module-info.java") ;

        Set<Couple<String, String>> set = new HashSet<>() ;
        set.add(entry1) ;
        set.add(entry2) ;
        set.add(entry3) ;
        set.add(entry4) ;
        set.add(entry5) ;
        set.add(entry6) ;
        set.add(entry7) ;
        set.add(entry8) ;
        set.add(entry9) ;

        Object[] elements = new Object[3] ;
        elements[0] = releaseList ;
        elements[1] = set ;
        elements[2] = "https://github.com/sebastianopriscan/TestRepo.git" ;

        List<Object[]> retVal = new ArrayList<>() ;
        retVal.add(elements) ;

        return retVal ;
    }

    public VersionClassExtractionTest(List<JIRARelease> releases, Set<Couple<String, String>> expected, String URL) {
        this.releases = releases ;
        this.expected = expected ;
        this.URL = URL ;
    }

    @Before
    public void prepEnv() {
        try {
            CacheManager.getInstance().cleanCache();
            CacheManager.getInstance().cloneRepository(URL) ;
        } catch (CacheException | CloningException e) {
            fail() ;
        }
    }

    @Test
    public void testExtraction() {
        VersionClassExtractor extractor = new VersionClassExtractor() ;

        try {
            List<Couple<String, String>> couples = extractor.getClassesVersionCouples(URL, "main", releases) ;
            Set<Couple<String, String>> coupleSet = new HashSet<>(couples);

            assertEquals(expected, coupleSet) ;

        } catch (VersionClassExtractionException e) {
            fail() ;
        }

    }
}
