<%--
  Created by IntelliJ IDEA.
  User: Phaenir
  Date: 25.11.2014
  Time: 14:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <title>Cake Event</title>
  </head>
  <body>
    <h1>Cake Event</h1>
    <form action="controller" method="post">
      <p>User Name: <input name="userName" value="guest" type="text" /></p>
      <p>User Password: <input name="userPassword" value="guest" type="password"/></p>
      <input type="submit" value="Confirm" name="btnOK" />
    </form>
  <ul>
    <li>
      <a href="controller" name="desc">Model description</a>
    </li>
    <li>
      <a href="test.html" name="register">New user registration</a>
    </li>
  </ul>
  </body>
</html>
