/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Parser;

import FinalClass.Action;
import FinalClass.Vehicule;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import metier.Solution;

/**
 *
 * @author belterius
 */
public class SolutionParser {
    List<Vehicule> myVehicules;
    public List<Solution> mySolutions;
    
    public SolutionParser(){
        myVehicules = new ArrayList<Vehicule>();        
        mySolutions = new ArrayList<Solution>();

    }
    public SolutionParser(List<Vehicule> mySolutions){
        this.myVehicules = mySolutions;
    }
    
    public void addSolution(Vehicule s){
        this.myVehicules.add(s);
    }
    public void addSolution(Solution s){
        this.mySolutions.add(s);
    }
    
    
    public void toCsv() {     
        FileWriter writer = null;
        try {
            String csvFile = "checker/Solution.csv";
            writer = new FileWriter(csvFile);
            writer.append("la;premiere;ligne;est;ignoree");
            writer.append(" \n");
            for (Vehicule value : myVehicules) {
                writer.append(value.toString());
                writer.append(" \n");
            }   writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(SolutionParser.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(SolutionParser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void toCsvFinal(){
        FileWriter writer = null;
        try {
            String csvFile = "checker/Solution.csv";
            writer = new FileWriter(csvFile);
            writer.append("la;premiere;ligne;est;ignoree");
            writer.append(" \n");
            int i =1;
            int j = 1;
            for (Vehicule vehicule : myVehicules) {
                for(Action action : vehicule.getActionRealisees()){
                    writer.append( "R"+i + ";"+j+";"+ action.toString());                
                    writer.append(" \n");
                    j++;
                }
                i++;
                j=1;
            }   
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(SolutionParser.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(SolutionParser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
        public void toCsvFinalSolution(){
        FileWriter writer = null;
        try {
            String csvFile = "checker/Solution.csv";
            writer = new FileWriter(csvFile);
            writer.append("la;premiere;ligne;est;ignoree");
            writer.append(" \n");
            int i =1;
            int j = 1;
            for (Solution solution : mySolutions) {
                writer.append(solution.toString());               
                writer.append(" \n");

            }   
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(SolutionParser.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(SolutionParser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
