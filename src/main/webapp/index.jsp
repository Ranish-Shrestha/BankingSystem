<%@page import="Model.Transaction"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Banking System - Home</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
        rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
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
            <h2>Hello, <span id="username"></span></h2>
            <dl>
                <dt>Account Number</dt>
                <dd><span id="accountNumber"></span></dd>
                <dt>Account Type</dt>
                <dd><span id="account_type"></span></dd>
                <dt>Balance</dt>
                <dd>
                    <span id="balance"></span>
                    <span><button onclick="checkBalance()" class="btn btn-outline-primary">Check Balance</button></span>
                </dd>
                <dt>Email</dt>
                <dd><span id="email"></span></dd>
            </dl>
			
			<button onclick="getTransactionDetails()" class="btn btn-outline-primary">Transaction Details</button>
            <h3>Transaction History</h3>
            <table class="table table-bordered">
                <thead>
                    <tr>
                        <th>Transaction ID</th>
                        <th>Type</th>
                        <th>Amount</th>
                        <th>Date</th>
                        <th>Description</th>
                    </tr>
                </thead>
                <tbody id="transactionBody">
                    
                </tbody>
            </table>

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
                if (username == null) {
                    response.sendRedirect("login.jsp");
                }
            %>
        </div>
    </section>
    
    <script>
 		// Call checkBalance on page load or button click 
 		document.addEventListener("DOMContentLoaded", getTransactionDetails);
        document.getElementById("username").innerText = getCookie("username");
        document.getElementById("balance").innerText = "$" + getCookie("balance");
        document.getElementById("email").innerText = getCookie("email");
        document.getElementById("accountNumber").innerText = getCookie("accountId");
        document.getElementById("account_type").innerText = getCookie("account_type");
        
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
        
        async function checkBalance(){
            let account_id = getCookie("accountId");
            
            
            try { 
                const response = await fetch("account?action=checkBalance&account_id="+ encodeURIComponent(account_id), { 
                    method: 'GET'
                }); 
                
                let data = await response.json(); 
                
                if (response.ok) { 
                    document.getElementById('balance').innerText = "$" + parseFloat(data.balance).toFixed(2); 
                    console.log(data); 
                } else { 
                    document.getElementById('balance').innerText = "Error: " + data.error; 
                    console.log(data); 
                } 
            } catch (error) { 
                document.getElementById('balance').innerText = "Error: " + error.message; 
                console.error('Error:', error); 
            }
        }
        

        let thtml = "<tr> <td colspan='5'>No transactions found.</td></tr>";
        document.getElementById("transactionBody").innerHTML = thtml;
        
        async function getTransactionDetails(){
            let account_id = getCookie("accountId");
            
            try { 
                const response = await fetch("account?action=getTransactions&account_id="+ encodeURIComponent(account_id), { 
                    method: 'GET'
                }); 
                
                let data = await response.json(); 
                let dataHTML = "";
                console.log(data)
                if (response.ok) { 
                	data.forEach((item) => {
             			dataHTML += 
                     		"<tr>" +
                     			"<td>" + item.transactionId + "</td>" +
                     			"<td>" + item.transactionType + "</td>" +
                     			"<td>" + parseFloat(item.amount).toFixed(2) + "</td>" +
                     			"<td>" + item.timestamp + "</td>" +
                     			"<td>" + item.description + "</td>" +
                     		"</tr>";
                	})
                	 
                	 document.getElementById("transactionBody").innerHTML = dataHTML;
                } else { 
                    console.log(data); 
                } 
            } catch (error) { 
                console.error('Error:', error); 
            }
        }
    </script>
</body>
</html>
