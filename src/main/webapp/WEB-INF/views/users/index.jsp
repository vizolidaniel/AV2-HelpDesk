<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="br.com.daniel.security.domain.UserPrincipal" %>

<%
    Object userAttr = request.getAttribute("users");
    if (userAttr == null) {
        response.sendRedirect("/");
        return;
    }

    List<UserPrincipal> users = new ArrayList<UserPrincipal>((Set<UserPrincipal>) userAttr);
    boolean hasNextPage = (boolean) request.getAttribute("hasNextPage");
    boolean hasPreviousPage = (boolean) request.getAttribute("hasPreviousPage");
    int thisPage = (int) request.getAttribute("page");
    int thisSize = (int) request.getAttribute("size");
    int nextPage = hasNextPage ? (int) request.getAttribute("nextPage") : thisPage;
    int previousPage = hasPreviousPage ? (int) request.getAttribute("previousPage") : thisPage;
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>HelpDesk - Usuários</title>
<script>
    var excluir = function(userId, userEmail) {
        var confirmation = confirm("Deseja excluir o usuário " + userEmail + "?");
        if (confirmation == 1) {
            console.log("Excluir");
            window.location.href = '/users/delete/' + userId;
        } else {
            console.log("Não excluir");
        }
    }
</script>
</head>
<body>
    <%@include file="../includes/message.jsp"%>
    <%@include file="../includes/nav.jsp"%>
    <%String usersHeaderTitle="Usuários";%>
    <%@include file="header.jsp"%>

    <div>
        <table>
            <tr>
                <th>Nome</th>
                <th>E-mail</th>
                <th colspan="2">Ações</th>
            </tr>
            <%
                for (int i = 0; i < users.size(); i++) {
                    UserPrincipal thisUser = users.get(i);
                    %>
                        <tr>
                            <td><a href="/users/update/<%=thisUser.getId()%>"><%=thisUser.getName()%></a></td>
                            <td><a href="/users/update/<%=thisUser.getId()%>"><%=thisUser.getEmail()%></a></td>
                            <td><a href="/users/update/<%=thisUser.getId()%>">Atualizar</a></td>
                            <td><a href="#" onclick="excluir('<%=thisUser.getId()%>','<%=thisUser.getEmail()%>')">Excluir</a></td>
                        </tr>
                    <%
                }
            %>
        </table>

        <div class="page-controls">
            <a href="/users?page=<%=previousPage%>&size=<%=thisSize%>">&lt;</a>
            <a href="/users?page=<%=thisPage%>&size=<%=thisSize%>"><%=thisPage%></a>
            <a href="/users?page=<%=nextPage%>&size=<%=thisSize%>">&gt;</a>
        </div>
	</div>
	<style>
	    .page-controls {
	        margin: auto;
	        width: 8rem;
	        display: flex;
	        justify-content: space-between;
	    }
	</style>
</body>
</html>