<%--
  Created by IntelliJ IDEA.
  User: Oleksandr Kononiuk
  Date: 10.10.2021
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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

<form method="POST" action="form2.jsp">
    <table>
        <tr>
            <td width="20%">DateTime:</td>
            <td width="80%">
                <input type="date" name="date-time" size="20" />
            </td>
        </tr>
        <tr>
            <td width="20%">Description:</td>
            <td width="80%">
                <input type="text" name="description" size="20" />
<%--                <select>--%>
<%--                    <c:forEach var="item" items="${obj.items}">--%>
<%--                        <option>${item}</option>--%>
<%--                    </c:forEach>--%>
<%--                </select>--%>
<%--                Зробити Description енамом в тут випадаючий список з енама--%>
            </td>
        </tr>
        <tr>
            <td width="20%">Calories:</td>
            <td width="80%">
                <input type="text" name="calories" size="20" />
            </td>
        </tr>
        <tr>
            <td>
                <br>
                <c:url var="cancel" value="meals.jsp"/>
                <input type="submit" value="Save" onclick="window.location.href = 'cancel'" />
                <input type="reset" value="Cancel" onclick="window.location.href = 'cancel'" />
            </td>
        </tr>
    </table>
</form>

</body>
</html>
