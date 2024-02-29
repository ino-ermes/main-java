<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html>

    <head>
        <meta charset="UTF-8">
        <title>Home</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                background-color: #f4f4f4;
                text-align: center;
                padding: 50px;
            }

            .welcome-box {
                background-color: #ffffff;
                padding: 20px;
                border-radius: 10px;
                box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                margin: auto;
                max-width: 400px;
            }

            .welcome-message {
                color: #333333;
                font-size: 24px;
            }
        </style>
    </head>

    <body>

        <div class="welcome-box">
            <h2 class="welcome-message">
            <%
                com.mio.entity.User user=(com.mio.entity.User) session.getAttribute("user");
                out.print("Welcome, " + user.getCasualName());
            %>
        </h2>
        <p>Thank you for using our application.</p>
        <a href=" logout">Logout</a>
        </div>

    </body>

    </html>