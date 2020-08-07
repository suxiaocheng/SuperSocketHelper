package app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import config.Config;
import database.Database;
import debug.Log;
import tool.SendEmail;
import tool.SyncTime;

/**
 * 
 * @author SuXiaocheng
 * @version 1.0
 * @since 2018/01/02
 *
 */
public class SinaStock {
	private static final String StockDetailFile = "stock_detail_file.csv";
	private static final String StockSuspendedFile = "stock_suspended_file.csv";
	public static List<String> codes = new ArrayList<String>();
	private static File stockDetailFile = null;
	private static File stockSuspendedFile = null;

	private Database stock_db;

	public static List<String> getStockInfoByCode(String stockCode)
			throws IOException {
		// 仅仅打印
		List<String> stockList = new ArrayList<String>();
		URL url = new URL("http://hq.sinajs.cn/?list=" + stockCode);
		URLConnection connection = url.openConnection();
		connection.setConnectTimeout(Config.CONNECTION_TIMEOUT);
		BufferedReader br = new BufferedReader(new InputStreamReader(
				connection.getInputStream()));
		String line = null;
		StringBuffer sb = new StringBuffer();
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		if (sb.length() > 0) {
			String rs = sb.toString();
			rs = rs.substring(rs.indexOf("\"") + 1, rs.lastIndexOf("\""));
			String[] rss = rs.split(",");
			for (int i = 0; i < rss.length; i++) {
				System.out.print(rss[i] + "\t|");
				stockList.add(rss[i]);
			}
		}
		return stockList;
	}

	public static class GetItemInfoThread implements Runnable {
		public int count;
		public String stockCode;
		BufferedWriter bwHandle;
		BufferedWriter bwSuspended;

		GetItemInfoThread(String tmp, BufferedWriter bw, BufferedWriter bw2) {
			super();
			stockCode = tmp;
			bwHandle = bw;
			bwSuspended = bw2;
		}

