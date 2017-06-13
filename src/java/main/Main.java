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
import java.util.logging.Level;
import java.util.logging.Logger;
import metier.Solution;
import metier.SolutionIndex;

/**
 *
 * @author Gimlibéta
 */
public class Main {

    
    public static double step  = 0.001;
    public static double ELOIGNEMENT_DROITE = 0.001;
    public static double best_eloignement = 0;
    public static double best_score = 999999999;//999 999 999
    
    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
                
        String nameFiles = "small_normal";
//        String nameFiles = "small_all_without_trailer";
//        String nameFiles = "small_all_with_trailer";
//        String nameFiles = "medium_normal";
//        String nameFiles = "medium_all_without_trailer";
//        String nameFiles = "medium_all_with_trailer";
//        String nameFiles = "large_normal";
//        String nameFiles = "large_all_without_trailer";
//        String nameFiles = "large_all_with_trailer";
        
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
        
        double resultSolution;
//        solution1(location,fleet );
//        solution2(location,fleet);
//        solution3(location,fleet);
//        solution4(location,fleet, nameFiles);
//        solution5(location,fleet, nameFiles);
//        solution6(location,fleet, nameFiles);
//            loopForSolution(coordinates, fleet, nameFiles);
//        resultSolution = solution7(location,fleet, nameFiles);
        resultSolution = solution8(location,fleet, nameFiles);
        System.out.println("Result solution : " + resultSolution);

    }
    
    /**
     * Solution : chercher le plus loin, puis toujours chercher le plus proche de ce plus loin
     * Utilise que des camions sauf si on doit livrer en double camion
     * @param location
     * @param fleet 
     */
    public static double solution8(LocationParser location, FleetParser fleet, String nameFiles) throws IOException{
        SolutionParser solution = new SolutionParser();
        List<Solution> mySolutions = new ArrayList<>();
        Vehicule myTruck;
        Boolean isDoubleCamion = false;
        int i= 1;
        while(location.getMyClients().size() >0)
        {
//            System.out.println("Size : " + location.getMyClients().size());
            System.out.println("New camion");
            isDoubleCamion = (location.getMyClients().stream().filter(client -> client.getQuantity() > fleet.getMyFleets().get(2).getCapacity()).count() > 0); 
            myTruck = new Vehicule(location.getMyDepots().get(0),isDoubleCamion, fleet.getMyFleets().get(2).getCapacity());
            
            //Avoir le plus loin
//            Client clientLePlusLoin = myTruck.trierPlusLoinParRapportDepot(location.getMyClients());
//            if(clientLePlusLoin == null){
//                throw new Error("Plus de livraison possible");
//            }
//            
//            myTruck.livrer(clientLePlusLoin, location.getMyClients(), true);
            
            if(! myTruck.chercherPlusLoinParRapportAuCamionEtLivrer(location.getMyClients())){
                throw new Error("Plus de livraison possible");
            }
            
            //Comportement comme avant
            while(myTruck.getBestToInsert(location.getMyClients())){
                System.out.println("Size : " + location.getMyClients().size());
//                System.out.println("Quantité restant : \t Remorque 1 : " + myTruck.getRemorque_1().getQuantityLeft() + "\t Remorque 2 : " + myTruck.getRemorque_2().getQuantityLeft());
            }
            
            myTruck.retour();
                        
            int j=1;
            for(Action action : myTruck.getActionRealisees()){
//                System.out.println(action);
                solution.addSolution(new Solution(i, j, action));
                j++;
            }
            solution.addSolution(myTruck);
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
        
        return SolutionParser.getResultat(solution.myVehicules);
    }
    
    /**
     * Boucle sur la solution 7 pour déterminer le meilleur écartement à la droite
     * @param coordinates
     * @param fleet
     * @param nameFiles 
     */
    public static void loopForSolution(DistanceTimesCoordinatesParser coordinates, FleetParser fleet, String nameFiles){
        LocationParser location = new LocationParser(nameFiles + "/Locations.csv", coordinates.getCoordinates());
        //Get a copy
        List<Client> myClients = new ArrayList<>(location.getMyClients());
        double totalMontant = 0;
        try {
            while(ELOIGNEMENT_DROITE < 0.5){
                LocationParser.myClients = new ArrayList<Client>(myClients);
                System.out.println("Eloignement : " + ELOIGNEMENT_DROITE);

                    totalMontant = solution7(location,fleet, nameFiles);
                    
                    if(best_score > totalMontant){
                        best_score = totalMontant;
                        best_eloignement = ELOIGNEMENT_DROITE;
                    }

                ELOIGNEMENT_DROITE += step;
            }
            
            System.out.println("Meilleur eloignement : " + best_eloignement + " - Score : " + best_score);
            
            ELOIGNEMENT_DROITE = best_eloignement;
            LocationParser.myClients = new ArrayList<Client>(myClients);
            solution7(location,fleet, nameFiles);
            
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
      
    /**
     * Solution : chercher le plus loin, puis toujours chercher le plus proche de ce plus loin
     * Utilise que des camions sauf si on doit livrer en double camion
     * Intercaller des clients entre le depot et le point le plus loin
     * @param location
     * @param fleet 
     */
    public static double solution7(LocationParser location, FleetParser fleet, String nameFiles) throws IOException{
        SolutionParser solution = new SolutionParser();
        List<Solution> mySolutions = new ArrayList<>();
        Vehicule myTruck;
        int i =1;
        Boolean isDoubleCamion = false;
        
        while(location.getMyClients().size() >0)
        {
            isDoubleCamion = (location.getMyClients().stream().filter(client -> client.getQuantity() > fleet.getMyFleets().get(2).getCapacity()).count() > 0); 
            myTruck = new Vehicule(location.getMyDepots().get(0),isDoubleCamion, fleet.getMyFleets().get(2).getCapacity());
            
            //Avoir le plus loin
            Client clientLePlusLoin = myTruck.trierPlusLoinParRapportDepot(location.getMyClients());
            if(clientLePlusLoin == null){
                throw new Error("Plus de livraison possible");
            }
            
            //Mettre dans une liste les clients entre le plus loin et le depot
            List<Client> listClientIntermediaire = new ArrayList<>();
            double distance = 0;
            
            for(Client c : location.getMyClients()){
                distance = myTruck.distancePointDroite(clientLePlusLoin, c);
//                System.out.println("Distance : " + distance);
                if( distance != 0 && Math.abs(distance) < ELOIGNEMENT_DROITE ){
                    listClientIntermediaire.add(c);
                }
            }
            
            while(myTruck.chercherPlusProcheParRapportAuCamionEtLivrer(listClientIntermediaire));
            myTruck.livrer(clientLePlusLoin,location.getMyClients(), false );
            
            //Comportement comme avant
            while(myTruck.chercherPlusProcheParRapportPlusLoinDestinationEtLivrer(location.getMyClients(), clientLePlusLoin)){
            }
            
            myTruck.retour();
                        
            int j=1;
            for(Action action : myTruck.getActionRealisees()){
//                System.out.println(action);
                solution.addSolution(new Solution(i, j, action));
                j++;
            }
            solution.addSolution(myTruck);
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
        
        return SolutionParser.getResultat(solution.myVehicules);
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
            isDoubleCamion = (location.getMyClients().stream().filter(client -> client.needDoubleTruck()).count() > 0); 
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
            Vehicule myTruck = new Vehicule(location.getMyDepots().get(0), client.needDoubleTruck(), FleetParser.getCapacite());
            myTruck.livrer(client, location.getMyClients(), false);
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
    
}
