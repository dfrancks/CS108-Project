package finalproject;

import java.util.ArrayList;

public class PictResponse extends Question {

	public PictResponse(String question, ArrayList<Answer> answers, int placement, int points) {
		super(question, answers, placement, QuestionType.PICTURE_RESPONSE, points);
	}
	
}