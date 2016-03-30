package finalproject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;

public class Quiz {
	private int quizID, author;
	private String title, description;
	private boolean singlePage, randomized, correction;
	private Timestamp createdDate;
	private ArrayList<Question> questions;
	private ArrayList<Integer> questionIDs;
	
	// Full constructor - used when user is taking quiz
	public Quiz(int quizID, int author, String title, String description, boolean singlePage, 
				boolean randomized, boolean correction, Timestamp timestamp, ArrayList<Integer> questionIDs) {
		this.quizID = quizID;
		this.author = author;
		this.title = title;
		this.description = description;
		this.singlePage = singlePage;
		this.randomized = randomized;
		this.correction = correction;
		this.createdDate = timestamp;
		this.questionIDs = questionIDs;
		this.questions = new ArrayList<Question>();
	}
	
	// Partial constructor - used when listing quizzes on homepage, etc.
	public Quiz(int quizID, String title, String description) {
		this.quizID = quizID;
		this.title = title;
		this.description = description;
	}
	
	// Empty constructor - used at outset of quiz creation process
	public Quiz() {
		this.questions = new ArrayList<Question>();
	}
	
	public int getSize(){ return questions.size(); }
	
	public void addQuestion(Question q) { questions.add(q); }
	
	public ArrayList<Question> getQuestions() { return questions; }
	
	public ArrayList<Integer> getQuestionIDs() { return questionIDs; }
 	
	public void setQuestions(ArrayList<Question> questions) { this.questions = questions; }
	
	public void randomizeQuestions(){
		Collections.shuffle(questions);
	}

	public int getQuizID() { return quizID; }
	
	public int getAuthor() { return author; }
	
	public void setAuthor(int author) { this.author = author; }
	
	public String getTitle() { return title; }
	
	public void setTitle(String title) { this.title = title; }
	
	public String getDescription() { return description; }
	
	public void setDescription(String description) { this.description = description; }
	
	public boolean getSinglePage() { return singlePage; }
	
	public void setSinglePage(boolean singlePage) { this.singlePage = singlePage; };
	
	public boolean getRandomized() { return randomized; }
	
	public void setRandomized(boolean randomized) { this.randomized = randomized; };
	
	public boolean getImmediateCorrection() { return correction; }
	
	public void setImmediateCorrection(boolean correction) { this.correction = correction; };
	
	public Timestamp getCreatedDate() { return createdDate; }
	
	public void setCreatedDate(Timestamp createdDate) { this.createdDate = createdDate; }
}