package com.opriscan.isw2projects.isw2datasetcreator.ticketdistributionbranches.boundaries;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TicketsNotInMasterPrinter extends Printer{

    private static final Logger LOGGER = Logger.getLogger(TicketsNotInMasterPrinter.class.getName()) ;

    @Override
    public void printResults(Map<String, Set<List<String>>> tickets) {
        File output = new File("./src/main/resources/output"+ TicketsNotInMasterPrinter.class.getSimpleName() + ".csv") ;

        try {
            deleteOldFile(output) ;
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error in creating output.csv");
            System.exit(1);
        }



        try(FileOutputStream out = new FileOutputStream(output)) {

            for (Map.Entry<String, Set<List<String>>> ticket : tickets.entrySet()) {

                boolean notInMaster = true ;

                try {
                    for (List<String> data : ticket.getValue()) {

                        String branch = data.get(3);

                        if (branch.contains("remotes/origin/master")) {
                            notInMaster = false;
                            break;
                        }
                    }
                }
                catch (IndexOutOfBoundsException e) {
                    LOGGER.log(Level.SEVERE, "Error in row format");
                    continue;
                }

                if (notInMaster) {
                    printMapDump(ticket, out) ;
                }
            }

            out.flush();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error in writing to output.csv");
            System.exit(1);
        }


    }
}
