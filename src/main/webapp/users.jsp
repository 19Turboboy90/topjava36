<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ru">
<head>
    <title>Users</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Users</h2>
<div>
    <div>
        <form method="post">
            <label for="id">
                <select name="id" id="id">
                    <option value="1">Admin</option>
                    <option value="2">User</option>
                </select>
                <input type="submit" value="Apply">
            </label>
        </form>
    </div>
</div>
</body>
</html>