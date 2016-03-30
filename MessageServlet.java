package finalproject;

import finalproject.Message.MessageType;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/MessageServlet")
public class MessageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MessageServlet() {
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
		MessageManager mm = (MessageManager)request.getServletContext().getAttribute(MessageManager.ATTR_NAME);
		String messageType = request.getParameter("typeSelect");
		int toUserID = Integer.parseInt(request.getParameter("toUserID"));
		String messageText = request.getParameter("textArea");
		int quizID = -1;
		MessageType type = MessageType.NOTE;
		if (messageType.equals("challenge")) {
			quizID = Integer.parseInt(request.getParameter("quizSelect"));
			type = MessageType.CHALLENGE;
		} else if (messageType.equals("request")) {
			um.sendFriendRequest(um.getCurrentUserID(request), toUserID);
			type = MessageType.REQUEST;
		}
		mm.createMessage(um.getCurrentUserID(request), toUserID, messageText, type, quizID);
		RequestDispatcher dispatcher = request.getRequestDispatcher("user-page.jsp?userID=" + toUserID);
		dispatcher.forward(request, response);
	}
}
