package Database;

import java.awt.Cursor;
import java.io.BufferedReader;
import java.util.Calendar;

import com.mysql.jdbc.log.Log;

/**
 * Created by suxch on 2016/1/2.
 */
public class ItemInfo extends SimplePropertyCollection {
    private static final String TAG = "ItemInfo";

    public ItemInfo() {
        super(TEXT_DEFAULTS_ALL);
    }

    public ItemInfo(BufferedReader reader, boolean skipId) throws Exception {
        super(TEXT_DEFAULTS_ALL, reader, skipId, ROW_ID);
    }
    
    public ItemInfo(int code, String name, double price_start, double price_last_end, 
    		double price_current, double price_today_high, double price_today_low, 
    		double price_compete_buy, double price_compete_seller, 
    		double price_num_deal, double price_deal, 
    		double price_buy1, int num_buy1, 
    		double price_buy2, int num_buy2, 
    		double price_buy3, int num_buy3, 
    		double price_buy4, int num_buy4, 
    		double price_buy5, int num_buy5, 
    		double price_seller1, int num_seller1, 
    		double price_seller2, int num_seller2, 
    		double price_seller3, int num_seller3, 
    		double price_seller4, int num_seller4, 
    		double price_seller5, int num_seller5){
    	super(TEXT_DEFAULTS_ALL);
    	set(ROW_ID, System.currentTimeMillis());
    	set(ROW_CODE, code);
    	set(ROW_NAME, name);
    	set(ROW_PRICE_START, price_start);
    	set(ROW_PRICE_LAST_END, price_last_end);
    	set(ROW_PRICE_CURRENT, price_current);
    	set(ROW_PRICE_TODAY_HIGH, price_today_high);
    	set(ROW_PRICE_TODAY_LOW, price_today_low);
    	set(ROW_PRICE_COMPETE_BUY, price_compete_buy);
    	set(ROW_PRICE_COMPETE_SELLER, price_compete_seller);
    	set(ROW_NUMBER_OF_DEAL, price_num_deal);
    	set(ROW_PRICE_OF_DEAL, price_deal);
    	set(ROW_PRICE_BUY1, price_buy1);
    	set(ROW_PRICE_BUY2, price_buy2);
    	set(ROW_PRICE_BUY3, price_buy3);
    	set(ROW_PRICE_BUY4, price_buy4);
    	set(ROW_PRICE_BUY5, price_buy5);
    	set(ROW_NUM_BUY1, num_buy1);
    	set(ROW_NUM_BUY2, num_buy2);
    	set(ROW_NUM_BUY3, num_buy3);
    	set(ROW_NUM_BUY4, num_buy4);
    	set(ROW_NUM_BUY5, num_buy5);
    	set(ROW_PRICE_SELLER1, price_seller1);
    	set(ROW_PRICE_SELLER2, price_seller2);
    	set(ROW_PRICE_SELLER3, price_seller3);
    	set(ROW_PRICE_SELLER4, price_seller4);
    	set(ROW_PRICE_SELLER5, price_seller5);
    	set(ROW_NUM_SELLER1, num_seller1);
    	set(ROW_NUM_SELLER1, num_seller2);
    	set(ROW_NUM_SELLER1, num_seller3);
    	set(ROW_NUM_SELLER1, num_seller4);
    	set(ROW_NUM_SELLER1, num_seller5);
    }

    /* Used for sql */
    public static final String ROW_ID = "ID";
    public static final String ROW_CODE = "CODE";
    public static final String ROW_NAME = "NAME";
    public static final String ROW_PRICE_START = "PRICE_START";
    public static final String ROW_PRICE_LAST_END = "PRICE_LAST_END";
    public static final String ROW_PRICE_CURRENT = "PRICE_CURRENT";
    public static final String ROW_PRICE_TODAY_HIGH = "PRICE_TODAY_HIGH";
    public static final String ROW_PRICE_TODAY_LOW = "PRICE_TODAY_LOW";
    public static final String ROW_PRICE_COMPETE_BUY = "PRICE_COMPETE_BUY";
    public static final String ROW_PRICE_COMPETE_SELLER = "PRICE_COMPETE_SELLER";
    public static final String ROW_NUMBER_OF_DEAL = "NUMBER_OF_DEAL";
    public static final String ROW_PRICE_OF_DEAL = "PRICE_OF_DEAL";
    
    public static final String ROW_PRICE_BUY1 = "PRICE_BUY1";
    public static final String ROW_PRICE_BUY2 = "PRICE_BUY2";
    public static final String ROW_PRICE_BUY3 = "PRICE_BUY3";
    public static final String ROW_PRICE_BUY4 = "PRICE_BUY4";
    public static final String ROW_PRICE_BUY5 = "PRICE_BUY5";
    
    public static final String ROW_NUM_BUY1 = "NUM_BUY1";
    public static final String ROW_NUM_BUY2 = "NUM_BUY2";
    public static final String ROW_NUM_BUY3 = "NUM_BUY3";
    public static final String ROW_NUM_BUY4 = "NUM_BUY4";
    public static final String ROW_NUM_BUY5 = "NUM_BUY5";
    
    public static final String ROW_PRICE_SELLER1 = "PRICE_SELLER1";
    public static final String ROW_PRICE_SELLER2 = "PRICE_SELLER2";
    public static final String ROW_PRICE_SELLER3 = "PRICE_SELLER3";
    public static final String ROW_PRICE_SELLER4 = "PRICE_SELLER4";
    public static final String ROW_PRICE_SELLER5 = "PRICE_SELLER5";
    
