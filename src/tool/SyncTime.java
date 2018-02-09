package tool;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

import debug.Log;

public class SyncTime {
	final static String webUrl2 = "http://www.baidu.com";
	final static String webUrl3 = "http://www.taobao.com";
	final static String webUrl4 = "http://www.ntsc.ac.cn";
	final static String webUrl5 = "http://www.360.cn";
	final static String webUrl6 = "http://www.beijing-time.org";
	final static String webUrl7 = "http://www.163.com/";
	final static String webUrl8 = "https://www.tmall.com/";

	final static String[] webUrl = { webUrl2, webUrl3, webUrl4, webUrl5,
			webUrl6, webUrl7, webUrl8 };

	public static void main(String[] args) {
		for (String url : webUrl) {
			System.out.println(getNetworkTime(url) + "->" + url);
		}
	}

	public static String getNetworkTime(String webUrl) {
		int iRetry = 0;
		int iTimeout = 1000;
		try {
			while (iRetry++ < 5) {
				URL url = new URL(webUrl);
				URLConnection conn = url.openConnection();
				
				iTimeout = (iRetry+1)*1000;
				conn.setConnectTimeout(iTimeout);
				try {
					conn.connect();
				} catch (SocketTimeoutException e) {

				}
				long dateL = conn.getDate();
				if (dateL != 0) {
					Date date = new Date(dateL);
					SimpleDateFormat dateFormat = new SimpleDateFormat(
							"YYYY-MM-dd HH:mm:ss");
					return dateFormat.format(date);
				}
				Log.d("Retry " + webUrl + ", times: " + iRetry);
			}
			return "unknown";
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "";
	}
}
