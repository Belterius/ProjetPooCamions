/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
}
