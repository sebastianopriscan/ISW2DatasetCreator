package com.opriscan.isw2projects.isw2datasetcreator.ticketdistributionbranches.boundaries;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TicketAnalysisPrinter {

    private static final Logger LOGGER = Logger.getLogger(TicketAnalysisPrinter.class.getName()) ;

    private TicketAnalysisPrinter() {}

    public static void printResults(Map<String, Set<List<String>>> tickets) {
        File output = new File("./src/main/resources/output.csv") ;

        try {
            if(!output.createNewFile()) {
                Files.delete(output.toPath()) ;

                if(!output.createNewFile()) throw new IOException() ;
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error in creating output.csv");
            System.exit(1);
        }



        try(FileOutputStream out = new FileOutputStream(output)) {

            for (Map.Entry<String, Set<List<String>>> ticket : tickets.entrySet()) {
                for (List<String> data : ticket.getValue()) {
                    String result = String.format("%s ", ticket.getKey()) ;
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
