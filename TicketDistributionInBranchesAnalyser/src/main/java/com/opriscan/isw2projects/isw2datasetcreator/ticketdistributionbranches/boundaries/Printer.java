package com.opriscan.isw2projects.isw2datasetcreator.ticketdistributionbranches.boundaries;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class Printer {

    protected void printMapDump(Map.Entry<String, Set<List<String>>> ticket, OutputStream out) throws IOException {
        for (List<String> data : ticket.getValue()) {
            String result = String.format("%s ", ticket.getKey()) ;
            for (String column : data) {
                result = result.concat(String.format("; %s ", column)) ;
            }
            result = result.concat("\n") ;
            out.write(result.getBytes());
        }
    }

    protected void deleteOldFile(File output) throws IOException{
        if(!output.createNewFile()) {
            Files.delete(output.toPath()) ;

            if(!output.createNewFile()) throw new IOException() ;
        }
    }

    public abstract void printResults(Map<String, Set<List<String>>> tickets) ;
}
