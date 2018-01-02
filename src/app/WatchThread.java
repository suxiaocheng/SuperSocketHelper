package app;
import java.util.Scanner;

import debug.Log;

public class WatchThread  implements Runnable {
	public static boolean bNeedQuit = false;
	
	WatchThread() {
		super();
		bNeedQuit = false;
	}
	
	public void run() {
		Scanner sc = new Scanner(System.in); 
		String str;
		while(true){
			str = sc.nextLine();
			Log.d("Your input is: " + str);
			if(str.compareToIgnoreCase("quit") == 0){
				bNeedQuit = true;
				break;
			}
		}
		sc = null;
		Log.d("Watch thread is quit");
	}
}
