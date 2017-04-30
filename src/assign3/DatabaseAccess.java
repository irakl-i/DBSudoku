package assign3;

import java.sql.*;

public class DatabaseAccess {
	private static String account = MyDBInfo.MYSQL_USERNAME;
	private static String password = MyDBInfo.MYSQL_PASSWORD;
	private static String server = MyDBInfo.MYSQL_DATABASE_SERVER;
	private static String database = MyDBInfo.MYSQL_DATABASE_NAME;

	private static Connection con;

	public static Connection getConnection() {
		return con;
	}

	public static void close() {
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://" + server, account, password);
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
