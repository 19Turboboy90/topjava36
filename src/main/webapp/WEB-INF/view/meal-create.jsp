<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Meal</title>
</head>
<body>
<div>
    <c:choose>
        <c:when test="${requestScope.meal == null}">
            <h2>Add Meal</h2>
        </c:when>
        <c:otherwise>
            <h2>Update Meal</h2>
        </c:otherwise>
    </c:choose>
</div>
<div>
    <form action="${requestScope.request.contextPath}meals" method="post" name="addMeal">
        <div>
            <input type="hidden" id="mealId" name="mealId"
                   value="${requestScope.meal != null ? requestScope.meal.id : ''}">
        </div>
        <div>
            <label for="dateTime">DateTime:
                <input type="datetime-local" id="dateTime" name="dateTime"
                       value="${requestScope.meal != null ? requestScope.meal.dateTime : ''}" required>
            </label>
        </div>
        <br>
        <div>
            <label for="description">Description:
                <input type="text" id="description" name="description"
                       value="${requestScope.meal != null ? requestScope.meal.description : ''}" required>
            </label>
        </div>
        <br>
        <div>
            <label for="calories">Calories:
                <input type="number" id="calories" name="calories"
                       value="${requestScope.meal != null ? requestScope.meal.calories : ''}" required>
            </label>
        </div>
        <br>
        <div>
            <input type="submit" value="Save">
            <input type="button" value="Cancel" onclick="location.href = 'meals'">
        </div>
    </form>
</div>
</body>
</html>
