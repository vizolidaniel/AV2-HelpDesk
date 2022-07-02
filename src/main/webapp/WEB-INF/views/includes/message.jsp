<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
    Object msgSessionAttr = session.getAttribute("message");
    Object msgRequestAttr = request.getAttribute("message");

    String message = msgSessionAttr != null ? (String) msgSessionAttr : null;
    if (message == null) message = msgRequestAttr != null ? (String) msgRequestAttr : null;

    if (message != null) {
        %>
            <div class="message">
                <p style="color: red;"><%= message %></p>
            </div>
        <%
    }
%>
<% session.removeAttribute("message"); %>
<% request.removeAttribute("message"); %>