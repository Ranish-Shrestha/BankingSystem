package Servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import DAO.BankSystemDAO;

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

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		BankSystemDAO dao = new BankSystemDAO();

		switch (action) {
			
		case "logout":
			dao.deleteAllCookies(request, response);
			response.sendRedirect("login.jsp");	
			break;
		
		case "checkBalance":
            dao.checkBalance(request, response);
            break;
            
		case "getTransactions":
			dao.getTransactions(request, response);
            break;

		default:
			throw new ServletException("Unknown action: " + action);
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		BankSystemDAO dao = new BankSystemDAO();

		switch (action) {
		case "create":
			dao.createAccount(request, response);
			break;

		case "login":
			dao.signupAccount(request, response);
			break;

		case "deposit":
			dao.depositMoney(request, response);
			break;

		case "withdraw":
			dao.withdrawMoney(request, response);
			break;

		case "transfer":
			dao.transferMoney(request, response);
			break;

		case "payBill":
			dao.payBills(request, response);
			break;

		default:
			throw new ServletException("Unknown action: " + action);
		}
	}

}
