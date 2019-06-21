<%@ page import="hr.fer.zemris.java.hw15.web.forms.LoginForm"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
	<head>
		<title> ${author.nick}'s blogs </title>
	</head>
	
	<body>
		<% LoginForm form = (LoginForm) request.getAttribute("form"); %>
		<% if (session.getAttribute("current.user.id") == null) { %>
			<b> Not logged in. </b> <br>

		<% } else {%>
		    Logged in as <b> ${sessionScope['current.user.nick']} </b>.
		    First name: <b> ${sessionScope['current.user.fn']} </b>.
		    Last name: <b> ${sessionScope['current.user.ln']} </b>.
			<br>
		<% } %>
		
		<a href="${pageContext.request.contextPath}/servleti/main"> Return to the front page. </a>
		
		<!-- ===================================================================================== -->
		<!-- List of entries. -->
		<!-- ===================================================================================== -->
		
		<br> <br>
		<h3> All entries submitted by ${author.nick} </h3>
		
		<p>
		<c:if test="${entries.isEmpty()}">
			There are currently no entries.
		</c:if>
		
		<c:if test="${!entries.isEmpty()}">
			<c:forEach items="${entries}" var="entry">
				<a href="${author.nick}/${entry.id}">${entry.title}</a> <br>
			</c:forEach>
		</c:if>
		</p>
		
		<!-- ===================================================================================== -->
		<!-- Adding new entry if the user is logged in. -->
		<!-- ===================================================================================== -->
		
		<c:if test="${sessionScope['current.user.id'] != null}">
			<c:if test="${sessionScope['current.user.nick'].equals(author.nick)}">
				<p> <b> <a href="${author.nick}/new"> Add new entry! </a> </b> </p>
			</c:if>
		</c:if>
		
	</body>
</html>
