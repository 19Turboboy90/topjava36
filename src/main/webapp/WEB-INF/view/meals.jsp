<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<div>
    <a href="meals?action=addMeal">Add Meal</a>
</div>
<br>
<div>
    <table border="1px">
        <tr>
            <th>DateTime</th>
            <th>Description</th>
            <th>Calories</th>
            <th></th>
            <th></th>
        </tr>
        <c:forEach var="meal" items="${meals}">
            <tr style="color:${meal.excess == true ? 'red' : 'green'}">
                <td>${meal.date} ${meal.time}</td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td>
                    <a href="meals?mealId=${meal.id}&action=update">update</a>
                </td>
                <td>
                    <a href="meals?mealId=${meal.id}&action=delete">delete</a>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>
</body>
</html>