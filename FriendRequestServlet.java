package finalproject;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import finalproject.UserManager.FriendRequestStatus;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/FriendRequestServlet")
public class FriendRequestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FriendRequestServlet() {
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
		UserManager m = (UserManager)request.getServletContext().getAttribute(UserManager.ATTR_NAME);
		String buttonName = request.getParameter("button");
		int otherUserID = Integer.parseInt(request.getParameter("otherUserID"));
		if (buttonName.equals("Un-friend")) {
			int currUserInitiated = Integer.parseInt(request.getParameter("currUserInitiated"));
			if (currUserInitiated == 1) {
				m.updateFriendStatus(otherUserID, m.getCurrentUserID(request), FriendRequestStatus.DELETED);
			} else {
				m.updateFriendStatus(m.getCurrentUserID(request), otherUserID, FriendRequestStatus.DELETED);
			}
		} else if (buttonName.equals("Accept")) {
			m.updateFriendStatus(m.getCurrentUserID(request), otherUserID, FriendRequestStatus.ACCEPTED);
		} else if (buttonName.equals("Reject")) {
			m.updateFriendStatus(m.getCurrentUserID(request), otherUserID, FriendRequestStatus.REJECTED);
		}
		RequestDispatcher dispatcher = request.getRequestDispatcher("user-page.jsp?userID=" + otherUserID);
		dispatcher.forward(request, response);
	}

}
