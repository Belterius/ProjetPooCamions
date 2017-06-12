/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Parser;

import FinalClass.Client;
import FinalClass.Depot;
import FinalClass.SwapLocation;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import static java.lang.Float.parseFloat;
import java.util.ArrayList;
import java.util.List;
import jdk.nashorn.internal.runtime.regexp.joni.Config;
import metier.CoordinateCSV;
import metier.LocationCSV;

/**
 *
 * @author belterius
 */
public class LocationParser {
    private List<Depot> myDepots;    
    private List<SwapLocation> mySwapLocations;
    public static List<Client> myClients;

    private CoordinateCSV myCoord;
    private String currentLine;
    private String separator = ";";

    public List<Depot> getMyDepots() {
        return myDepots;
    }

    public List<SwapLocation> getMySwapLocations() {
        return mySwapLocations;
    }

    public List<Client> getMyClients() {
        return myClients;
    }
    
    
    public LocationParser(String filePath, List<CoordinateCSV> myCoordinates){
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))){
            myClients = new ArrayList<Client>();            
            mySwapLocations = new ArrayList<SwapLocation>();
            myDepots = new ArrayList<Depot>();

            currentLine = br.readLine(); //Remove the line with column name information
            while ((currentLine = br.readLine()) != null) {
                String[] location = currentLine.split(separator);
                if( (myCoord = getCoordinateFromXY(location[4], location[5], myCoordinates)) != null){
                    if(location[0].equals("DEPOT")){
                        Depot tempoLoc = new Depot(location[0], location[1], location[2], location[3], myCoord);
                        myDepots.add(tempoLoc);
                    }
                    if(location[0].equals("SWAP_ACTION")){
                        SwapLocation tempoLoc = new SwapLocation(location[0], location[1], location[2], location[3], myCoord);
                        mySwapLocations.add(tempoLoc);
                    }
                    if(location[0].equals("CUSTOMER")){
                        Client tempoLoc = new Client(location[0], location[1], location[2], location[3], Integer.valueOf(location[6]) , Integer.valueOf(location[7]), Integer.valueOf(location[8]), myCoord);
                        myClients.add(tempoLoc);
                    }
                }else{
                    Config.log.print("Coordinate X:" + location[4] +" Y:" + location[5]+" not found in the Coordinates" );
                }
            }
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private CoordinateCSV getCoordinateFromXY(String X, String Y, List<CoordinateCSV> myCoordinates){
        for(CoordinateCSV c: myCoordinates){
            if( (c.getX() == Float.parseFloat(X)) && (c.getY() == parseFloat(Y))){
                return c;
            }
        }
        return null;
    }
    
}
