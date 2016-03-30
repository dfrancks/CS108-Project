package finalproject;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class QuizManager {
	private DBConnection conn;
	private HashMap<Integer, Quiz> currQuizzes;
	private HashMap<Integer, Quiz> currQuizzesTaken;
	
	public static final String ATTR_NAME = "Quiz Manager";
	
	public QuizManager() { 
		conn = DBConnection.getCurrentDBConnection(); 
		currQuizzes = new HashMap<Integer, Quiz>();
		currQuizzesTaken = new HashMap<Integer, Quiz>();
	}
	
	// Initializes empty quiz object and returns the ID used to fetch it (note: different from ID in the database)
	public Integer initQuiz() {
		Integer quizID = currQuizzes.size() + 1;
		currQuizzes.put(quizID, new Quiz());
		return quizID;
	}
	
	public Quiz getQuizFromHashMap(int quizID){
		return currQuizzes.get(quizID);
	}
	
	public Quiz getCurrentlyTakenQuiz(int quizID){
		return currQuizzesTaken.get(quizID);
	}
	
	public Quiz inProgressQuizForQuizID(int quizID) {
		return currQuizzes.get(quizID); 
	}
	
	public String booleanToString(boolean value){
		int result = value ? 1 : 0;
		return Integer.toString(result);
	}
	
	public int createQuiz(Integer quizID) {
		if (!currQuizzes.containsKey(quizID)) return 0;
		Quiz q = currQuizzes.get(quizID);
		String query = "INSERT INTO quiz (user_id, title, description, single_page, randomized, immediate_correction)"
						+ " VALUES(?, ?, ?, ?, ?, ?)";
		String[] args = { 
			Integer.toString(q.getAuthor()), q.getTitle(), q.getDescription(), booleanToString(q.getSinglePage()), 
			booleanToString(q.getRandomized()), booleanToString(q.getImmediateCorrection())
		};
		conn.executeUpdate(query, args);
		
		// Get primary key of quiz inserted above
		query = "SELECT LAST_INSERT_ID() FROM quiz LIMIT 1";
		String[] args1 = {};
		ResultSet rs = conn.executeQuery(query, args1);
		int quizIDinDB = 0;
		try {
			if (rs.next()) { 
				QuestionManager m = new QuestionManager();
				m.addQuestionsForQuizID(rs.getInt(1), q.getQuestions());
				quizIDinDB = rs.getInt(1);
			} else {
				// Throw exception?
				return 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return quizIDinDB;
	}
	
	public void updateQuiz(int quizID, int userID, String title, String description, 
						   boolean singlePage, boolean randomized, boolean correction) {
		String query = "UPDATE quiz SET user_id = ?, title = ?, description = ?, single_page = ?, "
						+ "randomized = ?, immediate_correction = ? WHERE quiz_id = ?";
		String[] args = {
			Integer.toString(userID), title, description, Boolean.toString(singlePage), 
			Boolean.toString(randomized), Boolean.toString(correction), Integer.toString(quizID)
		};
		conn.executeUpdate(query, args);
	}
	
	public Quiz quizForQuizID(int quizID) {
		String query = "SELECT * FROM quiz WHERE quiz_id = ? AND deleted = 0";
		String[] args = { Integer.toString(quizID) };
		ResultSet rs = conn.executeQuery(query, args);
		try {
			if (rs.next()) {
				QuestionManager qm = new QuestionManager();
				boolean randomized = rs.getBoolean("randomized");
				ArrayList<Integer> questionIDs = qm.questionIDsForQuiz(rs.getInt("quiz_id"), randomized);
				ArrayList<Question> questions = qm.questionsForQuiz(rs.getInt("quiz_id"), randomized);
				Quiz quiz = new Quiz(rs.getInt("quiz_id"), rs.getInt("user_id"), rs.getString("title"), 
								rs.getString("description"), rs.getBoolean("single_page"), randomized, 
								rs.getBoolean("immediate_correction"), rs.getTimestamp("created_date"), questionIDs);
				quiz.setQuestions(questions);
				if(randomized) quiz.randomizeQuestions();
				currQuizzesTaken.put(quizID, quiz);
				return quiz;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<Quiz> getRecentQuizzes() {
		String query = "SELECT title, description, quiz.quiz_id FROM quiz WHERE deleted = 0 ORDER BY created_date DESC LIMIT 10";
		String[] args = {};
		return getQuizList(query, args);
	}
	
	public ArrayList<Quiz> getAllQuizzes() {
		String query = "SELECT title, description, quiz.quiz_id FROM quiz WHERE deleted = 0 ORDER BY created_date DESC";
		String[] args = {};
		return getQuizList(query, args);
	}
	
	public ArrayList<Quiz> getPopularQuizzes() {
		String query = "SELECT title, description, quiz.quiz_id, COUNT(result_id) AS result_count FROM quiz "
					   	+ "LEFT OUTER JOIN result ON quiz.quiz_id = result.quiz_id WHERE quiz.deleted = 0 AND result.deleted = 0 "
					   	+ "GROUP BY(quiz_id) ORDER BY result_count DESC LIMIT 10";
		String[] args = {};
		return getQuizList(query, args);
	}
	
	public ArrayList<Quiz> getRecentlyTakenQuizzes(int userID) {
		String query = "SELECT DISTINCT quiz.quiz_id, title, description FROM quiz "
						+ "INNER JOIN result ON quiz.quiz_id = result.quiz_id WHERE result.user_id = ? AND quiz.deleted = 0 AND result.deleted = 0 "
						+ "ORDER BY result.date_taken DESC";
		String[] args = { Integer.toString(userID) };
		return getQuizList(query, args);
	}
	
	public ArrayList<Quiz> getCreatedQuizzes(int userID) {
		String query = "SELECT quiz_id, title, description FROM quiz WHERE user_id = ? AND deleted = 0 ORDER BY created_date DESC";
		String[] args = { Integer.toString(userID) };
		return getQuizList(query, args);
	}
	
	public ArrayList<Quiz> searchQuizzes(String searchString) {
		String query = "SELECT DISTINCT * FROM quiz WHERE UPPER(title) LIKE UPPER('%" + searchString + "%') AND deleted = 0";
		String[] args = {};
		return getQuizList(query, args);
	}
	
	public void deleteQuiz(int quizID) {
		String query = "UPDATE quiz SET deleted = ? WHERE quiz_id = ?";
		String[] args = {"1", Integer.toString(quizID) };
		conn.executeUpdate(query, args);
		
		query = "UPDATE result SET deleted = ? WHERE quiz_id = ?";
		conn.executeUpdate(query, args);
	}
	
	public void verifyQuiz(int quizID) {
		String query = "UPDATE quiz SET verified = ? WHERE quiz_id = ?";
		String[] args = {"1", Integer.toString(quizID) };
		conn.executeUpdate(query, args);
	}
	
	public void reportQuiz(int quizID, int userID, int type) {
		String query = "INSERT INTO report (quiz_id, user_id, type) VALUES(?, ?, ?)";
		String[] args = { Integer.toString(quizID), Integer.toString(userID), Integer.toString(type) };
		conn.executeUpdate(query, args);
	}
	
	public boolean userHasReportedQuiz(int userID, int quizID) {
		String query = "SELECT report_id FROM report WHERE user_id = ? AND quiz_id = ?";
		String[] args = { Integer.toString(userID), Integer.toString(quizID) };
		ResultSet rs = conn.executeQuery(query, args);
		try {
			if (rs.next()) return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public ArrayList<Quiz> getReportedQuizzes(int reportType) {
		String query = "SELECT DISTINCT * FROM quiz "
						+ "INNER JOIN report ON quiz.quiz_id = report.quiz_id "
						+ "WHERE report.type = ? AND quiz.deleted = 0 AND quiz.verified = 0 "
						+ "ORDER BY report.report_id DESC";
		String[] args = { Integer.toString(reportType) };
		return getQuizListFull(query, args);
	}
	
	private ArrayList<Quiz> getQuizList(String query, String[] args) {
		ArrayList<Quiz> quizzes = new ArrayList<Quiz>();
		ResultSet rs = conn.executeQuery(query, args);
		try {
			while (rs.next()) quizzes.add(new Quiz(rs.getInt("quiz_id"), rs.getString("title"), rs.getString("description")));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return quizzes;
	}
	
	private ArrayList<Quiz> getQuizListFull(String query, String[] args) {
		ArrayList<Quiz> quizzes = new ArrayList<Quiz>();
		ResultSet rs = conn.executeQuery(query, args);
		try {
			while (rs.next()) {
				QuestionManager qm = new QuestionManager();
				ArrayList<Integer> questionIDs = qm.questionIDsForQuiz(rs.getInt("quiz_id"), rs.getBoolean("randomized"));
				ArrayList<Question> questions = qm.questionsForQuiz(rs.getInt("quiz_id"), rs.getBoolean("randomized"));
				Quiz quiz = new Quiz(rs.getInt("quiz_id"), rs.getInt("user_id"), rs.getString("title"), 
								rs.getString("description"), rs.getBoolean("single_page"), rs.getBoolean("randomized"), 
								rs.getBoolean("immediate_correction"), rs.getTimestamp("created_date"), questionIDs);
				quiz.setQuestions(questions);
				quizzes.add(quiz);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return quizzes;
	}
	
}