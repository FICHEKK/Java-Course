<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<html>
   <body bgcolor="${pickedBgCol}">
     <a href="colors"> Background color chooser </a> <br><br>
     
     <form action="trigonometric" method="GET">
 		Starting angle:<br><input type="number" name="a" min="0" max="360" step="1" value="0"><br>
 		Ending angle:<br><input type="number" name="b" min="0" max="360" step="1" value="360"><br>
 	 	<input type="submit" value="Tabulate"><input type="reset" value="Reset">
	 </form>
     
     <a href="trigonometric?a=0&b=90"> Trigonometric functions in range [0, 90] </a> <br><br>
     <a href="funny"> A funny short story </a> <br><br>
     <a href="report"> OS usage report (April 2019) </a> <br><br>
     <a href="powers?a=1&b=100&n=3"> Powers from 1 to 100, n = 3 </a> <br><br>
     <a href="appinfo"> App info - time running </a> <br><br>
     <a href="servleti/index.html"> Voting polls! </a>
   </body>
</html>