/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import metier.SolutionIndex;

/**
 *
 * @author loic
 */
public class JpaSolutionIndexDao extends JpaDao<SolutionIndex> implements SolutionIndexDao {

    private JpaSolutionIndexDao() {
        
    }
    
    private static class SingletonHolder {
        private final static JpaSolutionIndexDao instance = new JpaSolutionIndexDao();
    }
    
    public static JpaSolutionIndexDao getInstance() {
        return SingletonHolder.instance;
    }
    
    @Override
    public SolutionIndex find(Integer id) {
        Query query = em.createNamedQuery("SolutionIndex.findById");
        query.setParameter("id", id);
        return (SolutionIndex) query.getSingleResult();
    }
    
    @Override
    public List<SolutionIndex> findAll() {
        Query query = em.createNamedQuery("SolutionIndex.findAll");
        return (List<SolutionIndex>) query.getResultList();
    }

    @Override
    public boolean deleteAll() {
        final EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            Query query = em.createQuery("DELETE FROM SolutionIndex");
            query.executeUpdate();
            et.commit();
        } catch (Exception ex) {
            et.rollback();
            return false;
        }
        return true;
    }
    
}
