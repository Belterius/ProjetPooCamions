/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import metier.Solution;

/**
 *
 * @author loic
 */
public class JpaSolutionDao extends JpaDao<Solution> implements SolutionDao {

    private JpaSolutionDao() {
        
    }
    
    private static class SingletonHolder {
        private final static JpaSolutionDao instance = new JpaSolutionDao();
    }
    
    public static JpaSolutionDao getInstance() {
        return SingletonHolder.instance;
    }
    
    @Override
    public Solution find(Integer id) {
        Query query = em.createNamedQuery("Solution.findById");
        query.setParameter("id", id);
        return (Solution) query.getSingleResult();
    }
    
    @Override
    public List<Solution> findAll() {
        Query query = em.createNamedQuery("Solution.findAll");
        return (List<Solution>) query.getResultList();
    }

    @Override
    public boolean deleteAll() {
        final EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            Query query = em.createQuery("DELETE FROM Solution");
            query.executeUpdate();
            et.commit();
        } catch (Exception ex) {
            et.rollback();
            return false;
        }
        return true;
    }
    
}
