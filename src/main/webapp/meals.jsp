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
</head>
<body>
<section>
    <h3><a href="index.html">Home</a></h3>
    <hr/>
    <h2>Meals</h2>

    <div>
        <form action="meals" method="get">
            <div>
                <label for="fromDate">
                    <input type="date" name="fromDate" id="fromDate" value="${param.fromDate}"
                           placeholder="От даты (включая)">
                </label>
                <label for="toDate">
                    <input type="date" name="toDate" id="toDate" value="${param.toDate}"
                           placeholder="До даты (включая)">
                </label>
            </div>
            <br>
            <div>
                <label for="fromTime">
                    <input type="time" name="fromTime" id="fromTime" value="${param.fromTime}"
                           placeholder="От времени (включая)">
                </label>
                <label for="toTime">
                    <input type="time" name="toTime" id="toTime" value="${param.toTime}"
                           placeholder="До времени (исключая)">
                </label>
            </div>
            <br>
            <div>
                <button type="submit">Filter</button>
                <input type="button" value="Cancel" onclick="location.href = 'meals'">
            </div>
        </form>
    </div>

    <a href="meals?action=create">Add Meal</a>
    <br><br>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <c:forEach items="${requestScope.meals}" var="meal">
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.to.MealTo"/>
            <tr class="${meal.excess ? 'excess' : 'normal'}">
                <td>
                        <%--${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}--%>
                        <%--<%=TimeUtil.toString(meal.getDateTime())%>--%>
                        <%--${fn:replace(meal.dateTime, 'T', ' ')}--%>
                        ${fn:formatDateTime(meal.dateTime)}
                </td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals?action=update&id=${meal.id}">Update</a></td>
                <td><a href="meals?action=delete&id=${meal.id}">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>