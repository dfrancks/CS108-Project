package finalproject;

import java.util.ArrayList;

public class QResponse extends Question {

	public QResponse(String question, ArrayList<Answer> answers, int placement, int points) {
		super(question, answers, placement, QuestionType.QUESTION_RESPONSE, points);
	}
}