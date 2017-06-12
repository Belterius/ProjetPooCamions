/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier;

import FinalClass.Client;
import FinalClass.Vehicule;

/**
 *
 * @author Gimlibéta
 */
public class DistanceTimesDataCSV {
    
    public static int[][] matrix;

    public DistanceTimesDataCSV(int width, int height) {
        this.matrix = new int[width][height];
    }
    
    public void setData(int x, int y, int value){
        this.matrix[x][y] = value;
    }
    
    public int getData(int x, int y){
        return this.matrix[x][y];
    }
    
    /**
     * Calcul le temps entre une location et le client passé en paramètre
     * @param pointDeDepart
     * @param c
     * @return 
     */
    public static int getDifferenceTimeBetweenLocationAndClient(LocationCSV pointDeDepart, Client c ){
        int temp = DistanceTimesDataCSV.matrix[pointDeDepart.getCoord().getId()][c.coord.getId()];
        return DistanceTimesDataCSV.matrix[pointDeDepart.getCoord().getId()][c.coord.getId()];
    }
    
}
