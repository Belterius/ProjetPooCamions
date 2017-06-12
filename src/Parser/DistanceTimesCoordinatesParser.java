/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Parser;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import metier.CoordinateCSV;

/**
 *
 * @author belterius
 */
public class DistanceTimesCoordinatesParser {
    private String currentLine;
    private String separator = ";";
    private List<CoordinateCSV> myCoordinates;

    public List<CoordinateCSV> getCoordinates() {
        return myCoordinates;
    }
    
    public DistanceTimesCoordinatesParser(String filePath){
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))){
            int index = 0;
            myCoordinates = new ArrayList<CoordinateCSV>();
            currentLine = br.readLine(); //Remove the line with column name information
            while ((currentLine = br.readLine()) != null) {
                String[] coordinate = currentLine.split(separator);
                CoordinateCSV tempoCoord = new CoordinateCSV(index++, coordinate[0], coordinate[1]);
                this.myCoordinates.add(tempoCoord);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
