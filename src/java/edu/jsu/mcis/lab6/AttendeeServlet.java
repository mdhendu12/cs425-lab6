/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package edu.jsu.mcis.lab6;

import edu.jsu.mcis.lab6.dao.AttendeeDAO;
import edu.jsu.mcis.lab6.dao.DAOFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author matthewh
 */
public class AttendeeServlet extends HttpServlet {

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
        try ( PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AttendeeServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AttendeeServlet at " + request.getContextPath() + "</h1>");
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
        
        DAOFactory daoFactory = null;

        ServletContext context = request.getServletContext();

        if (context.getAttribute("daoFactory") == null) {
            System.err.println("*** Creating new DAOFactory ...");
            daoFactory = new DAOFactory();
            context.setAttribute("daoFactory", daoFactory);
        }
        else {
            daoFactory = (DAOFactory) context.getAttribute("daoFactory");
        }
        
        response.setContentType("application/json; charset=UTF-8");
        
        try ( PrintWriter out = response.getWriter()) {
            
            
            AttendeeDAO dao = daoFactory.getAttendeeDAO();
                        
            String p_id = request.getParameter("attendeeid");            
            if (p_id == null || "".equals(p_id)) { 
                out.println(dao.list());
            }
            else {
                int attendeeid = Integer.parseInt(request.getParameter("attendeeid"));
                out.println(dao.list(attendeeid));
            }
            
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
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
        
        DAOFactory daoFactory = null;

        ServletContext context = request.getServletContext();

        if (context.getAttribute("daoFactory") == null) {
            System.err.println("*** Creating new DAOFactory ...");
            daoFactory = new DAOFactory();
            context.setAttribute("daoFactory", daoFactory);
        }
        else {
            daoFactory = (DAOFactory) context.getAttribute("daoFactory");
        }
        
        response.setContentType("application/json; charset=UTF-8");
        
        
        try ( PrintWriter out = response.getWriter()) {
                      
            HashMap<String, String> hm = new HashMap<>();
            
            hm.put("displayname", request.getParameter("displayname"));
            hm.put("firstname", request.getParameter("firstname"));
            hm.put("lastname", request.getParameter("lastname"));
            
            AttendeeDAO dao = daoFactory.getAttendeeDAO();
            
            out.println(dao.create(hm));
            
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        DAOFactory daoFactory = null;
        BufferedReader br = null;
        ServletContext context = request.getServletContext();

        if (context.getAttribute("daoFactory") == null) {
            System.err.println("*** Creating new DAOFactory ...");
            daoFactory = new DAOFactory();
            context.setAttribute("daoFactory", daoFactory);
        }
        else {
            daoFactory = (DAOFactory) context.getAttribute("daoFactory");
        }
        
        response.setContentType("application/json; charset=UTF-8");
        
        
        try ( PrintWriter out = response.getWriter()) {
            
            br = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String p = URLDecoder.decode(br.readLine().trim(), Charset.defaultCharset());
            
            HashMap<String, String> hm = new HashMap<>();
            
            String[] pairs = p.trim().split("&");
            
            for (int i = 0; i < pairs.length; ++i) {
                String[] pair = pairs[i].split("=");
                hm.put(pair[0], pair[1]);
            }
                                 
//            HashMap<String, String> hm = new HashMap<>();
            
//            hm.put("attendeeid", request.getParameter("attendeeid"));
//            hm.put("displayname", request.getParameter("displayname"));
//            hm.put("firstname", request.getParameter("firstname"));
//            hm.put("lastname", request.getParameter("lastname"));
                      
            AttendeeDAO dao = daoFactory.getAttendeeDAO();
            
            out.println(dao.update(hm));
            
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Attendee Servlet";
    }// </editor-fold>

}
