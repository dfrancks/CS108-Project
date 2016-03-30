package finalproject;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/UserSearchServlet")
public class UserSearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserSearchServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UserManager um = (UserManager)request.getServletContext().getAttribute(UserManager.ATTR_NAME);
		ArrayList<User> users = um.searchUsers(request.getParameter("searchText"), um.getCurrentUserID(request));
		request.setAttribute("userList", users);
		RequestDispatcher dispatcher;
		if (request.getParameter("select") != null) {
			dispatcher = request.getRequestDispatcher("all-users.jsp?select=1");
		} else {
			dispatcher = request.getRequestDispatcher("all-users.jsp");
		}
		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}
}
