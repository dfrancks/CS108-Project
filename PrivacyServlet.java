package finalproject;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import finalproject.User.PrivacySetting;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/PrivacyServlet")
public class PrivacyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PrivacyServlet() {
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
		UserManager um = (UserManager)request.getServletContext().getAttribute(UserManager.ATTR_NAME);
		String privacyStr = request.getParameter("privacy");
		PrivacySetting currSetting = um.userForUserID(um.getCurrentUserID(request)).getPrivacy();
		if (privacyStr.equals("everyone") && currSetting != PrivacySetting.ANYONE) {
			um.setPrivacySetting(um.getCurrentUserID(request), PrivacySetting.ANYONE.ordinal());
		} else if (privacyStr.equals("friends") && currSetting != PrivacySetting.FRIENDS) {
			um.setPrivacySetting(um.getCurrentUserID(request), PrivacySetting.FRIENDS.ordinal());
		} else if (privacyStr.equals("friendsOfFriends") && currSetting != PrivacySetting.FRIENDS_OF_FRIENDS) {
			um.setPrivacySetting(um.getCurrentUserID(request), PrivacySetting.FRIENDS_OF_FRIENDS.ordinal());
		}
		RequestDispatcher dispatcher = request.getRequestDispatcher("user-page.jsp?userID=" + um.getCurrentUserID(request));
		dispatcher.forward(request, response);
	}

}
