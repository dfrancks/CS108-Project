package finalproject;


import finalproject.User.PrivacySetting;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class UserManager {
	private DBConnection conn;
	
	public static final String ATTR_NAME = "User Manager";
	
	public enum FriendRequestStatus {
		PENDING, ACCEPTED, REJECTED, DELETED, NO_REQUEST
	}
	
	public UserManager() { conn = DBConnection.getCurrentDBConnection(); }
	
	public User userForUserID(int userID) {
		String query = "SELECT * FROM user WHERE user_id = ?";
		String[] args = { Integer.toString(userID) };
		ResultSet rs = conn.executeQuery(query, args);
		try {
			if (rs.next()) return new User(rs.getInt("user_id"), rs.getString("username"), 
					 PrivacySetting.values()[rs.getInt("privacy_setting")]);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String usernameForUserID(int userID) {
		String query = "SELECT username FROM user WHERE user_id = ?";
		String[] args = { Integer.toString(userID) };
		ResultSet rs = conn.executeQuery(query, args);
		try {
			if (rs.next()) return rs.getString(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public boolean isAdminForUserID(int userID) {
		String query = "SELECT is_admin FROM user WHERE user_id = ?";
		String[] args = { Integer.toString(userID) };
		ResultSet rs = conn.executeQuery(query, args);
		try {
			if (rs.next()) return rs.getBoolean(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public int userIDForUsername(String username) {
		String query = "SELECT user_id FROM user WHERE username = ?";
		String[] args = { username };
		ResultSet rs = conn.executeQuery(query, args);
		try {
			if (rs.next()) return rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public String getCurrentUsername(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if(cookies != null){
			for (int i = 0; i < cookies.length; i++){
				if(cookies[i].getName().equals("currentUserID")) {
					return usernameForUserID(Integer.parseInt(cookies[i].getValue()));
				}
			}
		}
		return null;
	}
	
	public int getCurrentUserID(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if(cookies != null){
			for (int i = 0; i < cookies.length; i++){
				if(cookies[i].getName().equals("currentUserID")) {
					return Integer.parseInt(cookies[i].getValue());
				}
			}
		}
		return -1;
	}
	
	public ArrayList<User> friendsForUser(int userID) {
		String query = "SELECT * FROM user U "
						+ "LEFT JOIN friendship F ON U.user_id = F.to_user_id WHERE F.from_user_id = ? AND status = 1 "
						+ "UNION SELECT * FROM user U "
						+ "LEFT JOIN friendship F ON U.user_id = F.from_user_id WHERE F.to_user_id = ? AND status = 1";
		String[] args = { Integer.toString(userID), Integer.toString(userID) };
		ResultSet rs = conn.executeQuery(query, args);
		ArrayList<User> friends = new ArrayList<User>();
		try {
			while (rs.next()) friends.add(new User(rs.getInt("user_id"), rs.getString("username"), 
												   PrivacySetting.values()[rs.getInt("privacy_setting")]));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return friends;
	}
	
	public void sendFriendRequest(int userID, int friendID) {
		String query = "INSERT INTO friendship (to_user_id, from_user_id, status) VALUES (?, ?, 0)";
		String[] args = { Integer.toString(friendID), Integer.toString(userID) };
		conn.executeUpdate(query, args);
	}
	
	public void updateFriendStatus(int userID, int friendID, FriendRequestStatus status) {
		String query = "UPDATE friendship SET status = ? WHERE to_user_id = ? AND from_user_id = ?";
		String[] args = { Integer.toString(status.ordinal()), Integer.toString(userID), Integer.toString(friendID) };
		conn.executeUpdate(query, args);
	}
	
	public FriendRequestStatus getFriendStatus(int toUserID, int fromUserID) {
		String query = "SELECT status FROM friendship WHERE to_user_id = ? AND from_user_id = ?";
		String[] args = { Integer.toString(toUserID), Integer.toString(fromUserID) };
		ResultSet rs = conn.executeQuery(query, args);
		try {
			if (rs.next()) return FriendRequestStatus.values()[rs.getInt(1)];
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return FriendRequestStatus.NO_REQUEST;
	}
	
	public ArrayList<User> getAllUsers(int currUserID) {
		String query = "SELECT * FROM user WHERE user_id <> ?";
		String[] args = { Integer.toString(currUserID) };
		return getUserList(query, args);
	}
	
	public ArrayList<User> searchUsers(String searchString, int currUserID) {
		String query = "SELECT DISTINCT * FROM user WHERE UPPER(username) LIKE UPPER('%" + searchString + "%') AND user_id <> ?";
		String[] args = { Integer.toString(currUserID) };
		return getUserList(query, args);
	}
	
	public void makeAdmin(int userID){
		String query = "UPDATE user SET is_admin = 1 WHERE user_id = ?";
		String[] args = {Integer.toString(userID)};
		conn.executeUpdate(query, args);
	}
	
	public void setActive(int userID, boolean isActive) {
		String query = "UPDATE user SET is_active = ? WHERE user_id = ?";
		int active = 1;
		if (!isActive)
			active = 0;
		String[] args = {Integer.toString(active), Integer.toString(userID)};
		conn.executeUpdate(query, args);
	}
	
	public void setPrivacySetting(int userID, int setting) {
		String query = "UPDATE user SET privacy_setting = ? WHERE user_id = ?";
		String[] args = { Integer.toString(setting), Integer.toString(userID) };
		conn.executeUpdate(query, args);
	}
	
	public boolean userIsActive(int userID){
		String query = "SELECT * from user WHERE user_id = ?";
		String[] args = {Integer.toString(userID)};
		ResultSet rs = conn.executeQuery(query, args);
		boolean result = false;
		try {
			if (rs.next()) result = rs.getBoolean("is_active");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
		
	private ArrayList<User> getUserList(String query, String[] args) {
		ResultSet rs = conn.executeQuery(query, args);
		ArrayList<User> users = new ArrayList<User>();
		try {
			while (rs.next()) users.add(new User(rs.getInt("user_id"), rs.getString("username"), 
												 PrivacySetting.values()[rs.getInt("privacy_setting")]));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return users;
	}
}