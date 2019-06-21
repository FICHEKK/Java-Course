<%@page import="hr.fer.zemris.java.hw15.web.forms.LoginForm"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
	<head>
		<title> Main page </title>
	</head>
	
	<body>
		<% LoginForm form = (LoginForm) request.getAttribute("form"); %>
		<% if (session.getAttribute("current.user.id") == null) { %>
		    <!-- ===================================================================================== -->
			<!-- Log-in form. -->
			<!-- ===================================================================================== -->
			
			<h3> Log-in: </h3>
			<form action="main" method="post">
				Nick <input value="<%= form.getNick() %>" type="text" name="nick" size="30">
				<% if(form.hasError("nick")) out.write(form.getError("nick")); %> <br>
				
				Password <input type="password" name="password" size="30">
				<% if(form.hasError("password")) out.write(form.getError("password")); %> <br>
				
				<% if(form.hasError("loginFail")) out.write(form.getError("loginFail")); %> <br>
				
				<input type="submit" value="Log in">
			</form>
			
			<!-- ===================================================================================== -->
			<!-- Registration link. -->
			<!-- ===================================================================================== -->
			
			<h3> Registration: </h3>
			<a href="register"> New to this site? Register here. </a>
			
		<% } else {%>
		    Logged in as <b> ${sessionScope['current.user.nick']} </b>.
		    First name: <b> ${sessionScope['current.user.fn']} </b>.
		    Last name: <b> ${sessionScope['current.user.ln']} </b>.
			<br>
		    <a href="logout"> Log-out </a>
		<% } %>
		
		
		<!-- ===================================================================================== -->
		<!-- List of authors. -->
		<!-- ===================================================================================== -->
		
		<br> <br>
		<h3> Authors: </h3>
		
		<p>
		<c:if test="${authors.isEmpty()}">
			There are currently no authors.
		</c:if>
		
		<c:if test="${!authors.isEmpty()}">
			<c:forEach items="${authors}" var="author">
				<a href="author/${author.nick}">${author.firstName} ${author.lastName}</a> <br>
			</c:forEach>
		</c:if>
		</p>
		
	</body>
</html>
