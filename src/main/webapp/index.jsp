<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>Banking System - Home</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body>

	<nav class="navbar navbar-expand-lg bg-body-tertiary">
		<div class="container-fluid">
			<a class="navbar-brand" href="index.jsp">Banking System</a>
			<button class="navbar-toggler" type="button"
				data-bs-toggle="collapse" data-bs-target="#navbarContent"
				aria-controls="navbarContent" aria-expanded="false"
				aria-label="Toggle navigation">
				<span class="navbar-toggler-icon"></span>
			</button>
			<div class="collapse navbar-collapse" id="navbarContent">
				<ul class="navbar-nav me-auto mb-2 mb-lg-0">
					<li class="nav-item">
						<a class="nav-link active" aria-current="page" href="index.jsp">Home</a>
					</li>
					<li class="nav-item">
						<a class="nav-link" href="balance.jsp">Check Balance</a>
					</li>
					<li class="nav-item">
						<a class="nav-link" href="deposit.jsp">Deposit Money</a>
					</li>
					<li class="nav-item">
						<a class="nav-link" href="withdraw.jsp">Withdraw Money</a>
					</li>
					<li class="nav-item">
						<a class="nav-link" href="transfer.jsp">Transfer Money</a>
					</li>
					<li class="nav-item">
						<a class="nav-link" href="pay.jsp">Pay Bills</a>
					</li>
				</ul>
				<form class="d-flex" action="account">
					 <input type="hidden" name="action" value="logout">
					<button class="btn btn-outline-danger" type="submit">Log Out</button>
				</form>
			</div>
		</div>
	</nav>

	<section>
		<div class="container">
			<h1>Welcome to Banking System</h1>
			<%
				Cookie[] cookies = request.getCookies();
				String username = null;
	
				if (cookies != null) {
					for (Cookie cookie : cookies) {
						if ("username".equals(cookie.getName())) {
							username = cookie.getValue();
						}
					}
				}
				if (username != null) {
					out.println("<h2>Hello, " + username + "</h2>");
				} else {
					response.sendRedirect("login.jsp");
				}
			%>
		</div>
	</section>
</body>
</html>