		public void run() {
			List<String> list;
			try {
				list = getStockInfoByCode(stockCode);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return;
			}
			try {
				if (list.get(0).contains("FAILED")) {
					Log.e("[Warning] " + stockCode + " get fail");
				} else {
					synchronized (codes) {
						bwHandle.write(stockCode);
						for (String s : list) {
							bwHandle.write(',');
							bwHandle.write(s);
						}
						bwHandle.newLine();
					}

					if (list.get(1).compareTo("0") == 0) {
						synchronized (codes) {
							bwSuspended.write(stockCode);
							for (String s : list) {
								bwSuspended.write(',');
								bwSuspended.write(s);
							}
							bwSuspended.newLine();
						}
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e("[Error] Write stock information");
			}
		}
	}

	public static void getAllStockInfo() throws IOException {
		String[] header = getHeaders();
		Thread t;
		List<Thread> listThread = new ArrayList<Thread>();
		int count;
		boolean needQuit = false;
		stockDetailFile = new File(StockDetailFile);
		stockSuspendedFile = new File(StockSuspendedFile);
		boolean padding = false;
		BufferedWriter bwDetail = new BufferedWriter(new FileWriter(
				stockDetailFile));
		BufferedWriter bwSuspended = new BufferedWriter(new FileWriter(
				StockSuspendedFile));
		for (String h : header) {
			if (padding) {
				bwDetail.write(',');
			} else {
				padding = true;
			}
			bwDetail.write(h);
		}
		bwDetail.newLine();

		count = 0;
		for (String code : codes) {
			t = new Thread(new GetItemInfoThread(code, bwDetail, bwSuspended));
			t.start();
			listThread.add(t);

			count++;
			if ((count % Config.CONNECTION_PARALLEL_EXEC) == 0) {
				Log.d("[Info] Process ["
						+ (count - Config.CONNECTION_PARALLEL_EXEC) + "~"
						+ count + "] cluster");
				for (Thread tmp : listThread) {
					try {
						tmp.join();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Log.e("[Info] Get stock information interrupt");
						needQuit = true;
					}
				}
				Log.d("[Info] Process end");
				listThread.clear();
				if (needQuit) {
					break;
				}
			}
		}

		for (Thread tmp : listThread) {
			try {
				tmp.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e("[Info] Get stock information interrupt");
			}
		}

		bwSuspended.flush();
		bwSuspended.close();
		bwSuspended = null;

		bwDetail.flush();
		bwDetail.close();
		bwDetail = null;
	}

	public static String[] getHeaders() {
		String[] header = { "股票代码", "股票名字", "今日开盘价	", "昨日收盘价", "当前价格", "今日最高价",
				"今日最低价", "竟买价", "竞卖价", "成交的股票数", "成交金额(元)", "买一", "买一", "买二",
				"买二", "买三", "买三", "买四", "买四", "买五", "买五", "卖一", "卖一", "卖二",
				"卖二", "卖三", "卖三", "卖四", "卖四", "卖五", "卖五", "日期", "时间" };
		return header;
	}

	public static List<String> getStockCodes() {
		return codes;
	}

	public static void main(String[] args) {
		Calendar start_calendar = Calendar.getInstance();
		Thread sendMailThread;
		Thread watchThread;
		Integer iNumberThread = 0;
		
		/* sync time first */
		boolean bStatus;
		while (true) {
			bStatus = SyncTime.isConnect();
			if (bStatus == true) {
				break;
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		bStatus = SyncTime.waitNetworkTimeSync();
		if(bStatus == false) {
			return;
		}
		System.out.println("Sync time sucessfully");
		
		long t1 = System.currentTimeMillis();
		File in = new File(Config.FILE_STORAGE_PATH + Config.db);
		if (Config.DEBUG_ALWAYS_CREATE_DB) {
			if (in.exists()) {
				in.delete();
			}
		}
		if (!in.exists()) {
			if (codes.size() < 1)
				try {
					codes = GetAllStockCode.getAllStockCodes();
				} catch (IOException e) {
					e.printStackTrace();
				}
		} else {
			if (codes.size() < 1) {
				try {
					codes = GetAllStockCode.getAllStockCodesFromLocal();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		Log.d("Get code execute " + (System.currentTimeMillis() - t1) + " s");

		watchThread = new Thread(new WatchThread());
		watchThread.start();

		while (true) {
			UpdateSocketThread.bNeedQuit = false;
			start_calendar = Calendar.getInstance();
			Database db = new Database("stock");
			List<Thread> listThread = new ArrayList<>();
			iNumberThread = 0;

			for (String code : codes) {
				Thread t = new Thread(new UpdateSocketThread(db, code));
				listThread.add(t);
			}
			
			for(Thread t: listThread){
				t.start();
				iNumberThread++;
				if(Config.MAX_EXECUTE_THREAD != -1){
					if(iNumberThread >= Config.MAX_EXECUTE_THREAD){
						break;
					}
				}
			}
			Log.d(iNumberThread + " is create and running in background");

			/* Check for the time */
			while (true) {
				Calendar cur_calendar = Calendar.getInstance();
				if ((cur_calendar.get(Calendar.DAY_OF_MONTH) != start_calendar
						.get(Calendar.DAY_OF_MONTH))
						|| (WatchThread.bNeedQuit == true)) {
					UpdateSocketThread.bNeedQuit = true;
					break;
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (UpdateSocketThread.iNumberThread == 0){
					Log.d("All thread has been quit");
					break;
				}
			}

			for (Thread tmp : listThread) {
				try {
					tmp.interrupt();
					tmp.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.e("[Info] Get stock information interrupt");
				}
			}
			
			if(UpdateSocketThread.iNumberThread != 0) {
				Log.e("Thread static data error: " + UpdateSocketThread.iNumberThread);
			}

			db.closeDatabase();

			sendMailThread = new Thread(new SendEmail("Stock",
					db.strDatabaseName + "-" +  db.strDate,
					Database.compressDB(db.strRawDatabaseName)));
			sendMailThread.start();
			try {
				sendMailThread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e("[Info] Sending mail get an interrupt");
			}

			if (WatchThread.bNeedQuit == true) {
				Log.d("[Info] User quit finally");
				break;
			}
		}
	}
}
