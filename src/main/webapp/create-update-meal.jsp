<%--
  Created by IntelliJ IDEA.
  User: Oleksandr Kononiuk
  Date: 10.10.2021
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html>
<head>
    <title>Edit Form</title>
</head>
<body>
<h3>
    <a href="index.html">Home</a>
    <a href="meals.jsp">/Meals</a>
</h3>
<hr>
<h2>Edit Form</h2>
<br>

<form method="POST" action='meals?action=update&mealId=<c:out value="${param.mealId}"/>' name="frmEditUser">
    <table>
        <tr>
            <td width="20%">DateTime:</td>
            <td width="80%">
                <input type="datetime-local" name="date-time" size="20"
                       value="<fmt:formatDate pattern="MM-dd-yyyy hh:mm" value="${meal.dateTime}"/>" />
            </td>
        </tr>
<%--        todo placeholder like old value of parameter?--%>
        <tr>
            <td width="20%">Description:</td>
            <td width="80%">
                <input type="text" name="description" size="20" placeholder="description" content="${meal.description}"
                       value="<c:out value="${meal.description}"/>" />
            </td>
        </tr>
        <tr>
            <td width="20%">Calories:</td>
            <td width="80%">
                <input type="text" name="calories" size="20" required placeholder="calories"
                       value="<c:out value="${meal.calories}"/>" />
            </td>
        </tr>
        <tr>
            <td>
                <br>
                <input type="submit" value="Save"/>
                <input type="reset" value="Clear"/>
                <input type="button" value="Cancel" onclick="window.location.href = 'meals'"/>
            </td>
        </tr>
    </table>
</form>

</body>
</html>
