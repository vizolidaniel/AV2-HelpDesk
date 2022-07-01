<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="br.com.daniel.security.domain.Role"%>

<%
    Object userAttr = request.getAttribute("updatingUser");
    Object rolesAttr = request.getAttribute("roles");

    if (userAttr == null || rolesAttr == null) {
        response.sendRedirect("/users");
        return;
    }

    UserPrincipal thisUser = (UserPrincipal) userAttr;
    List<Role> allRoles = (List<Role>) rolesAttr;
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>HelpDesk - Atualizar Usuário</title>
<script>
    var submit = function() {
        var confirmation = confirm("Deseja mesmo atualizar este usuário?");
        if (confirmation == 0) return;

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
	    <form id="form" method="POST" action="/users/update">
	        <input type="hidden" value="<%=thisUser.getId()%>" id="id" name="id" />
	        <fieldset>
                <label for="nome">Nome:</label>
                <input type="text" placeholder="Nome" id="nome" name="nome" value="<%=thisUser.getName()%>" required />
	        </fieldset>

            <fieldset>
                <label for="email">E-mail:</label>
                <input type="email" placeholder="E-mail" id="email" name="email" value="<%=thisUser.getEmail()%>" required />
	        </fieldset>

            <fieldset>
                <label for="senha">Senha:</label>
                <input type="password" placeholder="Senha" id="senha" name="senha" required />
	        </fieldset>

	        <%
	            for (Role role : allRoles) {
	                String checked = thisUser.listRoles().contains(role.getRole()) ? " checked" : " ";
	                %>
	                    <fieldset>
                            <input type="checkbox" id="<%=role.getId()%>" name="roles"<%=checked%> value="<%=role.getId()%>" />
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