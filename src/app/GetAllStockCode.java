package app;

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

import app.GetAllStockCode.GetURLInfoThread;
import config.Config;
import debug.Log;

public class GetAllStockCode {
	private static List<String> codes = new ArrayList<String>();

	public GetAllStockCode() {
	}

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
	 * 
	 * 
	 * @param url
	 *            URL need to handle;
	 * @return all the stock code in the current page;
	 * @throws IOException
	 */
	private static String getBatchStackCodes(URL url) throws IOException {
		URLConnection connection = url.openConnection();
		connection.setConnectTimeout(Config.CONNECTION_TIMEOUT);
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
				Log.d("[Info]Execute " + count + ", URL: " + assessURL.toString());
				code = getBatchStackCodes(assessURL);
				listTmp = handleStockCode(code);
				if (listTmp == null) {
					Log.d("url: " + assessURL + " is empty");
				} else {
					synchronized (codes) {
						codes.addAll(listTmp);
					}
					Log.d("URL: " + assessURL.toString());
					Log.d(listTmp.toString());
				}
				Log.d("[Info]Execute " + count + " over.");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e("[Error]Execute " + count);
			}
		}
	}

	public static List<String> getAllStockCodes() throws IOException {
		List<Thread> listThread = new ArrayList<Thread>();
		Thread updateDataThread;
		int i;
		URL url = null;
		codes.clear();
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
				// Log.d("Thread" + iThread.count + " is finish");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		if (!(new File(Config.FILE_STORAGE_PATH + Config.db)).exists())
			saveStockCodes(codes);
		return codes;
	}

	public static void saveStockCodes(List<String> codes) throws IOException {
		File out = new File(Config.FILE_STORAGE_PATH + Config.db);
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

	public static List<String> getAllStockCodesFromLocal() throws IOException {
		List<String> file_store_codes = new ArrayList<String>();
		File in = new File(Config.FILE_STORAGE_PATH + Config.db);
		if (!in.exists()) {
			Log.e("The data file [" + Config.FILE_STORAGE_PATH + Config.db + " ] is not exist");
			throw new IOException("The data file [" + Config.db + " ] is not exist");
		}
		BufferedReader br = new BufferedReader(new FileReader(in));
		String line = null;
		while ((line = br.readLine()) != null) {
			if (checkStockStringValid(line) == false) {
				Log.e("[Warning]Invalid stock string: " + line + "\n");
			} else {
				file_store_codes.add(line);
			}
		}
		if (br != null) {
			br.close();
			br = null;
		}
		return file_store_codes;
	}
}
