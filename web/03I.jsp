<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!-- Start: 02_inc.jsp -->
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<jsp:useBean id="sBean" scope="session" class="bean.ServiceBean" />
<br>
<p>
Modell: <jsp:getProperty name="sBean" property="modelName" />
</p>
<form action="controller" method="post">
<input type="hidden" name="action" value="02_selectModel">
<input type="submit" value="zuruck">
</form>
<br>
<form action="controller" method="post">
<jsp:getProperty name="sBean" property="zutatenTableau" /><br/>
<jsp:getProperty name="sBean" property="rezepteTableau" /><br/>
<jsp:getProperty name="sBean" property="solutionTableau" /><br/>
<input type="hidden" name="action" value="10_solveModel"><br/>
<jsp:getProperty name="sBean" property="hiddenModdelId" /><br/>
<input type="submit" value="solve"> 
</form>
<!-- End: 02_inc.jsp -->
