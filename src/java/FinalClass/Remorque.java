/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FinalClass;

import java.io.Serializable;
import metier.LocationCSV;

/**
 *
 * @author belterius
 */
public class Remorque implements Serializable {

    private int idOrigine; //1 ou 2
    
    LocationCSV currentRemorqueLocation;
    
    Boolean isAttached;
    
    float quantityLeft;

    public int getIdOrigine() {
        return idOrigine;
    }

    public void setIdOrigine(int idOrigine) {
        this.idOrigine = idOrigine;
    }

    public LocationCSV getCurrentRemorqueLocation() {
        return currentRemorqueLocation;
    }

    public void setCurrentRemorqueLocation(LocationCSV currentRemorqueLocation) {
        this.currentRemorqueLocation = currentRemorqueLocation;
    }

    public Boolean getIsAttached() {
        return isAttached;
    }

    public void setIsAttached(Boolean isAttached) {
        this.isAttached = isAttached;
    }

    public float getQuantityLeft() {
        return quantityLeft;
    }

    public void setQuantityLeft(float quantityLeft) {
        this.quantityLeft = quantityLeft;
    }
    
    public Remorque(){
        
    }
    /**
     * 
     * @param idOrigine
     * @param currentRemorqueLocation
     * @param isAttached
     * @param quantityLeft 
     */
    public Remorque(int idOrigine, LocationCSV currentRemorqueLocation, Boolean isAttached, float quantityLeft) {
        this.idOrigine = idOrigine;
        this.currentRemorqueLocation = currentRemorqueLocation;
        this.isAttached = isAttached;
        this.quantityLeft = quantityLeft;
    }
    
    /**
     * Enlève la quantité demandée
     * @param quantity
     * @return 
     */
    public float enleverQuantite(float quantity){
        if(quantity > quantityLeft){
            float qtRemoved = quantityLeft;
            quantityLeft = 0;
            return qtRemoved;
        }
        quantityLeft -= quantity;
        return quantity;
    }
}
