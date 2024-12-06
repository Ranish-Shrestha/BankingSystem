<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>Banking System - Current Balance</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
	async function checkBalance() { // Get account_id from cookies 
		let accountId = getCookie("accountId");
		if (!accountId) {
			document.getElementById("balance").innerText = "Error: No account ID found.";
			return;
		}
		const response = await fetch(`account?action=balance`);
		let data = await response.json();
		
		if (response.ok) {
			document.getElementById("balance").innerText = "Current Balance: $" + data.balance;
		} else {
			document.getElementById("balance").innerText = "Error: " + data.error;
		}
	}

	// Function to get a cookie by name 
	function getCookie(name) {
		let cookieArr = document.cookie.split(";");
		for (let i = 0; i < cookieArr.length; i++) {
			let cookiePair = cookieArr[i].split("=");
			if (name === cookiePair[0].trim()) {
				return decodeURIComponent(cookiePair[1]);
			}
		}
		return null;
	}

	// Call checkBalance on page load or button click 
	document.addEventListener("DOMContentLoaded", checkBalance);
</script>
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
						<a class="nav-link" aria-current="page" href="index.jsp">Home</a>
					</li>
					<li class="nav-item">
						<a class="nav-link active" href="balance.jsp">Check Balance</a>
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
			<h1>Check Account Balance</h1>
			<button onclick="checkBalance()" class="btn btn-outline-primary">Check
				Balance</button>
			<p id="balance"></p>
		</div>
	</section>
</body>
</html>


