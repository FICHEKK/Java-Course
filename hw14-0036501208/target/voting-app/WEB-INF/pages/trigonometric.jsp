<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>

<html>
   <body bgcolor="${pickedBgCol}">
   
   	<table border="1">
  		<tr>
    		<th>x</th>
    		<th>sin(x)</th>
    		<th>cos(x)</th>
  		</tr>
  		
  		<% 
     		int a = (Integer) request.getAttribute("a");
     		int b = (Integer) request.getAttribute("b");
     		double[] sinValues = (double[]) request.getAttribute("sinValues");
			double[] cosValues = (double[]) request.getAttribute("cosValues");
			
			for(int i = 0, n = b-a+1; i < n; i++) {
				%> <tr>
						<td> <%= i+a          %> </td>
						<td> <%= sinValues[i] %> </td>
						<td> <%= cosValues[i] %> </td>
				   </tr>
				<%
			}
     	%>
	</table>

   </body>
</html>