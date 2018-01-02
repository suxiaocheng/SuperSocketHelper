


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import database.ItemInfo;
import database.SimplePropertyCollection;
import debug.Log;

public class Database {
	private String databaseName;
	public String subName;
	private Connection conn = null;

	public Database(String name) {
		Date currentTime = new Date(System.currentTimeMillis());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd");
		String dateString = formatter.format(currentTime);

		subName = name + dateString + ".db";
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
		Log.d("Database: " + databaseName + " load sucessfully!");
	}

	void createTable(String table) {
		String sql = SimplePropertyCollection.getCreateTableStatement(ItemInfo.TEXT_DEFAULTS_ALL, table);
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sql);
		    stmt.close();
		} catch (SQLException e) {
			Log.e("Statement operation error!");
			e.printStackTrace();			
		}
		Log.d("Table created successfully");
	}
	
	boolean insertTable(String statement){
		boolean status = true;
		
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(statement);
		    stmt.close();
		} catch (SQLException e) {
			Log.e("Execute statement: " + statement + " error!");
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
			Log.d("Close database sucessfully!");
		}
	}
}
