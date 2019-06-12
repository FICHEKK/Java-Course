<%@ page import="hr.fer.zemris.java.hw14.database.PollOption"%>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>

<html>
	<body bgcolor="${pickedBgCol}">
 		<h1>${pollTitle}</h1>
 		<p>${pollMessage}!</p>
 		<ol>
 			<%
 				List<PollOption> pollOptions = (List<PollOption>) request.getSession().getAttribute("pollOptions");
 				
 				for(PollOption option : pollOptions) {
 					out.println("<li><a href=glasanje-glasaj?id=" + option.id + ">" + option.title + "</a></li>");
 				}
 			%>
 		</ol>
	</body>
</html>