package com.opriscan.isw2projects.isw2datasetcreator.boundaries;

import com.opriscan.isw2projects.isw2datasetcreator.beans.RESTQuery;
import com.opriscan.isw2projects.isw2datasetcreator.beans.RESTResult;
import com.opriscan.isw2projects.isw2datasetcreator.exceptions.RestCallException;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class RESTBoundary {

    public RESTResult restCall(RESTQuery query) throws RestCallException {
        try {

            URL url = new URL(query.getQuery()) ;
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection() ;
            con.setRequestMethod("GET") ;
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);

            int status = con.getResponseCode() ;

            InputStream stream ;

            if (status != 200)
            {
                stream = con.getErrorStream() ;
                byte[] result = stream.readAllBytes() ;
                stream.close();
                con.disconnect();

                throw new RestCallException("Unable to get http response from server, details :\n" + new String(result)) ;
            } else {
                String result = new String(con.getInputStream().readAllBytes()) ;

                RESTResult restResult = new RESTResult() ;
                restResult.setResult(result) ;

                return restResult ;
            }

        } catch (MalformedURLException e)
        {
            throw new RestCallException("Error in URL formatting") ;
        } catch (IOException e) {
            throw new RestCallException("IO error on system") ;
        }
    }
}
