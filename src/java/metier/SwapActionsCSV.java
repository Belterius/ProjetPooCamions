/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Gimlibéta
 */
public class SwapActionsCSV {
    
    private Map<String, Integer> hm;

    /**
     * Initializes this class
     */
    public SwapActionsCSV() {
        this.hm = new HashMap<>();
    }
    
    /**
     * Adds the given action, and associates this action to the given duration
     * @param action
     * @param duration 
     */
    public void addAction(String action, int duration){
        this.hm.put(action, duration);
    }
    
    /**
     * Return the duration corresponding to the action
     * @param action
     * @return the duration corresponding to the action
     */
    public int getDurationFromAction(String action){
        return hm.get(action);
    }
    
}
