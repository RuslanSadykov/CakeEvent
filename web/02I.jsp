<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!-- Start: 02I.jsp -->
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<jsp:useBean id="sBean" scope="session" class="bean.ServiceBean" />
<h4>Modell - Overview: <jsp:getProperty name="sBean" property="modelsOverview" /></h4>
<br/>
<form action="controller" method="post">
<p>Add a new Model:<br/>
<jsp:getProperty name="sBean" property="modelAddView" /></p>
    <br/>
<input type="hidden" name="action" value="14_addModel">
<input type="submit" value="add"> 
</form>

<!-- End: 02I.jsp -->
