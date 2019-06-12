<%@ page import="hr.fer.zemris.java.hw14.database.PollOption"%>
<%@ page import="java.util.List" %>
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
 			<thead><tr><th>Poll option</th><th>Number of votes</th></tr></thead>
 			<tbody>
 				<%
 					List<PollOption> pollOptionsSorted = (List<PollOption>) request.getSession().getAttribute("pollOptionsSorted");
 					
 					for(PollOption option : pollOptionsSorted) {
 						out.println("<tr><td>" + option.title + "</td><td>" + option.getVotes() + "</td></tr>");
 					}
 				%>
 			</tbody>
 		</table>

 		<h2>Results pie-chart:</h2>
 		<img alt="Pie-chart" src="glasanje-grafika" width="400" height="400" />

 		<h2>Results in XLS format</h2>
 		<p>Get results in XLS format by clicking <a href="glasanje-xls">here</a></p>

 		<h2>Other</h2>
 		<p>Poll winner example(s):</p>
 		<ul>
 			<%
 				List<PollOption> mostVotedPollOptions = (List<PollOption>) request.getAttribute("mostVotedPollOptions");
 					
 				for(PollOption option : mostVotedPollOptions) {
 					out.println("<li><a href=\"" + option.link + "\">" + option.title + "</a></li>");
 				}
 			%>
 		</ul>
	</body>
</html>