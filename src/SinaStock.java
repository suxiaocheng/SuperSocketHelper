
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * @author Don.W.Lee
 * @version 1.0
 * @since 2012-03-29
 *
 */
public class SinaStock {
	private static String db = ".\\sina-stock-codes.txt";
	private static final boolean DEBUG_ALWAYS_CREATE_DB = false;
	private static final boolean DEBUG_DATA_SAVE2CSV = true;
	private static final int CONNECTION_TIMEOUT = 30000;
	private static final int CONNECTION_PARALLEL_EXEC = 64;

	private static final String StockDetailFile = "stock_detail_file.csv";
	private static final String StockSuspendedFile = "stock_suspended_file.csv";
	private static List<String> codes = new ArrayList<String>();
	private static File stockDetailFile = null;
	private static File stockSuspendedFile = null;
	
	private Database stock_db;

	/**
	 * Check if the stock string is valid;
	 * 
	 * @param s
	 *            string used to check;
	 * @return true if string contain any alphabet or digit, otherwise false.
	 */
	static private boolean checkStockStringValid(String s) {
		boolean bRet = false;

		for (int i = 0; i < s.length(); i++) {
			if (Character.isDigit(s.charAt(i)) || Character.isAlphabetic(s.charAt(i))) {
				bRet = true;
				break;
			}
		}

		return bRet;
	}

	// 解析一组股票代码字符串 把code中包括的所有股票代码放入List中
	private static List<String> handleStockCode(String code) {
		List<String> outputList = null;
		int end = code.indexOf(";");
		int start;
		if (end != -1) {
			code = code.substring(0, end);
			start = code.lastIndexOf("=");
			if (start != -1) {
				code = code.substring(start);
				start = code.indexOf("\"");
				end = code.lastIndexOf("\"");
				if ((start != -1) && (end != -1)) {
					code = code.substring(start + 1, end);
					outputList = Arrays.asList(code.split(","));
				}
			}
		}
		return outputList;
	}

	/**
	 * 返回的值是一个js代码段 包括指定url页面包含的所有股票代码
	 * 
	 * @param url
	 *            URL need to handle;
	 * @return all the stock code in the current page;
	 * @throws IOException
	 */
	private static String getBatchStackCodes(URL url) throws IOException {
		URLConnection connection = url.openConnection();
		connection.setConnectTimeout(CONNECTION_TIMEOUT);
		BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String line = null;
		StringBuffer sb = new StringBuffer();
		boolean flag = false;
		while ((line = br.readLine()) != null) {
			if (line.contains("<script type=\"text/javascript\">") || flag) {
				sb.append(line);
				flag = true;
			}
			if (line.contains("</script>")) {
				flag = false;
				if (sb.length() > 0) {
					if (sb.toString().contains("code_list") && sb.toString().contains("element_list")) {
						break;
					} else {
						sb.setLength(0);
					}
				}
			}
		}
		if (br != null) {
			br.close();
			br = null;
		}
		return sb.toString();
	}

	public static class GetURLInfoThread implements Runnable {
		public URL assessURL;
		public int count;

		GetURLInfoThread(URL url, int i) {
			super();
			assessURL = url;
			count = i;
		}

		public void run() {
			String code = new String();
			List<String> listTmp;
			try {
				System.out.println("[Info]Execute " + count + ", URL: " + assessURL.toString());
				code = getBatchStackCodes(assessURL);
				listTmp = handleStockCode(code);
				synchronized (codes) {
					codes.addAll(listTmp);
				}
				System.out.println("[Info]Execute " + count + " over.");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("[Error]Execute " + count);
			}
		}
	}

