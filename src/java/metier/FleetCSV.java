/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier;

/**
 *
 * @author Gimlibéta
 */
public class FleetCSV {
    
    private String type;
    private int capacity;
    private float costs_mu_km;
    private float costs_mu_h;
    private float costs_mu_usage;
    private static float operating_time;

    /**
     * Constructor
     * @param type
     * @param capacity
     * @param costs_mu_km
     * @param costs_mu_h
     * @param costs_mu_usage
     * @param operating_time 
     */
    public FleetCSV(String type, int capacity, float costs_mu_km, float costs_mu_h, float costs_mu_usage, int operating_time) {
        this.type = type;
        this.capacity = capacity;
        this.costs_mu_km = costs_mu_km;
        this.costs_mu_h = costs_mu_h;
        this.costs_mu_usage = costs_mu_usage;
        this.operating_time = operating_time;
    }

    public String getType() {
        return type;
    }

    /**
     * Permet d'avoir la capacité max d'un swap_body
     * @return 
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Permet d'avoir le coût en fonction des km d'un swap body
     * @return 
     */
    public float getCosts_mu_km() {
        return costs_mu_km;
    }

    /**
     * Permet d'avoir le coût du conducteur en fonction des heures
     * @return 
     */
    public float getCosts_mu_h() {
        return costs_mu_h;
    }

    /**
     * Permet d'avoir le coût d'usage unique d'une tournée
     * @return 
     */
    public float getCosts_mu_usage() {
        return costs_mu_usage;
    }

    /**
     * Permet d'avoir le temps max pour une tournée
     * @return 
     */
    public static float getOperating_time() {
        return operating_time;
    }
    
    
    
}
