package finalproject;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Servlet implementation class CreateQuizServlet
 */
@WebServlet("/CreateQuestionServlet")
public class CreateQuestionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateQuestionServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		saveFields(request);
		saveQuestion(request, response);
		RequestDispatcher dispatcher = request.getRequestDispatcher("create-quiz.jsp");
		dispatcher.forward(request, response);
	}
	
	private void saveFields(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.setAttribute("title", request.getParameter("title"));
		session.setAttribute("description", request.getParameter("description"));
		session.setAttribute("randomized", request.getParameter("randomized"));
		session.setAttribute("numpages", request.getParameter("numpages"));
		session.setAttribute("correction", request.getParameter("correction"));
		session.setAttribute("points", request.getParameter("points"));
	}
	
	private Integer getValidID(HttpServletRequest request){
		HttpSession session = request.getSession();
		ServletContext context = request.getServletContext();
		QuizManager quizManager = (QuizManager) context.getAttribute(QuizManager.ATTR_NAME);
		Integer quizId = (Integer) session.getAttribute("currentQuizId");
		if (quizId == null){
			quizId = quizManager.initQuiz();
			session.setAttribute("currentQuizId", quizId);
		}
		Quiz quiz = quizManager.inProgressQuizForQuizID(quizId);
		if(quiz == null){
			quizId = quizManager.initQuiz();
			session.setAttribute("currentQuizId", quizId);
		}
		return quizId;
	}
	
	private void saveQuestion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		ServletContext context = request.getServletContext();
		QuizManager quizManager = (QuizManager) context.getAttribute(QuizManager.ATTR_NAME);
		Quiz quiz = quizManager.inProgressQuizForQuizID(getValidID(request));
		int size = quiz.getSize();
		String questionType = request.getParameter("questionType");
		String question;
		String[] answer;
		String pointsStr = request.getParameter("points");
		if (pointsStr == null || pointsStr.trim().length() == 0) pointsStr = "1";
		int points = Integer.parseInt(pointsStr);
		if(questionType.equals("1")){
			question = request.getParameter("question");
			answer = request.getParameterValues("answer");
			quiz.addQuestion(new QResponse(question, createAnswerList(answer, true), size, points));
		} else if (questionType.equals("2")){
			question = request.getParameter("primaryQuestion");
			String secondaryQuestion = request.getParameter("secondaryQuestion");
			answer = request.getParameterValues("answer");
			quiz.addQuestion(new FillBlank(question, secondaryQuestion, createAnswerList(answer, true), size, points));
		} else if(questionType.equals("3")){
			question = request.getParameter("question");
			int length = Integer.parseInt(request.getParameter("length"));
			int radioValue = Integer.parseInt(request.getParameter("radio"));
			String[] correctAnswer = {request.getParameter("field" + radioValue)};
			ArrayList<Answer> answerList = createAnswerList(correctAnswer, true);
			String[] incorrectAnswer = new String[length - 1];
			int j = 0;
			for (int i = 0; i < length; i++){
				if (i != radioValue) {
					incorrectAnswer[j] = request.getParameter("field" + i);
					j++;
				}
			}
			answerList.addAll(createAnswerList(incorrectAnswer, false));
			quiz.addQuestion(new MultipleChoice(question, answerList, size, points));
		} else{
			question = request.getParameter("question");
			answer = request.getParameterValues("answer");
			quiz.addQuestion(new PictResponse(question, createAnswerList(answer, true), size, points));
		}
	}
	
	private ArrayList<Answer> createAnswerList(String[] answers, boolean correct){
		ArrayList<Answer> answerList = new ArrayList<Answer>();
		for(int i = 0; i < answers.length; i++){
			answerList.add(new Answer(answers[i], correct));
		}
		return answerList;
	}
}
