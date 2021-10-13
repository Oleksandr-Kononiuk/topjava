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
        <tr>
<%--            todo <td><<fmt:formatDate pattern="yyyy-MMM-dd" value="${user.dob}"</td>--%>
            <td>${meal.dateTime}</td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
            <td>
                <input type="button" value="Update"
                       onclick="window.location.href = 'meals?action=update&mealId=<c:out value="${meal.id}"/>'">
<%--                <a href="meals?action=update&mealId=<c:out value="${meal.id}"/>">Update</a>--%>
            </td>
            <td>
                <input type="button" value="Delete"
                       onclick="window.location.href = 'meals?action=delete&mealId=<c:out value="${meal.id}"/>'">
            </td>
        </tr>
    </c:forEach>

</table>
</body>
</html>
