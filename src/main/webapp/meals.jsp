<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<%--<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>--%>
<html>
<head>
    <title>Meal list</title>
    <style>
        .normal {
            color: green;
        }
        .excess {
            color: red;
        }
    </style>
    <style type="text/css" >
        FORM {
            border: 1px solid black; /* Рамка вокруг таблицы */
            width: 719px;
        }
    </style>
</head>
<body>
<section>
    <h3><a href="index.html">Home</a></h3>
    <hr/>
    <h2>Meals</h2>
    <br>
    <form method="get" action="meals">
        <style type="text/css" >
            dl {
                margin-top: 5px;
                margin-bottom: 5px;
                margin-left: 5px;
            }
        </style>
        <table cellpadding="3">
            <tr align="center">
                <th>От даты (включая)</th>
                <th>До даты (включая)</th>
                <th>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th>
                <th>От времени (включая)</th>
                <th>До времени (исключая)</th>
            </tr>
            <tr align="center">
                <td>
                    <input type="date" id="startDate" autocomplete="off" name="startDate" value="${param.startDate}">
                </td>
                <td>
                    <input type="date" id="endDate" autocomplete="off" name="endDate" value="${param.endDate}">
                </td>
                <td>
                    <pre>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</pre>
                </td>
                <td>
                    <input type="time" id="startTime" autocomplete="off" name="startTime" value="${param.startTime}">
                </td>
                <td>
                    <input type="time" id="endTime" autocomplete="off" name="endTime" value="${param.endTime}">
                </td>
            </tr>
        </table>
    <dl>
        <button type="submit">Apply Filter</button>
        <button type="submit" name="reset" value="reset">Reset</button>
    </dl>
    </form>
    <br>
    <%--    <a href="meals?action=create">Add Meal</a>--%>
    <input type="button" value="Add meal"
           onclick="window.location.href = 'meals?action=create'">
    <br><br>
    <table border="1" cellpadding="6" cellspacing="0">
        <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th colspan="2">Operations</th>
        </tr>
        </thead>
        <c:forEach items="${meals}" var="meal">
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.to.MealTo"/>
            <tr class="${meal.excess ? 'excess' : 'normal'}">
                <td>
                        <%--${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}--%>
                        <%--<%=TimeUtil.toString(meal.getDateTime())%>--%>
                        <%--${fn:replace(meal.dateTime, 'T', ' ')}--%>
                        ${fn:formatDateTime(meal.dateTime)}
                </td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td>
                        <%--                    <a href="meals?action=update&id=${meal.id}">Update</a>--%>
                    <input type="button" value="Update"
                           onclick="window.location.href = 'meals?action=update&id=${meal.id}'"/>
                </td>
                <td>
                        <%--                    <a href="meals?action=delete&id=${meal.id}">Delete</a>--%>
                    <input type="button" value="Delete"
                           onclick="window.location.href = 'meals?action=delete&id=${meal.id}'"/>
                </td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>