<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    Object principal = session.getAttribute("principal");
    boolean isLogged = principal != null;

    Object msgAttr = request.getAttribute("message");
    if (msgAttr == null) {
        response.sendRedirect("/login");
        return;
    }

    String message = (String) msgAttr;
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>HelpDesk</title>
</head>
<body>
    <% if (isLogged) {%>
        <%@include file="includes/nav.jsp"%>
    <%}%>
	<p styles="color: red;"><%=message%></p>
</body>
</html>