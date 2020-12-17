/*
Class: PatientServlet
Description: the servlet for handing patient interactions
Created: 14/12/2020
Updated: 16/12/2020
Author/s: Asia Benyadilok
*/
package smartcare.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import smartcare.models.Appointment;
import smartcare.models.database.Jdbc;
import smartcare.models.User;

/**
 *
 * @author jitojar
 */
public class PatientServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    private HttpServletRequest bookAppointment(HttpServletRequest request){
        Jdbc jdbc = new Jdbc();
        HttpSession session = request.getSession();
        
        //get parameters from form
        String starttime = request.getParameter("starttime");
        //add ten minutes to start time (not implemented yet)
        String endtime = request.getParameter("starttime");
        String date = request.getParameter("date");
        String comment = request.getParameter("comment");
        
        //get the right user ID from the session variable
        User user = (User)session.getAttribute("user");
        System.out.println("userID = " + user.getUserID());
        
        //check if that time slot is free (not implemented yet)
        
        //Add to database
        String table = "appointments (appointmentdate, starttime, endtime, comment, patientID)";
        String values = "('"  + date + "', '"+ starttime+ "', '" 
                + endtime + "', '" + comment + "', " + user.getUserID() +")";
        
        int success = jdbc.addRecords(table, values);
        if(success != 0){
            request.setAttribute("updateSuccess", "The appointment has been scheduled!");
        }else{
            request.setAttribute("updateSuccess", "There has been a problem.");
        }
        
        return request;
    }
    
    private HttpServletRequest showAppointments(HttpServletRequest request){
        
        ArrayList<Appointment> appointmentList = new ArrayList<Appointment>();
        String appointments = new String();
        HttpSession session = request.getSession();
        Jdbc jdbc = new Jdbc();
        User user = (User)session.getAttribute("user");
        
        //retreve all available appointments from database
        String column = "appointmentid, starttime, endtime, appointmentdate, comment";
        int numOfColumns = 5;
        String table = "Appointments";
        String condition = "patientID = " + user.getUserID();
        
        //Get all of the appointments for this user
        appointments = jdbc.getResultSet(column, condition, table, numOfColumns);
        System.out.println("the appointments for this user are, appointments = " + appointments);
        
        //split the data received and put it into appointmentList
        String singleAppointment[] = appointments.split("<br>");
        for (String element : singleAppointment){
            String val[] = element.split(" ");
            Appointment temp = new Appointment(val[0], val[1], val[2], val[3], val[4], user.getUserID());
            appointmentList.add(temp);
            System.out.println(element);

        }
        
        //show on the patientLanding
        request.setAttribute("Appointments", appointmentList);
        
        return request;
    }
    
    private HttpServletRequest deleteAppointment(HttpServletRequest request){
        //after pressing the delete button the appointment will be deleted
        String appointmentId = request.getParameter("appointmentId");
        System.out.println("deleting appointment" + appointmentId);
        Jdbc jdbc = new Jdbc();
        
        int success = jdbc.delete("Appointments", "appointmentId = " + appointmentId);
        String deleteSuccess = new String();
        if(success == 0){
            deleteSuccess = "Failed to cancel appointment";
        }else{
            deleteSuccess = "Successfully cancelled appointment";
        }
        
        request.setAttribute("deleteSuccess", deleteSuccess);
        return request;
    }
    
      private HttpServletRequest reIssuePrescription(HttpServletRequest request){
        
        //create obj of database
        Jdbc jdbc = new Jdbc();
        HttpSession session = request.getSession();
        
                
       //get parameters from prescription form
       String patientID = request.getParameter("patientID");
       String issuedate = request.getParameter("issuedate");

       String prescription = null;
       String patientDetail = null;


       //get prescription from database
       try 
        {
            //get patient detail from database
            patientDetail = jdbc.getResultSet("firstname, lastname, dob", "uuid = "+patientID, "users",3);
            prescription = jdbc.getResultSet("weight, allergies, medicine", "(issuedate = '"+issuedate.toString()+"' AND patientid = "+patientID+")","prescription",3);
            String weight = jdbc.getResultSet("weight","(issuedate = '"+issuedate.toString()+"' AND patientid = "+patientID+")","prescription",1);
            String allergies = jdbc.getResultSet("allergies","(issuedate = '"+issuedate.toString()+"' AND patientid = "+patientID+")","prescription",1);
            String medicine = jdbc.getResultSet("medicine", "(issuedate = '"+issuedate.toString()+"' AND patientid = "+patientID+")","prescription",1);
            
            //check if patient and prescription are available or not
            if(!patientDetail.equals(""))
            {
                if(!prescription.equals(""))
                {
                    String detailList [] = patientDetail.split(" ");
                    session.setAttribute("prescriptionDetail","Patient Name: "+detailList[0]+"<br/>"+
                                                              "Patient Surname: "+detailList[1]+"<br/>"+
                                                              "Date of Birth : "+detailList[2]+"<br/>"+
                                                              "Weight : "+weight+"<br/>"+
                                                              "Allergies : "+allergies+"<br/>"+
                                                              "Medicine : "+medicine+"<br/>");
                } 
                else
                {
                    session.setAttribute("prescriptionDetail","Prescription not found!");
                }
            }
            else
            {
                session.setAttribute("prescriptionDetail","Patient not found!");
                
            }
        }

        catch(Exception e)
        {
            session.setAttribute("prescriptionDetail","Patient not found!");
        }

        
        //when deleting an appointment
        
        return request;
    }
    
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String viewPath = "views/landing/patientLanding.jsp";
        
        //get action from patient landing
        String action = request.getParameter("action");
        
        if(action.equals("Book Appointment")){
            request = bookAppointment(request);
        }
        else if(action.equals("request for re-issue"))
        {
            request = reIssuePrescription(request);
        }
        if(action.equals("Cancel")){
            request = deleteAppointment(request);
        }
        
        
        request = showAppointments(request);
        
        RequestDispatcher view = request.getRequestDispatcher(viewPath);
        view.forward(request,response);
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
        processRequest(request, response);
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
