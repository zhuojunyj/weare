package cn.edu.zhku.b2c.util;

import java.io.FileOutputStream;
import java.util.Properties;

import android.content.Context;

public class ConfigParser {
	
	//∂¡≥ˆ≈‰÷√
	public static String loadConfig(Context context , String key){
		Properties properties = new Properties();
		try{
			properties.load(context.getAssets().open("config.properties"));
		}catch(Exception e){
			e.printStackTrace(); 
		}
		return (String) properties.get(key);
	}
	
	//–¥»Î≈‰÷√
	public static void saveConfig(Context context , String key , String value) {  
		Properties properties = new Properties();
		properties.put(key , value) ;
		try {  
			FileOutputStream s = new FileOutputStream("assets/config.properties", false);  
			properties.store(s, "");  
		} catch (Exception e){  
			e.printStackTrace();  
		}  
	}
}
