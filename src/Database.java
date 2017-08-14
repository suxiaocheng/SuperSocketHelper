

import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class Database {
	private String databaseName;
	private Connection conn = null;
	
	public Database(String name) {
		try {			
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Install MySQL driver ok!");
		} catch (ClassNotFoundException e) {
			System.out.println("Install MySQL driver ok fail!");
			e.printStackTrace();
		}

		databaseName = "jdbc:mysql:"+name;
		try {
			conn = (Connection) DriverManager.getConnection(databaseName);
		} catch (SQLException e) {
			System.out.print("Link to database fail!");
			e.printStackTrace();
		}
	}
	
	
	
	void closeDatabase(){
		if(conn != null){
			try {
				conn.close();
				conn = null;
			} catch (SQLException e) {
				System.out.print("Close database fail!");
				e.printStackTrace();
			}
		}
	}
}
