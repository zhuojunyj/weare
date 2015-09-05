package cn.edu.zhku.b2c.util;

import org.json.JSONObject;

public class JsonUtil{
	
	
	public static String parseError(String jsonStr){
		try{
			JSONObject jsonObj = new JSONObject(jsonStr) ;
			if(jsonObj.has("errorObject")){
				JSONObject error = jsonObj.getJSONObject("errorObject") ;
				String errorMsg = error.getString("errorText") ;
				return new String(errorMsg.getBytes("ISO-8859-1"),"UTF-8") ;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null ;
	}
}
