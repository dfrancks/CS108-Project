package finalproject;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		ServletContext context = request.getServletContext();
		AccountManager accManager = (AccountManager)context.getAttribute(AccountManager.ATTR_NAME);
		RequestDispatcher dispatcher;
		if(accManager.accountActive(username) && accManager.checkAccount(username, password)){
			UserManager usrManager = (UserManager)request.getServletContext().getAttribute(UserManager.ATTR_NAME);
			int userID;
			while (true) {
				userID = usrManager.userIDForUsername(username);
				if (userID != -1) break;
			}
			Cookie cookie = new Cookie("currentUserID", Integer.toString(userID));
			response.addCookie(cookie);
			response.sendRedirect("homepage.jsp");
		} else if (accManager.accountExists(username) && !accManager.accountActive(username)){
			dispatcher = request.getRequestDispatcher("suspended-account.jsp?username=" + username);
			dispatcher.forward(request, response);
		} else {
			dispatcher = request.getRequestDispatcher("failed-login.html");
			dispatcher.forward(request, response);
		}
	}

}
