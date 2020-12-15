<%-- 
    Document   : login
    Created on : 03-Dec-2020, 09:18:55
    Author     : Michael
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Smart Care: Login</title>
    </head>
    <body>
        <div>
            <form method="post" action="<%=request.getContextPath() %>/Login.do">
                <p>Email</p>
                <input type="text" name="email">
                <p>Password:</p>
                <input type="password" name="password"> <br />
                <input type="Submit" value="Login">
            </form>
        </div>
        <div>
            <%
                String error = (String)request.getAttribute("errorMsg");
                if (error != null) out.println(error);
            %>
        </div>
    </body>
</html>
