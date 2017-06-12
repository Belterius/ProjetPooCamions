/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

/**
 *
 * @author loic
 */
public class JpaFactory {
    
    public JpaSolutionDao getJpaSolutionDao() {
        return JpaSolutionDao.getInstance();
    }
    
    public JpaSolutionIndexDao getJpaSolutionIndexDao() {
        return JpaSolutionIndexDao.getInstance();
    }
    
    
}
