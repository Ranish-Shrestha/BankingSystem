package DAO;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
			int clientId = clientStatement.executeUpdate();
			System.out.println("Client Added. Rows affected: " + clientId);

			String accountSQL = "INSERT INTO Accounts (client_id, account_type, balance) VALUES (?, ?, ?)";
			accountStatement = connection.prepareStatement(accountSQL);
			accountStatement.setInt(1, clientId);
			accountStatement.setString(2, accountType);
			accountStatement.setBigDecimal(3, BigDecimal.ZERO);
			accountStatement.executeUpdate();
			System.out.println("Account Added");

			connection.commit();

			response.setContentType("application/json"); 
			response.getWriter().write(String.format("{\"message\": \"Account created successfully\", \"name\": %1s}", name));
			//request.setAttribute("successMessage", "Account created successfully for " + name);
			//request.getRequestDispatcher("success.jsp").forward(request, response);

		} catch (SQLException e) {
			if (connection != null) {
				try {
					connection.rollback();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
			//request.setAttribute("errorMessage", e.getMessage());
			//request.getRequestDispatcher("error.jsp").forward(request, response);
			
			response.setContentType("application/json"); 
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); 
			response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
		} finally {
			closeResources(clientStatement, accountStatement, connection);
		}	
	}
	
	public void signupAccount(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
	    String email = request.getParameter("email");
	    String password = request.getParameter("password");

	    Connection connection = null;
	    PreparedStatement statement = null;
	    PreparedStatement accountStatement = null;
	    ResultSet userResultSet = null;
	    ResultSet accountResultSet = null;

	    try {
	        connection = setupConnection();

	        if (connection == null) {
	            throw new ServletException("Unable to establish database connection");
	        }

	        String sql = "SELECT * FROM Clients WHERE email = ? AND password = ?";
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
	            
	            response.addCookie(usernameCookie);
	            response.addCookie(userCookie);

	            String accountSql = "SELECT * FROM Accounts WHERE client_id = ?";
	            accountStatement = connection.prepareStatement(accountSql);
	            accountStatement.setInt(1, clientId);
	            accountResultSet = accountStatement.executeQuery();

	            if (accountResultSet.next()) {
	                int accountId = accountResultSet.getInt("account_id");
	                Cookie accountCookie = new Cookie("accountId", String.valueOf(accountId));
	                accountCookie.setMaxAge(60 * 60); // 1 hour expiry
	                
	                response.addCookie(accountCookie);
	                response.setContentType("application/json");
	                response.getWriter().write("{\"message\": \"Login successful\"}");
	            } else {
	                response.setContentType("application/json");
	                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	                response.getWriter().write("{\"error\": \"No account found for this client\"}");
	            }
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
	        closeResources(userResultSet, accountResultSet, statement, accountStatement, connection);
	    }
	}

	public void displayBalance(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			int accountId = 0;
			for (Cookie cookie : request.getCookies()) {
			    if ("accountId".equals(cookie.getName())) {
			    	accountId = Integer.parseInt(cookie.getValue());
			    }
			}

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
				response.getWriter().write("{\"balance\": " + balance + "}"); 
			} else {
//				request.setAttribute("errorMessage", "Account not found");
//				request.getRequestDispatcher("error.jsp").forward(request, response);
				
				response.setContentType("application/json"); 
				response.setStatus(HttpServletResponse.SC_NOT_FOUND); 
				response.getWriter().write("{\"error\": \"Account not found\"}");
			}

		} catch (SQLException | NumberFormatException e) {
			//request.setAttribute("errorMessage", e.getMessage());
			//request.getRequestDispatcher("error.jsp").forward(request, response);
			
			response.setContentType("application/json"); 
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); 
			response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");

		} finally {
			closeResources(resultSet, statement, connection);
		}
	}

	public void depositMoney(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Implementation for depositing money

	}

	public void withdrawMoney(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Implementation for withdrawing money

	}

	public void transferMoney(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Implementation for transferring money

	}

	public void payBills(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Implementation for paying utility bills

	}
}
