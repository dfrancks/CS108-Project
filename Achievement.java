package finalproject;

public class Achievement {
	private int userID, quizID;
	private AchievementType type;
	
	public enum AchievementType {
		ONE_QUIZ, FIVE_QUIZ, TEN_QUIZ, TAKE_TEN_QUIZ, HIGH_SCORE, PERFECT_SCORE
	}
	
	public Achievement(int userID, int quizID, AchievementType type) {
		this.userID = userID;
		this.quizID = quizID;
		this.type = type;
	}
	
	public int getUserID() { return userID; }
	
	public int getQuizID() { return quizID; }
	
	public AchievementType getType() { return type; }
	
	public String getText(){
		switch (type){
			case ONE_QUIZ: return "Amateur Author: You created a quiz.";
			case FIVE_QUIZ: return "Prolific Author: You created 5 quizzes.";
			case TEN_QUIZ: return "Prodigious Author: You created 10 quizzes.";
			case TAKE_TEN_QUIZ: return "Quiz Machine: You took 10 quizzes.";
			case HIGH_SCORE: return "Top Banana: You got the high score on a quiz.";
			case PERFECT_SCORE: return "Perfection: You scored 100% on a quiz.";
			default: return "";
		}
	}
}