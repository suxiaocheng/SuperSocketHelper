package config;

public class Config {
	public static final boolean DEBUG_ALWAYS_CREATE_DB = false;
	public static final int CONNECTION_TIMEOUT = 30000;
	public static final int CONNECTION_PARALLEL_EXEC = 64;
	public static final int MAX_FAIL_TIMES_ALLOW = 16;
	public static String db = ".\\sina-stock-codes.txt";
	public static final int MAX_EXECUTE_THREAD = 64;
}
