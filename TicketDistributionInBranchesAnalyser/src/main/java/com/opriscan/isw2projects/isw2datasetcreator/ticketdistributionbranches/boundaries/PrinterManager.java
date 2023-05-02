package com.opriscan.isw2projects.isw2datasetcreator.ticketdistributionbranches.boundaries;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PrinterManager {

    private static final List<Printer> printers = List.of(
            new TicketsNotInMasterPrinter(), new TicketAnalysisPrinter()) ;

    public static void executePrinters(Map<String, Set<List<String>>> tickets) {
        for (Printer printer : printers) {
            printer.printResults(tickets);
        }
    }
}
