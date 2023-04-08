package com.opriscan.isw2projects.isw2datasetcreator.file_scrapers;

import com.opriscan.isw2projects.isw2datasetcreator.exceptions.CloningException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class GitCloner {

    private static final String CACHE_PATH = "./src/main/resources/.cache" ;

    public GitCloner() throws CloningException{
        File cache = new File(CACHE_PATH) ;

        if(!cache.exists()) {
            if(!cache.mkdir()) throw new CloningException("Unable to create .cache directory") ;
        }
    }

    public void cloneRepository(String gitURL) throws CloningException
    {
        try {
            checkGitInstalled();
        } catch (IOException | CloningException e) {
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

        errors.read(content) ;

        if(content[0] != 0) {
            throw new CloningException("Git not found, please install before running") ;
        }
    }
}
