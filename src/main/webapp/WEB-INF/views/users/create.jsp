<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="br.com.daniel.security.domain.Role"%>

<%
    Object rolesAttr = request.getAttribute("roles");

    if (rolesAttr == null) {
        response.sendRedirect("/users");
        return;
    }
    List<Role> allRoles = (List<Role>) rolesAttr;
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta id="viewport" name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, viewport-fit=cover">
<title>HelpDesk - Cadastrar Usuário</title>
<script>
    var submit = function() {
        var senhaElement = document.getElementById("password");
        senhaElement.value = btoa(senhaElement.value);

        document.getElementById("form").submit();
    }
</script>
</head>
<body>
    <%@include file="../includes/message.jsp"%>
    <%@include file="../includes/nav.jsp"%>
    <%String usersHeaderTitle="Cadastro de Usuário";%>
    <%@include file="header.jsp"%>
	<div class="update-form">
	    <form id="form" method="POST" action="/users/create">
	        <fieldset>
                <label for="name">Nome:</label>
                <input type="text" placeholder="Nome" id="name" name="name" required />
	        </fieldset>

            <fieldset>
                <label for="email">E-mail:</label>
                <input type="email" placeholder="E-mail" id="email" name="email" required />
	        </fieldset>

            <fieldset>
                <label for="password">Senha:</label>
                <input type="password" placeholder="Senha" id="password" name="password" required />
	        </fieldset>

	        <%
	            for (Role role : allRoles) {
	                %>
	                    <fieldset>
                            <input type="checkbox" id="<%=role.getId()%>" name="roles" value="<%=role.getId()%>" />
                            <label for="<%=role.getId()%>"><%=role.getRole()%></label>
	                    </fieldset>
	                <%
	            }
	        %>
	    </form>
	    <button type="button" onclick="submit()">Salvar</button>
	</div>

	<style>
	    .update-form form {
	        display: flex;
	        flex-direction: column;
	    }
	    .update-form form fieldset {
            border: none;
        }
	</style>
</body>
</html>