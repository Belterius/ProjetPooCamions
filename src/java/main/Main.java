/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import FinalClass.Action;
import FinalClass.Client;
import FinalClass.Vehicule;
import Parser.DistanceTimesCoordinatesParser;
import Parser.DistanceTimesDataParser;
import Parser.FleetParser;
import Parser.LocationParser;
import Parser.SolutionParser;
import Parser.SwapActionParser;
import dao.JpaFactory;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import metier.Solution;
import metier.SolutionIndex;

/**
 *
 * @author Gimlibéta
 */
public class Main {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
                
//        String nameFiles = "small_normal";
//        String nameFiles = "medium_normal";
        String nameFiles = "large_normal";
        
        DistanceTimesCoordinatesParser coordinates = new DistanceTimesCoordinatesParser("dima/DistanceTimesCoordinates.csv");
        DistanceTimesDataParser data = new DistanceTimesDataParser("dima/DistanceTimesData.csv");
        FleetParser fleet = new FleetParser(nameFiles + "/Fleet.csv");
        LocationParser location = new LocationParser(nameFiles + "/Locations.csv", coordinates.getCoordinates());
        SwapActionParser swapAction = new SwapActionParser(nameFiles + "/SwapActions.csv");
        
        //
        Path from = Paths.get(nameFiles + "/Fleet.csv"); //convert from File to Path
        Path to = Paths.get("checker/Fleet.csv"); //convert from String to Path
        Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
        from = Paths.get(nameFiles + "/Locations.csv"); //convert from File to Path
        to = Paths.get("checker/Locations.csv"); //convert from String to Path
        Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
        from = Paths.get(nameFiles + "/SwapActions.csv"); //convert from File to Path
        to = Paths.get("checker/SwapActions.csv"); //convert from String to Path
        Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
        
//        solution1(location,fleet );
//        solution2(location,fleet);
//        solution3(location,fleet);
//        solution4(location,fleet, nameFiles);
//        solution5(location,fleet, nameFiles);
        solution6(location,fleet, nameFiles);

    }
    
    /**
     * Solution : chercher le plus loin, puis toujours chercher le plus proche de ce plus loin
     * Utilise que des camions sauf si on doit livrer en double camion
     * Ordonner la tournée pour faire une sorte de cercle partant à droite puis revenant à gauche
     * @param location
     * @param fleet 
     */
    public static void solution6(LocationParser location, FleetParser fleet, String nameFiles) throws IOException{
        SolutionParser solution = new SolutionParser();
        List<Solution> mySolutions = new ArrayList<>();
        Vehicule myTruck;
        int i =1;
        Boolean isDoubleCamion = false;
        
        while(location.getMyClients().size() >0)
        {
            isDoubleCamion = (location.getMyClients().stream().filter(client -> client.getQuantity() > fleet.getMyFleets().get(2).getCapacity()).count() > 0); 
            myTruck = new Vehicule(location.getMyDepots().get(0),isDoubleCamion, fleet.getMyFleets().get(2).getCapacity());
            
            if(! myTruck.chercherPlusLoinParRapportAuCamionEtLivrer(location.getMyClients())){
                throw new Error("Plus de livraison possible");
            }

            while(myTruck.chercherPlusProcheParRapportPermiereDestinationEtLivrer(location.getMyClients())){

            }
            
            //Retrier la tournée pour aller de la droite vers la gauche
            myTruck.ordonnerTournée(isDoubleCamion, fleet.getMyFleets().get(2).getCapacity());
                     
            myTruck.retour();
            
            int j=1;
            for(Action action : myTruck.getActionRealisees()){
                solution.addSolution(new Solution(i, j, action));
                j++;
            }
            //solution.addSolution(myTruck);
            i++;
        }
        //solution.toCsvFinal();
        
        solution.toCsvFinalSolution();
        JpaFactory factory = new JpaFactory();
        SolutionIndex sIndex = new SolutionIndex(nameFiles);
        
        for(Solution mySol : solution.mySolutions){
            sIndex.addSolution(mySol);
        }
        
        factory.getJpaSolutionIndexDao().create(sIndex);
    }
    
    /**
     * Solution : chercher le plus loin, puis toujours chercher le plus proche de ce plus loin
     * Utilise que des camions sauf si on doit livrer en double camion
     * @param location
     * @param fleet 
     */
    public static void solution5(LocationParser location, FleetParser fleet, String nameFiles) throws IOException{
        SolutionParser solution = new SolutionParser();
        List<Solution> mySolutions = new ArrayList<>();
        Vehicule myTruck;
        int i =1;
        
        while(location.getMyClients().size() >0)
        {
            if(location.getMyClients().stream().filter(client -> client.getQuantity() > fleet.getMyFleets().get(2).getCapacity()).count() > 0){
                myTruck = new Vehicule(location.getMyDepots().get(0),true, fleet.getMyFleets().get(2).getCapacity());
            }else{
                myTruck = new Vehicule(location.getMyDepots().get(0),false, fleet.getMyFleets().get(2).getCapacity());
            }
              
            if(! myTruck.chercherPlusLoinParRapportAuCamionEtLivrer(location.getMyClients())){
                throw new Error("Plus de livraison possible");
            }

            while(myTruck.chercherPlusProcheParRapportPermiereDestinationEtLivrer(location.getMyClients())){

            }
            
            myTruck.retour();
            
            int j=1;
            for(Action action : myTruck.getActionRealisees()){
                solution.addSolution(new Solution(i, j, action));
                j++;
            }
            //solution.addSolution(myTruck);
            i++;
        }
        //solution.toCsvFinal();
        
        solution.toCsvFinalSolution();
        JpaFactory factory = new JpaFactory();
        SolutionIndex sIndex = new SolutionIndex(nameFiles);
        
        for(Solution mySol : solution.mySolutions){
            sIndex.addSolution(mySol);
        }
        
        factory.getJpaSolutionIndexDao().create(sIndex);
    }
    
     /**
     * Solution (inutile ): chercher le plus loin, puis le plus proche par rapport au dépot
     * Utilise que des camions sauf si on doit livrer en double camion
     * @param location
     * @param fleet 
     */
    public static void solution4(LocationParser location, FleetParser fleet, String nameFiles) throws IOException{

        SolutionParser solution = new SolutionParser();
        Vehicule myTruck;
        int i =1;
        
        while(location.getMyClients().size() >0)
        {
            if(location.getMyClients().stream().filter(client -> client.getQuantity() > fleet.getMyFleets().get(2).getCapacity()).count() > 0){
                myTruck = new Vehicule(location.getMyDepots().get(0),true, fleet.getMyFleets().get(2).getCapacity());
            }else{
                myTruck = new Vehicule(location.getMyDepots().get(0),false, fleet.getMyFleets().get(2).getCapacity());
            }
              
            if(! myTruck.chercherPlusLoinParRapportAuCamionEtLivrer(location.getMyClients())){
                throw new Error("Plus de livraison possible");
            }

            while(myTruck.chercherPlusProcheParRapportDepotEtLivrer(location.getMyClients())){

            }
            myTruck.retour();
            int j=1;
            for(Action action : myTruck.getActionRealisees()){
                solution.addSolution(new Solution(i, j, action));
                j++;
            }
            //solution.addSolution(myTruck);
            i++;
        }
        
        solution.toCsvFinalSolution();
        JpaFactory factory = new JpaFactory();
        SolutionIndex sIndex = new SolutionIndex(nameFiles);
        
        for(Solution mySol : solution.mySolutions){
            sIndex.addSolution(mySol);
        }
        
        factory.getJpaSolutionIndexDao().create(sIndex);
    }
    
    /**
     * Solution : chercher le plus loin, puis le plus proche du plus loin, 
     * et ensuite toujours le plus proche du dernier plus proche
     * Utilise que des camions sauf si on doit livrer en double camion
     * @param location
     * @param fleet 
     */
    public static void solution3(LocationParser location, FleetParser fleet) throws IOException{

        SolutionParser solution = new SolutionParser();
        Vehicule myTruck;
        
        while(location.getMyClients().size() >0)
        {
            if(location.getMyClients().stream().filter(client -> client.getQuantity() > fleet.getMyFleets().get(2).getCapacity()).count() > 0){
                myTruck = new Vehicule(location.getMyDepots().get(0),true, fleet.getMyFleets().get(2).getCapacity());
            }else{
                myTruck = new Vehicule(location.getMyDepots().get(0),false, fleet.getMyFleets().get(2).getCapacity());
            }
              
            if(! myTruck.chercherPlusLoinParRapportAuCamionEtLivrer(location.getMyClients())){
                throw new Error("Plus de livraison possible");
            }

            while(myTruck.chercherPlusProcheParRapportAuCamionEtLivrer(location.getMyClients())){

            }
            
            myTruck.retour();
            
            solution.addSolution(myTruck);
        }
        solution.toCsvFinal();
    }
    
    /**
     * Solution : chercher le plus loin, puis le plus proche du plus loin, 
     * et ensuite toujours le plus proche du dernier plus proche
     * Utilise que des doubles camions si possible (permet de parcourir plus de 
     * distance avant de revenir à la base)
     * @param location
     * @param fleet 
     */
    public static void solution2(LocationParser location, FleetParser fleet) throws IOException{

        SolutionParser solution = new SolutionParser();
        Vehicule myTruck;
        
        while(location.getMyClients().size() >0)
        {
            if(location.getMyClients().stream().filter(client -> client.getIsTrainPossible() == 1).count() > 0){
                myTruck = new Vehicule(location.getMyDepots().get(0),true, fleet.getMyFleets().get(2).getCapacity());
            }else{
                myTruck = new Vehicule(location.getMyDepots().get(0),false, fleet.getMyFleets().get(2).getCapacity());
            }
            
            
            if(! myTruck.chercherPlusLoinParRapportAuCamionEtLivrer(location.getMyClients())){
                throw new Error("Plus de livraison possible");
            }

            while(myTruck.chercherPlusProcheParRapportAuCamionEtLivrer(location.getMyClients())){

            }
            
            myTruck.retour();
            
            solution.addSolution(myTruck);
        }
        solution.toCsvFinal();
    }
    
    /**
     * Solution triviale 1 : 1 camion pour 1 client, ou utilisation d'un double camion si nécessaire
     * @param location
     * @param fleet 
     */
    public static void solution1(LocationParser location, FleetParser fleet){
        SolutionParser solution = new SolutionParser();
        List<Client> myClients = new ArrayList<Client>(location.getMyClients());
        for(Client client : myClients){
            Vehicule myTruck = new Vehicule(location.getMyDepots().get(0), client.getQuantity()> fleet.getMyFleets().get(2).getCapacity() ? true : false, fleet.getMyFleets().get(2).getCapacity());
            myTruck.livrer(client, location.getMyClients());
            myTruck.retour();
            solution.addSolution(myTruck);
        }
        solution.toCsvFinal();
    }
    
    public static int amountToDeliver(int toDeliver, int maxPerSwap, Boolean secondSwap){
        
        if(!secondSwap){
            if(toDeliver < maxPerSwap){
                return toDeliver;
            }else{
                return maxPerSwap;
            }
        }else{
            if(toDeliver > maxPerSwap){
                return toDeliver - maxPerSwap;
            }else{
                return 0;
            }
        }
    }
    public void save(){
//        DistanceTimesCoordinatesParser coordinates = new DistanceTimesCoordinatesParser("dima/DistanceTimesCoordinates.csv");
//        DistanceTimesDataParser data = new DistanceTimesDataParser("dima/DistanceTimesData.csv");
//        FleetParser fleet = new FleetParser("small_normal/Fleet.csv");
//        LocationParser location = new LocationParser("small_normal/Locations.csv", coordinates.getCoordinates());
//        SwapActionParser swapAction = new SwapActionParser("small_normal/SwapActions.csv");
//        
//        SolutionParser solution = new SolutionParser();
//        int i = 0;
//        int j;
//        for(Client loc : location.getMyClients()){
//                j = 0;
//                i++;
//                int max = fleet.getMyFleets().get(fleet.getMyFleets().size()-1).getCapacity();
//                solution.addSolution(new Solution(i, ++j, "DEPOT", "D1", true, 1,2, "NONE", 0,0));
//                solution.addSolution(new Solution(i, ++j, loc.getLocation_type(), loc.getLocation_id() , true, 1,2, "NONE", amountToDeliver(loc.getQuantity(), max, false), amountToDeliver(loc.getQuantity(), max, true)));
//                solution.addSolution(new Solution(i, ++j, "DEPOT", "D1", true, 1,2, "NONE", 0,0));
//        }
//        
//        solution.toCsv();
        
    }
}
