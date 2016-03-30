package finalproject;

import finalproject.Message.MessageType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class MessageManager {
	private DBConnection conn;
	
	public static final String ATTR_NAME = "Message Manager";
	
	public MessageManager() { conn = DBConnection.getCurrentDBConnection(); }
	
	public void createMessage(int senderID, int recipientID, String text, MessageType type, int quizID) {
		if (type == MessageType.CHALLENGE) {
			String query = "INSERT INTO message (sender_id, recipient_id, message_text, type, quiz_id)"
							+ " VALUES(?, ?, ?, ?, ?)";
			String[] args = { 
				Integer.toString(senderID), Integer.toString(recipientID), text, 
				Integer.toString(type.ordinal()), Integer.toString(quizID)
			};
			conn.executeUpdate(query, args);
		} else {
			String query = "INSERT INTO message (sender_id, recipient_id, message_text, type)"
							+ " VALUES(?, ?, ?, ?)";
			String[] args = { 
				Integer.toString(senderID), Integer.toString(recipientID), text, 
				Integer.toString(type.ordinal())
			};
			conn.executeUpdate(query, args);
		}
	}
	
	public ArrayList<Message> receivedMessagesForUser(int userID) {
		String query = "SELECT * FROM message WHERE recipient_id = ? ORDER BY created_date DESC";
		String[] args = { Integer.toString(userID) };
		return getMessageList(query, args);
	}
	
	public ArrayList<Message> sentMessagesForUser(int userID) {
		String query = "SELECT * FROM message WHERE sender_id = ? ORDER BY created_date DESC";
		String[] args = { Integer.toString(userID) };
		return getMessageList(query, args);
	}
	
	private ArrayList<Message> getMessageList(String query, String[] args) {
		ArrayList<Message> messages = new ArrayList<Message>();
		ResultSet rs = conn.executeQuery(query, args);
		try {
			while (rs.next()) {
				Message msg = new Message(rs.getInt("message_id"), rs.getInt("recipient_id"), rs.getInt("sender_id"),
										  rs.getString("message_text"), MessageType.values()[rs.getInt("type")], rs.getInt("quiz_id"));
				messages.add(msg);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return messages;
	}
	
	public Message messageForMessageID(int messageID) {
		String query = "SELECT * FROM message WHERE message_id = ?";
		String[] args = { Integer.toString(messageID) };
		ResultSet rs = conn.executeQuery(query, args);
		try {
			if (rs.next()) {
				return new Message(messageID, rs.getInt("recipient_id"), rs.getInt("sender_id"), rs.getString("message_text"), 
								   MessageType.values()[rs.getInt("type")], rs.getInt("quiz_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}