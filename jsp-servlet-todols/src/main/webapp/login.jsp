<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Bookshop Website</title>
</head>
<body>
    <div style="text-align: center">
        <h1>Admin Login</h1>
        <form action="login" method="post">
            <label for="email">Email:</label>
            <input name="email" size="30" />
            <br><br>
            <label for="password">Password:</label>
            <input type="password" name="password" size="30" />
            <br>
            <%
                if (request.getAttribute("message") != null) {
                    out.print(request.getAttribute("message"));
                }
            %>
            <br><br>
            <button type="submit">Login</button>
        </form>
    </div>
</body>
</html>