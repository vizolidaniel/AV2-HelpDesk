<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="br.com.daniel.security.permissions.ViewRoles" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta id="viewport" name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, viewport-fit=cover">
<title>HelpDesk - Home</title>
</head>
<body>
    <%@include file="includes/nav.jsp"%>
    <%@include file="includes/message.jsp"%>
    <div class="main-menu">
        <%
            if (ViewRoles.canManageUsers()) {
                %>
                    <div class="item">
                        <a href="/users">Usuários</a>
                    </div>
                <%
            }
            if (ViewRoles.canViewRequests()) {
                %>
                    <div class="item">
                        <a href="/requests">Chamados</a>
                    </div>
                <%
            }
        %>
    </div>
</body>
</html>