/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlleur;

import dao.JpaFactory;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.Collator;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import metier.Solution;
import metier.SolutionIndex;

/**
 *
 * @author loic
 */
@WebServlet(name = "Controleur", urlPatterns = {"/Controleur"})
public class Controleur extends HttpServlet {
    JpaFactory myFactory = new JpaFactory();

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Controleur</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Controleur at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        try (PrintWriter out = response.getWriter()) {
            switch(action){
                case "accueil" :
                    doShowAccueil(request, response);
                break; 
                case "detailSolution" :
                    doDetailSolution(request, response, out);
                break;
                case "detailVehicule" :
                    dodetailVehicule(request, response, out);
                break;
                default :
                    response.sendError(response.SC_BAD_REQUEST, "error");
                break;
            }
        }
    }
    private void doShowAccueil(HttpServletRequest request, HttpServletResponse
    response) throws ServletException, IOException {
                try {
            request.setAttribute("listSolutions", myFactory.getJpaSolutionIndexDao().findAll());
            forward("/vue/accueil.jsp", request, response);
        } catch (ServletException ex) {
            Logger.getLogger(Controleur.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Controleur.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void dodetailVehicule(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws ServletException, IOException {
                try {
            List<Solution> myVehicules = new ArrayList<>();
            for(Solution sol : myFactory.getJpaSolutionDao().findBySolutionIndexAndVehicule(Integer.parseInt(request.getParameter("idSolution")), request.getParameter("routeNumber"))){
                    myVehicules.add(sol);
            }
            java.util.Collections.sort(myVehicules);
            request.setAttribute("myVehicule", myVehicules);
            forward("/vue/vehicule.jsp", request, response);
        } catch (Exception ex) {
            Logger.getLogger(Controleur.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void doDetailSolution(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
        
        try {

            List<Solution> mySolutions = myFactory.getJpaSolutionDao().findBySolutionIndex(Integer.parseInt(request.getParameter("idSolution")));
            java.util.Collections.sort(mySolutions);
            
            List<String> uniqueRoutes = new ArrayList<>();
            for(Solution sol : mySolutions){
                if(!uniqueRoutes.contains(sol.getTour_id())){
                    uniqueRoutes.add(sol.getTour_id());
                }
            }
            request.setAttribute("myVehicules", uniqueRoutes);            
            request.setAttribute("idSolution", request.getParameter("idSolution"));

            forward("/vue/solution.jsp", request, response);
        } catch (ServletException ex) {
            Logger.getLogger(Controleur.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Controleur.class.getName()).log(Level.SEVERE, null, ex);
        }catch(Exception e){
            System.out.println(e);
        }
    }

    private void forward(String url, HttpServletRequest request, HttpServletResponse
    response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher(url);
        rd.forward(request, response);
    }
    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
