package com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.tests;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.fail;

public class GitVersionTest {

    @Test
    public void testGitVersion() {

        try {
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
                fail() ;
            }
        } catch(IOException e) {
            fail();
        }


    }

}
