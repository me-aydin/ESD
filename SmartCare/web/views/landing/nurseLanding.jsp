<%-- 
    Document   : nurseLanding
    Created on : 06-Dec-2020, 09:34:16
    Author     : Michael
--%>

<%@page import="smartcare.models.Appointment"%>
<%@page import="java.util.ArrayList"%>
<%@page import="smartcare.models.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>SmartCare - Nurse</title>
        <link rel="icon" 
              type="image/png" 
              href="https://i.imgur.com/Vwma7mV.png">
    </head>
    <body>

        <%
            //allow access only if session exists
            String user = (String) session.getAttribute("userEmail");
            String userName = null;
            String sessionID = null;
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("user")) {
                        userName = cookie.getValue();
                    }
                    if (cookie.getName().equals("JSESSIONID")) {
                        sessionID = cookie.getValue();
                    }
                }
            }
        %>
        <div class="top-bar">
            <p>Nurse Dashboard</p>

            <div class="logout-button">
                <form action="Logout.do" method="post" class="logout-form">
                    <input type="submit" value="Logout" >
                </form>  
            </div>
        </div>

        <div class="outer-container">
            <div class="appointment-container">
                <h2>Welcome Nurse <%out.println(((User)session.getAttribute("user")).getName());%></h2>
                
                <div class="appointment-list">
                    <%
                        //check if to show doctors appointments
                        boolean showTable = false;
                        if(!((ArrayList)request.getAttribute("appointments")).isEmpty()){
                            showTable = true;
                        }
                    %>
                    <%
                        if(showTable){
                            out.print("<h4>Your daily booked appointments:</h4>");
                        }else{
                            out.print("<h4>You have no appointments scheduled for today</h4>");
                        }
                    %>

                    <table <% if(!showTable){out.print("hidden='true'");} %> >
                        <tr>
                            <th>Appointment ID</th>
                            <th>Start time</th>
                            <th>Comment</th>
                            <th>Date</th>
                        </tr>
                        <%
                            if (showTable) {
                                ArrayList<Appointment> a;
                                a = (ArrayList) request.getAttribute("appointments");

                                //loop through all of the appointments in the array list
                                for (Appointment appointment : a) {
                                    out.print("<input type='hidden' name='appointmentId' value='" + appointment.getID() + "'");
                                    out.print("<tr>");
                                    out.print("<td>");
                                    out.print(appointment.getID());
                                    out.print("</td>");
                                    out.print("<td>");
                                    out.print(appointment.getDate());
                                    out.print("</td>");
                                    out.print("<td>");
                                    out.print(appointment.getStarttime());
                                    out.print("</td>");
                                    out.print("<td>");
                                    out.print(appointment.getComment());
                                    out.print("</td>");
                                    out.print("</tr>");
                                }
                            }
                        %>
                    </table>
                </div>
            </div>
        </div>
            
    </body>
    <style>
        <%@ include file="../css/nurseLanding.css" %>
    </style>
</html>
