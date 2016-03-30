package finalproject;
import java.util.ArrayList;

import finalproject.Question.QuestionType;

public class QuestionResult {
	private int questionIndex;
	private String questionText, secQuestionText, userAnswer;
	private ArrayList<Answer> correctAnswers;
	private int score;
	private QuestionType type;

	public QuestionResult(int questionIndex, String questionText, String secQuestionText, String userAnswer, ArrayList<Answer> correctAnswers, int score, QuestionType type) {
		this.questionIndex = questionIndex;
		this.questionText = questionText;
		this.secQuestionText = secQuestionText;
		this.userAnswer = userAnswer;
		this.correctAnswers = correctAnswers;
		this.score = score;
		this.type = type;
	}
	
	public int getQuestionIndex(){
		return this.questionIndex;
	}
	
	public String getQuestionText() {
		return this.questionText;
	}
	
	public String getSecQuestionText() {
		return this.secQuestionText;
	}
	
	public String getUserAnswer(){
		return this.userAnswer;
	}
	
	public int getScore(){
		return this.score;
	}
	
	public QuestionType getType(){
		return this.type;
	}
	
	public ArrayList<Answer> getCorrectAnswers(){
		return this.correctAnswers;
	}
}
