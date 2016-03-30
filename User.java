package finalproject;

public class User {
  int userID;
  String username;
  PrivacySetting privacy;
  
  public enum PrivacySetting {
	  ANYONE, FRIENDS, FRIENDS_OF_FRIENDS
  }

  public User(int userID, String username, PrivacySetting privacy) {
    this.userID = userID;
    this.username = username;
    this.privacy = privacy;
  }
  
  public int getID() { return userID; }
  
  public String getUsername() { return username; }
  
  public PrivacySetting getPrivacy() { return privacy; }
}