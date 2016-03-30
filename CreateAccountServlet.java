package finalproject;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class CreateAccountServlet
 */
@WebServlet("/CreateAccountServlet")
public class CreateAccountServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateAccountServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		ServletContext context = request.getServletContext();
		AccountManager accManager = (AccountManager) context.getAttribute(AccountManager.ATTR_NAME);
		RequestDispatcher dispatcher;
		
		if(!accManager.accountExists(username)){
			if (username.length() > 0 && password.length() > 0){
				accManager.createAccount(username, password);
				UserManager usrManager = (UserManager)request.getServletContext().getAttribute(UserManager.ATTR_NAME);
				int userID;
				while (true) {
					userID = usrManager.userIDForUsername(username);
					if (userID != -1) break;
				}
				Cookie cookie = new Cookie("currentUserID", Integer.toString(userID));
				response.addCookie(cookie);
				response.sendRedirect("homepage.jsp");
			} else{
				dispatcher = request.getRequestDispatcher("name-in-use.jsp");
				dispatcher.forward(request, response);
			}
		} else{
			dispatcher = request.getRequestDispatcher("name-in-use.jsp");
			dispatcher.forward(request, response);
		}
	}

}
