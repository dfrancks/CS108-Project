package finalproject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AnnouncementManager {
	private DBConnection conn;
	
	public static final String ATTR_NAME = "Announcement Manager";
	
	public AnnouncementManager() { conn = DBConnection.getCurrentDBConnection(); }
	
	public ArrayList<Announcement> getAnnouncements() {
		String query = "SELECT * FROM announcement";
		String [] args = {};
		return getAnnouncementList(query, args);
	}
	
	public ArrayList<Announcement> getActiveAnnouncements() {
		String query = "SELECT * FROM announcement WHERE is_active = ?";
		String[] args = { Integer.toString(1) };
		return getAnnouncementList(query, args);
	}
	
	private ArrayList<Announcement> getAnnouncementList(String query, String[] args) {
		ResultSet rs = conn.executeQuery(query, args);
		ArrayList<Announcement> announcements = new ArrayList<Announcement>();
		try {
			while (rs.next()) {
				announcements.add(new Announcement(rs.getInt("announcement_id"), rs.getString("announcement_text"), rs.getBoolean("is_active")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return announcements;
	}
	
	public void insertAnnouncement(String text, boolean isActive){
		String query = "INSERT INTO announcement (announcement_text, is_active) VALUES (?, ?)";
		int active = 1;
		if (!isActive)
			active = 0;
		String[] args = {text, Integer.toString(active)};
		conn.executeUpdate(query, args);
	}
	
	public void updateAnnouncement(int announcementID, boolean isActive){
		String query = "UPDATE announcement SET is_active = ? WHERE announcement_id = ?";
		int active = 1;
		if (!isActive)
			active = 0;
		String[] args = {Integer.toString(active), Integer.toString(announcementID)};
		conn.executeUpdate(query, args);
	}
}