/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FinalClass;

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
@Table(name = "SWAPLOCATION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SwapLocation.findAll", query = "SELECT a FROM SwapLocation a")
    , @NamedQuery(name = "SwapLocation.findById", query = "SELECT a FROM SwapLocation a WHERE a.location_id = :IDLOCATION")})
public class SwapLocation extends LocationCSV implements Serializable{

    protected SwapLocation() {
    }
    
    public SwapLocation(String location_type, String location_id, String post_code, String city, CoordinateCSV coord){
        this.location_type = location_type;
        this.location_id = location_id;
        this.post_code = post_code;
        this.city = city;
        this.coord = coord;
    }
}
