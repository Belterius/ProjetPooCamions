/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FinalClass;

import java.io.Serializable;
import metier.DistanceTimesDataCSV;
import metier.LocationCSV;

/**
 *
 * @author belterius
 */
public class Action implements Serializable {

    LocationCSV origineLocation;
    
    LocationCSV destinationLocation;
    
    private Vehicule vehiculeActing;
    
    boolean semi_Trailer_Attached;
    
    int id_First_Remorque;
    
    int id_Second_Remorque;
    
    String swap_Action;
    
    float quantity_Swap_Body_1;
    
    float quantity_Swap_Body_2;
    
    int timeSpent;
    
    int distanceSpent;

    protected Action() {
    }

    public int getTimeSpent() {
        return timeSpent;
    }

    public int getDistanceSpent() {
        return distanceSpent;
    }

    public LocationCSV getOrigineLocation() {
        return origineLocation;
    }

    public LocationCSV getDestinationLocation() {
        return destinationLocation;
    }

    public boolean isSemi_Trailer_Attached() {
        return semi_Trailer_Attached;
    }

    public int getId_First_Remorque() {
        return id_First_Remorque;
    }

    public int getId_Second_Remorque() {
        return id_Second_Remorque;
    }

    public String getSwap_Action() {
        return swap_Action;
    }

    public float getQuantity_Swap_Body_1() {
        return quantity_Swap_Body_1;
    }

    public float getQuantity_Swap_Body_2() {
        return quantity_Swap_Body_2;
    }

    /**
     * 
     * @param destinationLocation
     * @param remoqueEmplacement1
     * @param remoqueEmplacement2
     * @param swapAction 
     */
    public Action(LocationCSV origineLocation, LocationCSV destinationLocation, Remorque remoqueEmplacement1, Remorque remoqueEmplacement2, String swapAction) {
        this.origineLocation = origineLocation;        
        this.destinationLocation = destinationLocation;

        id_First_Remorque = remoqueEmplacement1.getIdOrigine();
        swap_Action = swapAction.toUpperCase();
        if(remoqueEmplacement2 != null){
            semi_Trailer_Attached = remoqueEmplacement2.isAttached;
            id_Second_Remorque = remoqueEmplacement2.getIdOrigine();
            
            livrerClient(remoqueEmplacement1, remoqueEmplacement2);
        }else{
            livrerClient(remoqueEmplacement1);
        }
        
    }

    public void livrerClient(Remorque rq1, Remorque rq2) {
        if(destinationLocation instanceof Client){
            float quantitytToRemove = ((Client)destinationLocation).getQuantity();
            float qtrq1;
            float qtrq2;
            if (rq1.getQuantityLeft() > rq2.getQuantityLeft()) {
                qtrq1 = rq1.enleverQuantite(quantitytToRemove);
                quantitytToRemove -= qtrq1;
                qtrq2 = rq2.enleverQuantite(quantitytToRemove);
                quantitytToRemove -= qtrq2;
                if (quantitytToRemove != 0) {
                    throw new Error("Quantité insuffisante !");
                }
            } else {
                qtrq2 = rq2.enleverQuantite(quantitytToRemove);
                quantitytToRemove -= qtrq2;
                qtrq1 = rq1.enleverQuantite(quantitytToRemove);
                quantitytToRemove -= qtrq1;
                if (quantitytToRemove != 0) {
                    throw new Error("Quantité insuffisante !");
                }
            }
            
            if(rq1.getIdOrigine() == 1){
                quantity_Swap_Body_1 = qtrq1;
                quantity_Swap_Body_2 = qtrq2;
            }else{
                quantity_Swap_Body_1 = qtrq2;
                quantity_Swap_Body_2 = qtrq1;
            }
            
        }else{
            quantity_Swap_Body_1 = 0;
            quantity_Swap_Body_2 = 0;
        }
        addTimeSpent();
        addDistanceSpent();
    }
    public void livrerClient(Remorque rq1) {
        if(destinationLocation instanceof Client){
            float quantitytToRemove = ((Client)destinationLocation).getQuantity();
            float qtrq1;
                qtrq1 = rq1.enleverQuantite(quantitytToRemove);
                quantitytToRemove -= qtrq1;
                if (quantitytToRemove != 0) {
                    throw new Error("Quantité insuffisante !");
                }
                    quantity_Swap_Body_1 = qtrq1;
        }else{
            quantity_Swap_Body_1 = 0;
            quantity_Swap_Body_2 = 0;
        }
        addTimeSpent();
        addDistanceSpent();
    }

    @Override
    public String toString() {
        return String.format("%s;%s;%s;%s;%s;%s;%s;%s",this.destinationLocation.getLocation_id(), this.destinationLocation.getLocation_type(), this.semi_Trailer_Attached ? 1 : 0, this.id_First_Remorque, this.id_Second_Remorque, swap_Action, quantity_Swap_Body_1, quantity_Swap_Body_2);
    }
//    public void addTimeSpent(){
//        try{
//            if(destinationLocation instanceof Client)
//                timeSpent =DistanceTimesDataCSV.matrix[2*origineLocation.getCoord().getId()+1][destinationLocation.getCoord().getId()] + ((Client)destinationLocation).getService_time();
//            else
//                timeSpent =DistanceTimesDataCSV.matrix[2*origineLocation.getCoord().getId()+1][destinationLocation.getCoord().getId()];
//        }catch(Exception e){//Pour l'init
//            System.out.println(e);
//        }
//    }
    public void addTimeSpent(){
        try{
            int i = 2*destinationLocation.getCoord().getId()+1;
            int j = origineLocation.getCoord().getId();
            if(destinationLocation instanceof Client)
                timeSpent =DistanceTimesDataCSV.matrix[i][j] + ((Client)destinationLocation).getService_time();
            else
                timeSpent =DistanceTimesDataCSV.matrix[i][j];
        }catch(Exception e){
            if(origineLocation == null){
                System.out.println("Nouveau Camion");
            }else{
                System.out.println(e);
            }
        }
    }
    
    private void addDistanceSpent(){
        try{
            int i = 2*destinationLocation.getCoord().getId();
            int j = origineLocation.getCoord().getId();
            
            distanceSpent = DistanceTimesDataCSV.matrix[i][j];
        }catch(Exception e){
            System.out.println(e);
        }
    }
}
