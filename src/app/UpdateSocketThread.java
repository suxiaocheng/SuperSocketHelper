package app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import config.Config;
import database.Database;
import database.ItemInfo;
import debug.Log;

public class UpdateSocketThread implements Runnable {
	private static final String TAG = "UpdateSocketThread";
	private static final int CONNECTION_TIMEOUT = 30000;
	private static final boolean DEBUG = false;
	public static boolean bNeedQuit = false;
	Database db;
	String stock;
	int count = 0;
	int updateCount = 0;
	private static final String strValidTime[] = { "9:00:00", "11:30:00",
			"13:00:00", "15:00:00" };
	public static Integer iNumberThread = 0;

	UpdateSocketThread(Database _db, String _stock) {
		super();
		db = _db;
		stock = _stock;
	}

	public static int diffTime(String time1, String time2) {
		DateFormat df = new SimpleDateFormat("HH:mm:ss");
		long diff;

		try {
			diff = df.parse(time1).getTime() - df.parse(time2).getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			diff = 0;
		}

		return (int) (diff / 1000);
	}

	public static int checkTimeIntervalValid(String strTime) {
		int ret = 0;
		int tmp;

		ret = diffTime(strValidTime[0], strTime);
		if (ret < 0) {
			ret = diffTime(strValidTime[1], strTime);
			if (ret > 0) {
				return 0;
			} else {
				ret = diffTime(strValidTime[2], strTime);
				if (ret < 0) {
					ret = diffTime(strValidTime[3], strTime);
					if (ret > 0) {
						return 0;
					} else {
						ret = diffTime(strValidTime[0], strTime);
						ret += 24 * 60 * 60;
					}
				}
			}
		}

		return ret;
	}

