/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FinalClass;

import Parser.DistanceTimesDataParser;
import Parser.FleetParser;
import Parser.LocationParser;
import Parser.SolutionParser;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import metier.DistanceTimesDataCSV;
import metier.FleetCSV;
import metier.LocationCSV;
import metier.Solution;

/**
 *
 * @author belterius
 */

@Entity
@Table(name = "VEHICULE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Vehicule.findAll", query = "SELECT a FROM Vehicule a")
    , @NamedQuery(name = "Vehicule.findById", query = "SELECT a FROM Vehicule a WHERE a.idVehicule = :IDVEHICULE")})
public class Vehicule implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @Column(name = "IDVEHICULE")
    private Long idVehicule;
    
    
    @JoinColumn(name = "MYDEPOT", referencedColumnName = "IDLOCATION")
    @ManyToOne(optional = false)
    public Depot myDepot;
    
    
    
    @JoinColumn(name = "CURRENTEMPLACEMENT", referencedColumnName = "IDLOCATION")
    @ManyToOne(optional = false)
    private LocationCSV currentEmplacement;
    
    
    
    @JoinColumn(name = "REMORQUE1", referencedColumnName = "IDREMORQUE")
    @OneToOne(optional = false)
    private Remorque remorque_1;
    
    
    @JoinColumn(name = "REMORQUE2", referencedColumnName = "IDREMORQUE")
    @OneToOne(optional = true)
    private Remorque remorque_2;
    
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "vehiculeActing")
    private List<Action> actionRealisees;
    
    private float timeSpent;

    protected Vehicule() {
    }
    

    /**
     * Constructor
     * @param startLocation - Il démarre normalement au dépot
     * @param doubleRemorque - Est-ce que c'est une double remorque ou non ?
     * @param sizeRemorque  - Avoir la capacité max d'une remorque
     */
    public Vehicule(Depot startLocation, boolean doubleRemorque, float sizeRemorque){
        initVehicule(startLocation, doubleRemorque, sizeRemorque);
    }
    
    private void initVehicule(Depot startLocation, boolean doubleRemorque, float sizeRemorque){
        actionRealisees = new ArrayList<Action>();
        remorque_1 = new Remorque(1, startLocation, true, sizeRemorque);
        if(doubleRemorque){
            remorque_2 = new Remorque(2, startLocation, true, sizeRemorque);
        }else{
            remorque_2 = null;
        }
        currentEmplacement = startLocation;
        myDepot = startLocation;
        timeSpent = 0;
        demarrer(startLocation);
    }
    
    
    public LocationCSV getCurrentEmplacement() {
        return currentEmplacement;
    }

    public void setCurrentEmplacement(LocationCSV currentEmplacement) {
        this.currentEmplacement = currentEmplacement;
        if(remorque_1.isAttached)this.remorque_1.currentRemorqueLocation = this.currentEmplacement;        
        if(remorque_2 != null && remorque_2.isAttached)this.remorque_2.currentRemorqueLocation = this.currentEmplacement;
    }

    public Remorque getRemorque_1() {
        return remorque_1;
    }

    public void setRemorque_1(Remorque remorque_1) {
        this.remorque_1 = remorque_1;
    }

    public Remorque getRemorque_2() {
        return remorque_2;
    }

    public void setRemorque_2(Remorque remorque_2) {
        this.remorque_2 = remorque_2;
    }

    public List<Action> getActionRealisees() {
        return actionRealisees;
    }

    public void setActionRealisees(List<Action> actionRealisees) {
        this.actionRealisees = actionRealisees;
    }
    
    private void demarrer(Depot startLocation){
        Action start = new Action(currentEmplacement,startLocation, remorque_1, remorque_2, "NONE");
        ajouterAction(start);
        timeSpent += start.getTimeSpent();
        setCurrentEmplacement(startLocation);

    }
    public boolean retour(){
        Action back = new Action(currentEmplacement, this.myDepot, remorque_1, remorque_2, "NONE");
        ajouterAction(back);
        timeSpent += back.getTimeSpent();
        setCurrentEmplacement(this.myDepot);
        return true;
    }
    
    /**
     * Livre un client
     * @param destination
     * @param listClientALivrer
     * @param checkRentability
     */
    public boolean livrer(Client destination, List<Client> listClient, boolean checkRentability){
        //Vérifie si le client peut etre livré avec un double remorque ou non ?
        if(destination.getIsTrainPossible()== 0 && remorque_2 != null &&remorque_2.isAttached){
//            throw new Error("Impossible de livrer ce client avec un semi remorque");
            return false;
        }
        if(!enoughTime(destination)){//enought time to deliver & comeback
            return false;
        }
        float totalQuantity = remorque_1.getQuantityLeft() + (remorque_2 != null && remorque_2.isAttached ? remorque_2.getQuantityLeft() : 0);
        if(destination.getQuantity() > totalQuantity){//enought quantity to deliver at once
            return false;
        }
        
        //Est-ce que c'est rentable de livrer le client lors de cette tournée ?
        if(checkRentability && this.actionRealisees.size() > 2 && isRentableLivrerClient(destination)){
            return false;
        }
        
        Action delivery = new Action(currentEmplacement, destination, remorque_1, remorque_2, "NONE");
        ajouterAction(delivery);
        timeSpent += delivery.timeSpent;
        setCurrentEmplacement(destination);
        
        //Enlever le client de la liste des clients à livrer   
        
        if(LocationParser.myClients.equals(listClient)){
            listClient.remove(destination);
        }else{
            LocationParser.myClients.remove(destination);
            listClient.remove(destination);
        }
        
//        System.out.println("Livraison !");
//        System.out.println("Restant : " + LocationParser.myClients.size());
        return true;
    }
        public boolean livrerNoRetrait(Client destination, boolean checkRentability){
        //Vérifie si le client peut etre livré avec un double remorque ou non ?
        if(destination.getIsTrainPossible()== 0 && remorque_2 != null &&remorque_2.isAttached){
//            throw new Error("Impossible de livrer ce client avec un semi remorque");
            return false;
        }
        if(!enoughTime(destination)){//enought time to deliver & comeback
            return false;
        }
        float totalQuantity = remorque_1.getQuantityLeft() + (remorque_2 != null && remorque_2.isAttached ? remorque_2.getQuantityLeft() : 0);
        if(destination.getQuantity() > totalQuantity){//enought quantity to deliver at once
            return false;
        }
        
        //Est-ce que c'est rentable de livrer le client lors de cette tournée ?
        if(checkRentability && this.actionRealisees.size() > 2 && isRentableLivrerClient(destination)){
            return false;
        }
        
        Action delivery = new Action(currentEmplacement, destination, remorque_1, remorque_2, "NONE");
        ajouterAction(delivery);
        timeSpent += delivery.timeSpent;
        setCurrentEmplacement(destination);
        
        
//        System.out.println("Livraison !");
//        System.out.println("Restant : " + LocationParser.myClients.size());
        return true;
    }
    
    /**
     * Livre un client
     * @param destination
     * @param listClientALivrer
     */
    public boolean livrerWithoutCheckRentability(Client destination){
        //Vérifie si le client peut etre livré avec un double remorque ou non ?
        if(destination.getIsTrainPossible()== 0 && remorque_2 != null &&remorque_2.isAttached){
//            throw new Error("Impossible de livrer ce client avec un semi remorque");
            return false;
        }
        if(!enoughTime(destination)){//enought time to deliver & comeback
            return false;
        }
        float totalQuantity = remorque_1.getQuantityLeft() + (remorque_2 != null && remorque_2.isAttached ? remorque_2.getQuantityLeft() : 0);
        if(destination.getQuantity() > totalQuantity){//enought quantity to deliver at once
            return false;
        }
        
        Action delivery = new Action(currentEmplacement, destination, remorque_1, remorque_2, "NONE");
        ajouterAction(delivery);
        timeSpent += delivery.timeSpent;
        setCurrentEmplacement(destination);
        
        return true;
    }    
    
    public void park(LocationCSV destination){
        if(!destination.getLocation_type().equals("SWAP_LOCATION")){
            throw new Error("Impossible d'effectuer l'action à un non swap location");
        }
        if(!remorque_2.isAttached){
            throw new Error("Impossible de laisser notre seule remorque");
        }
        setCurrentEmplacement(destination);
        remorque_2.isAttached = false;
        //TODO ajouter le timeSpent park
    }
    public void swap(LocationCSV destination){
        if(!destination.getLocation_type().equals("SWAP_LOCATION")){
            throw new Error("Impossible d'effectuer l'action à un non swap location");
        }
        if(!remorque_2.isAttached){
            throw new Error("Impossible de swap notre seule remorque");
        }
        setCurrentEmplacement(destination);
        Remorque tempo = remorque_1;
        remorque_1 = remorque_2;
        remorque_2 = tempo;
        //TODO ajouter le timeSpent swap

        
    }
    public void pickup(LocationCSV destination){
        if(!destination.getLocation_type().equals("SWAP_LOCATION")){
            throw new Error("Impossible d'effectuer l'action à un non swap location");
        }
        if(remorque_2.isAttached){
            throw new Error("Impossible de prendre une 3eme remorque");
        }
        remorque_2.isAttached = true;
        setCurrentEmplacement(destination);
        //TODO ajouter le timeSpent pickup

    }
    public void exchange(LocationCSV destination){
        if(!destination.getLocation_type().equals("SWAP_LOCATION")){
            throw new Error("Impossible d'effectuer l'action à un non swap location");
        }
        if(!remorque_2.isAttached){
            throw new Error("Impossible d'échanger notre seule remorque");
        }
        this.swap(destination);
        this.park(destination);
        //TODO ajouter le timeSpent exchange
    }
    
    /**
     * Est-ce que l'on a assez de temps ?
     * @param destination
     * @return 
     */
    private boolean enoughTime(LocationCSV destination){
       int i =2*myDepot.getCoord().getId()+1;
       int j = destination.getCoord().getId();
       
       int i2 = 2*destination.getCoord().getId()+1;
       int j2 = currentEmplacement.getCoord().getId();
       if(destination instanceof Client){
            return timeSpent + DistanceTimesDataCSV.matrix[i][j] + DistanceTimesDataCSV.matrix[i2][j2] + ((Client)destination).getService_time()  <= FleetCSV.getOperating_time();
       }
            return timeSpent + DistanceTimesDataCSV.matrix[i][j] + DistanceTimesDataCSV.matrix[i2][j2] <= FleetCSV.getOperating_time();
   }
    /**
     * Récupère la distance entre deux locations
     * @param origine
     * @param destination
     * @return 
     */
    private int getDistanceBetweenTwoLocation(LocationCSV origine, LocationCSV destination){
        int i = 2*origine.getCoord().getId()+1;
        int j = destination.getCoord().getId();
        
        return DistanceTimesDataCSV.matrix[i][j];
    }
    
    /**
     * Récupère le temps de parcours entre deux locations
     * @param origine
     * @param destination
     * @return 
     */
    private int getTempsBetweenTwoLocation(LocationCSV origine, LocationCSV destination){
        int i = 2*origine.getCoord().getId();
        int j = destination.getCoord().getId();
        
        return DistanceTimesDataCSV.matrix[i][j];
    }
   
    /**
     * Cherche le client le plus loin par rapport à un point donnée
     * @param listeClient
     * @param pointDeDepart
     * @return 
     */
    private List<Client> chercherClientPlusLoin(List<Client> listeClient, LocationCSV pointDeDepart) {
                
        List<Client> listeClientOrdonnée = listeClient;
        
        //Sorting
        Collections.sort(listeClientOrdonnée, new Comparator<Client>() {
            @Override
            public int compare(Client client1, Client client2) {
                
                int tempsDistanceC1 = DistanceTimesDataCSV.getDifferenceTimeBetweenLocationAndClient(pointDeDepart, client1);
                int tempsDistanceC2 = DistanceTimesDataCSV.getDifferenceTimeBetweenLocationAndClient(pointDeDepart, client2);
                
                if (tempsDistanceC1 < tempsDistanceC2) {
                    return 1;
                } else if (tempsDistanceC1 > tempsDistanceC2) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
        
        return listeClientOrdonnée;
    }
    
    /**
     * Cherche le client le plus proche par rapport à un point donnée
     * @param listeClient
     * @param pointDeDepart
     * @return 
     */
    private List<Client> chercherClientPlusProche(List<Client> listeClient, LocationCSV pointDeDepart) {
                
        List<Client> listeClientOrdonnée = listeClient;
        
        //Sorting
        Collections.sort(listeClientOrdonnée, new Comparator<Client>() {
            @Override
            public int compare(Client client1, Client client2) {
                
                int tempsDistanceC1 = DistanceTimesDataCSV.getDifferenceTimeBetweenLocationAndClient(pointDeDepart, client1);
                int tempsDistanceC2 = DistanceTimesDataCSV.getDifferenceTimeBetweenLocationAndClient(pointDeDepart, client2);
                
                if (tempsDistanceC1 > tempsDistanceC2) {
                    return 1;
                } else if (tempsDistanceC1 < tempsDistanceC2) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
        
        return listeClientOrdonnée;
    }
    
    /**
     * Cherche le client le plus loin par rapport au camion et va le livrer
     * @param listeClient
     * @return 
     */
    public Boolean chercherPlusLoinParRapportAuCamionEtLivrer(List<Client> listeClient){
        
        for(Client c : chercherClientPlusLoin(listeClient, this.currentEmplacement)){
            if(livrer(c, listeClient, false)){
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Cherche le client le plus proche par rapport au camion et va le livrer
     * @param listeClient
     * @return 
     */
    public Boolean chercherPlusProcheParRapportAuCamionEtLivrer(List<Client> listeClient){
        
        for(Client c : chercherClientPlusProche(listeClient, this.currentEmplacement)){
            if(livrer(c, listeClient, true)){
                return true;
            }
        }
        
//        System.out.println(listeClient.toString());
        return false;
    }
    
    /**
     * Cherche le client le plus loin par rapport à la permière destination et va le livrer
     * @param listeClient
     * @return 
     */
    public Boolean chercherPlusLoinParRapportPermiereDestinationEtLivrer(List<Client> listeClient){
        
        for(Client c : chercherClientPlusLoin(listeClient, this.getActionRealisees().get(1).destinationLocation)){
            if(livrer(c, listeClient, false)){
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Cherche le client le plus proche par rapport à la permière destination et va le livrer
     * @param listeClient
     * @return 
     */
    public Boolean chercherPlusProcheParRapportPermiereDestinationEtLivrer(List<Client> listeClient){
        chercherClientPlusProche(listeClient, this.getActionRealisees().get(1).destinationLocation);
        int max = listeClient.size();
        int i = 0;
        while(i < max){
            if(livrer(listeClient.get(i), listeClient, true)){
                return true;
            }
            i++;  
        }
        return false;
    }
    
    public Boolean livrerDanslOrdre(List<Client> listeClient){
        int max = listeClient.size();
        int i = 0;
        while(i < max){
            if(livrer(listeClient.get(i), listeClient, true)){
                return true;
            }
            i++;  
        }
        return false;
    }
    
    
    
    /**
     * Cherche le client le plus loin par rapport au depot
     * @param listeClient
     * @return 
     */
    public Boolean chercherPlusLoinParRapportDepotEtLivrer(List<Client> listeClient){
        
        for(Client c : chercherClientPlusLoin(listeClient, this.getActionRealisees().get(0).destinationLocation)){
            if(livrer(c, listeClient, false)){
                return true;
            }
        }
        
        return false;
    }
    
     /**
     * Cherche le client le plus proche par rapport au depot
     * @param listeClient
     * @return 
     */
    public Boolean chercherPlusProcheParRapportDepotEtLivrer(List<Client> listeClient){
        
        for(Client c : chercherClientPlusProche(listeClient, this.getActionRealisees().get(0).destinationLocation)){
            if(livrer(c, listeClient, false)){
                return true;
            }
        }
        return false;
    }
    
    /**
     * Cherche le client le plus proche par rapport au client de reference et va le livrer
     * @param listeClient
     * @return 
     */
    public Boolean chercherPlusProcheParRapportPlusLoinDestinationEtLivrer(List<Client> listeClient, Client clientReference){
        
        for(Client c : chercherClientPlusProche(listeClient, clientReference)){
            if(livrer(c, listeClient, true)){
                return true;
            }
        }
        return false;
    }
    
    
    public void ordonnerTournée(boolean doubleRemorque, float sizeRemorque){
        
        LocationCSV plusLoinPoint = this.getActionRealisees().get(1).destinationLocation;
        
        int i = 2;
        Action actionAComparer;
        LocationCSV locationAComparer;
        List<Client> listClientDroite = new ArrayList<>();
        List<Client> listClientGauche = new ArrayList<>();
        
        float position = 0;
        
        // On coupe en deux les actions, celles qui se déroulent à droite de la droite
        // Et celles qui se déroulent à gauche (ou sur la droite)
        // On commence à 1 car on enlève le dépot
        // On enlève 1 car on enlève le retour au dépot
        for(i = 1 ; i < this.getActionRealisees().size(); i++){
            actionAComparer = this.getActionRealisees().get(i);
            locationAComparer = actionAComparer.destinationLocation;
            //Inutile pour l'instant car on prend que des clients, 
            //mais pourrait être utile avec des swap
            if(locationAComparer instanceof Client){
                position = distancePointDroite(plusLoinPoint,locationAComparer);
                if(position < 0 ){
                    //Droite
                    listClientDroite.add((Client)locationAComparer);
                }else{
                    //Gauche
                    listClientGauche.add((Client)locationAComparer);
                }
            }
        }
        //On reset les actions à effectuer pour pouvoir livrer la liste des clients que l'on a précédemment triée
        initVehicule(myDepot, doubleRemorque, sizeRemorque);
        
        //On va maintenant trier ces listes de manières à avoir en premier 
        //dans un cas les destinations les plus proches et dans l'autre les destionations les plus éloignées
        while(chercherPlusProcheParRapportAuCamionEtLivrer(listClientDroite));        
        while(chercherPlusProcheParRapportAuCamionEtLivrer(listClientGauche));
        
        LocationParser.myClients.addAll(listClientDroite);
        LocationParser.myClients.addAll(listClientGauche);
                
    }
    
    
    /**
     * Retourne la distance d'un point par rapport à une droite
     * @param b
     * @param c
     * @return Si <0 c'est à droite ; Si >0 c'est à gauche ; Sinon c'est sur la droite
     */
    public float distancePointDroite(LocationCSV b, LocationCSV c){
        float xA = myDepot.getCoord().getX();
        float xB = b.getCoord().getX();
        float xC = c.getCoord().getX();
        
        float yA = myDepot.getCoord().getY();
        float yB = b.getCoord().getY();
        float yC = c.getCoord().getY();
        
        return (xB - xA) * (yC - yA) - (yB - yA) * (xC - xA);
    }
        
    /**
     * Cherche le client le plus loin par rapport au depot et le retourne
     * @param listeClient
     * @return List<Client> Triée par le plus loin d'abord
     */
    public Client trierPlusLoinParRapportDepot(List<Client> listeClient){
        return chercherClientPlusLoin(listeClient, myDepot).get(0);        
    }
//    
//    /**
//     * Cherche le client le plus proche par rapport au depot et le retourne
//     * @param listeClient
//     * @return List<Client> Triée par le plus proche d'abord
//     */
//    public void trierPlusProcheParRapportDepotEtLivrer(List<Client> listeClient){
//        chercherClientPlusProche(listeClient, myDepot);        
//    }
    
    /**
     * Ajoute une action à la liste des déplacements / actions effectués par le véhicule
     * @param action
     * @return 
     */
    private Boolean ajouterAction(Action action){
        actionRealisees.add(action);
        return true;
    }
    
    private Boolean isRentableLivrerClient(Client client){
        
        Vehicule testNewVehicule = new Vehicule(myDepot, client.needDoubleTruck(), FleetParser.getCapacite());
        testNewVehicule.livrerWithoutCheckRentability(client);
        testNewVehicule.retour();
        //On a remove des clients à livrer dans le livrer (durant le test), on doit donc
        //le remettre pour la suite
//        LocationParser.myClients.add(client);
        
        double resultatNewVehicule = SolutionParser.getResultatForOneVehicule(testNewVehicule);
        
//        double resultatBackDirectToDepot = getAmountToBackToDepot();
//        double resultatDeliverAndBackToDepot = getAmountToDeliverClientAndGoBackToDepot(client) ;
//        double resultatKeepThisTournee =  resultatDeliverAndBackToDepot - resultatBackDirectToDepot;
        
        
        //Si service
        //Distance
        int distanceCurrentLocationToDestination = getDistanceBetweenTwoLocation(currentEmplacement, client);
        int distanceDestinationToDepot = getDistanceBetweenTwoLocation(client, myDepot);
        int distanceTotal = distanceCurrentLocationToDestination + distanceDestinationToDepot;
        double prixDistanceTotal =  distanceTotal * FleetParser.getCoutDistanceTruck();
        if(this.remorque_2 != null){
            prixDistanceTotal += distanceTotal * FleetParser.getCoutDistanceTruck();
        }
        
        //Temps
        int tempsCurrentLocationToDestination = getTempsBetweenTwoLocation(currentEmplacement, client);
        int tempsDestinationToDepot = getTempsBetweenTwoLocation(client, myDepot);
        int tempsService = client.getService_time();
        int tempsTotal = tempsCurrentLocationToDestination + tempsDestinationToDepot + tempsService;
        double prixTempsTotal =  tempsTotal * FleetParser.getCoutDuree();
        
        //Si retour direct
        //Distance
        int distanceRetourDirectDepot = getDistanceBetweenTwoLocation(currentEmplacement, myDepot);
        double prixDistanceRetourDirectDepot = distanceRetourDirectDepot * FleetParser.getCoutDistanceTruck();
        if(this.remorque_2 != null){
            prixDistanceRetourDirectDepot += distanceRetourDirectDepot * FleetParser.getCoutDistanceTruck();
        }
        
        //Temps 
        int tempsRetourDirectDepot = getTempsBetweenTwoLocation(currentEmplacement, myDepot);
        double prixTempsRetourDirectDepot = tempsRetourDirectDepot * FleetParser.getCoutDuree();
        
        return resultatNewVehicule  + prixDistanceRetourDirectDepot +  prixTempsRetourDirectDepot< prixDistanceTotal + prixTempsTotal ;
    }
    
    /**
     * Théorie : calcul le cout de la tournée si on va livrer le client et on retourne ensuite au depot
     * @param clientToDeliver
     * @return 
     */
     private double getAmountToDeliverClientAndGoBackToDepot(Client clientToDeliver){
        try {
            Vehicule v = (Vehicule) this.clone();
            v.livrerWithoutCheckRentability(clientToDeliver);
            v.retour();
            return SolutionParser.getResultatForOneVehicule(v);
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(Vehicule.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        throw new Error("Impossible de calculer le prix si on veut finir tout de suite la tournée");
        
        
    
    }
    
    /**
     * Théorie : calcul le cout de la tournée si on retourne tout de suite au dépot
     * @return 
     */
    private double getAmountToBackToDepot(){
        try {
            Vehicule v = (Vehicule) this.clone();
            v.retour();
            
            return SolutionParser.getResultatForOneVehicule(v);
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(Vehicule.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        throw new Error("Impossible de calculer le prix si on veut finir tout de suite la tournée");
    }
    
    
}
