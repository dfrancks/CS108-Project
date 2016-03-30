package finalproject;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AnalyticsManager {
	private DBConnection conn;
	
	public static final String ATTR_NAME = "Analytics Manager";
	
	public AnalyticsManager() { conn = DBConnection.getCurrentDBConnection(); }
	
	public int getNumCreatedQuizzesWithDays(int days) {
		String query = "SELECT COUNT(CASE WHEN created_date > DATE_SUB(CURDATE(), INTERVAL ? DAY) THEN 1 END) as quiz_count FROM quiz WHERE deleted = 0";
		String[] args = { Integer.toString(days) };
		return runCountQuery(query, args);
	}
	
	public int getNumCreatedQuizzesWithMonths(int months) {
		String query = "SELECT COUNT(CASE WHEN created_date > DATE_SUB(CURDATE(), INTERVAL ? MONTH) THEN 1 END) as quiz_count FROM quiz WHERE deleted = 0";
		String[] args = { Integer.toString(months) };
		return runCountQuery(query, args);
	}
	
	public int getTotalNumQuizzes() {
		String query = "SELECT COUNT(*) as quiz_count FROM quiz WHERE deleted = 0";
		String[] args = {};
		return runCountQuery(query, args);
	}
	
	public int getNumUsersTakenQuizWithDays(int days) {
		String query = "SELECT COUNT(DISTINCT(CASE WHEN date_taken > DATE_SUB(CURDATE(), INTERVAL ? DAY) " +
						"THEN user_id end)) as user_count FROM result WHERE deleted = 0";
		String[] args = { Integer.toString(days) };
		return runCountQuery(query, args);
	}
	
	public int getNumUsersTakenQuizWithMonths(int months) {
		String query = "SELECT COUNT(DISTINCT(CASE WHEN date_taken > DATE_SUB(CURDATE(), INTERVAL ? MONTH) " +
						"THEN user_id end)) as user_count FROM result WHERE deleted = 0";
		String[] args = { Integer.toString(months) };
		return runCountQuery(query, args);
	}
	
	public int getTotalNumUsersTakenQuiz() {
		String query = "SELECT COUNT(DISTINCT user_id) as user_count FROM result WHERE deleted = 0";
		String[] args = { };
		return runCountQuery(query, args);
	}
	
	public int getNumNewUsersWithDays(int days) {
		String query = "SELECT COUNT(CASE WHEN date_joined > DATE_SUB(CURDATE(), INTERVAL ? DAY) THEN 1 END) as user_count FROM user";
		String[] args = { Integer.toString(days) };
		return runCountQuery(query, args);
	}
	
	public int getNumNewUsersWithMonths(int months) {
		String query = "SELECT COUNT(CASE WHEN date_joined > DATE_SUB(CURDATE(), INTERVAL ? MONTH) THEN 1 END) as user_count FROM user";
		String[] args = { Integer.toString(months) };
		return runCountQuery(query, args);
	}
	
	public int getTotalNumUsers() {
		String query = "SELECT COUNT(*) as user_count FROM user";
		String[] args = { };
		return runCountQuery(query, args);
	}
	
	private int runCountQuery(String query, String[] args) {
		ResultSet rs = conn.executeQuery(query, args);
		try {
			if (rs.next()) return rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
}