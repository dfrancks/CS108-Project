package finalproject;

import java.util.ArrayList;

public class FillBlank extends Question {

	public FillBlank(String question, String secondaryText, ArrayList<Answer> answers, int placement, int points) {
		super(question, answers, placement, QuestionType.FILL_IN_THE_BLANK, 1);
		this.secondaryText = secondaryText;
	}
}