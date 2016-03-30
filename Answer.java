package finalproject;

public class Answer {
	private String answerText;
	private boolean correct;
	
	public Answer(String answerText, boolean correct) {
		this.answerText = answerText;
		this.correct = correct;
	}
	
	public String getAnswerText() { return answerText; }
	
	public boolean isCorrect() { return correct; }
	
}