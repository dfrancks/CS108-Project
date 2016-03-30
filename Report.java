package finalproject;

public class Report {
  private int userID, quizID;
  private ReportType type;

  public enum ReportType {
    INAPPROPRIATE, TARGETS_PERSON, SPAM
  }

  public Report(int userID, int quizID, ReportType type) {
    this.userID = userID;
    this.quizID = quizID;
    this.type = type;
  }

  public int getUserID() { return userID; }
  public int getQuizID() { return quizID; }
  public ReportType getReportType() { return type; }
}