	private static List<String> getAllStackCodes() throws IOException {
		List<Thread> listThread = new ArrayList<Thread>();
		Thread updateDataThread;
		int i;
		URL url = null;
		for (i = 1; i < 45; i++) {
			url = new URL("http://vip.stock.finance.sina.com.cn/q/go.php/vIR_CustomSearch/index.phtml?p=" + i);
			updateDataThread = new Thread(new GetURLInfoThread(url, i));
			// updateDataThread.run();
			updateDataThread.start();

			listThread.add(updateDataThread);
		}

		for (Thread iThread : listThread) {
			try {
				// 等待所有线程执行完毕
				iThread.join();
				// System.out.println("Thread" + iThread.count + " is finish");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		if (!(new File(db)).exists())
			saveStockCodes(codes);
		return codes;
	}

	private static void saveStockCodes(List<String> codes) throws IOException {
		// 将所有股票代码存入文件中
		File out = new File(db);
		if (!out.exists())
			out.createNewFile();
		BufferedWriter bw = new BufferedWriter(new FileWriter(out));
		for (String code : codes) {
			bw.write(code);
			bw.newLine();
		}
		if (bw != null) {
			bw.close();
			bw = null;
		}
	}

	private static List<String> getAllStockCodesFromLocal() throws IOException {
		List<String> codes = new ArrayList<String>();
		File in = new File(db);
		if (!in.exists())
			throw new IOException("指定数据文件不存在!");
		BufferedReader br = new BufferedReader(new FileReader(in));
		String line = null;
		while ((line = br.readLine()) != null) {
			if (checkStockStringValid(line) == false) {
				System.out.println("[Warning]Invalid stock string: " + line + "\n");
			} else {
				codes.add(line);
			}
		}
		if (br != null) {
			br.close();
			br = null;
		}
		return codes;
	}

	public static List<String> getStockInfoByCode(String stockCode) throws IOException {
		// 仅仅打印
		List<String> stockList = new ArrayList<String>();
		URL url = new URL("http://hq.sinajs.cn/?list=" + stockCode);
		URLConnection connection = url.openConnection();
		connection.setConnectTimeout(CONNECTION_TIMEOUT);
		BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
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
				if (!DEBUG_DATA_SAVE2CSV) {
					System.out.print(rss[i] + "\t|");
				}
				stockList.add(rss[i]);
			}
			// if (!DEBUG_DATA_SAVE2CSV) {
			// System.out.println("\n------------------------------------");
			// }
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
					System.out.println("[Warning] " + stockCode + " get fail");
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
				System.out.println("[Error] Write stock information");
			}
		}
	}

	public static void getAllStockInfo() throws IOException {
		String[] header = getHeaders();
		Thread t;
		List<Thread> listThread = new ArrayList<Thread>();
		int count;
		boolean needQuit = false;
		if (DEBUG_DATA_SAVE2CSV) {
			stockDetailFile = new File(StockDetailFile);
			stockSuspendedFile = new File(StockSuspendedFile);
			boolean padding = false;
			BufferedWriter bwDetail = new BufferedWriter(new FileWriter(stockDetailFile));
			BufferedWriter bwSuspended = new BufferedWriter(new FileWriter(StockSuspendedFile));
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
				if ((count % CONNECTION_PARALLEL_EXEC) == 0) {
					System.out.println(
							"[Info] Process [" + (count - CONNECTION_PARALLEL_EXEC) + "~" + count + "] cluster");
					for (Thread tmp : listThread) {
						try {
							tmp.join();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							System.out.println("[Info] Get stock information interrupt");
							needQuit = true;
						}
					}
					System.out.println("[Info] Process end");
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
					System.out.println("[Info] Get stock information interrupt");
				}
			}

			bwSuspended.flush();
			bwSuspended.close();
			bwSuspended = null;

			bwDetail.flush();
			bwDetail.close();
			bwDetail = null;
		} else {
			System.out.println(header.length);
			for (int i = 0; i < header.length; i++) {
				System.out.print(header[i] + "\t|");
			}
			for (String code : codes) {
				getStockInfoByCode(code);
			}
		}
	}

	public static String[] getHeaders() {
		String[] header = { "股票代码", "股票名字", "今日开盘价	", "昨日收盘价", "当前价格", "今日最高价", "今日最低价", "竟买价", "竞卖价", "成交的股票数",
				"成交金额(元)", "买一", "买一", "买二", "买二", "买三", "买三", "买四", "买四", "买五", "买五", "卖一", "卖一", "卖二", "卖二", "卖三",
				"卖三", "卖四", "卖四", "卖五", "卖五", "日期", "时间" };
		return header;
	}

	public static List<String> getStockCodes() {
		return codes;
	}

	public static void main(String[] args) {
		long t1 = System.currentTimeMillis();
		File in = new File(db);
		if (DEBUG_ALWAYS_CREATE_DB) {
			if (in.exists()) {
				in.delete();
			}
		}
		if (!in.exists()) {
			// 从网络获取
			if (codes.size() < 1)
				try {
					codes = getAllStackCodes();
				} catch (IOException e) {
					e.printStackTrace();
				}
		} else {
			// 从本地获取
			if (codes.size() < 1) {
				try {
					codes = getAllStockCodesFromLocal();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		codes.clear();
		codes.add("sz300041");
		codes.add("sz002434");
		codes.add("sz300221");
		
		Database db = new Database("stock");
		List<Thread> listThread = new ArrayList<>();
		
		for (String code : codes) {
			Thread t = new Thread(new UpdateSocketThread(db, code));
			t.start();
			listThread.add(t);			
		}

		for (Thread tmp : listThread) {
			try {
				tmp.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("[Info] Get stock information interrupt");
			}
		}
		
		db.closeDatabase();
		
		/*
		t1 = System.currentTimeMillis() - t1;
		System.out.println("[Info] Update database execute (" + t1 / 1000 + "." + t1 % 1000 + "s) sucessfully");

		try {
			t1 = System.currentTimeMillis();
			getAllStockInfo();
			t1 = System.currentTimeMillis() - t1;
			System.out.println("[Info] Update data execute (" + t1 / 1000 + "." + t1 % 1000 + "s) sucessfully");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("[Info] Program execute fail");
		}
		*/
	}
}
