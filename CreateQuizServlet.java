package finalproject;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.sql.Timestamp;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class CreateQuizServlet
 */
@WebServlet("/CreateQuizServlet")
public class CreateQuizServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateQuizServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		ServletContext context = request.getServletContext();
		QuizManager quizManager = (QuizManager) context.getAttribute(QuizManager.ATTR_NAME);
		Integer quizId = (Integer) session.getAttribute("currentQuizId");
		if(quizId == null){
			RequestDispatcher dispatcher = request.getRequestDispatcher("create-quiz.jsp");
			dispatcher.include(request, response);
		} else{
			Quiz quiz = quizManager.inProgressQuizForQuizID(quizId);
			UserManager userManager = (UserManager) context.getAttribute(UserManager.ATTR_NAME);	
			
			String title = request.getParameter("title");
			quiz.setTitle(title);
		
			String description = request.getParameter("description");
			quiz.setDescription(description);
			
			int author = userManager.getCurrentUserID(request);
			quiz.setAuthor(author);
			
			String randomizedText = request.getParameter("randomized");
			boolean randomized = randomizedText.equals("yes") ? true: false;
			quiz.setRandomized(randomized);
			
			String numPagesText = request.getParameter("numpages");
			boolean singlePage = numPagesText.equals("single") ? true: false;
			quiz.setSinglePage(singlePage);
			
			String correctionText = request.getParameter("correction");
			boolean correction = correctionText.equals("immediate") ? true: false;
			quiz.setImmediateCorrection(correction);
			
			Calendar calendar = Calendar.getInstance();
			Date now = calendar.getTime();
			Timestamp createdDate = new Timestamp(now.getTime());
			quiz.setCreatedDate(createdDate);
		
			int quizIDinDB = quizManager.createQuiz(quizId);
			AchievementManager am = (AchievementManager)request.getServletContext().getAttribute(AchievementManager.ATTR_NAME);
			Achievement ach = am.checkQuizCreatingAchievement(author);
			if (ach == null){
				RequestDispatcher dispatcher = request.getRequestDispatcher("quiz-created.jsp?quizID=" + quizIDinDB + "&ach=");
				dispatcher.include(request, response);
			} else {
				RequestDispatcher dispatcher = request.getRequestDispatcher("quiz-created.jsp?quizID=" + quizIDinDB + "&ach=" + ach.getText());
				dispatcher.include(request, response);
			}
			session.removeAttribute("currentQuizId");
			session.removeAttribute("description");
			session.removeAttribute("title");
		}
	}

}
