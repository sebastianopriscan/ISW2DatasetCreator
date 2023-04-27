package com.opriscan.isw2projects.isw2datasetcreator.ticketdistributionbranches.ticketanalyser;

import com.opriscan.isw2projects.isw2datasetcreator.ticketdistributionbranches.exceptions.TicketFormatterException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TicketFormatter {

    public List<String> getBugTicketNames(JSONObject json) throws TicketFormatterException {

        try {
            List<String> retVal = new ArrayList<>() ;
            JSONArray issues = json.getJSONArray("issues") ;

            for (int i = 0 ; i < issues.length() ; i++) {
                JSONObject issue = issues.getJSONObject(i) ;
                retVal.add(issue.getString("key")) ;
            }

            return retVal ;
        } catch (JSONException e) {
            throw new TicketFormatterException("Error in parsing json") ;
        }
    }
}
