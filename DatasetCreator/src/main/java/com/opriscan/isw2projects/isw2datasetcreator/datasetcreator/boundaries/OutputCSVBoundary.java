package com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.boundaries;

import com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.beans.CSVBean;
import com.opriscan.isw2projects.isw2datasetcreator.datasetcreator.exceptions.CSVCreationException;

import java.io.*;
import java.util.List;

public class OutputCSVBoundary {

    public void generateCSV(CSVBean bean) throws CSVCreationException {

        try {
            File file = new File("./src/main/resources/" + bean.getName() +".csv") ;

            if (file.exists()) {
                if(!file.delete()) throw new CSVCreationException("Unable to replace file") ;
            }
            else {
                if(!file.createNewFile()) throw new CSVCreationException("Unable to create new file") ;
            }
        } catch (IOException e) {
            throw new CSVCreationException("Unable to create output file") ;
        }

        try (OutputStream out = new BufferedOutputStream(new FileOutputStream("./src/main/resources/" + bean.getName() +".csv"))){

            int min = 10000000 ;

            int colNum = bean.getPayload().size() ;

            for (List<String> column : bean.getPayload()) {
                min = Math.min(min, column.size()) ;
            }

            for (int i = 0 ; i < min ; i++) {
                for (int j = 0 ; j < colNum ; j++) {
                    out.write((bean.getPayload().get(j).get(i).getBytes())) ;
                    if(j != colNum -1)
                        out.write(';') ;
                }

                out.write('\n');
            }

            out.flush() ;
        } catch (IOException e) {
            throw new CSVCreationException("Unable to create CSV : \n" + e.getMessage()) ;
        }
    }
}
