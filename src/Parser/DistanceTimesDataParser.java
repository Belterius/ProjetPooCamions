/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import metier.CoordinateCSV;
import metier.DistanceTimesDataCSV;

/**
 *
 * @author belterius
 */
public class DistanceTimesDataParser {
    private String currentLine;
    private String separator = ";";
    private DistanceTimesDataCSV myDistanceData;

    public DistanceTimesDataCSV getMyDistanceData() {
        return myDistanceData;
    }

    public DistanceTimesDataParser(String filePath){
        int numberOfColumns = 0;
        int numberOfLines = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))){            
            currentLine = br.readLine(); //Remove the line with column name information
            currentLine = br.readLine(); //Reads the first line
            String[] data = currentLine.split(separator);
            numberOfColumns = data.length;
            numberOfLines++;
            while ((currentLine = br.readLine()) != null) {
                numberOfLines++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))){
            currentLine = br.readLine(); //Remove the line with column name information
            int indexLine = 0;
            myDistanceData = new DistanceTimesDataCSV(numberOfColumns, numberOfLines);
            while ((currentLine = br.readLine()) != null) {
                String[] data = currentLine.split(separator);
                for(int i =0; i<numberOfColumns;i++){
                    myDistanceData.setData(i, indexLine, Integer.valueOf(data[i]));
                }
                indexLine++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
