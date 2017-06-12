 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Gimlibéta
 */

public class CoordinateCSV implements Serializable {
    
    private int idCoordinateCSV;
    private float x;
    private float y;

    public CoordinateCSV(int id, String x, String y) {
        this.idCoordinateCSV = id;
        this.x = Float.parseFloat(x);
        this.y = Float.parseFloat(y);
    }

    public int getId() {
        return idCoordinateCSV;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
