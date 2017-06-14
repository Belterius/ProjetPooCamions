/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier;

import FinalClass.Client;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Gimlibéta
 */
@Entity
public abstract class LocationCSV implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @Column(name = "IDLOCATION")
    private Long idLocation;
    
    /** TODO - Utiliser un enum pour la "location_type" **/
    protected String location_type;
    protected String location_id;
    protected String post_code;
    protected String city;
    protected CoordinateCSV coord;

    
    /**
     * Gets the unique Id
     * @return 
     */
    public int getId(){
        return this.coord.getId();
    }

    public String getLocation_type() {
        return location_type;
    }

    public String getLocation_id() {
        return location_id;
    }

    public String getPost_code() {
        return post_code;
    }

    public String getCity() {
        return city;
    }

    public CoordinateCSV getCoord() {
        return coord;
    }
    
    public long getTimeTo(LocationCSV loc){
        int i = 2*loc.getCoord().getId()+1;
        int j = this.getCoord().getId();
        return DistanceTimesDataCSV.matrix[i][j];
    }
    
}
