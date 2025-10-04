<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Meal</title>
</head>
<body>
<div>
    <h2><c:out value="${requestScope.meal == null ? 'Add Meal' : 'Update Meal'}"/></h2>
</div>
<div>
    <form action="meals" method="post" name="addMeal">
        <div>
            <input type="hidden" id="mealId" name="mealId"
                   value="${meal != null ? meal.id : ''}">
        </div>
        <div>
            <label for="dateTime">DateTime:
                <input type="datetime-local" id="dateTime" name="dateTime"
                       value="${meal != null ? meal.dateTime : ''}" required>
            </label>
        </div>
        <br>
        <div>
            <label for="description">Description:
                <input type="text" id="description" name="description"
                       value="${meal != null ? meal.description : ''}" required>
            </label>
        </div>
        <br>
        <div>
            <label for="calories">Calories:
                <input type="number" id="calories" name="calories"
                       value="${meal != null ? meal.calories : ''}" required>
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
