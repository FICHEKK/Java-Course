<%@page import="hr.fer.zemris.java.hw15.web.forms.RegistrationForm"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
	<head>
		<title> Registration </title>
	</head>
	
	<body>
		<% if (session.getAttribute("current.user.id") == null) { %>
			<% RegistrationForm form = (RegistrationForm) request.getAttribute("form"); %>
		
			<!-- ===================================================================================== -->
			<!-- Registration. -->
			<!-- ===================================================================================== -->
			
			<h3> Create a new account: </h3>
			<form action="register" method="post">
				First name <input value="<%= form.getFirstName() %>" type="text" name="firstName" size="30">
				<% if(form.hasError("firstName")) out.write(form.getError("firstName")); %> <br>
				
				Last name  <input value="<%= form.getLastName() %>" type="text" name="lastName" size="30">
				<% if(form.hasError("lastName")) out.write(form.getError("lastName")); %> <br>
				
				E-mail 	   <input value="<%= form.getEmail()     %>" type="text" name="email" size="30">
				<% if(form.hasError("email")) out.write(form.getError("email")); %> <br>
				
				Nick       <input value="<%= form.getNick()      %>" type="text" name="nick" size="30">
				<% if(form.hasError("nick")) out.write(form.getError("nick")); %> <br>
				
				Password   <input type="password" name="password" size="30">
				<% if(form.hasError("password")) out.write(form.getError("password")); %> <br>
				
				<input type="submit" value="Register">
				
				<br> <br>
				
				<a href="main"> Go back to the main page! </a>
			</form>
			
		<% } else { %>
			<h4> Hey, ${sessionScope['current.user.nick']} ! </h4>
			If you wish to create a new account, please log-out. You can log-out at the front page <a href="main"> here! </a>
			
		<% } %>
		
		
	</body>
</html>
