<%@ page import="hr.fer.zemris.java.hw15.web.forms.LoginForm"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
	<head>
		<title> ${entry.title} </title>
	</head>
	
	<body>
		<!-- ===================================================================================== -->
		<!-- User display. -->
		<!-- ===================================================================================== -->
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
		<!-- Entry display. -->
		<!-- ===================================================================================== -->
		
		<h2> ${entry.title} </h2>
		${entry.text}
		
		<!-- ===================================================================================== -->
		<!-- Editing entry if the user is logged in. -->
		<!-- ===================================================================================== -->
		
		<c:if test="${sessionScope['current.user.id'] != null}">
			<c:if test="${sessionScope['current.user.nick'].equals(author.nick)}">
				<p> <b> <a href="${entry.id}/edit"> Edit this entry! </a> </b> </p>
			</c:if>
		</c:if>
		
		<!-- ===================================================================================== -->
		<!-- Comments display. -->
		<!-- ===================================================================================== -->
		
		<h3> Comments: </h3>
		
		<p>
		<c:if test="${comments.isEmpty()}">
			There are currently no comments.
		</c:if>
		
		<c:if test="${!comments.isEmpty()}">
			<c:forEach items="${comments}" var="comment">
				${comment.message} <br>
				Posted by <b> ${comment.usersEMail} </b> on <b> ${comment.postedOn} </b>
				<hr>
			</c:forEach>
		</c:if>
		</p>
		
		<!-- ===================================================================================== -->
		<!-- Adding new comment. -->
		<!-- ===================================================================================== -->

		<h3> Add new comment: </h3>
		<form action="comment" method="post">
			<c:if test="${sessionScope['current.user.id'] == null}">
				<h4> E-mail: </h4>
				<input type="email" name="email" size="30" required="required">
				<br>
			</c:if>
			
			<c:if test="${sessionScope['current.user.id'] != null}">
				<input type="hidden" name="email" value="${sessionScope['current.user.email']}"> 
			</c:if>
			
			<input type="hidden" name="entryID" value="${entry.id}"> 
			
			<h4> Message: </h4>
			<input type="text" name="message" size="128" required="required">
			<br>
			<input type="submit" value="Comment">
		</form>
		
	</body>
</html>
