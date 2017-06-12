/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier;

import FinalClass.Action;
import FinalClass.Vehicule;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author belterius
 */

@Entity
@Table(name = "SOLUTION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Solution.findAll", query = "SELECT a FROM Solution a")
    , @NamedQuery(name = "Solution.findById", query = "SELECT a FROM Solution a WHERE a.idSolution = :id")
    , @NamedQuery(name = "Solution.findBySolutionIndex", query = "SELECT a FROM Solution a WHERE a.solutionindex.idSolution = :idindex")
    , @NamedQuery(name = "Solution.findBySolutionIndexAndVehicule", query = "SELECT a FROM Solution a WHERE a.solutionindex.idSolution = :idindex AND a.tour_id = :idvehicule")
})
public class Solution implements Serializable, Comparable<Solution> {
    @Basic(optional=false) 
    private int tour_id;
    @Basic(optional=false) 
    private int tour_position;
    @Basic(optional=false) 
    private String location_id;
    @Basic(optional=false) 
    private String location_x;
    @Basic(optional=false) 
    private String location_y;
    @Basic(optional=false) 
    private String location_type;
    @Basic(optional=false) 
    private boolean semi_trailer_attached;
    @Basic(optional=false) 
    private int swap_body_trailer;
    @Basic(optional=false) 
    private int swap_body_semi_trailer;
    @Basic(optional=false) 
    private String swap_action;
    @Basic(optional=false) 
    private float swap_body_1_quantity;
    @Basic(optional=false) 
    private float swap_body_2_quantity;
    
    
    @ManyToOne SolutionIndex solutionindex;
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSolution;

    public SolutionIndex getSolutionindex() {
        return solutionindex;
    }

    public void setSolutionindex(SolutionIndex solutionindex) {
        this.solutionindex = solutionindex;
    }

    public String getLocation_x() {
        return location_x;
    }

    public String getLocation_y() {
        return location_y;
    }


    public Solution() {
    }


    /**
     * 
     * @param tour_id                   Numéro de tournée
     * @param tour_position             Numéro de l'action dans la tournée
     * @param location_type             DEPOT, SWAP_LOCATION, CUSTOMER
     * @param location_id               Le meme que dans location
     * @param semi_trailer_attached     Camion = 0 ; Routier = 1
     * @param swap_body_trailer         Camion = 1 ; Sinon quel swap body en premier
     * @param swap_body_semi_trailer    Camion = 0 ; Sinon quel swap-body en dernier
     * @param swap_action               None si pas un location != swap ; Sinon le nom du swap_action
     * @param swap_body_1_quantity      Si pas client = 0 ; Sinon, quantité livré par le swap_body 1
     * @param swap_body_2_quantity      Si pas client = 0 ; Sinon, quantité livré par le swap_body 2
     */

    public Solution(int tour_id, int tour_position, LocationCSV loc, boolean truck,int swap_body_trailer ,int swap_body_semi_trailer,String swap_action, int qt1, int qt2){
        this.tour_id = tour_id;
        this.tour_position = tour_position;
        this.location_type = loc.getLocation_type();
        this.location_id = loc.getLocation_id();
        this.semi_trailer_attached = truck;
        this.swap_body_trailer = swap_body_trailer;
        this.swap_body_semi_trailer = swap_body_semi_trailer;
        this.swap_action = swap_action;
        this.swap_body_1_quantity = qt1;
        this.swap_body_2_quantity = qt2;
    }
    
    
    public Solution(int tour_id, int tour_position, String location_type, String location_id, boolean semi_trailer_attached, int swap_body_trailer, int swap_body_semi_trailer, String swap_action, int swap_body_1_quantity, int swap_body_2_quantity) {
        this.tour_id = tour_id;
        this.tour_position = tour_position;
        this.location_type = location_type;
        this.location_id = location_id;
        this.semi_trailer_attached = semi_trailer_attached;
        this.swap_body_trailer = swap_body_trailer;
        this.swap_body_semi_trailer = swap_body_semi_trailer;
        this.swap_action = swap_action;
        this.swap_body_1_quantity = swap_body_1_quantity;
        this.swap_body_2_quantity = swap_body_2_quantity;
    }
    
    public Solution(int route, int actionId, Action action){
        this.tour_id = route;
        this.tour_position = actionId;
        this.location_type = action.getDestinationLocation().getLocation_type();
        this.location_id = action.getDestinationLocation().getLocation_id();
        this.location_x = String.valueOf(action.getDestinationLocation().getCoord().getX());        
        this.location_y = String.valueOf(action.getDestinationLocation().getCoord().getY());        
        this.semi_trailer_attached = action.isSemi_Trailer_Attached();
        this.swap_body_trailer = action.getId_First_Remorque();
        this.swap_body_semi_trailer = action.getId_Second_Remorque();
        this.swap_action = action.getSwap_Action();
        this.swap_body_1_quantity = action.getQuantity_Swap_Body_1();
        this.swap_body_2_quantity = action.getQuantity_Swap_Body_2();
    }

    public String getTour_id() {
        return "R"+tour_id;
    }

    public void setTour_id(int tour_id) {
        this.tour_id = tour_id;
    }

    public int getTour_position() {
        return tour_position;
    }

    public void setTour_position(int tour_position) {
        this.tour_position = tour_position;
    }

    public String getLocation_type() {
        return location_type;
    }

    public void setLocation_type(String location_type) {
        this.location_type = location_type;
    }

    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    public boolean isSemi_trailer_attached() {
        return semi_trailer_attached;
    }

    public void setSemi_trailer_attached(boolean semi_trailer_attached) {
        this.semi_trailer_attached = semi_trailer_attached;
    }

    public int getSwap_body_trailer() {
        return swap_body_trailer;
    }

    public void setSwap_body_trailer(int swap_body_trailer) {
        this.swap_body_trailer = swap_body_trailer;
    }

    public int getSwap_body_semi_trailer() {
        return swap_body_semi_trailer;
    }

    public void setSwap_body_semi_trailer(int swap_body_semi_trailer) {
        this.swap_body_semi_trailer = swap_body_semi_trailer;
    }

    public String getSwap_action() {
        return swap_action;
    }

    public void setSwap_action(String swap_action) {
        this.swap_action = swap_action;
    }

    public float getSwap_body_1_quantity() {
        return swap_body_1_quantity;
    }

    public void setSwap_body_1_quantity(int swap_body_1_quantity) {
        this.swap_body_1_quantity = swap_body_1_quantity;
    }

    public float getSwap_body_2_quantity() {
        return swap_body_2_quantity;
    }

    public void setSwap_body_2_quantity(int swap_body_2_quantity) {
        this.swap_body_2_quantity = swap_body_2_quantity;
    }

    @Override
    public String toString() {
        return String.format("%s;%s;%s;%s;%s;%s;%s;%s;%s;%s",this.getTour_id(), tour_position, location_id, location_type, semi_trailer_attached ? 1 : 0, swap_body_trailer, swap_body_semi_trailer, swap_action, swap_body_1_quantity, swap_body_2_quantity);
    }

    public Long getIdSolution() {
        return idSolution;
    }

    public void setIdSolution(Long idSolution) {
        this.idSolution = idSolution;
    }

    @Override
    public int compareTo(Solution o) {
        if(o.tour_id > this.tour_id){
            return -1;
        }
        if(o.tour_id < this.tour_id){
            return 1;
        }
        if(o.tour_position > this.tour_position){
            return -1;
        }
        if(o.tour_position < this.tour_position){
            return 1;
        }
        return 0;
    }
    
    
}
