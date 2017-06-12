/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 *
 * @author belterius
 */
public abstract class JpaDao<T> implements Dao<T> {
    
    private final String persistUnitName = "Projet_PooPU";
    protected final EntityManager em;
    private EntityManagerFactory emf;

    public JpaDao() {
        emf = Persistence.createEntityManagerFactory(persistUnitName);
        em = emf.createEntityManager();
    }

    @Override
    public boolean create(T obj) {
        final EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            em.persist(obj);
            et.commit();
            return true;
        } catch (Exception e) {
            if (et.isActive()) {
                et.rollback();
            }
            return false;
        }
    }

    @Override
    public boolean update(T obj) {
        final EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            em.merge(obj);
            et.commit();
            return true;
        } catch (Exception e) {
            if (et.isActive()) {
                et.rollback();
            }
            return false;
        }
    }

    @Override
    public boolean delete(T obj) {
        final EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            em.remove(obj);
            et.commit();
            return true;
        } catch (Exception e) {
            if (et.isActive()) {
                et.rollback();
            }
            return false;
        }
    }

    @Override
    public void close() {
        if (em != null && em.isOpen())
            em.close();
    }
    
}
