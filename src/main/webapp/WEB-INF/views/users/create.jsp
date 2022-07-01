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
<title>HelpDesk - Cadastrar Usuário</title>
<script>
    var submit = function() {
        var senhaElement = document.getElementById("senha");
        senhaElement.value = btoa(senhaElement.value);

        document.getElementById("form").submit();
    }
</script>
</head>
<body>
    <%
        Object param = session.getAttribute("message");
        if (param != null) {
            %>
                <div>
                    <% String message = (String) param; %>
                    <p style="color: red;"><%= message %></p>
                </div>
            <%
        }
    %>
    <% session.removeAttribute("message"); %>
    <%@include file="../includes/nav.jsp"%>
	<div class="update-form">
	    <form id="form" method="POST" action="/users/create">
	        <fieldset>
                <label for="nome">Nome:</label>
                <input type="text" placeholder="Nome" id="nome" name="nome" required />
	        </fieldset>

            <fieldset>
                <label for="email">E-mail:</label>
                <input type="email" placeholder="E-mail" id="email" name="email" required />
	        </fieldset>

            <fieldset>
                <label for="senha">Senha:</label>
                <input type="password" placeholder="Senha" id="senha" name="senha" required />
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