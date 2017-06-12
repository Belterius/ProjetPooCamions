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

    public int getCapacity() {
        return capacity;
    }

    public float getCosts_mu_km() {
        return costs_mu_km;
    }

    public float getCosts_mu_h() {
        return costs_mu_h;
    }

    public float getCosts_mu_usage() {
        return costs_mu_usage;
    }

    public static float getOperating_time() {
        return operating_time;
    }
    
    
    
}
