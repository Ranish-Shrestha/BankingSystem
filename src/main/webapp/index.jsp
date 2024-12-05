<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>Banking System</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
	crossorigin="anonymous">
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
	integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
	crossorigin="anonymous"></script>
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
			</div>
		</div>
	</nav>
	
	<section>
		<div class="container">
	        <h1>Create a New Account</h1>
	        <form action="account" method="post" class="col-8">
	            <input type="hidden" name="action" value="create">
	            <div class="form-group">
	                <label class="form-label" for="name">Name:</label>
	                <input type="text" class="form-control" id="name" name="name" required>
	            </div>
	            <div class="form-group">
	                <label class="form-label" for="address">Address:</label>
	                <input type="text" class="form-control" id="address" name="address" required>
	            </div>
	            <div class="form-group">
	                <label class="form-label" for="phone_number">Phone Number:</label>
	                <input type="text" class="form-control" id="phone_number" name="phone_number" required>
	            </div>
	            <div class="form-group">
	                <label class="form-label" for="email">Email:</label>
	                <input type="email" class="form-control" id="email" name="email" required>
	            </div>
	            <div class="form-group">
	                <label class="form-label" for="account_type">Account Type:</label>
	                <select class="form-control" id="account_type" name="account_type" required>
	                    <option value="savings">Savings</option>
	                    <option value="checking">Checking</option>
	                </select>
	            </div>
	            <div class="mt-3">
	            	<button type="submit" class="btn btn-outline-success">Create Account</button>
	            </div>
	        </form>
	    </div>
	</section>
</body>
</html>
