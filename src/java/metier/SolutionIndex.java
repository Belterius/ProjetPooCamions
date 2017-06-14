/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier;

import FinalClass.Client;
import FinalClass.Vehicule;
import Parser.FleetParser;
import Parser.LocationParser;
import dao.JpaFactory;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author loic
 */
@Entity
@Table(name = "SOLUTIONINDEX")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SolutionIndex.findAll", query = "SELECT a FROM SolutionIndex a")
    , @NamedQuery(name = "SolutionIndex.findById", query = "SELECT a FROM SolutionIndex a WHERE a.idSolution = :id")
})
public class SolutionIndex implements Serializable {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSolution;
    
    private String solutionType;
    
    @OneToMany(mappedBy="solutionindex", cascade = CascadeType.ALL) 
    private List<Solution> solutions;

    public Long getIdSolution() {
        return idSolution;
    }

    public String getSolutionType() {
        return solutionType;
    }
    
    public SolutionIndex(){
        solutions = new ArrayList<>();
    }
    public SolutionIndex(String solutionType){
        solutions = new ArrayList<>();
        this.solutionType = solutionType;
    }
    
    public void addSolution(Solution sol){
        sol.setSolutionindex(this);
        solutions.add(sol);
    }
    
    /**
     * 
     * @param location
     * @param fleet 
     */
    public List<Vehicule> databaseToEntities(LocationParser location, FleetParser fleet){
        Set<String> trucks = new HashSet<>();
        
        for(Solution sol : solutions){
            String truckString = sol.toString().split(";")[0];
            trucks.add(truckString);
        }
        List<Vehicule> myTrucks = new ArrayList<>();
        for(String s : trucks){
            List<Solution> truckAction = new JpaFactory().getJpaSolutionDao().findBySolutionIndexAndVehicule(Integer.valueOf(this.idSolution.toString()), s);
            Collections.sort(truckAction);
            Vehicule myTruck = new Vehicule(location.getMyDepots().get(0),truckAction.get(0).isSemi_trailer_attached(), fleet.getMyFleets().get(2).getCapacity());
            for(Solution sol : truckAction){
                if(!sol.getLocation_id().contains("D")){
                    Client myClient = location.getMyClients().stream().filter(client -> client.getLocation_id().equals(sol.getLocation_id())).findFirst().get();
                    myTruck.livrer(myClient);
                }
            }
            myTruck.retour();
            myTrucks.add(myTruck);
        }
        return myTrucks;
    }
    
    
}
