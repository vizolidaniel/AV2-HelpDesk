<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Set" %>
<%@ page import="br.com.daniel.security.permissions.ViewRoles" %>

<nav>
    <ul>
        <li><a href="/">Home</a></li>
        <% if (ViewRoles.canManageUsers()) {
            %>
            <li><a href="/users">Gerenciar Usuários</a></li>
            <%
        } %>
        <% if (ViewRoles.canViewRequests()) {
            %>
            <li><a href="/requests">Chamados</a></li>
            <%
        } %>
        <% if (ViewRoles.canViewSelfRequests()) {
            %>
            <li><a href="/requests/my">Meus Chamados</a></li>
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
