<%@ page import="hr.fer.zemris.java.hw15.web.forms.LoginForm"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
	<head>
		<title> ${actionTitle} </title>
	</head>
	
	<body>
		<!-- ===================================================================================== -->
		<!-- User display. -->
		<!-- ===================================================================================== -->
		<% LoginForm form = (LoginForm) request.getAttribute("form"); %>
		<% if (session.getAttribute("current.user.id") == null) { %>
			<h3> Error. </h3>
			You are not logged in. You cannot perform action '${action}'.
			<br>
		<% } else {%>
		    Logged in as <b> ${sessionScope['current.user.nick']} </b>.
		    First name: <b> ${sessionScope['current.user.fn']} </b>.
		    Last name: <b> ${sessionScope['current.user.ln']} </b>.
			<br>
			
			<!-- ===================================================================================== -->
			<!-- New/Edit (based on the provided attribute "action"). -->
			<!-- ===================================================================================== -->
	
			<h3> ${actionTitle} </h3>
			<c:if test="${entry == null}">
				<form action="new" method="post">
					<h4> Title: </h4>
					<input type="text" name="title" size="30" required="required">

					<br>
					
					<h4> Text: </h4>
					<input type="text" name="text" size="128" required="required">
					
					<br>
					
					<input type="submit" value="Submit">
				</form>
			</c:if>
			
			<c:if test="${entry != null}">
				<form action="${pageContext.request.contextPath}/servleti/author/${sessionScope['current.user.nick']}/edit" method="post">
					<h4> Title: </h4>
					<input value="${entry.title}" type="text" name="title" size="30" required="required">
					
					<br>
					
					<h4> Text: </h4>
					<input value="${entry.text}" type="text" name="text" size="30" required="required">
					<input type="hidden" name="entryID" value="${entry.id}"> 
						
					<br>
						
					<input type="submit" value="Submit">
				</form>
			</c:if>
		<% } %>
		
		<a href="${pageContext.request.contextPath}/servleti/main"> Return to the front page. </a>
		
	</body>
</html>
