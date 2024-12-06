package Servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
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

		case "balance":
			dao.displayBalance(request, response);
			break; 
			
		case "logout":
			Cookie[] cookies = request.getCookies(); 
			if (cookies != null) { 
				for (Cookie cookie : cookies) { 
					if ("username".equals(cookie.getName())) { 
						cookie.setMaxAge(0); // Delete the cookie 
						response.addCookie(cookie); 
						} 
					} 
				} 
			response.sendRedirect("login.jsp");	
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

		case "pay":
			dao.payBills(request, response);
			break;

		default:
			throw new ServletException("Unknown action: " + action);
		}
	}

}
