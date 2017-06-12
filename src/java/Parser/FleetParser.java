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
import metier.FleetCSV;

/**
 *
 * @author belterius
 */
public class FleetParser {
    private List<FleetCSV> myFleets;
    private String currentLine;
    private String separator = ";";

    public List<FleetCSV> getMyFleets() {
        return myFleets;
    }
    
    public FleetParser(String filePath){
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))){
            myFleets = new ArrayList<FleetCSV>();
            currentLine = br.readLine(); //Remove the line with column name information
            while ((currentLine = br.readLine()) != null) {
                String[] fleet = currentLine.split(separator);
                FleetCSV tempoFleet = new FleetCSV(fleet[0], Integer.valueOf(fleet[1]), Float.parseFloat(fleet[2]),  Float.parseFloat(fleet[3]),  Float.parseFloat(fleet[4]), Integer.valueOf(fleet[5]));
                this.myFleets.add(tempoFleet);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
