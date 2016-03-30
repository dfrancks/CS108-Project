package finalproject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;

public class ResultManager {
	private DBConnection conn;
	private HashMap<Integer, QuestionResult[]> resultMap;
	
	public static final String ATTR_NAME = "Result Manager";
	
	public ResultManager() { 
		conn = DBConnection.getCurrentDBConnection(); 
		resultMap = new HashMap<Integer, QuestionResult[]>();
	}
	
	public void addResult(int userID, int quizID, int score, Time timeElapsed) {
		String query = "INSERT INTO result (user_id, quiz_id, score, time_elapsed)" + " VALUES (?, ?, ?, ?)";
		String[] args = { Integer.toString(userID), Integer.toString(quizID), Integer.toString(score), timeElapsed.toString() };
		conn.executeUpdate(query, args);
	}
	
	public Result bestUserResultOnQuiz(int userID, int quizID) {
		String query = "SELECT * FROM result WHERE user_id = ? AND quiz_id = ? AND deleted = 0 ORDER BY score, time_elapsed DESC LIMIT 1";
		String[] args = { Integer.toString(userID), Integer.toString(quizID) };
		ResultSet rs = conn.executeQuery(query, args);
		try {
			if (rs.next()) return new Result(userID, quizID, rs.getInt("score"), rs.getTimestamp("date_taken"), rs.getTime("time_elapsed")); 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void deleteResults(int quizID){
		String query = "UPDATE result SET deleted = ? WHERE quiz_id = ?";
		String[] args = {"1", Integer.toString(quizID) };
		conn.executeUpdate(query, args);
	}
	
	public ArrayList<Result> getUserResultsOnQuiz(int userID, int quizID) {
		String query = "SELECT * FROM result WHERE user_id = ? AND quiz_id = ? AND deleted = 0 ORDER BY date_taken DESC";
		String[] args = { Integer.toString(userID), Integer.toString(quizID) };
		return getResultList(query, args);
	}
	
	public ArrayList<Result> highScoresForQuiz(int quizID) {
		String query = "SELECT * FROM result WHERE quiz_id = ? AND deleted = 0 ORDER BY score DESC, time_elapsed DESC";
		String[] args= { Integer.toString(quizID) };
		return getResultList(query, args);
	}
	
	public ArrayList<Result> highScoresForUser(int userID) {
		String query = "SELECT * FROM result WHERE user_id = ? AND deleted = 0 ORDER BY score DESC, time_elapsed DESC";
		String[] args= { Integer.toString(userID) };
		return getResultList(query, args);
	}
	
	public ArrayList<Result> recentScoresForUser(int userID) {
		String query = "SELECT * FROM result WHERE user_id = ? AND deleted = 0 ORDER BY date_taken DESC";
		String[] args= { Integer.toString(userID) };
		return getResultList(query, args);
	}
	
	public ArrayList<Result> recentTopPerformers(int quizID, int minuteInterval) {
		String query = "SELECT * FROM result WHERE quiz_id = ? AND deleted = 0 AND date_taken >= NOW() - INTERVAL ? MINUTE ORDER BY score DESC";
		String[] args = { Integer.toString(quizID), Integer.toString(minuteInterval) };
		return getResultList(query, args);
	}
	
	public ArrayList<Result> allResultsForQuiz(int quizID) {
		String query = "SELECT * FROM result WHERE quiz_id = ? AND deleted = 0 ORDER BY date_taken DESC";
		String[] args = { Integer.toString(quizID) };
		return getResultList(query, args);
	}
	
	private ArrayList<Result> getResultList(String query, String[] args) {
		ResultSet rs = conn.executeQuery(query, args);
		ArrayList<Result> results = new ArrayList<Result>();
		try {
			while (rs.next()) results.add(new Result(rs.getInt("user_id"), rs.getInt("quiz_id"), rs.getInt("score"), 
													 rs.getTimestamp("date_taken"), rs.getTime("time_elapsed")));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return results;
	}
	
	public void addQuestionToResult(Integer quizID, QuestionResult result, int size){
		if(resultMap.containsKey(quizID)){
			QuestionResult[] qr = resultMap.get(quizID);
			qr[result.getQuestionIndex()] = result;
			resultMap.put(quizID, qr);
		} else {
			QuestionResult[] qr = new QuestionResult[size];
			qr[result.getQuestionIndex()] = result;
			resultMap.put(quizID, qr);
		}
	}
	
	public QuestionResult[] getQuestionResults(int quizID){
		return resultMap.get(quizID);
	}
	
}