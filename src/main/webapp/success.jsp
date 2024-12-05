<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Success</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
</head>
<body>
    <div class="container">
        <h1>Success</h1>
        <p class="alert alert-success">
            <%= request.getAttribute("successMessage") %>
        </p>
        <a href="index.jsp" class="btn btn-primary">Back to Home</a>
    </div>
</body>
</html>