	public static List<String> getStockInfoByCode(String stockCode)
			throws IOException {
		List<String> stockList = new ArrayList<String>();
		URL url = new URL("http://hq.sinajs.cn/?list=" + stockCode);
		URLConnection connection = url.openConnection();
		connection.setConnectTimeout(CONNECTION_TIMEOUT);
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
				if (DEBUG) {
					System.out.print(rss[i] + "\t|");
				}
				stockList.add(rss[i]);
			}
		}
		return stockList;
	}

	public void run() {
		List<String> listTmp;
		String sql;
		ItemInfo item;
		ItemInfo oldItem = null;
		boolean bNeedAddToDb = false;
		String code;
		String name;
		double price_start;
		double price_last_end;
		double price_current;
		double price_today_high;
		double price_today_low;
		double price_compete_buy;
		double price_compete_seller;
		double price_num_deal;
		double price_deal;
		double[] price_buy = new double[5];
		int[] num_buy = new int[5];
		double[] price_seller = new double[5];
		int[] num_seller = new int[5];
		String date, time;
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		DateFormat dateFormatTable = new SimpleDateFormat("yyyy-MM-dd");
		Date dateCurrent = new Date();
		int iFailTimes = 0;

		synchronized (iNumberThread) {
			iNumberThread++;
		}

		/* Check if connection data valid */
		try {
			listTmp = getStockInfoByCode(stock);
			listTmp.get(31);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			Log.e("IOException: Connection " + stock + " is invalid");
			synchronized (iNumberThread) {
				if (iNumberThread > 0) {
					iNumberThread--;
				} else {
					Log.e("Thread number error");
				}
			}
			return;
		} catch (IndexOutOfBoundsException e1) {
			Log.e("IndexOutOfBoundsException: Connection " + stock
					+ " is invalid");
			synchronized (SinaStock.codes) {
				boolean status = SinaStock.codes.remove(stock);
				if (status == true) {
					Log.d("Remove " + stock + " ok");
				} else {
					Log.d("Remove " + stock + " fail");
				}
				try {
					GetAllStockCode.saveStockCodes(SinaStock.codes);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			synchronized (iNumberThread) {
				if (iNumberThread > 0) {
					iNumberThread--;
				} else {
					Log.e("Thread number error");
				}
			}
			return;
		}

		while (true) {
			dateCurrent = new Date();
			String strDate = dateFormat.format(dateCurrent);
			int iNeedWaitTime = checkTimeIntervalValid(strDate);
			int iNeedSleepTime;
			if (iNeedWaitTime >= 60) {
				// Log.d("Thread " + stock + " is going to sleep");
				iNeedSleepTime = 60000;
			} else {
				break;
			}
			try {
				Thread.sleep(iNeedSleepTime);
			} catch (InterruptedException e) {
				// e.printStackTrace();
			}
			if (bNeedQuit == true) {
				Log.d("User quit at wait state");
				synchronized (iNumberThread) {
					if (iNumberThread > 0) {
						iNumberThread--;
					} else {
						Log.e("Thread number error");
					}
				}
				return;
			}
			if (iNeedWaitTime != 0) {
				continue;
			}
		}

		db.createTable(stock + ":" + dateFormatTable.format(dateCurrent));
		while (true) {
			dateCurrent = new Date();
			String strDate = dateFormat.format(dateCurrent);
			int iNeedWaitTime = checkTimeIntervalValid(strDate);
			int iNeedSleepTime;
			if (iNeedWaitTime >= 60) {
				iNeedSleepTime = 60000;
			} else {
				iNeedSleepTime = 1000;
			}
			try {
				Thread.sleep(iNeedSleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (bNeedQuit == true) {
				Log.d("User quit");
				break;
			}
			if (iNeedWaitTime != 0) {
				continue;
			}

			if ((count % 100) == 0) {
				Log.d("->" + stock + " is: " + count);
			}

			count++;
			try {
				listTmp = getStockInfoByCode(stock);

				code = stock;
				name = listTmp.get(0);
				price_start = Double.valueOf(listTmp.get(1));
				price_last_end = Double.valueOf(listTmp.get(2));
				price_current = Double.valueOf(listTmp.get(3));
				price_today_high = Double.valueOf(listTmp.get(4));
				price_today_low = Double.valueOf(listTmp.get(5));
				price_compete_buy = Double.valueOf(listTmp.get(6));
				price_compete_seller = Double.valueOf(listTmp.get(7));
				price_num_deal = Integer.valueOf(listTmp.get(8));
				price_deal = Double.valueOf(listTmp.get(9));

				for (int i = 0; i < 5; i++) {
					num_buy[i] = Integer.valueOf(listTmp.get(10 + i * 2));
					price_buy[i] = Double.valueOf(listTmp.get(11 + i * 2));
				}

				for (int i = 0; i < 5; i++) {
					num_seller[i] = Integer.valueOf(listTmp.get(20 + i * 2));
					price_seller[i] = Double.valueOf(listTmp.get(21 + i * 2));
				}
				date = listTmp.get(30);
				time = listTmp.get(31);

				dateCurrent = new Date();
				String time_actual = dateFormat.format(dateCurrent);

				item = new ItemInfo(code, name, price_start, price_last_end,
						price_current, price_today_high, price_today_low,
						price_compete_buy, price_compete_seller,
						price_num_deal, price_deal, price_buy, num_buy,
						price_seller, num_seller, date, time, time_actual);

				bNeedAddToDb = true;
				if ((oldItem != null) && item.compareItem(oldItem)) {
					bNeedAddToDb = false;
				}
				oldItem = item;
				if (bNeedAddToDb) {
					sql = "INSERT INTO " + stock + " " + item.getTitle()
							+ "VALUES " + item.getValue() + ";";
					db.insertTable(sql);
					updateCount++;
				}
				iFailTimes = 0;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e("[Error]Execute " + stock);
			} catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
				Log.e("Out of bound");
				if (++iFailTimes > Config.MAX_FAIL_TIMES_ALLOW) {
					break;
				}
			}
		}
		Log.d("Execute " + stock + " count: " + count + ", Times: "
				+ updateCount);
		synchronized (iNumberThread) {
			if (iNumberThread > 0) {
				iNumberThread--;
			} else {
				Log.e("Thread number error");
			}
		}
	}
}
