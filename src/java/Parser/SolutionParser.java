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
    
    /**
     * Liste des véhicules utilisés pour la solution
     */
    public List<Vehicule> myVehicules;
    /**
     * Liste des tournées sous forme de fichier solution
     */
    public List<Solution> mySolutions;
    
    public SolutionParser(){
        myVehicules = new ArrayList<Vehicule>();        
        mySolutions = new ArrayList<Solution>();
    }
    public SolutionParser(List<Vehicule> myVehicules){
        this.myVehicules = myVehicules;
    }
    
    public void addSolution(Vehicule s){
        this.myVehicules.add(s);
    }
    public void addSolution(Solution s){
        this.mySolutions.add(s);
    }
    
    /**
     * Permet de transformer l'objet
     */
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

    /**
     * Retourne le résultat d'une solution (avec plusieurs véhicules)
     * @param listVehicule
     * @return 
     */
    public static float getResultat(List<Vehicule> listVehicule) {
                
        int nbSwap = 0;
        int nbTruck = 0;
        int distanceTruck = 0;
        int distanceSemiTrailer = 0;
        int duree = 0;
        boolean doubleCamion = false;
        for(Vehicule v : listVehicule){
            nbTruck++;
            
            doubleCamion = v.getRemorque_2() != null;
            if(doubleCamion){
                nbSwap++;
            }
            
            for(Action a : v.getActionRealisees()){
                distanceTruck += a.getDistanceSpent();
                if(doubleCamion){
                    distanceSemiTrailer +=  a.getDistanceSpent();
                }
                duree += a.getTimeSpent();
            }
            
        }
  
        return getTotalMontant(nbSwap,nbTruck,distanceTruck,distanceSemiTrailer,duree );
    }
    
    /**
     * Retourne le résultat d'une tournée
     * @param vehicule
     * @return 
     */
    public static float getResultatForOneVehicule(Vehicule vehicule){
               
        float totalMontant = 0;
        
        int nbSwap = 0;
        int nbTruck = 1;
        int distanceTruck = 0;
        int distanceSemiTrailer = 0;
        int duree = 0;
        boolean doubleCamion = false;
            
        doubleCamion = vehicule.getRemorque_2() != null;
        if(doubleCamion){
            nbSwap++;
        }

        for(Action a : vehicule.getActionRealisees()){
            distanceTruck += a.getDistanceSpent();
            if(doubleCamion){
                distanceSemiTrailer +=  a.getDistanceSpent();
            }
            duree += a.getTimeSpent();
        }
        
        return getTotalMontant(nbSwap,nbTruck,distanceTruck,distanceSemiTrailer,duree );
    }
    
    /**
     * Calcul le coût total en fonction des paramètres
     * @param nbSwap
     * @param nbTruck
     * @param distanceTruck
     * @param distanceSemiTrailer
     * @param duree
     * @return 
     */
    private static float getTotalMontant(int nbSwap, int nbTruck, int distanceTruck, int distanceSemiTrailer, int duree){
        float costUsageSwap = nbSwap * FleetParser.getCoutUsageFixeSwap(); // cout fixe swap
        float costUsageTruck = nbTruck * FleetParser.getCoutUsageFixeTruck();// cout fixe truck
        float costDistanceTruck = distanceTruck * FleetParser.getCoutDistanceTruck();
        float costDistanceSemiTrailer = distanceSemiTrailer * FleetParser.getCoutDistanceSemiTrailer();
        float costDuree = duree * FleetParser.getCoutDuree();
        
        float totalMontant = costUsageSwap + costUsageTruck + costDistanceTruck + costDistanceSemiTrailer + costDuree;
//        System.out.println("Total : " + totalMontant);
        
        return totalMontant;
    }
    
}
