package Servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Servlet implementation class AccountServlet
 */
@WebServlet("/account")
public class AccountServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AccountServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");

		switch (action) {

		case "create":
			createAccount(request, response);
			break;

		case "balance":
			displayBalance(request, response);
			break;

		case "deposit":
			depositMoney(request, response);
			break;

		case "withdraw":
			withdrawMoney(request, response);
			break;

		case "transfer":
			transferMoney(request, response);
			break;

		case "pay":
			payBills(request, response);
			break;

		default:
			throw new ServletException("Unknown action: " + action);

		}
	}

	private Connection setupConnection() {
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

	private void createAccount(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String name = request.getParameter("name");
		String address = request.getParameter("address");
		String phoneNumber = request.getParameter("phone_number");
		String email = request.getParameter("email");
		String accountType = request.getParameter("account_type");

		Connection connection = null;
		PreparedStatement clientStatement = null;
		PreparedStatement accountStatement = null;

		try {
			connection = setupConnection();

			if (connection == null) {
				throw new ServletException("Unable to establish database connection");
			}

			connection.setAutoCommit(false);

			String clientSQL = "INSERT INTO Clients (name, address, phone_number, email) VALUES (?, ?, ?, ?)";
			clientStatement = connection.prepareStatement(clientSQL, PreparedStatement.RETURN_GENERATED_KEYS);
			clientStatement.setString(1, name);
			clientStatement.setString(2, address);
			clientStatement.setString(3, phoneNumber);
			clientStatement.setString(4, email);
			clientStatement.executeUpdate();
			System.out.println("Client Added");
			
			int clientId = 0;
			var generatedKeys = clientStatement.getGeneratedKeys();
			if (generatedKeys.next()) {
				clientId = generatedKeys.getInt(1);
			}

			String accountSQL = "INSERT INTO Accounts (client_id, account_type, balance) VALUES (?, ?, ?)";
			accountStatement = connection.prepareStatement(accountSQL);
			accountStatement.setInt(1, clientId);
			accountStatement.setString(2, accountType);
			accountStatement.setBigDecimal(3, BigDecimal.ZERO);
			accountStatement.executeUpdate();
			System.out.println("Account Added");
			
			connection.commit();

			request.setAttribute("successMessage", "Account created successfully for " + name); 
			request.getRequestDispatcher("success.jsp").forward(request, response);

		} catch (SQLException e) {
			if (connection != null) {
				try {
					connection.rollback();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
			request.setAttribute("errorMessage", e.getMessage()); 
			request.getRequestDispatcher("error.jsp").forward(request, response);
		} finally {
			if (clientStatement != null) {
				try {
					clientStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			if (accountStatement != null) {
				try {
					accountStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void displayBalance(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Implementation for displaying the current balance

	}

	private void depositMoney(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Implementation for depositing money

	}

	private void withdrawMoney(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Implementation for withdrawing money

	}

	private void transferMoney(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Implementation for transferring money

	}

	private void payBills(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Implementation for paying utility bills

	}
}
