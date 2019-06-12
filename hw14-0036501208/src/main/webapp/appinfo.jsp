<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>

<html>
   <body bgcolor="${pickedBgCol}">
   	 <%
   	 	// DateFormat will not work here unfortunately...
   	 	long duration = System.currentTimeMillis() - (long) application.getAttribute("startTime");
   	 	
   	 	// Durations in ms for each time unit
   	 	long secMS  = 1000;
   	 	long minMS  = secMS  * 60;
   	 	long hourMS = minMS  * 60;
   	 	long dayMS  = hourMS * 24;
   	 	
   	 	long d = duration / dayMS;
   	 	duration -= d * dayMS;
   	 	
   	 	long h = duration / hourMS;
   	 	duration -= h * hourMS;
   	 	
   	 	long m = duration / minMS;
   	 	duration -= m * minMS;
   	 	
   	 	long s = duration / secMS;
   	 	duration -= s * secMS;
   	 	
   	 	long ms = duration;

		String formattedDuration = String.format("%d days %d hours %d minutes %d seconds %3d milliseconds", d, h, m, s, ms);
   	 	out.print("Web application has been running for " + formattedDuration);
   	 %>
   </body>
</html>