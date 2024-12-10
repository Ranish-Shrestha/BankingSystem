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
    <link rel="stylesheet" href="styles.css">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body>
    <header>
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
    </header>

    <section>
        <div class="container">
            <div class="details-box mx-auto my-4 p-4 bg-light rounded">
                <h2 class="text-center">Welcome, <span id="username"></span>!</h2>
                <p><b>Account ID:</b> <span id="accountNumber"></span></p>
                <p><b>Account Balance:</b> <span id="balance"></span></p>
                <p><b>Email: </b> <span id="email"></span></p>
            </div>
            
            <button onclick="checkBalance()" class="btn btn-outline-primary">Check Balance</button>
            <button onclick="getTransactionDetails()" class="btn btn-outline-primary">Transaction Details</button>
            <h3 class="mt-4">Transaction History</h3>
            <table class="table table-bordered">
                <thead class="table-dark">
                    <tr>
                        <th>Transaction ID</th>
                        <th>Type</th>
                        <th>Amount</th>
                        <th>Date</th>
                        <th>Description</th>
                    </tr>
                </thead>
                <tbody id="transactionBody">
                    <!-- Add table rows here -->
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
