<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="br.com.daniel.model.decorator.ServiceRequestWithUsersDataAndComments" %>
<%@ page import="br.com.daniel.model.decorator.ServiceRequestCommentWithUsersData" %>
<%@ page import="br.com.daniel.domain.ServiceRequestStatus" %>
<%@ page import="java.util.List" %>

<%
    ServiceRequestWithUsersDataAndComments thisRequest = (ServiceRequestWithUsersDataAndComments) request.getAttribute("request");
    List<ServiceRequestCommentWithUsersData> thisComments = thisRequest.getComments();
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta id="viewport" name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, viewport-fit=cover">
<title>HelpDesk - Chamado <%=thisRequest.getReadableId()%></title>
<script type="text/javascript" src="/resources/scripts.js" ></script>
<link rel="stylesheet" type="text/css" href="/resources/styles.css">

<script>
    var commentAndClose = function() {
        var commentForm = document.getElementById("add-comment-form");
        commentForm.action = '/requests/<%=thisRequest.getId()%>/comment?close=true';
        commentForm.submit();
    }
</script>
</head>
<body>
    <%@include file="../includes/nav.jsp"%>
    <%@include file="../includes/message.jsp"%>
	<%String serviceDeskHeaderTitle="Chamado " + thisRequest.getReadableId();%>
    <%@include file="header.jsp"%>

    <div class="request">
        <div class="data">
            <p><b>Descrição:</b> <%=thisRequest.getDescription()%></p>
            <p><b>Status:</b> <%=thisRequest.getStatus().getTranslation()%></p>
            <p><b>Aberto Por:</b> <%=thisRequest.getCreatedByAsString()%></p>
            <p><b>Aberto Em:</b> <%=thisRequest.getCreatedAtAsString()%></p>
            <%
                if (thisRequest.gotToAnalysis()) {
                    %>
                        <p><b>Em análise por:</b> <%=thisRequest.getAnalyzedByAsString()%></p>
                    <%
                }
            %>
        </div>
        <div class="comments">
            <%
                for (ServiceRequestCommentWithUsersData comment : thisComments) {
                    %>
                        <div class="comment">
                            <p><b>Comentário:</b> <%=comment.getComment()%></p>
                            <p><b>Feito Em:</b> <%=comment.getCreatedAtAsString()%></p>
                            <p><b>Feito Por:</b> <%=comment.getCreatedByName()%></p>
                            <%
                                if (thisComments.indexOf(comment) == thisComments.size() - 1 && thisRequest.getStatus() == ServiceRequestStatus.CLOSED) {
                                    %>
                                        <p><b>Comentário Finalizador</b></p>
                                    <%
                                }
                            %>
                        </div>
                    <%
                }
            %>
        </div>
    </div>
    <%
        if (thisRequest.getStatus() != ServiceRequestStatus.CLOSED) {
            %>
                <div class="add-comment">
                    <form id="add-comment-form" method="POST" action="/requests/<%=thisRequest.getId()%>/comment">
                        <input type="hidden" id="service_requests_id" value="<%=thisRequest.getId()%>" />
                        <fieldset>
                            <label for="comment">Comentário:</label>
                            <textarea maxlength="2048" placeholder="Escreva algo aqui..." id="comment" name="comment" ></textarea>
                        </fieldset>
                        <fieldset>
                            <button type="submit">Adicionar Commentário</button>
                            <button type="button" onclick="commentAndClose()">Fechar Chamado com Commentário</button>
                        </fieldset>
                    </form>
                </div>
            <%
        }
    %>
    <style>
        .request .data {
            border-bottom: solid 0.05rem #cecece;
        }
        .request .comments {
            width: 100%;
            max-width: 30rem;
            text-align: center;
            margin: auto;
            padding: 1rem 0rem;
            margin-top: 0.5rem;
        }
        .request .comments .comment {
            border: solid 0.05rem #cecece;
            border-radius: 1rem;
            margin-bottom: 1rem;
        }

        .request .comments .comment::after {
            content: '|';
            color: #cecece;
            text-align: center;
            position: absolute;
            margin-top: -3px;
        }
        .request .comments .comment:last-child::after {
            content: none;
        }

        .request .comments .comment::before {
            content: '|';
            color: #cecece;
            text-align: center;
            position: absolute;
            margin-top: -15px;
        }

        .request .comments .comment:first-child::before {
            content: none;
        }
    </style>
</body>
</html>