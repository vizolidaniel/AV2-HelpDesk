<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="br.com.daniel.model.decorator.ServiceRequestWithUsersData" %>
<%@ page import="br.com.daniel.domain.ServiceRequestStatus" %>
<%@ page import="java.util.List" %>

<%
    List<ServiceRequestWithUsersData> requests = (List<ServiceRequestWithUsersData>) request.getAttribute("requests");
    int thisPage = (int) request.getAttribute("thisPage");
    int thisSize = (int) request.getAttribute("thisSize");
    int nextPage = (int) request.getAttribute("nextPage");
    int previousPage = (int) request.getAttribute("previousPage");
    ServiceRequestStatus[] thisPossibleStatus = (ServiceRequestStatus[]) request.getAttribute("possibleStatus");
    ServiceRequestStatus selectedStatus = (ServiceRequestStatus) request.getAttribute("selectedStatus");
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta id="viewport" name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, viewport-fit=cover">
<title>HelpDesk - Chamados</title>
<script type="text/javascript" src="/resources/scripts.js" ></script>
<link rel="stylesheet" type="text/css" href="/resources/styles.css">

<script>
    var filterStatus = function(event) {
        var status = event.target.value;
        addQueryParam('status', status);
    }
</script>
</head>
<body>
    <%@include file="../includes/nav.jsp"%>
    <%@include file="../includes/message.jsp"%>
	<%String serviceDeskHeaderTitle="Chamados";%>
    <%@include file="header.jsp"%>

    <div>
        <table cellspacing="0">
            <tr>
                <th>ID</th>
                <th>Aberto Para</th>
                <th>Descrição</th>
                <th>
                    <select id="status" onchange="filterStatus(event)">
                        <%
                            if (selectedStatus == null) {
                                %>
                                    <option value="">Status</option>
                                <%
                            } else {
                                %>
                                    <option value="<%=selectedStatus.name()%>"><%=selectedStatus.getTranslation()%></option>
                                <%
                            }
                            for (ServiceRequestStatus status : thisPossibleStatus) {
                                if(status != selectedStatus) {
                                    %>
                                        <option value="<%=status.name()%>"><%=status.getTranslation()%></option>
                                    <%
                                }
                            }
                        %>
                    </select>
                </th>
                <th>Analista</th>
            </tr>
            <%
                for (int i = 0; i < requests.size(); i++) {
                    ServiceRequestWithUsersData thisRequest = requests.get(i);
                    %>
                        <tr>
                            <td><a href="/requests/<%=thisRequest.getId()%>"><%=thisRequest.getReadableId()%></a></td>
                            <td><%=thisRequest.getCreatedByAsString()%></td>
                            <td><%=thisRequest.getShortDescription()%></td>
                            <td><%=thisRequest.getStatus().getTranslation()%></td>
                            <%
                                if (thisRequest.gotToAnalysis()) {
                                    %>
                                        <td><%=thisRequest.getAnalyzedByAsString()%></td>
                                    <%
                                } else {
                                    %>
                                        <td></td>
                                    <%
                                }
                            %>
                        </tr>
                    <%
                }
            %>
        </table>

        <div class="page-controls">
            <a href="/requests?page=<%=previousPage%>&size=<%=thisSize%>&status=<%=selectedStatus%>">&lt;</a>
            <a href="/requests?page=<%=thisPage%>&size=<%=thisSize%>&status=<%=selectedStatus%>"><%=thisPage%></a>
            <a href="/requests?page=<%=nextPage%>&size=<%=thisSize%>&status=<%=selectedStatus%>">&gt;</a>
        </div>
    </div>
</body>
</html>