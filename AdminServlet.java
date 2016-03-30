package finalproject;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class LogoutServlet
 */
@WebServlet("/AdminServlet")
public class AdminServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminServlet() {
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
		QuizManager qm = (QuizManager)request.getServletContext().getAttribute(QuizManager.ATTR_NAME);
		ResultManager rm = (ResultManager)request.getServletContext().getAttribute(ResultManager.ATTR_NAME);
		int quizID = Integer.parseInt(request.getParameter("quizID"));
		if (request.getParameter("admin").equals("Delete Quiz")){
			qm.deleteQuiz(quizID);
		} else if (request.getParameter("admin").equals("Verify Quiz")) {
			qm.verifyQuiz(quizID);
		} else {
			rm.deleteResults(quizID);
		}
		RequestDispatcher dispatcher = request.getRequestDispatcher("homepage.jsp");
		if (request.getParameter("adminPanel") != null) {
			dispatcher = request.getRequestDispatcher("manage-reports.jsp");
		}
		dispatcher.forward(request, response);
	}

}