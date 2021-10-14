<%--
  Created by IntelliJ IDEA.
  User: Oleksandr Kononiuk
  Date: 08.10.2021
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page import="java.time.format.DateTimeFormatter" %>
<html>
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>

<br>
<input type="button" value="Add meal"
       onclick="window.location.href = 'meals?action=add'">
<br>
<br>

<table border="2" cellpadding="3" cellspacing="0">
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
        <th colspan="2">Operations</th>
    </tr>

    <c:forEach var="meal" items="${mealList}">
        <tr style="color:${false ? 'red' : 'green'};">
            <td>${meal.dateTime.format( DateTimeFormatter.ofPattern("MM-dd-yyyy hh:mm"))}</td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
            <td>
                <input type="button" value="Update"
                       onclick="window.location.href = 'meals?action=update&mealId=${meal.id}'"/>
            </td>
            <td>
                <input type="button" value="Delete"
                       onclick="window.location.href = 'meals?action=delete&mealId=${meal.id}'"/>
            </td>
        </tr>
    </c:forEach>

</table>
</body>
</html>
