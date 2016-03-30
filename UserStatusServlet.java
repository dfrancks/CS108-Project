package finalproject;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/UserStatusServlet")
public class UserStatusServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserStatusServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UserManager um = (UserManager)request.getServletContext().getAttribute(UserManager.ATTR_NAME);
		if (request.getParameter("Path").equals("Promote to Admin")){
			um.makeAdmin(Integer.parseInt(request.getParameter("userID")));
		} else {
			if (request.getParameter("Path").equals("Suspend Account"))
				um.setActive(Integer.parseInt(request.getParameter("userID")), false);
			else
				um.setActive(Integer.parseInt(request.getParameter("userID")), true);
		}
		RequestDispatcher dispatcher = request.getRequestDispatcher("user-page.jsp?userID=" + request.getParameter("userID"));
		dispatcher.include(request, response);
	}
}
