package com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.entities;

import com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.common_classes.Couple;
import com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.exceptions.*;
import com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.file_scrapers.CacheManager;
import com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.file_scrapers.ClassesScanner;
import com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.file_scrapers.GitLogParser;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

//Retrieves couples class-version
public class VersionClassExtractor {

    public List<Couple<String, String>> getClassesVersionCouples(String gitURL, String branchName, List<JIRARelease> releases) throws VersionClassExtractionException{

        Set<Couple<String, String>> coupleSet = new HashSet<>() ;

        List<String> logEntries ;

        try {
            CacheManager.getInstance().cloneRepository(gitURL) ;

            logEntries = GitLogParser.getLogChunks(CacheManager.getInstance().logRepo(gitURL, "%H%n%as%n--", branchName ), "--", "--");

            if (Objects.isNull(logEntries)) return new ArrayList<>() ;

            analyseReleases(releases, logEntries, gitURL, coupleSet) ;

            return new ArrayList<>(coupleSet) ;

        } catch (CacheException e) {
            throw new VersionClassExtractionException("Error in instantiating CacheManager") ;
        } catch (CloningException e) {
            throw new VersionClassExtractionException("Error in cloning repository " + gitURL) ;
        } catch (LogParsingException e) {
            throw new VersionClassExtractionException("Error in parsing log") ;
        } catch (ExtractionException e) {
            throw new VersionClassExtractionException("Error in extracting messages from directory.\nDetails: " + e.getMessage()) ;
        }
    }

    private void analyseReleases(List<JIRARelease> releases, List<String> logEntries, String gitURL, Set<Couple<String, String>> coupleSet) throws VersionClassExtractionException, CacheException, CloningException, ExtractionException {
        for (JIRARelease release : releases) {
            for (String chunk : logEntries) {
                String[] parts = chunk.split("--") ;

                if (parts.length != 2) throw new VersionClassExtractionException("Error in parsing log entries") ;

                LocalDate date = LocalDate.parse(parts[1], DateTimeFormatter.ofPattern("yyyy-MM-dd")) ;

                if(date.isBefore(release.getEnd()) && date.isAfter(release.getStart())) {
                    if(!CacheManager.getInstance().checkOutRepository(gitURL, parts[0])) throw new VersionClassExtractionException("Error in checking out repository; wrong commit hash") ;

                    List<String> classes = new ClassesScanner().extractClasses(CacheManager.getInstance().extractRepoName(gitURL)) ;

                    for (String singleClass : classes) {
                        coupleSet.add(new Couple<>(release.getName(), singleClass)) ;
                    }
                }
            }
        }
    }
}
