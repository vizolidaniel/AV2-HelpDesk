<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="br.com.daniel.security.permissions.ViewRoles" %>

<div class="requests-header">
    <h1><%=serviceDeskHeaderTitle%></h1>
    <% if (ViewRoles.canCreateRequests()) {
        %>
            <a href="/requests/create">Cadastrar Novo</a>
        <%
    } %>

    <style>
        .requests-header {
            display: flex;
            flex-direction: column;
            padding: 0.5rem;
            margin: 2rem 2rem;
            background-color: #f9f9f9;
        }
    </style>
</div>
