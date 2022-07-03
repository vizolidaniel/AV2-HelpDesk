<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta id="viewport" name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, viewport-fit=cover">
<title>HelpDesk - Chamados</title>
<script type="text/javascript" src="/resources/scripts.js" ></script>
<link rel="stylesheet" type="text/css" href="/resources/styles.css">
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
                <textarea id="description" name="description" maxlength="2048" ></textarea>
            </fieldset>
        </form>
        <button type="button" onclick="submit()">Abrir</button>
    </div>
</body>
</html>