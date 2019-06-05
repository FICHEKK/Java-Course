<%@ page import="java.util.List, hr.fer.zemris.java.hw13.servlets.GlasanjeServlet.Band" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>

<html>
	<body bgcolor="${pickedBgCol}">
 		<h1>Vote for your favourite band:</h1>
 		<p>Click on your favourite band to vote!</p>
 		<ol>
 			<%
 				List<Band> bands = (List<Band>) request.getSession().getAttribute("bands");
 				
 				for(Band band : bands) {
 					out.println("<li><a href=glasanje-glasaj?id=" + band.id + ">" + band.name + "</a></li>");
 				}
 			%>
 		</ol>
	</body>
</html>