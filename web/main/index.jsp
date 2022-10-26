<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Lab #6</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
    </head>
    <body>
        <p>Registration Desk:</p>
        <a href="<%= request.getContextPath() %>/main/registration.html">registration</a>
        <a href="<%= request.getContextPath() %>/main/session.html">session</a>
        <%
            if ( request.isUserInRole("administrator")) {
        %>
        <a href="<%= request.getContextPath() %>/main/attendee.html">attendee</a>
        <%
            }
        %>
        <p>
            <input type="button" value="Logout" onclick="window.open('<%= request.getContextPath() %>/main/logout.jsp', '_self', false);" />
        </p>
    </body>
</html>