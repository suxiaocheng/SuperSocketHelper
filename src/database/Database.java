package database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.tukaani.xz.LZMA2Options;
import org.tukaani.xz.XZOutputStream;

import config.Config;
import debug.Log;

public class Database {
	public String strDatabaseName;
	private Connection conn = null;
	public String strDate;
	public String strRawDatabaseName;

	public Database(String name) {
		DateFormat dateFormatTable = new SimpleDateFormat("-yyyy-MM-dd");
		Date dateCurrent = new Date();
		strDate = dateFormatTable.format(dateCurrent);

		strRawDatabaseName = name + dateFormatTable + ".db";
		strDatabaseName = "jdbc:sqlite:" + Config.FILE_STORAGE_PATH + name + dateFormatTable + ".db";
		try {
			Class.forName("org.sqlite.JDBC");
			conn = (Connection) DriverManager.getConnection(strDatabaseName);
		} catch (SQLException e) {
			Log.e("Connection error!");
			e.printStackTrace();
			System.exit(-1);
		} catch (ClassNotFoundException e) {
			Log.e("Class no found error!");
			e.printStackTrace();
			System.exit(-2);
		}
		Log.d("Database: " + strDatabaseName + " load sucessfully!");
	}

	public void createTable(String table) {
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

	public boolean insertTable(String statement) {
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

	public void closeDatabase() {
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

	public static String compressDB(String strDBName) {
		LZMA2Options options = new LZMA2Options();
		FileOutputStream outfile;
		String strDBXZName;
		long time_start = System.currentTimeMillis();

		Log.d("Encoder memory usage: " + options.getEncoderMemoryUsage() + " KiB");
		Log.d("Decoder memory usage: " + options.getDecoderMemoryUsage() + " KiB");

		strDBXZName = strDBName + ".xz";
		Log.d("Origin file: " + strDBName + ", compress file: " + strDBXZName);
		try {
			File fDBOut = new File(Config.FILE_STORAGE_PATH + strDBXZName);
			File fDBIn = new File(Config.FILE_STORAGE_PATH + strDBName);
			if (fDBOut.exists() == true) {
				fDBOut.delete();
			}
			outfile = new FileOutputStream(Config.FILE_STORAGE_PATH + strDBXZName);
			XZOutputStream out = new XZOutputStream(outfile, options);
			InputStream in = new FileInputStream(fDBIn);

			byte[] buf = new byte[8192];
			int size;
			while ((size = in.read(buf, 0, buf.length)) != -1)
				out.write(buf, 0, size);
			out.finish();
			out.close();
			in.close();
			
			/* If compress sucessfully, delete the uncompress file */
			fDBIn.delete();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			outfile = null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			outfile = null;
		}

		if (outfile == null) {
			return Config.FILE_STORAGE_PATH + strDBName;
		}
		long time_end = System.currentTimeMillis();
		float sec = ((float) (time_end - time_start) / 1000);

		Log.d("Compress used " + sec + " s");

		return Config.FILE_STORAGE_PATH + strDBXZName;
	}

	public static void main(String[] args) {
		compressDB("sina-stock-codes.txt");
	}
}
