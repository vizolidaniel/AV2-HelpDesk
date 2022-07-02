<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    Object principal = session.getAttribute("principal");
    boolean isLogged = principal != null;
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>HelpDesk - ERROR</title>
</head>
<body>
    <% if (isLogged) {%>
        <%@include file="../includes/nav.jsp"%>
    <%}%>
	<%@include file="../includes/message.jsp"%>
	<%
	    if (message == null) {
            response.sendRedirect("/login");
            return;
        }
	%>
</body>
</html>