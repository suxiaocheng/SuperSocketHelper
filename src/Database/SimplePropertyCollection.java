package Database;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;

public class SimplePropertyCollection {
	private HashMap<String, SimpleProperty> mProperties;
	
	public SimplePropertyCollection(SimpleProperty[] defaults) {
		mProperties = new HashMap<String, SimpleProperty>(defaults.length);
		for (int i = 0; i<defaults.length; i++) {
			mProperties.put(defaults[i].getKey(), new SimpleProperty(defaults[i]));
		}
	}
	
	public SimplePropertyCollection(SimpleProperty[] defaults, BufferedReader reader, boolean skipId, String idKey) throws Exception {
		String nextLine = reader.readLine();
		mProperties = new HashMap<String, SimpleProperty>();
		
		while(!nextLine.equals("enditem")) {
			nextLine = nextLine.trim();
			if (!nextLine.equals("") && !nextLine.equals("startitem")) {
				SimpleProperty sp = new SimpleProperty(nextLine);
				if ((!skipId) || (!sp.getKey().equals(idKey))) {
					mProperties.put(sp.getKey(), sp);
				}
			}
			nextLine = reader.readLine();
		}
		
		for (int i = 0; i < defaults.length; i++) {
			if (!mProperties.containsKey(defaults[i].getKey())) {
				mProperties.put(defaults[i].getKey(), new SimpleProperty(defaults[i]));
			}
		}
	}
	
	//Returns true if write was successful
	public boolean writeToFileWriter(BufferedWriter bw) {
		try {
			bw.write("startitem\n");
			
			Object[] props = mProperties.values().toArray();
			SimpleProperty sp = null;
			
			for (int i = 0; i<props.length; i++) {
				sp = (SimpleProperty)props[i];
				bw.write(sp.toString() + "\n");
			}
			
			bw.write("enditem\n");
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	public String toString() {
		String rtr = "startitem\n";
		
		Object[] props = mProperties.values().toArray();
		SimpleProperty sp = null;
		
		for (int i = 0; i<props.length; i++) {
			sp = (SimpleProperty)props[i];
			rtr += sp.toString() + "\n";
		}
		
		rtr += "enditem\n";
		return rtr;
	}	
	
	public void set(String key, int val) {
		mProperties.get(key).set(val);
	}
	public void set(String key, boolean val) {
		mProperties.get(key).set(val);
	}
	public void set(String key, String val) {
		mProperties.get(key).set(val);
	}
	public void set(String key, double val) {
		mProperties.get(key).set(val);
	}
	public void set(SimpleProperty sp) {
		String key = sp.getKey();
		
		switch(sp.getType()) {
		case SimpleProperty.TYPE_INT:
			set(key, sp.getInt());
			break;
		case SimpleProperty.TYPE_BOOL:
			set(key, sp.getBool());
			break;
		case SimpleProperty.TYPE_TEXT:
			set(key, sp.getString());
			break;
		case SimpleProperty.TYPE_DOUBLE:
			set(key, sp.getDouble());
			break;
		}
	}
	
	public boolean hasProperty(String key) {
		return mProperties.containsKey(key);
	}
	
	public int getInt(String key) {
		return mProperties.get(key).getInt();
	}
	public boolean getBool(String key) {
		return mProperties.get(key).getBool();
	}
	public String getString(String key) {
		return mProperties.get(key).getString();
	}
	public double getDouble(String key) {
		return mProperties.get(key).getDouble();
	}
	
	public SimpleProperty get(String key) {
		return mProperties.get(key);
	}
	
	public static String[] getKeyArray(SimpleProperty[] props) {
		String[] keys = new String[props.length];
		for (int i = 0; i<props.length; i++) {
			keys[i] = props[i].getKey();
		}
		return keys;
	}
	
	public static String getCreateTableStatement(SimpleProperty[] props, String tableName) {
		String rtr = "CREATE TABLE " + tableName + " (";
		for (int i = 0; i<props.length; i++) {
			SimpleProperty p = props[i];
			rtr += (i > 0 ? ", " : "") + p.getKey();
			switch(p.getType()) {
			case SimpleProperty.TYPE_INT:
				rtr += " INTEGER" + (p.getKey().equals("_id") ? " PRIMARY KEY AUTOINCREMENT" : "");
				break;
			case SimpleProperty.TYPE_BOOL:
				rtr += " BOOLEAN";
				break;
			case SimpleProperty.TYPE_TEXT:
				rtr += " TEXT";
				break;
			case SimpleProperty.TYPE_DOUBLE:
				rtr += " REAL";
				break;
			}
		}
		rtr += ")";
		return rtr;
	}
}
