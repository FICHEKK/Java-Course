<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
	<head>
		<title> Success! </title>
	</head>

	<body>
		<h1> Welcome, <%= session.getAttribute("current.user.nick") %>! </h1>
		
		<h3> You have successfully registered! To get back to the front page, click the link below! </h3>

		<p><a href="main"> Start browsing blogs! </a></p>
	</body>
</html>