
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import Database.ItemInfo;
import Database.SimplePropertyCollection;

public class Database {
	private String databaseName;
	private Connection conn = null;

	public Database(String name) {
		Date currentTime = new Date(System.currentTimeMillis());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd");
		String dateString = formatter.format(currentTime);

		databaseName = "jdbc:sqlite:" + name + dateString + ".db";
		try {
			Class.forName("org.sqlite.JDBC");
			conn = (Connection) DriverManager.getConnection(databaseName);
		} catch (SQLException e) {
			System.out.println("Connection error!");
			e.printStackTrace();
			System.exit(-1);
		} catch (ClassNotFoundException e) {
			System.out.println("Class no found error!");
			e.printStackTrace();
			System.exit(-2);
		}
		System.out.println("Database: " + databaseName + " load sucessfully!");
	}

	void createTable(String table) {
		String sql = SimplePropertyCollection.getCreateTableStatement(ItemInfo.TEXT_DEFAULTS_ALL, table);
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sql);
		    stmt.close();
		} catch (SQLException e) {
			System.out.println("Statement operation error!");
			e.printStackTrace();			
		}
		System.out.println("Table created successfully");
	}
	
	boolean insertTable(String statement){
		boolean status = true;
		
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(statement);
		    stmt.close();
		} catch (SQLException e) {
			System.out.println("Execute statement: " + statement + " error!");
			e.printStackTrace();			
		}
		
		return status;
	}

	void closeDatabase() {
		if (conn != null) {
			try {
				conn.close();
				conn = null;
			} catch (SQLException e) {
				System.out.print("Close database fail!");
				e.printStackTrace();
			}
			System.out.println("Close database sucessfully!");
		}
	}
}
