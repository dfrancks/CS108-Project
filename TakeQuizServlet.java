package finalproject;

import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/TakeQuizServlet")
public class TakeQuizServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TakeQuizServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		QuizManager qm = (QuizManager)request.getServletContext().getAttribute(QuizManager.ATTR_NAME);
		ResultManager rm = (ResultManager)request.getServletContext().getAttribute(ResultManager.ATTR_NAME);
		int quizID = Integer.parseInt(request.getParameter("quizID"));
		Quiz currentQuiz = qm.getCurrentlyTakenQuiz(quizID);
		int size = currentQuiz.getSize();
		if (currentQuiz.getSinglePage()) {
			correctQuestions(request, currentQuiz, response);
		} else { 
			int count = Integer.parseInt(request.getParameter("questionCount"));
			RequestDispatcher dispatcher;
			int score = Integer.parseInt(request.getParameter("currentScore"));
			Question q = currentQuiz.getQuestions().get(count - 1);
			score += correctQuestion(request, response, q, count - 1, size, quizID);
			UserManager userManager = (UserManager)request.getServletContext().getAttribute(UserManager.ATTR_NAME);	
			int author = userManager.getCurrentUserID(request);
			AchievementManager am = (AchievementManager)request.getServletContext().getAttribute(AchievementManager.ATTR_NAME);
			Achievement ach = am.checkQuizTakingAchievement(author);
			String text = "";
			if (ach != null) text = ach.getText();
			if (count < size) {
				dispatcher = request.getRequestDispatcher("take-quiz.jsp?quizID=" + quizID + "&currentScore=" + score);
				dispatcher.include(request, response);
			} else {
				long afterTime = System.currentTimeMillis();
				Long elapsedTime = (afterTime - Long.parseLong(request.getParameter("startedTime")))/1000;
				Time time = new Time(elapsedTime);
				request.setAttribute("elapsedTime", elapsedTime);
				rm.addResult(author, quizID, score, time); 
				dispatcher = request.getRequestDispatcher("quiz-complete.jsp?quizID=" + quizID + "&score=" + score + "&ach=" + text + "&time=" + time);
				dispatcher.include(request, response);
			}
		}
	}
	
	private void correctQuestions(HttpServletRequest request, Quiz quiz, HttpServletResponse response) throws ServletException, IOException{
		ArrayList<Question> questions = quiz.getQuestions();
		int correct = 0;
		int size = quiz.getSize();
		for (int i = 0; i < questions.size(); i++){
			correct += correctQuestion(request, response, questions.get(i), i, size, quiz.getQuizID());
		}
		UserManager userManager = (UserManager)request.getServletContext().getAttribute(UserManager.ATTR_NAME);	
		int author = userManager.getCurrentUserID(request);
		AchievementManager am = (AchievementManager)request.getServletContext().getAttribute(AchievementManager.ATTR_NAME);
		Achievement ach = am.checkQuizTakingAchievement(author);
		String text = "";
		if (ach != null)
			text = ach.getText();
		ResultManager rm = (ResultManager)request.getServletContext().getAttribute(ResultManager.ATTR_NAME);
		long afterTime = System.currentTimeMillis();
		Long elapsedTime = (afterTime - Long.parseLong(request.getParameter("startedTime")))/1000;
		Time time = new Time(elapsedTime);
		request.setAttribute("elapsedTime", elapsedTime);
		rm.addResult(author, quiz.getQuizID(), correct, time); 
		RequestDispatcher dispatcher = request.getRequestDispatcher("quiz-complete.jsp?score=" + correct + "&ach=" + text + "&time=" + time);
		dispatcher.include(request, response);
	}
	
	private int correctQuestion(HttpServletRequest request, HttpServletResponse response, Question q, int i, int size, int quizID){
		ResultManager rm = (ResultManager)request.getServletContext().getAttribute(ResultManager.ATTR_NAME);
		String userAnswer = request.getParameter("question" + i);
		ArrayList<Answer> possibleAnswers = q.getAnswers();
		for (int j = 0; j < possibleAnswers.size(); j++) {
			String answer = possibleAnswers.get(j).getAnswerText();
			if ((userAnswer.toLowerCase()).equals(answer.toLowerCase()) && possibleAnswers.get(j).isCorrect()){
				String text = "Congratulations on selecting the correct answer of " + userAnswer + "!";
				request.setAttribute("evaluationText", text);
				rm.addQuestionToResult(quizID, new QuestionResult(i, q.getQuestionText(), q.getSecondaryQuestionText(), userAnswer, possibleAnswers, q.getPoints(), q.getType()), size);
				return q.getPoints();
			}
		}
		String text = "Unfortunately, your answer " + userAnswer + " was incorrect. A possible correct answer is " + possibleAnswers.get(0).getAnswerText() + ".";
		request.setAttribute("evaluationText", text);
		rm.addQuestionToResult(quizID, new QuestionResult(i, q.getQuestionText(), q.getSecondaryQuestionText(), userAnswer, possibleAnswers, 0, q.getType()), size);
		return 0;
	}
}