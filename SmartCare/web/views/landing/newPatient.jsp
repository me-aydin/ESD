<%-- 
    Document   : newPatient
    Created on : 15-Dec-2020, 22:01:25
    Author     : David
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>New patient page</h1>
        
        <h1>Patient adding page</h1>
        <h4>Fill in the form to add a new patient</h4>
        <form action="<%=request.getContextPath() %>/AddPatient.do" name="addpatient" method="Post">
            First name: <input type="text" name="firstname"><br/>
            Last name: <input type="text" name="lastname"><br/>
            Date of birth: <input type="date" name="dob"><br/>
            Email: <input type="text" name="email"><br/>
            Password: <input type="password" name="password"><br/>
            Phone: <input type="text" name="phone"><br/>
            Address: <input type="text" name="address"><br/>

            <button type="Submit" value="Submit">Submit</button>
        </form>
        <p>
            <% 
               // if(request.getAttribute("updateSuccess")!=null){
                //    out.println(request.getAttribute("updateSuccess"));
                //}
            %>
        </p>
        <%
            String error = (String)request.getAttribute("updateSuccess");
            if (error != null) out.println(error);
            %>
    </body>
</html>
