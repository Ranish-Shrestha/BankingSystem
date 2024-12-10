<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Banking System - Transfer Money</title>
<link
    href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
    rel="stylesheet">
<link rel="stylesheet" href="styles.css">
<script
    src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container-fluid">
        <a class="navbar-brand" href="index.jsp">Banking System</a>
        <button class="navbar-toggler" type="button"
            data-bs-toggle="collapse" data-bs-target="#navbarContent"
            aria-controls="navbarContent" aria-expanded="false"
            aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse justify-content-end" id="navbarContent">
            <ul class="navbar-nav">
                <li class="nav-item">
                    <a class="nav-link" aria-current="page" href="index.jsp">Home</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="deposit.jsp">Deposit Money</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="withdraw.jsp">Withdraw Money</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link active" href="transfer.jsp">Transfer Money</a>
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

<div class="container mt-5">
    <div class="row justify-content-center">
        <div class="col-md-6">
            <div class="details-box mx-auto my-4 p-4 bg-light rounded">
                <h1 class="text-center">Transfer Money</h1>
                <form id="transferForm">
                    <div class="form-group mb-3">
                        <label for="source_account_id">Source Account ID:</label>
                        <input type="text" class="form-control" id="source_account_id" name="source_account_id" placeholder="Source Account ID" required />
                    </div>
                    <div class="form-group mb-3">
                        <label for="destination_account_id">Destination Account ID:</label>
                        <input type="text" class="form-control" id="destination_account_id" name="destination_account_id" placeholder="Destination Account ID" required />
                    </div>
                    <div class="form-group mb-3">
                        <label for="amount">Amount:</label>
                        <input type="number" class="form-control" id="amount" name="amount" placeholder="Amount" required />
                    </div>
                    <button type="submit" class="btn btn-primary w-100">Transfer</button>
                </form>
                <p id="result" class="mt-3 text-center"></p>
            </div>
        </div>
    </div>
</div>

<script>
    document.getElementById('transferForm').addEventListener('submit', async function(event) {
        event.preventDefault();

        let formData = new FormData(document.getElementById('transferForm'));

        try {
            const response = await fetch("account?action=transfer", {
                method: 'POST',
                body: new URLSearchParams(formData)
            });

            let data = await response.json();

            if (response.ok) {
                document.getElementById('result').innerText = 'Transfer successful';
                console.log(data);
            } else {
                document.getElementById('result').innerText = 'Transfer failed: ' + data.error;
                console.log(data);
            }
        } catch (error) {
            document.getElementById('result').innerText = 'Error: ' + error.message;
            console.error('Error:', error);
        }
    });
</script>
</body>
</html>
