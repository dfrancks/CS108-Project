package finalproject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import finalproject.Achievement.AchievementType;

public class AchievementManager {
	private DBConnection conn;
	
	public static final String ATTR_NAME = "Achievement Manager";
	
	public AchievementManager() { conn = DBConnection.getCurrentDBConnection(); }
	
	public ArrayList<Achievement> achievementsForUser(int userID) {
		String query = "SELECT * FROM achievement WHERE user_id = ? ORDER BY achievement_id DESC";
		String[] args = { Integer.toString(userID) };
		ResultSet rs = conn.executeQuery(query, args);
		ArrayList<Achievement> achievements = new ArrayList<Achievement>();
		try {
			while (rs.next()) {
				achievements.add(new Achievement(rs.getInt("user_id"),  
								 rs.getInt("quiz_id"),
								 Achievement.AchievementType.values()[rs.getInt("type")]));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return achievements;
	}
	
	public Achievement createAchievement(int userID, Integer quizID, AchievementType type) {
		String query = "INSERT INTO achievement (user_id, quiz_id, type) VALUES (?, ?, ?)";
		String qID = null;
		if (quizID != null)
			qID = Integer.toString(quizID);
		String[] args = { Integer.toString(userID), qID, Integer.toString(type.ordinal()) };
		conn.executeUpdate(query, args);
		if (quizID == null)
			quizID = -1;
		return new Achievement(userID, quizID, type);
	} 
	
	public Achievement checkQuizCreatingAchievement(int userID) {
		String query = "SELECT COUNT(CASE WHEN user_id = ? THEN 1 END) as quiz_count FROM quiz WHERE deleted = 0";
		String[] args = { Integer.toString(userID) };
		ResultSet rs = conn.executeQuery(query, args);
		try {
			if (rs.next()) {
				int quizzesCreated = rs.getInt(1);
				if (quizzesCreated == 1) return createAchievement(userID, null, AchievementType.ONE_QUIZ);
				if (quizzesCreated == 5) return createAchievement(userID, null, AchievementType.FIVE_QUIZ);
				if (quizzesCreated == 10) return createAchievement(userID, null, AchievementType.TEN_QUIZ);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Achievement checkQuizTakingAchievement(int userID) {
		String query = "SELECT COUNT(CASE WHEN user_id = ? THEN 1 END) as result_count FROM result WHERE deleted = 0";
		String[] args = { Integer.toString(userID) };
		ResultSet rs = conn.executeQuery(query, args);
		try {
			if (rs.next()) {
				int quizzesTaken = rs.getInt(1);
				if (quizzesTaken == 10) return createAchievement(userID, null, AchievementType.TAKE_TEN_QUIZ);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Achievement checkHighScoreAchievement(int userID, int quizID, int score) {
		ArrayList<Achievement> previous = achievementsForUser(userID);
		for (Achievement a: previous){
			if (a.getType() == Achievement.AchievementType.HIGH_SCORE)
				return null;
		}
		String query = "SELECT MAX(score) FROM result WHERE quiz_id = ? AND deleted = 0";
		String[] args = { Integer.toString(quizID) };
		ResultSet rs = conn.executeQuery(query, args);
		try {
			if (rs.next()) {
				int highScore = rs.getInt(1);
				if (score >= highScore) return createAchievement(userID, quizID, AchievementType.HIGH_SCORE);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}