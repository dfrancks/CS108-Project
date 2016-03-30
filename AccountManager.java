package finalproject;

import java.security.*;
import java.sql.*;

public class AccountManager {
	private DBConnection connection;
	private ResultSet currentResultSet;
	private static final String SALT = "salt";
	private static final int PASSWORD_INDEX_IN_USER = 3;
	
	public static final String ATTR_NAME = "Account Manager";

	public AccountManager() {
		connection = DBConnection.getCurrentDBConnection();	
	}
	
	public boolean accountExists(String username){
		try {
			String[] arguments = {username};
			ResultSet rs = connection.executeQuery("SELECT * FROM user WHERE username = ? ", arguments);
			currentResultSet = rs;
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private String hashPassword(String password){
		String hashedPassword = password + SALT;
		try {
			byte[] passwordByte = hashedPassword.getBytes();
			MessageDigest md = MessageDigest.getInstance("SHA");
			md.update(passwordByte);
			byte[] hashedPasswordByte = md.digest();
			hashedPassword = hexToString(hashedPasswordByte);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hashedPassword;
	}
	
	public boolean accountActive(String username){
		String[] arguments = {username};
		ResultSet rs = connection.executeQuery("SELECT * FROM user WHERE username = ? ", arguments);
			try {
				if (rs.next()) return rs.getBoolean("is_active");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return false;
	}
	
	public boolean checkAccount(String username, String password){
		if(accountExists(username)){
			String pass = "";
			try {
				pass = currentResultSet.getString(PASSWORD_INDEX_IN_USER);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			String hashedPassword = hashPassword(password);
			return pass.equals(hashedPassword);
		}
		return false;
	}
	
	public void createAccount(String username, String password){
		java.sql.Timestamp now = new java.sql.Timestamp(new java.util.Date().getTime());
		String hashedPassword = hashPassword(password);
		String[] arguments = {username, hashedPassword, Integer.toString(0), now.toString() };
		connection.executeUpdate("INSERT INTO user(username,password,is_admin, date_joined) VALUES(?,?,?,?)", arguments);
	}

	/*
	 Given a byte[] array, produces a hex String,
	 such as "234a6f". with 2 chars for each byte in the array.
	 (provided code)
	*/
	public static String hexToString(byte[] bytes) {
		StringBuffer buff = new StringBuffer();
		for (int i=0; i<bytes.length; i++) {
			int val = bytes[i];
			val = val & 0xff;  // remove higher bits, sign
			if (val<16) buff.append('0'); // leading 0
			buff.append(Integer.toString(val, 16));
		}
		return buff.toString();
	}
}
