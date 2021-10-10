<%--
  Created by IntelliJ IDEA.
  User: Oleksandr Kononiuk
  Date: 08.10.2021
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>

<br>
<input type="button" value="Add meal" onclick="window.location.href = 'create-update-meal.jsp'">
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

        <c:url var="updateButton" value="meals.jsp">
            <c:param name="meal" value="${meal.calories}"/>
        </c:url>

        <c:url var="deleteButton" value="meals.jsp">
            <c:param name="meal" value="${meal.calories}"/>
        </c:url>

        <tr>
            <td>${meal.dateTime}</td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
            <td>
                <input type="button" value="Update" onclick="window.location.href = 'create-update-meal.jsp'">
            </td>
            <td>
                <input type="button" value="Delete" onclick="window.location.href = '${deleteButton}'">
            </td>
        </tr>
    </c:forEach>

</table>
</body>
</html>
