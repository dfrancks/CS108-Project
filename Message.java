package finalproject;

public class Message {
	private int messageID, recipientID, senderID, quizID;
	private String text;
	private MessageType type;
	
	public enum MessageType {
		REQUEST, CHALLENGE, NOTE
	}
	
	public Message(int messageID, int recipientID, int senderID, String text, MessageType type, int quizID) {
		this.messageID = messageID;
		this.recipientID = recipientID;
		this.senderID = senderID;
		this.text = text;
		this.type = type;
		this.quizID = quizID;
	}
	
	public int getMessageID() { return messageID; }
	
	public int getRecipientID() { return recipientID; }
	
	public int getSenderID() { return senderID; }
	
	public String getMessageText() { return text; }
	
	public MessageType getType() { return type; }
	
	public String getTypeString() {
		if (type == MessageType.REQUEST) return "Friend request";
		if (type == MessageType.CHALLENGE) return "Challenge";
		return "Note";
	}
	
	public int getQuizID() { return quizID; }
}