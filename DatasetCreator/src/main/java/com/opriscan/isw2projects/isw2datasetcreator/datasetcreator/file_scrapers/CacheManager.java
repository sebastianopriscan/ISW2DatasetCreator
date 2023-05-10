package com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.file_scrapers;

import com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.exceptions.CacheException;
import com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.exceptions.CloningException;
import com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.exceptions.FileDeletionException;
import com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.exceptions.LogParsingException;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

public class CacheManager {

    private static final String CACHE_PATH = "./src/main/resources/.cache" ;

    private final Map<String, String> entries = new HashMap<>() ;

    private static CacheManager instance ;

    private String[] prepareCommands() {
        String[] commands = new String[3] ;

        if (System.getProperty("os.name").toLowerCase().contains("windows"))
        {
            commands[0] = "cmd.exe" ;
            commands[1] = "/c" ;
        } else {
            commands[0] = "/bin/bash" ;
            commands[1] = "-c" ;
        }

        return commands ;
    }

    public static synchronized CacheManager getInstance() throws CacheException {
        try {
            if(instance == null) {
                instance = new CacheManager() ;
            }

            return instance ;

        } catch (CloningException e) {
            throw new CacheException(e.getMessage()) ;
        }
    }

    private CacheManager() throws CloningException{
        File cache = new File(CACHE_PATH) ;

        if(!cache.exists() && !cache.mkdir()) {
            throw new CloningException("Unable to create .cache directory") ;
        }
    }

    public String extractRepoName(String url) throws CacheException {
        int lastSlashIndex = 0;
        int dotIndex = url.length() ;

        for(int i = 0; i < url.length() ; i++) {
            if(url.charAt(i) == '/') {
                lastSlashIndex = i ;
            }
        }

        if(lastSlashIndex +1 >= url.length()) {
            throw new CacheException("Error in parsing string, no content after last /") ;
        }

        for(int i = lastSlashIndex ; i < url.length() ; i++) {
            if(url.charAt(i) == '.') {
                dotIndex = i ;
            }
        }

        return url.substring(++lastSlashIndex, dotIndex) ;
    }

    private void waitForProcess(Process process) {
        try {
            process.waitFor() ;
        } catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
    }

    public boolean cloneRepository(String gitURL) throws CloningException
    {

        String repoName ;

        try {
            checkGitInstalled();
            repoName = extractRepoName(gitURL) ;

            File repoDir = new File(CACHE_PATH, repoName) ;

            if(repoDir.exists() && repoDir.isDirectory()) {
                entries.put(gitURL, repoName) ;
                return false ;
            }

        } catch (IOException | CloningException | CacheException e) {
            throw new CloningException(e.getMessage()) ;
        }

        String[] commands = prepareCommands() ;

        commands[2] = String.format("cd %s ; git clone %s", CACHE_PATH, gitURL) ;

        try {
            Process process = Runtime.getRuntime().exec(commands) ;

            waitForProcess(process);

            entries.put(gitURL, repoName) ;

            return true ;


        }catch (IOException e)
        {
            throw new CloningException("Unable to launch git for cloning") ;
        }
    }

    public boolean checkOutRepository(String repoURL, String commitHash) throws CloningException {

        if(!entries.containsKey(repoURL)) cloneRepository(repoURL) ;

        String[] commands = prepareCommands() ;

        commands[2] = String.format("cd %s ; git checkout %s", CACHE_PATH + "/" + entries.get(repoURL), commitHash) ;

        try {
            Process process = Runtime.getRuntime().exec(commands);

            waitForProcess(process) ;

            String errors = new String(process.getErrorStream().readAllBytes()) ;

            return !errors.startsWith("error") ;

        } catch (IOException e)
        {
            Thread.currentThread().interrupt() ;
            throw new CloningException("Unable to launch git to checkout commit") ;

        }


    }

    public String logRepo(String repoURL, String logFormat, String branch) throws CloningException {
        if(!entries.containsKey(repoURL)) cloneRepository(repoURL) ;

        String[] commands = prepareCommands() ;

        commands[2] = String.format("cd %s ; git --no-pager log %s --pretty=format:\"%s\"", CACHE_PATH + "/" + entries.get(repoURL), branch, logFormat) ;

        try {
            Process process = Runtime.getRuntime().exec(commands) ;

            Thread.sleep(100);

            return new String(process.getInputStream().readAllBytes()) ;

        }catch (IOException | InterruptedException e)
        {
            Thread.currentThread().interrupt() ;
            throw new CloningException("Unable to launch git to get repo log") ;

        }
    }

    public List<GitCommit> findCommitsContaining(String githubURL, String text) throws CloningException, LogParsingException {

        String log = logRepo(githubURL, "%H%n%B%n--", "--all") ;
        return GitLogParser.parseCommitLog(log, text) ;
    }

    public Set<String> findCommitsBranches(String githubURL, String commitID) throws CloningException {
        if(!entries.containsKey(githubURL)) cloneRepository(githubURL) ;

        String[] commands = prepareCommands() ;
        commands[2] = String.format("cd %s ; git branch -a --contains %s", CACHE_PATH + "/" + entries.get(githubURL), commitID) ;

        try {
            Process process = Runtime.getRuntime().exec(commands) ;

            waitForProcess(process);

            String result = new String(process.getInputStream().readAllBytes()) ;

            BufferedReader reader = new BufferedReader(new StringReader(result)) ;

            String firstLine = reader.readLine() ;

            if (firstLine == null)
                return new HashSet<>() ;

            Set<String> retVal = new HashSet<>() ;

            do {
                String line = reader.readLine() ;
                if(line == null) break;
                retVal.add(line) ;
            } while (true) ;

            return retVal ;

        }catch (IOException e)
        {
            throw new CloningException("Unable to launch git to find a commit branches") ;
        }
    }

    private void checkGitInstalled() throws IOException, CloningException {
        String[] commands = prepareCommands() ;

        commands[2] = "git --version" ;

        Process process = Runtime.getRuntime().exec(commands) ;

        waitForProcess(process) ;

        InputStream errors = process.getErrorStream() ;

        byte[] content = new byte[1] ;

        if(errors.read(content) != -1) throw new CloningException("Git not found, please install before running") ;
    }

    private void deleteDirectory(File file) throws FileDeletionException {
        if(file.isDirectory())
        {
            File[] subDirs = file.listFiles() ;

            if (subDirs == null) throw new FileDeletionException() ;

            for(File cacheFile : subDirs)
            {
                deleteDirectory(cacheFile);
            }
        }

        try {
            Files.delete(file.toPath());
        } catch (IOException e) {
            throw new FileDeletionException() ;
        }
    }

    public void cleanCache() throws CacheException {

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

                entries.clear() ;

            } catch (FileDeletionException e)
            {
                throw new CacheException("Error in deleting from cache, check if folder is present and if you have permission to write its contents and in its subdirectories") ;
            }
        }
    }
}
