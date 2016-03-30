package finalproject;

import finalproject.Report.ReportType;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/ReportServlet")
public class ReportServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UserManager um = (UserManager)request.getServletContext().getAttribute(UserManager.ATTR_NAME);
		QuizManager qm = (QuizManager)request.getServletContext().getAttribute(QuizManager.ATTR_NAME);
		int quizID = Integer.parseInt(request.getParameter("quizID"));
		String reportTypeStr = request.getParameter("report");
		int reportType = 0;
		if (reportTypeStr.equals("inappropriate")) {
			reportType = ReportType.INAPPROPRIATE.ordinal();
		} else if (reportTypeStr.equals("targets")) {
			reportType = ReportType.TARGETS_PERSON.ordinal();
		} else if (reportTypeStr.equals("spam")) {
			reportType = ReportType.SPAM.ordinal();
		}
		if (!qm.userHasReportedQuiz(um.getCurrentUserID(request), quizID)) {
			qm.reportQuiz(quizID, um.getCurrentUserID(request), reportType);
		}
		RequestDispatcher dispatcher = request.getRequestDispatcher("quiz-page.jsp?quizid=" + request.getParameter("quizID"));
		dispatcher.forward(request, response);
	}
}
