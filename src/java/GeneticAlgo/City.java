/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GeneticAlgo;

import metier.CoordinateCSV;
import metier.DistanceTimesDataCSV;

/**
 *
 * @author loic
 */
public class City {
    protected CoordinateCSV coord;
    
    // Constructs a city at chosen x, y location
    public City(CoordinateCSV coord){
        this.coord = coord;
    }
    
    // Gets city's x coordinate
    public float getX(){
        return this.coord.getX();
    }
    
    // Gets city's y coordinate
    public float getY(){
        return this.coord.getY();
    }

    public CoordinateCSV getCoord() {
        return coord;
    }
    
    // Gets the distance to given city
    public double distanceTo(City city){
        int i = 2*city.getCoord().getId() + 1;
        int j = this.getCoord().getId();
        return DistanceTimesDataCSV.matrix[i][j];
    }
    
    @Override
    public String toString(){
        return getX()+", "+getY();
    }
}
