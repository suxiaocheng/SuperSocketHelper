package database;

import java.io.BufferedReader;

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
    
    public ItemInfo(String code, String name, double price_start, double price_last_end, 
    		double price_current, double price_today_high, double price_today_low, 
    		double price_compete_buy, double price_compete_seller, 
    		double price_num_deal, double price_deal, 
    		double[] price_buy, int[] num_buy,     		
    		double[] price_seller, int[] num_seller,
    		String date, String time, String time_actual){
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
    	set(ROW_PRICE_BUY1, price_buy[0]);
    	set(ROW_PRICE_BUY2, price_buy[1]);
    	set(ROW_PRICE_BUY3, price_buy[2]);
    	set(ROW_PRICE_BUY4, price_buy[3]);
    	set(ROW_PRICE_BUY5, price_buy[4]);
    	set(ROW_NUM_BUY1, num_buy[0]);
    	set(ROW_NUM_BUY2, num_buy[1]);
    	set(ROW_NUM_BUY3, num_buy[2]);
    	set(ROW_NUM_BUY4, num_buy[3]);
    	set(ROW_NUM_BUY5, num_buy[4]);
    	set(ROW_PRICE_SELLER1, price_seller[0]);
    	set(ROW_PRICE_SELLER2, price_seller[1]);
    	set(ROW_PRICE_SELLER3, price_seller[2]);
    	set(ROW_PRICE_SELLER4, price_seller[3]);
    	set(ROW_PRICE_SELLER5, price_seller[4]);
    	set(ROW_NUM_SELLER1, num_seller[0]);
    	set(ROW_NUM_SELLER2, num_seller[1]);
    	set(ROW_NUM_SELLER3, num_seller[2]);
    	set(ROW_NUM_SELLER4, num_seller[3]);
    	set(ROW_NUM_SELLER5, num_seller[4]);
    	
    	set(ROW_DATE, date);
    	set(ROW_TIME, time);
    	set(ROW_TIME_ACTUAL, time_actual);
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
    
    public static final String ROW_DATE = "DATE";
    public static final String ROW_TIME = "TIME";
    public static final String ROW_TIME_ACTUAL = "TIME_ACTUAL";
    
    public static final String[] titleList = {ROW_ID, ROW_CODE, ROW_NAME, ROW_PRICE_START,
			ROW_PRICE_LAST_END, ROW_PRICE_CURRENT, ROW_PRICE_TODAY_HIGH, 
			ROW_PRICE_TODAY_LOW, ROW_PRICE_COMPETE_BUY, ROW_PRICE_COMPETE_SELLER,
			ROW_NUMBER_OF_DEAL, ROW_PRICE_OF_DEAL, ROW_PRICE_BUY1, ROW_NUM_BUY1,
			ROW_PRICE_BUY2, ROW_NUM_BUY2, ROW_PRICE_BUY3, ROW_NUM_BUY3,
			ROW_PRICE_BUY4, ROW_NUM_BUY4, ROW_PRICE_BUY5, ROW_NUM_BUY5,
			ROW_PRICE_SELLER1, ROW_NUM_SELLER1, ROW_PRICE_SELLER2, ROW_NUM_SELLER2, 
			ROW_PRICE_SELLER3, ROW_NUM_SELLER3, ROW_PRICE_SELLER4, ROW_NUM_SELLER4, 
			ROW_PRICE_SELLER5, ROW_NUM_SELLER5, ROW_DATE, ROW_TIME, ROW_TIME_ACTUAL};
    
    public static final String[] compareList = {ROW_CODE, ROW_NAME, ROW_PRICE_START,
			ROW_PRICE_LAST_END, ROW_PRICE_CURRENT, ROW_PRICE_TODAY_HIGH, 
			ROW_PRICE_TODAY_LOW, ROW_PRICE_COMPETE_BUY, ROW_PRICE_COMPETE_SELLER,
			ROW_NUMBER_OF_DEAL, ROW_PRICE_OF_DEAL, ROW_PRICE_BUY1, ROW_NUM_BUY1,
			ROW_PRICE_BUY2, ROW_NUM_BUY2, ROW_PRICE_BUY3, ROW_NUM_BUY3,
			ROW_PRICE_BUY4, ROW_NUM_BUY4, ROW_PRICE_BUY5, ROW_NUM_BUY5,
			ROW_PRICE_SELLER1, ROW_NUM_SELLER1, ROW_PRICE_SELLER2, ROW_NUM_SELLER2, 
			ROW_PRICE_SELLER3, ROW_NUM_SELLER3, ROW_PRICE_SELLER4, ROW_NUM_SELLER4, 
			ROW_PRICE_SELLER5, ROW_NUM_SELLER5};

    public static final SimpleProperty[] TEXT_DEFAULTS_ALL = new SimpleProperty[]{
            new SimpleProperty(ROW_ID, 0.),
            new SimpleProperty(ROW_CODE, ""),
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
            new SimpleProperty(ROW_NUM_SELLER5, 0),
            new SimpleProperty(ROW_DATE, ""),
            new SimpleProperty(ROW_TIME, ""),
            new SimpleProperty(ROW_TIME_ACTUAL, "")
    };
    
    public String getValue() {
    	String str;
    	boolean appendColon = false;
    	
    	str = "(";
    	
    	for(String s: titleList){
    		if(appendColon == true){
    			str += ",";
    		}
    		
    		switch(get(s).getType()){
    		case SimpleProperty.TYPE_BOOL:
    			str += get(s).getBool();
    			break;
    		case SimpleProperty.TYPE_DOUBLE:
    			str += get(s).getDouble();
    			break;
    		case SimpleProperty.TYPE_INT:
    			str += get(s).getInt();
    			break;
    		case SimpleProperty.TYPE_TEXT:
    			str += "\"" + get(s).getString() + "\"";
    			break;
    		}
    		
    		appendColon = true;
    	}
    	str += ")";
    	
    	return str;
    }
    
    public String getCompareList() {
    	String str;
    	boolean appendColon = false;
    	
    	str = "(";
    	
    	for(String s: compareList){
    		if(appendColon == true){
    			str += ",";
    		}
    		
    		switch(get(s).getType()){
    		case SimpleProperty.TYPE_BOOL:
    			str += get(s).getBool();
    			break;
    		case SimpleProperty.TYPE_DOUBLE:
    			str += get(s).getDouble();
    			break;
    		case SimpleProperty.TYPE_INT:
    			str += get(s).getInt();
    			break;
    		case SimpleProperty.TYPE_TEXT:
    			str += "\"" + get(s).getString() + "\"";
    			break;
    		}
    		
    		appendColon = true;
    	}
    	str += ")";
    	
    	return str;
    }
    
    public String getTitle(){
    	String str;
    	boolean appendColon = false;
    	
    	str = "(";
    	
    	for(String s: titleList){
    		if(appendColon == true){
    			str += ",";
    		}
    		str += s;
    		appendColon = true;
    	}
    	str += ")";
    	
    	return str;
    }
    
    public boolean compareItem(ItemInfo diff){
    	boolean status = false;
    	String str1 = diff.getCompareList();
    	String str2 = getCompareList();
    	if(str1.compareTo(str2) == 0){
    		status = true;
    	}
    	return status;
    }
}
