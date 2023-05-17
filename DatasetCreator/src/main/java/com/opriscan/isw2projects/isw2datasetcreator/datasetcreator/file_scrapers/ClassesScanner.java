package com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.file_scrapers;

import com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.exceptions.ExtractionException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ClassesScanner {

    private static final String CACHE_PATH = "./src/main/resources/.cache/" ;

    public List<String> extractClasses(String projectName) throws ExtractionException {

        List<String> retVal = new ArrayList<>() ;

        File head = new File(CACHE_PATH + projectName) ;

        if(!head.exists()) throw new ExtractionException("Directory not found in .cache") ;

        exploreFiles(retVal, head);

        return retVal ;
    }

    private void exploreFiles(List<String> list, File file) {
        if (!file.isDirectory()) {
            if(file.getName().endsWith(".java") &&
                    !file.getName().toLowerCase().contains("test")) {
                String pathName = file.getPath();

                list.add(pathName.substring(CACHE_PATH.length())) ;
            }

            return ;
        }

        for (File subFile : Objects.requireNonNull(file.listFiles())) {
            exploreFiles(list, subFile) ;
        }
    }
}
