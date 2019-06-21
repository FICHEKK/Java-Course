<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
	<head>
		<title> Error </title>
	</head>

	<body>
		<h1> An error occured. </h1>
		<p> ${errorMessage} </p>

		<p><a href="${pageContext.request.contextPath}/servleti/main"> Return to the front page </a></p>
	</body>
</html>