package com.opriscan.isw2projects.isw2datasetcreator.file_scrapers;

import com.opriscan.isw2projects.isw2datasetcreator.exceptions.CacheException;
import com.opriscan.isw2projects.isw2datasetcreator.exceptions.CloningException;
import com.opriscan.isw2projects.isw2datasetcreator.exceptions.FileDeletionException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class CacheManager {

    private static final String CACHE_PATH = "./src/main/resources/.cache" ;

    private static CacheManager instance ;

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

        if(!cache.exists()) {
            if(!cache.mkdir()) throw new CloningException("Unable to create .cache directory") ;
        }
    }

    private String extractRepoName(String url) throws CacheException {
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

    public boolean cloneRepository(String gitURL) throws CloningException
    {
        try {
            checkGitInstalled();
            String repoName = extractRepoName(gitURL) ;

            File repoDir = new File(CACHE_PATH + "/" + repoName) ;

            if(repoDir.exists() && repoDir.isDirectory()) return false ;

        } catch (IOException | CloningException | CacheException e) {
            throw new CloningException(e.getMessage()) ;
        }

        String[] commands = new String[3] ;

        if (System.getProperty("os.name").toLowerCase().contains("windows"))
        {
            commands[0] = "cmd.exe" ;
            commands[1] = "/c" ;
        } else {
            commands[0] = "/bin/bash" ;
            commands[1] = "-c" ;
            commands[2] = String.format("cd %s ; git clone %s", CACHE_PATH, gitURL) ;
        }

        try {
            Process process = Runtime.getRuntime().exec(commands) ;

            try {
                process.waitFor() ;
            } catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }

            return true ;


        }catch (IOException e)
        {
            throw new CloningException("Unable to launch git") ;
        }

    }

    private void checkGitInstalled() throws IOException, CloningException {
        String[] commands = new String[3] ;

        if (System.getProperty("os.name").toLowerCase().contains("windows"))
        {
            commands[0] = "cmd.exe" ;
            commands[1] = "/c" ;
        } else {
            commands[0] = "/bin/bash" ;
            commands[1] = "-c" ;
        }

        commands[2] = "git --version" ;

        Process process = Runtime.getRuntime().exec(commands) ;

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

        if(!file.delete()) throw new FileDeletionException() ;
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

            } catch (FileDeletionException e)
            {
                throw new CacheException("Error in deleting from cache, check if folder is present and if you have permission to write its contents and in its subdirectories") ;
            }
        }
    }
}
