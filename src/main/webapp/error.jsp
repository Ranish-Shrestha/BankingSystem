<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Error</title>
</head>
<body>
    <h1>Database Error</h1>
    <p>An error occurred while processing your request:</p>
    <p style="color: red;">
        <%= request.getAttribute("errorMessage") %>
    </p>
    <a href="createAccount.jsp">Back to Create Account</a>
</body>
</html>
