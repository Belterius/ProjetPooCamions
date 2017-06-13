/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FinalClass;

/**
 *
 * @author Gimlibéta
 */
public class MeilleurRentabiliteObject {
    
    /**
     * Il faut insérer entre l'origine et la destination de cette action
     */
    public Action actionToModify; 
    
    /**
     * Nouveau client à servir et à insérer
     */
    public Client clientToInsert;
    
    /**
     * Le prix du détour
     */
    public double price;

    public MeilleurRentabiliteObject(Action actionToModify, Client clientToInsert, double price) {
        this.actionToModify = actionToModify;
        this.clientToInsert = clientToInsert;
        this.price = price;
    }
    
    
    
}
