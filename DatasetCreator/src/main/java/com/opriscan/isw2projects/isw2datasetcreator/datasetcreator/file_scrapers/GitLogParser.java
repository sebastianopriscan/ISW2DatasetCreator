package com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.file_scrapers;

import com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.exceptions.LogParsingException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GitLogParser {

    private GitLogParser() {}

    public static List<GitCommit> parseCommitLog(String log, String regexp) throws LogParsingException{

        List<GitCommit> retVal = new ArrayList<>() ;

        BufferedReader reader = new BufferedReader(new StringReader(log)) ;

        String commitID ;
        String line ;
        String message = "";

        do {
            try {
                line = reader.readLine() ;
                if (line == null) return retVal;

                commitID = line ;

                line = reader.readLine() ;
                if (line == null) return retVal;

                while (!Objects.equals(line, "--"))
                {
                    message = message.concat(line) ;
                    line = reader.readLine() ;
                    if (line == null) break ;
                }

                if (message.matches(regexp)) {
                    GitCommit commit = new GitCommit() ;
                    commit.setCommitID(commitID);
                    commit.setMessage(message);
                    retVal.add(commit) ;
                }

                message = "" ;
            }
            catch (IOException e) {
                throw new LogParsingException("I/O error in parsing log") ;
            }

        } while (true) ;

    }

    public static List<String> getLogChunks(String log, String logDelimiter, String lineDelimiter) throws LogParsingException {

        List<String> retVal = new ArrayList<>() ;

        BufferedReader reader = new BufferedReader(new StringReader(log)) ;

        String line ;
        String chunk = "" ;

        do {
            try {
                line = reader.readLine() ;
                if (line == null) return retVal;

                while (!Objects.equals(line, logDelimiter))
                {
                    chunk = chunk.concat(lineDelimiter.concat(line)) ;
                    line = reader.readLine() ;
                    if (line == null) {
                        retVal.add(chunk) ;
                        break ;
                    }
                }

                retVal.add(chunk.substring(logDelimiter.length())) ;

                chunk = "" ;
            }
            catch (IOException e) {
                throw new LogParsingException("I/O error in parsing log") ;
            }

        } while (true) ;

    }
}
