import java.util.Scanner;

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
			System.out.println("Your input is: " + str);
			if(str.compareToIgnoreCase("quit") == 0){
				bNeedQuit = true;
				break;
			}
		}
		sc = null;
		System.out.println("Watch thread is quit");
	}
}
