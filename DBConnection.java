package finalproject;

import java.sql.*;
import java.util.ArrayList;

public class DBConnection {
	static String account = MyDBInfo.MYSQL_USERNAME;
	static String password = MyDBInfo.MYSQL_PASSWORD;
	static String server = MyDBInfo.MYSQL_DATABASE_SERVER;
	static String database = MyDBInfo.MYSQL_DATABASE_NAME;

	private static DBConnection dbConnect;

	public static DBConnection getCurrentDBConnection(){
		if (dbConnect == null){
			dbConnect = new DBConnection();
		}
		return dbConnect;
	}
	
	private Connection connection;
	private Statement statement;
	
	public DBConnection(){
		connect();
	}
	
	private void connect(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://" + server, account, password);
			statement = connection.createStatement();
			statement.executeQuery("USE " + database);
		} catch (SQLException e){
			e.printStackTrace();
		} catch (ClassNotFoundException e){
			e.printStackTrace();
		}
	}
	
	public ResultSet executeQuery(String command, String[] arguments) {
		try{
			PreparedStatement preparedStatement = connection.prepareStatement(command);
			for (int i = 0; i < arguments.length; i++){
				preparedStatement.setString(i+1, arguments[i]);
			}
			ResultSet set = preparedStatement.executeQuery();
			return set;
		} catch (SQLException e){
			e.printStackTrace();
		}
		return null;
	}	
	
	public void executeUpdate(String command, String[] arguments){
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(command);
			for (int i = 0; i < arguments.length; i++){
				preparedStatement.setString(i+1, arguments[i]);
			}
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void executeUpdates(String command, ArrayList<String[]> argumentsList) {
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(command);
			for (int i = 0; i < argumentsList.size(); i++) {
				for (int j = 0; j < argumentsList.get(0).length; j++) {
					int index = argumentsList.get(0).length * i + j + 1;
					preparedStatement.setString(index, argumentsList.get(i)[j]);
				}
			}
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method is responsible for closing the connection to the SQL server.
	 */
	public void closeConnection() {
		try {
			connection.close();
		} catch (SQLException e){
			e.printStackTrace();
		}
	}

}
