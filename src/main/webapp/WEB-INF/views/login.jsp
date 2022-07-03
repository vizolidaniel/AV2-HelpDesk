<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta id="viewport" name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, viewport-fit=cover">
<title>HelpDesk - Login</title>
<script type="text/javascript" src="/resources/scripts.js" ></script>
<link rel="stylesheet" type="text/css" href="/resources/styles.css">
<script>
    var submit = function() {
        var senhaElement = document.getElementById("senha");
        senhaElement.value = btoa(senhaElement.value);

        document.getElementById("form").submit();
    }
</script>
</head>
<body>
    <%@include file="includes/message.jsp"%>
	<div class="login">
	    <form id="form" method="POST" action="/login">
	        <p>Login</p>
	        <input type="email" placeholder="E-mail" id="email" name="email" />
	        <input type="password" placeholder="Senha" id="senha" name="senha" />
	    </form>
	    <button type="button" onclick="submit()">Entrar</button>
	</div>
	<style>
        .login {
            display: flex;
            flex-direction: column;
            width: 100%;
            max-width: 21rem;
            margin: auto;
            text-align: center;
            justify-content: space-between;
            border: solid 0.01rem #cecece;
            border-radius: 1rem;
        }
	    form {
            display: flex;
            flex-direction: column;
            width: 100%;
            max-width: 20rem;
            margin: auto;
            justify-content: space-between;
            margin-bottom: 1rem;
            text-align: center;
            border: solid 0.01rem #cecece;
            border-radius: 1rem;
            padding: 0.5rem;
        }
        form input {
            margin-bottom: 1rem;
        }
        button {
            height: 2rem;
            border-bottom-right-radius: 1rem;
            border-bottom-left-radius: 1rem;
        }
	</style>
</body>
</html>