package com.opriscan.isw2projects.isw2datasetcreator.ticketdistributionbranches.boundaries;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TicketAnalysisPrinter {

    private static final Logger LOGGER = Logger.getLogger(TicketAnalysisPrinter.class.getName()) ;

    public static void printResults(Map<String, Set<List<String>>> tickets) {
        File output = new File("./src/main/resources/output.csv") ;

        try {
            if(!output.createNewFile()) {
                if(!output.delete()) throw new IOException() ;

                if(!output.createNewFile()) throw new IOException() ;
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error in creating output.csv");
            System.exit(1);
        }



        try(FileOutputStream out = new FileOutputStream(output)) {

            for (String ticket : tickets.keySet()) {
                for (List<String> data : tickets.get(ticket)) {
                    String result = String.format("%s ", ticket) ;
                    for (String column : data) {
                        result = result.concat(String.format("; %s ", column)) ;
                    }
                    result = result.concat("\n") ;
                    out.write(result.getBytes());
                }
            }

            out.flush();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error in writing to output.csv");
            System.exit(1);
        }


    }
}
