package finalproject;

public class Announcement {
	
	private int announcementID;
	private String text;
	private boolean isActive;
	
	public Announcement(int ID, String textinput, boolean active){
		announcementID = ID;
		text = textinput;
		isActive = active;
	}
	
	public int getID(){ return announcementID; }
	
	public String getText() { return text; }
	
	public boolean isActive(){ return isActive; }
}