    public static final String ROW_NUM_SELLER1 = "NUM_SELLER1";
    public static final String ROW_NUM_SELLER2 = "NUM_SELLER2";
    public static final String ROW_NUM_SELLER3 = "NUM_SELLER3";
    public static final String ROW_NUM_SELLER4 = "NUM_SELLER4";
    public static final String ROW_NUM_SELLER5 = "NUM_SELLER5";

    public static final SimpleProperty[] TEXT_DEFAULTS_ALL = new SimpleProperty[]{
            new SimpleProperty(ROW_ID, 0),
            new SimpleProperty(ROW_CODE, 0),
            new SimpleProperty(ROW_NAME, ""),
            new SimpleProperty(ROW_PRICE_START, 0.),
            new SimpleProperty(ROW_PRICE_LAST_END, 0.),
            new SimpleProperty(ROW_PRICE_CURRENT, 0.),
            new SimpleProperty(ROW_PRICE_TODAY_HIGH, 0.),
            new SimpleProperty(ROW_PRICE_TODAY_LOW, 0.),
            new SimpleProperty(ROW_PRICE_COMPETE_BUY, 0.),
            new SimpleProperty(ROW_PRICE_COMPETE_SELLER, 0.),
            new SimpleProperty(ROW_NUMBER_OF_DEAL, 0.),
            new SimpleProperty(ROW_PRICE_OF_DEAL, 0.),
            
            new SimpleProperty(ROW_PRICE_BUY1, 0.),
            new SimpleProperty(ROW_PRICE_BUY2, 0.),
            new SimpleProperty(ROW_PRICE_BUY3, 0.),
            new SimpleProperty(ROW_PRICE_BUY4, 0.),
            new SimpleProperty(ROW_PRICE_BUY5, 0.),
            
            new SimpleProperty(ROW_NUM_BUY1, 0),
            new SimpleProperty(ROW_NUM_BUY2, 0),
            new SimpleProperty(ROW_NUM_BUY3, 0),
            new SimpleProperty(ROW_NUM_BUY4, 0),
            new SimpleProperty(ROW_NUM_BUY5, 0),
            
            new SimpleProperty(ROW_PRICE_SELLER1, 0.),
            new SimpleProperty(ROW_PRICE_SELLER2, 0.),
            new SimpleProperty(ROW_PRICE_SELLER3, 0.),
            new SimpleProperty(ROW_PRICE_SELLER4, 0.),
            new SimpleProperty(ROW_PRICE_SELLER5, 0.),
            
            new SimpleProperty(ROW_NUM_SELLER1, 0),
            new SimpleProperty(ROW_NUM_SELLER2, 0),
            new SimpleProperty(ROW_NUM_SELLER3, 0),
            new SimpleProperty(ROW_NUM_SELLER4, 0),
            new SimpleProperty(ROW_NUM_SELLER5, 0)            
    };
    
    public static String dumpItemTitle() {
        String str = "id, code, name, price start, "
        		+ "price last day, price current, " 
        		+ "price today high, price today low, price compete buy, " 
        		+"price compete seller, number of deal, price of deal, "
        		+ "price buy 1, num buy 1, price buy 2, num buy 2, "
        		+ "price buy 3, num buy 3, price buy 4, num buy 4, "
        		+ "price buy 5, num buy 5, price seller 1, num seller 1, "
        		+ "price seller 2, num seller 2, price seller 3, num seller 3, "
        		+ "price seller 4, num seller 4, price seller 5, num seller 5";
        
        return str;
    }

    public static String dumpItemInfo(ItemInfo info) {
        String str;
        
        str = new String();
        str += info.get(ROW_ID) + ",";
        str += info.get(ROW_CODE) + ",";
        str += info.get(ROW_NAME) + ",";
        str += info.get(ROW_PRICE_START) + ",";
        str += info.get(ROW_PRICE_LAST_END) + ",";
        str += info.get(ROW_PRICE_CURRENT) + ",";
        str += info.get(ROW_PRICE_TODAY_HIGH) + ",";
        str += info.get(ROW_PRICE_TODAY_LOW) + ",";
        str += info.get(ROW_PRICE_COMPETE_BUY) + ",";
        str += info.get(ROW_PRICE_COMPETE_SELLER) + ",";
        str += info.get(ROW_NUMBER_OF_DEAL) + ",";
        str += info.get(ROW_PRICE_OF_DEAL) + ",";
        
        str += info.get(ROW_PRICE_BUY1) + ",";
        str += info.get(ROW_NUM_BUY1) + ",";
        str += info.get(ROW_PRICE_BUY2) + ",";
        str += info.get(ROW_NUM_BUY2) + ",";
        str += info.get(ROW_PRICE_BUY3) + ",";
        str += info.get(ROW_NUM_BUY3) + ",";
        str += info.get(ROW_PRICE_BUY4) + ",";
        str += info.get(ROW_NUM_BUY4) + ",";
        str += info.get(ROW_PRICE_BUY5) + ",";
        str += info.get(ROW_NUM_BUY5) + ",";
        
        str += info.get(ROW_PRICE_SELLER1) + ",";
        str += info.get(ROW_NUM_SELLER1) + ",";
        str += info.get(ROW_PRICE_SELLER2) + ",";
        str += info.get(ROW_NUM_SELLER2) + ",";
        str += info.get(ROW_PRICE_SELLER3) + ",";
        str += info.get(ROW_NUM_SELLER3) + ",";
        str += info.get(ROW_PRICE_SELLER4) + ",";
        str += info.get(ROW_NUM_SELLER4) + ",";
        str += info.get(ROW_PRICE_SELLER5) + ",";
        str += info.get(ROW_NUM_SELLER5);
        
        return str;
    }
}
