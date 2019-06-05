<%@ page import="java.util.List" %>
<%@ page import="hr.fer.zemris.java.hw13.servlets.GlasanjeServlet.Band" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>

<html>
	<head>
 		<style type="text/css">
 			table.rez td {text-align: center;}
 		</style>
 	</head>
 	
 	<body bgcolor="${pickedBgCol}">
 		<h1>Voting results</h1>
 		
 		<p>These are the results:</p>
 		<table border="1" class="rez">
 			<thead><tr><th>Band</th><th>Number of votes</th></tr></thead>
 			<tbody>
 				<%
 					List<Band> bandsWithVotes = (List<Band>) request.getSession().getAttribute("bandsWithVotes");
 					
 					for(Band band : bandsWithVotes) {
 						out.println("<tr><td>" + band.name + "</td><td>" + band.getVotes() + "</td></tr>");
 					}
 				%>
 			</tbody>
 		</table>

 		<h2>Results pie-chart:</h2>
 		<img alt="Pie-chart" src="glasanje-grafika" width="400" height="400" />

 		<h2>Results in XLS format</h2>
 		<p>Get results in XLS format by clicking <a href="glasanje-xls">here</a></p>

 		<h2>Other</h2>
 		<p>Winning band(s) song example(s):</p>
 		<ul>
 			<%
 				List<Band> mostVotedBands = (List<Band>) request.getAttribute("mostVotedBands");
 					
 				for(Band band : mostVotedBands) {
 					out.println("<li><a href=\"" + band.link + "\">" + band.name + "</a></li>");
 				}
 			%>
 		</ul>
	</body>
</html>