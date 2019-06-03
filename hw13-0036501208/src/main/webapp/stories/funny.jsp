<%@ page import="java.util.Random" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>

<%!
	private static String[] colors = {"red", "green", "blue", "white", "cyan", "yellow"};
	private static Random random = new Random();
%>

<html>
   <body bgcolor="${pickedBgCol}">
     <font color="<%= colors[random.nextInt(colors.length)] %>">
     	Bio jednom jedan pas <br>
     	Ukrao mesaru kost <br>
     	Mesar se naljutio <br>
     	I razbio mu nos <br>
     	<br>
     	U baš taj čas <br>
     	Iskopali mu grob psi koji se skupiše <br>
     	I stavili natpis na kojemu piše: <br>
     	"Bio jednom jedan pas"
     </font>
   </body>
</html>