/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FinalClass;

import Parser.FleetParser;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import metier.CoordinateCSV;
import metier.LocationCSV;

/**
 *
 * @author belterius
 */
@Entity
@Table(name = "CLIENT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Client.findAll", query = "SELECT a FROM Client a")
    , @NamedQuery(name = "Client.findById", query = "SELECT a FROM Client a WHERE a.location_id = :IDLOCATION")})
public class Client extends LocationCSV implements Serializable{
    
    @Basic(optional = false)
    @Column(name = "QUANTITY")
    private int quantity;
    
    @Basic(optional = false)
    @Column(name = "ISTRAINPOSSIBLE")
    private int isTrainPossible;
    
    @Basic(optional = false)
    @Column(name = "SERVICETIME")
    private int service_time;

    protected Client() {
    }
    
    /**
     * 
     */
    public Client(String location_type, String location_id, String post_code, String city, int quantity, int nb_possible_train, int service_time, CoordinateCSV coord) {
        this.location_type = location_type;
        this.location_id = location_id;
        this.post_code = post_code;
        this.city = city;
        this.coord = coord;
        this.isTrainPossible = nb_possible_train;
        this.service_time = service_time;
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    /**
     * 0 ou 1
     * @return 
     */
    public int getIsTrainPossible() {
        return isTrainPossible;
    }

    public int getService_time() {
        return service_time;
    }
    
    /**
     * A t on besoin d'un double camion ou non
     * @return 
     */
    public Boolean needDoubleTruck(){
        return (this.quantity > FleetParser.getCapacite());
    }
    
}
