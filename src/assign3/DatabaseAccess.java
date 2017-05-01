package assign3;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseAccess {
	private static String account = MyDBInfo.MYSQL_USERNAME;
	private static String password = MyDBInfo.MYSQL_PASSWORD;
	private static String server = MyDBInfo.MYSQL_DATABASE_SERVER;
	private static String database = MyDBInfo.MYSQL_DATABASE_NAME;

	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;

	/**
	 * Constructor. Makes a connection to the MySQL database.
	 */
	public DatabaseAccess() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://" + server, account, password);
			statement = connection.createStatement();
			statement.executeQuery("USE " + database);
			resultSet = statement.executeQuery("SELECT * FROM metropolises");
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Closes connection to the database.
	 */
	public void closeConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Adds entry to the database.
	 * @param entry value
	 */
	public void addEntry(Entry entry) {
		String query = String.format("INSERT INTO metropolises VALUES(\"%s\", \"%s\", %d);", entry.getMetropolis(), entry.getContinent(), entry.getPopulation()); // Nice
		try {
			statement.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Searches the database for entries base on search modifiers.
	 * @param entry value
	 * @param larger search modifier
	 * @param exact search modifier
	 * @return list of entries in the database
	 */
	public List<Entry> searchEntry(Entry entry, boolean larger, boolean exact) {
		StringBuilder query = new StringBuilder();
		query.append("SELECT * FROM metropolises WHERE ");

		// Checks population search modifier and constructs the query accordingly.
		if (larger) {
			query.append("population" + " > " + entry.getPopulation());
		} else {
			query.append("population" + " <= " + entry.getPopulation());
		}
		query.append(" AND ");

		// Checks metropolis/continent modifier and constructs the query accordingly.
		if (exact) {
			query.append("metropolis" + " = " + "\"" + entry.getMetropolis() + "\"");
			query.append(" AND ");
			query.append("continent" + " = " + "\"" + entry.getContinent() + "\"" + ";");
		} else {
			query.append("metropolis" + " LIKE " + "\"%" + entry.getMetropolis() + "%\"");
			query.append(" AND ");
			query.append("continent" + " LIKE " + "\"%" + entry.getContinent() + "%\"" + ";");
		}

		// Executes the query and saves it in a result set.
		try {
			resultSet = statement.executeQuery(query.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// Actually generates the list.
		return getEntryList();
	}

	/**
	 * Gets and returns list of column names from the database.
	 * @return list of column names
	 */
	public List<String> getColumnNames() {
		List<String> columnNames = new ArrayList<>();

		try {
			resultSet.beforeFirst();
			ResultSetMetaData metaData = resultSet.getMetaData();
			int columnCount = metaData.getColumnCount();

			// Column indices start from 1.
			for (int i = 1; i <= columnCount; i++) {
				columnNames.add(metaData.getColumnName(i));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return columnNames;
	}

	/**
	 * Returns whole database as a list of entries.
	 * @return list of database entries
	 */
	public List<Entry> getEntryList() {
		List<Entry> entries = new ArrayList<>();

		try {
			resultSet.beforeFirst();
			while (resultSet.next()) {
				String metropolis = resultSet.getString("metropolis");
				String continent = resultSet.getString("continent");
				int population = resultSet.getInt("population");
				entries.add(new Entry(metropolis, continent, population));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return entries;
	}
}
