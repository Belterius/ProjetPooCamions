/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import dao.JpaFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import metier.Solution;
import metier.SolutionIndex;

/**
 *
 * @author loic
 */
public class Test1 {
    public static void main(String[] args) {
//        JpaFactory myFactory = new JpaFactory();
//        for(SolutionIndex sol : myFactory.getJpaSolutionIndexDao().findAll()){
//            System.out.println("id : " + sol.getIdSolution() + "Type :" + sol.getSolutionType());
//            int idSol = Integer.parseInt(sol.getIdSolution().toString());
//            System.out.println("******************************************************************************************");
//            for(Solution detailsol : myFactory.getJpaSolutionDao().findBySolutionIndex(idSol)){
//                System.out.println(detailsol.toString());
//            }
//        }
        
        JpaFactory myFactory = new JpaFactory();
        List<Solution> mySols = new ArrayList<>();
        for(SolutionIndex sol : myFactory.getJpaSolutionIndexDao().findAll()){
            int idSol = Integer.parseInt(sol.getIdSolution().toString());
            for(Solution detailsol : myFactory.getJpaSolutionDao().findBySolutionIndex(idSol)){
                mySols.add(detailsol);
            }
            break;
        }
       Collections.sort(mySols);
       
       for(Solution sol : mySols){
           System.out.println(sol.toString());
       }
    }
}
