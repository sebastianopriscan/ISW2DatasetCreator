package com.opriscan.isw2projects.isw2datasetcreator.ticketdistributionbranches.boundaries;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TicketAnalysisPrinter extends Printer{

    private static final Logger LOGGER = Logger.getLogger(TicketAnalysisPrinter.class.getName()) ;

    @Override
    public void printResults(Map<String, Set<List<String>>> tickets) {
        File output = new File("./src/main/resources/output" + TicketAnalysisPrinter.class.getSimpleName() + ".csv") ;

        try {
            deleteOldFile(output) ;
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error in creating output.csv");
            System.exit(1);
        }



        try(FileOutputStream out = new FileOutputStream(output)) {

            for (Map.Entry<String, Set<List<String>>> ticket : tickets.entrySet()) {
                printMapDump(ticket, out);
            }

            out.flush();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error in writing to output.csv");
            System.exit(1);
        }


    }
}
