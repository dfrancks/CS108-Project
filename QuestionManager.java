package finalproject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class QuestionManager {
	private DBConnection conn;
	
	public static final String ATTR_NAME = "Question Manager";
	
	public QuestionManager() { conn = DBConnection.getCurrentDBConnection(); }
	
	public void addQuestionsForQuizID(int quizID, ArrayList<Question> questions) {
		for (Question q : questions) {
			String query = "INSERT INTO question (quiz_id, type, question_text, secondary_question_text, placement, points) VALUES (?, ?, ?, ?, ?, ?)";
			String[] args = { 
				Integer.toString(quizID), Integer.toString(q.getType().ordinal()), q.getQuestionText(), 
				q.getSecondaryQuestionText(), Integer.toString(q.getPlacement()), Integer.toString(q.getPoints())
			};
			conn.executeUpdate(query, args);
			
			// Get primary key of question inserted above
			query = "SELECT LAST_INSERT_ID() FROM question LIMIT 1";
			String[] args1 = {};
			ResultSet rs = conn.executeQuery(query, args1);
			try {
				if (rs.next()) { 
					addAnswersForQuestionID(rs.getInt(1), q.getAnswers());
				} else {
					// Throw exception?
					return;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private String booleanToString(boolean value){
		int result = value ? 1 : 0;
		return Integer.toString(result);
	}
	
	public void addAnswersForQuestionID(int questionID, ArrayList<Answer> answers) {
		StringBuilder sb = new StringBuilder("INSERT INTO answer (question_id, answer_text, correct) VALUES ");
		ArrayList<String[]> argsList = new ArrayList<String[]>();
		for (Answer a : answers) {
			sb.append("(?, ?, ?),");
			String[] args = { Integer.toString(questionID), a.getAnswerText(), booleanToString(a.isCorrect()) };
			argsList.add(args);
		}
		sb.setLength(sb.length() - 1);
		conn.executeUpdates(sb.toString(), argsList);
	}
	
	public ArrayList<Question> questionsForQuiz(int quizID, boolean randomized){
		ArrayList<Question> questions = new ArrayList<Question>();
		ArrayList<Integer> questionIDs = questionIDsForQuiz(quizID, randomized);
		for(Integer questionID : questionIDs){
			questions.add(questionForQuestionID(questionID));
		}
		return questions;
	}
	
	public ArrayList<Integer> questionIDsForQuiz(int quizID, boolean randomized) {
		String query = "SELECT question_id FROM question WHERE quiz_id = ?";
		if (!randomized) {
			query += " ORDER BY placement";
		}
		String[] args = { Integer.toString(quizID) };
		ResultSet rs = conn.executeQuery(query, args);
		ArrayList<Integer> questionIDs = new ArrayList<Integer>();
		try {
			while (rs.next()) questionIDs.add(rs.getInt(1));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return questionIDs;
	}
	
	public Question questionForQuestionID(int questionID) {
		String query = "SELECT * FROM question WHERE question_id = ?";
		String[] args = { Integer.toString(questionID) };
		ResultSet rs = conn.executeQuery(query, args);
		try {
			if (rs.next()) {
				query = "SELECT * FROM answer WHERE question_id = ?";
				ResultSet rs2 = conn.executeQuery(query, args);
				ArrayList<Answer> answers = new ArrayList<Answer>();
				while (rs2.next()) answers.add(new Answer(rs2.getString("answer_text"), rs2.getBoolean("correct")));
				Question q  = new Question(rs.getString("question_text"), answers, rs.getInt("placement"), 
										   Question.QuestionType.values()[rs.getInt("type")], rs.getInt("points"));
				q.setSecondaryText(rs.getString("secondary_question_text"));
				return q;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
