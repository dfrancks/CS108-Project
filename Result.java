package finalproject;

import java.sql.Time;
import java.sql.Timestamp;

public class Result {
  private int userID, quizID, score;
  private Timestamp dateTaken;
  private Time timeElapsed;


  public Result(int userID, int quizID, int score, Timestamp dateTaken, Time timeElapsed) {
	  this.userID = userID;
	  this.quizID = quizID;
	  this.score = score;
	  this.dateTaken = dateTaken;
	  this.timeElapsed = timeElapsed;
  }
  
  public int getUserID() { return userID; }
  public int getQuizID() { return quizID; }
  public int getScore() { return score; }
  public Timestamp getDateTaken() { return dateTaken; }
  public Time getTimeElapsed() { return timeElapsed; }
  
}