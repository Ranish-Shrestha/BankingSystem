package DAO;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import Model.Transaction;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class BankSystemDAO {
	
	public Connection setupConnection() {
		Connection con = null;

		// Database Credentials
		String url = "jdbc:mysql://localhost:3306/BankingSystem";
		String user = "root";
		String pwd = "12345";

		try {
			// Load MySQL JDBC Driver
			Class.forName("com.mysql.cj.jdbc.Driver");

			// Establish Connection
			con = DriverManager.getConnection(url, user, pwd);
			System.out.println("Connection Successful");

		} catch (ClassNotFoundException e) {
			System.err.println("MySQL JDBC Driver not found");
			e.printStackTrace();
		} catch (SQLException e) {
			System.err.println("Failed to establish connection");
			e.printStackTrace();
		}

		return con;
	}
	
	public void closeResources(AutoCloseable... resources) {
		for (AutoCloseable resource : resources) { 
			if (resource != null) { 
				try { 
					resource.close(); 
					
				} catch (Exception e) { 
					e.printStackTrace(); 
				} 
			} 	
		}
	}
	
	public void deleteAllCookies(HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				cookie.setMaxAge(0);
				cookie.setPath("/BankingSystem");
				response.addCookie(cookie);
			}
		}
	}
	
	public void createAccount(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String name = request.getParameter("name");
		String address = request.getParameter("address");
		String phoneNumber = request.getParameter("phone_number");
		String email = request.getParameter("email");
		String accountType = request.getParameter("account_type");
		String password = request.getParameter("password");

		Connection connection = null;
		PreparedStatement clientStatement = null;
		PreparedStatement accountStatement = null;
		ResultSet generatedKeys = null;

		try {
			connection = setupConnection();

			if (connection == null) {
				throw new ServletException("Unable to establish database connection");
			}

			connection.setAutoCommit(false);

			String clientSQL = "INSERT INTO Clients (name, address, phone_number, email, password) VALUES (?, ?, ?, ?, ?)";
			clientStatement = connection.prepareStatement(clientSQL, PreparedStatement.RETURN_GENERATED_KEYS);
			clientStatement.setString(1, name);
			clientStatement.setString(2, address);
			clientStatement.setString(3, phoneNumber);
			clientStatement.setString(4, email);
			clientStatement.setString(5, password);
			clientStatement.executeUpdate();
			
			// Retrieve the generated client ID 
			generatedKeys = clientStatement.getGeneratedKeys(); 
			int clientId = 0; 
			if (generatedKeys.next()) { 
				clientId = generatedKeys.getInt(1); 
			} else {
				throw new SQLException("Creating client failed, no ID obtained.");
			}

			String accountSQL = "INSERT INTO Accounts (client_id, account_type, balance) VALUES (?, ?, ?)";
			accountStatement = connection.prepareStatement(accountSQL);
			accountStatement.setInt(1, clientId);
			accountStatement.setString(2, accountType);
			accountStatement.setBigDecimal(3, BigDecimal.ZERO);
			accountStatement.executeUpdate();
			System.out.println("Account Added");

			connection.commit();

			response.setContentType("application/json"); 
			response.getWriter().write(String.format("{\"message\": \"Account created successfully\", \"name\": \"%s\"}", name));

		} catch (SQLException e) {
			if (connection != null) {
				try {
					connection.rollback();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
			response.setContentType("application/json"); 
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); 
			response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
		} finally {
			closeResources(generatedKeys, clientStatement, accountStatement, connection);
		}	
	}
	
	public void signupAccount(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
	    String email = request.getParameter("email");
	    String password = request.getParameter("password");
		
	    Connection connection = null;
	    PreparedStatement statement = null;
	    ResultSet userResultSet = null;

	    try {
	    	deleteAllCookies(request, response);
	    	
	        connection = setupConnection();

	        if (connection == null) {
	            throw new ServletException("Unable to establish database connection");
	        }

	        String sql = "SELECT c.client_id, c.name, c.address, c.phone_number, c.email, "
	                   + "a.account_id, a.account_type, a.balance "
	                   + "FROM bankingsystem.Clients AS c "
	                   + "INNER JOIN bankingsystem.accounts AS a on c.client_id = a.client_id "
	                   + "WHERE c.email = ? AND c.password = ?";
	        statement = connection.prepareStatement(sql);
	        statement.setString(1, email);
	        statement.setString(2, password);
	        userResultSet = statement.executeQuery();

	        if (userResultSet.next()) {
	            int clientId = userResultSet.getInt("client_id");
	            Cookie userCookie = new Cookie("clientId", String.valueOf(clientId));
	            userCookie.setMaxAge(60 * 60); // 1 hour expiry

	            String username = userResultSet.getString("name");
	            Cookie usernameCookie = new Cookie("username", username);
	            usernameCookie.setMaxAge(60 * 60); // 1 hour expiry 

	            int accountId = userResultSet.getInt("account_id");
	            Cookie accountCookie = new Cookie("accountId", String.valueOf(accountId));
	            accountCookie.setMaxAge(60 * 60);

	            Cookie emailCookie = new Cookie("email", email);
	            emailCookie.setMaxAge(60 * 60);

	            String accountType = userResultSet.getString("account_type");
	            Cookie accountTypeCookie = new Cookie("account_type", accountType);
	            accountTypeCookie.setMaxAge(60 * 60);

	            BigDecimal balance = userResultSet.getBigDecimal("balance");
	            Cookie balanceCookie = new Cookie("balance", balance.toString());
	            balanceCookie.setMaxAge(60 * 60);

	            String phone = userResultSet.getString("phone_number");
	            Cookie phoneCookie = new Cookie("phone_number", phone);
	            phoneCookie.setMaxAge(60 * 60);
	            
	            // Add cookies to response
	            response.addCookie(userCookie);
	            response.addCookie(usernameCookie);
	            response.addCookie(accountCookie);
	            response.addCookie(emailCookie);
	            response.addCookie(accountTypeCookie);
	            response.addCookie(balanceCookie);
	            response.addCookie(phoneCookie);

	            response.setContentType("application/json");
	            response.getWriter().write("{\"message\": \"Login successful\"}");
	        } else {
	            response.setContentType("application/json");
	            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	            response.getWriter().write("{\"error\": \"Invalid email or password\"}");
	        }

	    } catch (SQLException | NumberFormatException e) {
	        response.setContentType("application/json");
	        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
	    } finally {
	        closeResources(userResultSet, statement, connection);
	    }
	}

	public void depositMoney(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String accountIdStr = request.getParameter("account_id"); 
		String amountStr = request.getParameter("amount"); 
		
		Connection connection = null; 
		PreparedStatement statement = null; 
		
		try { 
			int accountId = Integer.parseInt(accountIdStr); 
			BigDecimal amount = new BigDecimal(amountStr); 
			
			connection = setupConnection(); 
			if (connection == null) { 
				throw new ServletException("Unable to establish database connection"); 
			} 
			
			String sql = "UPDATE Accounts SET balance = balance + ? WHERE account_id = ?"; 
			statement = connection.prepareStatement(sql); 
			statement.setBigDecimal(1, amount); 
			statement.setInt(2, accountId); 
			
			int rowsAffected = statement.executeUpdate(); 
			
			if (rowsAffected > 0) {
                // Add an entry to the Transactions table
                String recordTransactionSql = "INSERT INTO Transactions (account_id, transaction_type, amount, description) VALUES (?, ?, ?, ?)";
                statement = connection.prepareStatement(recordTransactionSql);
                statement.setInt(1, accountId);
                statement.setString(2, "DEPOSIT");
                statement.setBigDecimal(3, amount);
                statement.setString(4, "Deposit into account");
                statement.executeUpdate();
                
				response.setContentType("application/json"); 
				response.getWriter().write("{\"message\": \"Deposit successful\"}"); 
			} else { 
				response.setContentType("application/json"); 
				response.setStatus(HttpServletResponse.SC_NOT_FOUND); 
				response.getWriter().write("{\"error\": \"Account not found\"}"); 
			} 
		} catch (SQLException | NumberFormatException e) { 
			response.setContentType("application/json"); 
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); 
			response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}"); 
		} finally { 
			closeResources(statement, connection); 
		}
	}
	
	public void checkBalance(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accountIdStr = request.getParameter("account_id");

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            int accountId = Integer.parseInt(accountIdStr);

            connection = setupConnection();

            if (connection == null) {
                throw new ServletException("Unable to establish database connection");
            }

            String sql = "SELECT balance FROM Accounts WHERE account_id = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, accountId);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                BigDecimal balance = resultSet.getBigDecimal("balance");
                response.setContentType("application/json");
                response.getWriter().write("{\"balance\": " + balance.toString() + "}");
            } else {
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"error\": \"Account not found\"}");
            }

        } catch (SQLException | NumberFormatException e) {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

	public void withdrawMoney(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String accountIdStr = request.getParameter("account_id");
        String amountStr = request.getParameter("amount");

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            int accountId = Integer.parseInt(accountIdStr);
            BigDecimal amount = new BigDecimal(amountStr);

            connection = setupConnection();

            if (connection == null) {
                throw new ServletException("Unable to establish database connection");
            }

            connection.setAutoCommit(false); // Start transaction

            // Check if account has sufficient balance
            String checkBalanceSql = "SELECT balance FROM Accounts WHERE account_id = ?";
            statement = connection.prepareStatement(checkBalanceSql);
            statement.setInt(1, accountId);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                BigDecimal balance = resultSet.getBigDecimal("balance");

                if (balance.compareTo(amount) >= 0) {
                    // Deduct amount from account balance
                    String deductBalanceSql = "UPDATE Accounts SET balance = balance - ? WHERE account_id = ?";
                    statement = connection.prepareStatement(deductBalanceSql);
                    statement.setBigDecimal(1, amount);
                    statement.setInt(2, accountId);
                    statement.executeUpdate();

                    // Add an entry to the Transactions table
                    String recordTransactionSql = "INSERT INTO Transactions (account_id, transaction_type, amount, description) VALUES (?, ?, ?, ?)";
                    statement = connection.prepareStatement(recordTransactionSql);
                    statement.setInt(1, accountId);
                    statement.setString(2, "WITHDRAW");
                    statement.setBigDecimal(3, amount);
                    statement.setString(4, "Withdrawal from account");
                    statement.executeUpdate();

                    connection.commit(); // Commit transaction

                    response.setContentType("application/json");
                    response.getWriter().write("{\"message\": \"Withdrawal successful\"}");
                } else {
                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"error\": \"Insufficient balance\"}");
                }
            } else {
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"error\": \"Account not found\"}");
            }

        } catch (SQLException | NumberFormatException e) {
            if (connection != null) {
                try {
                    connection.rollback(); // Rollback transaction on error
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

	public void transferMoney(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String sourceAccountIdStr = request.getParameter("source_account_id");
        String destinationAccountIdStr = request.getParameter("destination_account_id");
        String amountStr = request.getParameter("amount");

        Connection connection = null;
        PreparedStatement checkBalanceStmt = null;
        PreparedStatement updateBalanceStmt = null;
        ResultSet resultSet = null;

        try {
            int sourceAccountId = Integer.parseInt(sourceAccountIdStr);
            int destinationAccountId = Integer.parseInt(destinationAccountIdStr);
            BigDecimal amount = new BigDecimal(amountStr);

            connection = setupConnection();

            if (connection == null) {
                throw new ServletException("Unable to establish database connection");
            }

            connection.setAutoCommit(false); // Start transaction

            // Check if source account has sufficient balance
            String checkBalanceSql = "SELECT balance FROM Accounts WHERE account_id = ?";
            checkBalanceStmt = connection.prepareStatement(checkBalanceSql);
            checkBalanceStmt.setInt(1, sourceAccountId);
            resultSet = checkBalanceStmt.executeQuery();

            if (resultSet.next()) {
                BigDecimal sourceBalance = resultSet.getBigDecimal("balance");

                if (sourceBalance.compareTo(amount) >= 0) {
                    // Deduct amount from source account
                    String deductBalanceSql = "UPDATE Accounts SET balance = balance - ? WHERE account_id = ?";
                    updateBalanceStmt = connection.prepareStatement(deductBalanceSql);
                    updateBalanceStmt.setBigDecimal(1, amount);
                    updateBalanceStmt.setInt(2, sourceAccountId);
                    updateBalanceStmt.executeUpdate();

                    // Add amount to destination account
                    String addBalanceSql = "UPDATE Accounts SET balance = balance + ? WHERE account_id = ?";
                    updateBalanceStmt = connection.prepareStatement(addBalanceSql);
                    updateBalanceStmt.setBigDecimal(1, amount);
                    updateBalanceStmt.setInt(2, destinationAccountId);
                    updateBalanceStmt.executeUpdate();
                    
                    // Add entries to the Transactions table for both accounts
                    String recordTransactionSql = "INSERT INTO Transactions (account_id, transaction_type, amount, description) VALUES (?, ?, ?, ?)";
                    updateBalanceStmt = connection.prepareStatement(recordTransactionSql);
                    updateBalanceStmt.setInt(1, sourceAccountId);
                    updateBalanceStmt.setString(2, "TRANSFER_OUT");
                    updateBalanceStmt.setBigDecimal(3, amount);
                    updateBalanceStmt.setString(4, "Transfer to account " + destinationAccountId);
                    updateBalanceStmt.executeUpdate();

                    updateBalanceStmt.setInt(1, destinationAccountId);
                    updateBalanceStmt.setString(2, "TRANSFER_IN");
                    updateBalanceStmt.setBigDecimal(3, amount);
                    updateBalanceStmt.setString(4, "Transfer from account " + sourceAccountId);
                    updateBalanceStmt.executeUpdate();
                    
                    connection.commit(); // Commit transaction

                    response.setContentType("application/json");
                    response.getWriter().write("{\"message\": \"Transfer successful\"}");
                } else {
                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"error\": \"Insufficient balance in source account\"}");
                }
            } else {
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"error\": \"Source account not found\"}");
            }

        } catch (SQLException | NumberFormatException e) {
            if (connection != null) {
                try {
                    connection.rollback(); // Rollback transaction on error
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        } finally {
            closeResources(resultSet, checkBalanceStmt, updateBalanceStmt, connection);
        }
    }

	public void payBills(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accountIdStr = request.getParameter("account_id");
        String biller = request.getParameter("biller");
        String amountStr = request.getParameter("amount");

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            int accountId = Integer.parseInt(accountIdStr);
            BigDecimal amount = new BigDecimal(amountStr);

            connection = setupConnection();

            if (connection == null) {
                throw new ServletException("Unable to establish database connection");
            }

            connection.setAutoCommit(false); // Start transaction

            // Check if account has sufficient balance
            String checkBalanceSql = "SELECT balance FROM Accounts WHERE account_id = ?";
            statement = connection.prepareStatement(checkBalanceSql);
            statement.setInt(1, accountId);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                BigDecimal balance = resultSet.getBigDecimal("balance");

                if (balance.compareTo(amount) >= 0) {
                    // Deduct amount from account balance
                    String deductBalanceSql = "UPDATE Accounts SET balance = balance - ? WHERE account_id = ?";
                    statement = connection.prepareStatement(deductBalanceSql);
                    statement.setBigDecimal(1, amount);
                    statement.setInt(2, accountId);
                    statement.executeUpdate();

                    // Record the bill payment
                    String recordPaymentSql = "INSERT INTO Payments (account_id, biller, amount) VALUES (?, ?, ?)";
                    statement = connection.prepareStatement(recordPaymentSql);
                    statement.setInt(1, accountId);
                    statement.setString(2, biller);
                    statement.setBigDecimal(3, amount);
                    statement.executeUpdate();

                    // Add an entry to the Transactions table
                    String recordTransactionSql = "INSERT INTO Transactions (account_id, transaction_type, amount, description) VALUES (?, ?, ?, ?)";
                    statement = connection.prepareStatement(recordTransactionSql);
                    statement.setInt(1, accountId);
                    statement.setString(2, "PAY_BILL");
                    statement.setBigDecimal(3, amount);
                    statement.setString(4, "Payment to " + biller);
                    statement.executeUpdate();

                    connection.commit(); // Commit transaction

                    response.setContentType("application/json");
                    response.getWriter().write("{\"message\": \"Bill payment successful\"}");
                } else {
                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"error\": \"Insufficient balance in account\"}");
                }
            } else {
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"error\": \"Account not found\"}");
            }

        } catch (SQLException | NumberFormatException e) {
            if (connection != null) {
                try {
                    connection.rollback(); // Rollback transaction on error
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }
	
	public void getTransactions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    String accountIdStr = request.getParameter("account_id");

	    int cookieAccountId = 0;
	    Cookie[] cookies = request.getCookies();
	    if (cookies != null) {
	        for (Cookie cookie : cookies) {
	            if ("accountId".equals(cookie.getName())) {
	                cookieAccountId = Integer.parseInt(cookie.getValue());
	            }
	        }
	    }

	    Connection connection = null;
	    PreparedStatement statement = null;
	    ResultSet resultSet = null;

	    try {
	        int accountId = Integer.parseInt(accountIdStr) <= 0 ? cookieAccountId : Integer.parseInt(accountIdStr);

	        connection = setupConnection();

	        if (connection == null) {
	            throw new ServletException("Unable to establish database connection");
	        }

	        String sql = "SELECT transaction_id, transaction_type, amount, timestamp, description FROM Transactions WHERE account_id = ?";
	        statement = connection.prepareStatement(sql);
	        statement.setInt(1, accountId);
	        resultSet = statement.executeQuery();

	        List<Transaction> transactions = new ArrayList<>();
	        while (resultSet.next()) {
	            Transaction transaction = new Transaction(
	                    resultSet.getInt("transaction_id"),
	                    resultSet.getString("transaction_type"),
	                    resultSet.getBigDecimal("amount"),
	                    resultSet.getTimestamp("timestamp"),
	                    resultSet.getString("description")
	            );
	            transactions.add(transaction);
	        }

	        response.setContentType("application/json");
	        response.setCharacterEncoding("UTF-8");
	        response.getWriter().write(new Gson().toJson(transactions)); // Using Gson to convert to JSON

	    } catch (SQLException | NumberFormatException e) {
	        response.setContentType("application/json");
	        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
	    } finally {
	        closeResources(resultSet, statement, connection);
	    }
	}

}
