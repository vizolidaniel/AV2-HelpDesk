<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>HelpDesk - Chamados</title>
<script>
    var submit = function() {
        document.getElementById("form").submit();
    }
</script>
</head>
<body>
    <%@include file="../includes/nav.jsp"%>
    <%@include file="../includes/message.jsp"%>
	<%String serviceDeskHeaderTitle="Abrir Chamado";%>
    <%@include file="header.jsp"%>

    <div class="requests-create">
        <form id="form" method="POST" action="/requests/create">
            <fieldset>
                <label for="description">Descrição:</label>
                <textarea id="description" name="description" ></textarea>
            </fieldset>
        </form>
        <button type="button" onclick="submit()">Abrir</button>
    </div>
    <style>
        .requests-create fieldset {
            border: none;
            display: flex;
            flex-direction: column;
        }
        .requests-create textarea {
            min-width: 100%;
            width: 100%;
            max-width: 50rem;
            max-height: 20.5rem;
            min-height: 20.5rem;
        }
    </style>
</body>
</html>