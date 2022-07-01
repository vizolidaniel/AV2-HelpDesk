<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Set" %>
<%@ page import="br.com.daniel.security.domain.UserPrincipal" %>
<%@ page import="br.com.daniel.security.permissions.ViewRoles" %>

<%
    Object loggedUser = session.getAttribute("principal");
    if (loggedUser == null) {
        response.sendRedirect("/login");
        return;
    }

    UserPrincipal user = ((UserPrincipal) loggedUser);
    Set<String> roles = user.listRoles();

    boolean CAN_MANAGE_USERS = roles.containsAll(ViewRoles.USERS_ROOT_ROLES);
    boolean CAN_MANAGE_REQUESTS = roles.containsAll(ViewRoles.SERVICE_DESK_ROOT_ROLES);
    boolean IS_CLIENT = roles.containsAll(ViewRoles.CLIENT_ROOT_ROLES);
%>
<nav>
    <ul>
        <li><a href="/">Home</a></li>
        <% if (CAN_MANAGE_USERS) {
            %>
            <li><a href="/users">Gerenciar Usuários</a></li>
            <%
        } %>
        <% if (CAN_MANAGE_REQUESTS) {
            %>
            <li><a href="/service-desk">Atendimento</a></li>
            <%
        } %>
        <% if (IS_CLIENT) {
            %>
            <li><a href="/service-desk/requests">Chamados</a></li>
            <%
        } %>
        <li><a href="/logout">Logout</a></li>
    </ul>
    <style>
        ul {
           display: flex;
           justify-content: space-between;
        }
        ul li {
            list-style: none;
        }
        ul li::marker {
            content: '';
        }
    </style>
</nav>