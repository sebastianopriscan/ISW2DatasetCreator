package com.opriscan.isw2projects.isw2datasetcreator.ticketdistributionbranches.boundaries;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class PrinterManager {

    private PrinterManager() {

    }

    private static final List<Printer> printers = List.of(
            new TicketsNotInMasterPrinter()) ;

    public static void executePrinters(Map<String, Set<List<String>>> tickets) {
        for (Printer printer : printers) {
            printer.printResults(tickets);
        }
    }
}
