/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import metier.Solution;

/**
 *
 * @author loic
 */
public interface SolutionDao extends Dao<Solution> {
    
    public List<Solution> findBySolutionIndex(int solutionIndexId);
}
