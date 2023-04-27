package com.opriscan.isw2projects.isw2datasetcreator.ticketdistributionbranches.ticketanalyser;

import com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.beans.RESTQuery;
import com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.beans.RESTResult;
import com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.boundaries.RESTBoundary;
import com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.exceptions.CacheException;
import com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.exceptions.CloningException;
import com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.exceptions.LogParsingException;
import com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.exceptions.RestCallException;
import com.opriscan.isw2projects.isw2datasetcreator.ticketdistributionbranches.boundaries.TicketAnalysisPrinter;
import com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.file_scrapers.CacheManager;
import com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.file_scrapers.GitCommit;
import com.opriscan.isw2projects.isw2datasetcreator.ticketdistributionbranches.exceptions.TicketFormatterException;
import org.json.JSONObject;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TicketAnalyser {

    private static final Logger LOGGER = Logger.getLogger(TicketAnalyser.class.getName()) ;

    public void analyseTickets(String jiraURL, String githubUrl) {

        try {

            Map<String, Set<String>> map = new HashMap<>() ;

            CacheManager manager = CacheManager.getInstance() ;

            RESTQuery jiraQuery = new RESTQuery() ;
            jiraQuery.setQuery(jiraURL);

            RESTResult jiraResult = new RESTBoundary().restCall(jiraQuery) ;

            List<String> ticketNames = new TicketFormatter().getBugTicketNames(new JSONObject(jiraResult.getResult())) ;

            for(String ticket : ticketNames) {
                Set<String> branches = new HashSet<>() ;
                List<GitCommit> commits = manager.findCommitsContaining(githubUrl, ".{0,100000}"+ticket+".{0,100000}") ;
                for (GitCommit commit : commits) {

                    branches.addAll(manager.findCommitsBranches(githubUrl, commit.getCommitID())) ;
                }
                map.put(ticket, branches) ;
            }

            TicketAnalysisPrinter.printResults(map);

        } catch (CacheException | CloningException | LogParsingException | RestCallException |
                 TicketFormatterException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
            System.exit(1) ;
        }

    }
}
