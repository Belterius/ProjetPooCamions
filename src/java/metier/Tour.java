/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier;

import FinalClass.Client;
import FinalClass.Depot;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author belterius
 */
public class Tour {
    private List<Client> clients = new ArrayList<>();
    private Depot depot;

    public List<Client> getClients() {
        return clients;
    }

    public Depot getDepot() {
        return depot;
    }

    /**
     * Constructor par défaut
     */
    public Tour() {
        this.depot = null;
        this.clients = null;
    }

    /**
     * Constructor
     * @param depot
     * @param clients 
     */
    public Tour(Depot depot, List<Client> clients) {
        this.depot = depot;
        this.clients = new ArrayList(clients);
    }
    
    /**
     * Calcule le temps d'une tournée
     * @return 
     */
    public long getTempsTour(){
        if(clients == null)
            return Long.MAX_VALUE;
        if(depot == null)
            return Long.MAX_VALUE;
        long tempsTour = 0;
        tempsTour += depot.getTimeTo(clients.get(0));
        
        for(int i = 1; i<clients.size();i++){
            tempsTour += clients.get(i-1).getTimeTo(clients.get(i));
        }
        tempsTour += clients.get(clients.size()-1).getTimeTo(depot);
        return tempsTour;
    }
    
    
}
