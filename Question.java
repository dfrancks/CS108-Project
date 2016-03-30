package finalproject;

import java.util.ArrayList;

public class Question {
	protected String text;
	protected String secondaryText;
	protected ArrayList<Answer> answers;
	protected QuestionType type;
	protected int placement, points;
	
	public enum QuestionType {
		QUESTION_RESPONSE, FILL_IN_THE_BLANK, MULTIPLE_CHOICE, PICTURE_RESPONSE	
	}
	
	public Question(String text, ArrayList<Answer> answers, int placement, QuestionType type, int points) {
		this.text = text;
		this.secondaryText = "";
		this.answers = answers;
		this.placement = placement;
		this.type = type;
		this.points = points;
	}
	
	public String getQuestionText() {
		return text;
	}
	
	public String getSecondaryQuestionText(){
		return secondaryText;
	}
	
	public QuestionType getType() {
		return type;
	}
	
	public ArrayList<Answer> getAnswers() {
		return answers;
	}
	
	public void addAnswer(Answer a) {
		answers.add(a);
	}
	
	public void setPlacement(int placement){
		this.placement = placement;
	}
	
	public int getPlacement() {
		return placement;
	}
	
	public int getPoints() {
		return points;
	}
	
	public void setSecondaryText(String text) { secondaryText = text; } 
}