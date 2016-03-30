package finalproject;
import java.util.ArrayList;

public class MultipleChoice extends Question {
	public MultipleChoice(String question, ArrayList<Answer> answers, int placement, int points) {
		super(question, answers, placement, QuestionType.MULTIPLE_CHOICE, points);
	}
	
}