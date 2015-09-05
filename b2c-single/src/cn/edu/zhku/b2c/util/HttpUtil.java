package cn.edu.zhku.b2c.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPathException;

public class HttpUtil {
	 private static final String CHARSET = HTTP.UTF_8;
	private static HttpClient httpClient = null ;
	public static String POST = "post";
	public static String GET = "get";
	
	/*д�ɵ���ģʽ*/
	private HttpUtil(){}
	
	public static synchronized HttpClient getInstance(){
		if (null == httpClient) {
            HttpParams params = new BasicHttpParams();
            // ����һЩ��������
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params,
                    CHARSET);
            HttpProtocolParams.setUseExpectContinue(params, true);
            // ��ʱ����
            /* �����ӳ���ȡ���ӵĳ�ʱʱ�� */
            ConnManagerParams.setTimeout(params, 8000);
            /* ���ӳ�ʱ */
            HttpConnectionParams.setConnectionTimeout(params, 8000);
            /* ����ʱ */
            HttpConnectionParams.setSoTimeout(params, 8000);
			
            // �������ǵ�HttpClient֧��HTTP��HTTPS����ģʽ
            SchemeRegistry schReg = new SchemeRegistry();
            schReg.register(new Scheme("http", PlainSocketFactory
                    .getSocketFactory(), 80));
            schReg.register(new Scheme("https", SSLSocketFactory
                    .getSocketFactory(), 443));

            // ʹ���̰߳�ȫ�����ӹ���������HttpClient
            ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
                    params, schReg);
            httpClient = new DefaultHttpClient(conMgr, params);
            HttpClientParams.setCookiePolicy(httpClient.getParams(), CookiePolicy.BROWSER_COMPATIBILITY); 
//            httpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
        }
		return httpClient ;
	}
	
	//http get ����
	@SuppressWarnings("deprecation")
	public static String httpGet(String url , Map<String,Object> args , String urlEncoding){
		String result = null ;
		try{
			//��װ����
			String oldUrl = url + "?" ;
			if(args != null){
				for(Map.Entry<String,Object> entry : args.entrySet()){
					oldUrl = oldUrl +  entry.getKey() + "=" + entry.getValue() + "&" ;
				}
			}
			//ȥ�����һ��"&"
			String newUrl = oldUrl.substring(0, (oldUrl.length()-1)) ;
			HttpGet httpGet = new HttpGet(newUrl) ;
			httpGet.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
			httpClient = getInstance() ;
            
			HttpResponse httpResponse = httpClient.execute(httpGet); 
			result = EntityUtils.toString(httpResponse.getEntity());
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return result ;
	}
	
	
	// http post ����
	public static String httpPost(String url , Map<String,Object> args , String urlEncoding){
		String result = null ;
		try{
			//��װ����
			 List<NameValuePair> params = new ArrayList<NameValuePair>();  
			 if(args != null){
				 for(Map.Entry<String,Object> entry : args.entrySet()){
					 	if(entry.getValue() != null){
					 		params.add(new BasicNameValuePair(entry.getKey(),entry.getValue().toString())) ;
					 	}
					}
			 }
			 //�����ַ���  
            HttpEntity httpentity = new UrlEncodedFormEntity(params, urlEncoding == null ? "UTF-8" : urlEncoding); 
            //����httpRequest  
            HttpPost httpRequest = new HttpPost(url);
            httpRequest.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
            httpRequest.setEntity(httpentity); 
            httpClient = getInstance() ;
            
			HttpResponse httpResponse = httpClient.execute(httpRequest); 
			result = EntityUtils.toString(httpResponse.getEntity()) ;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return result ;
	}
	
	//====================================���·�װhttp��������error,success�ص�================
	public static void send(String url, String method, Map<String, Object> sendData, HttpCallback callback){
		if(method == null || GET.equals(method)){
			sendGet(url, sendData, callback);
		}
		
		if(POST.equals(method)){
			sendPost(url, sendData, callback);
		}

	}
	
	//post����
	public static void sendPost(String url, Map<String, Object> sendData, HttpCallback callback){
		
		try{
			//��װ����
			 List<NameValuePair> params = new ArrayList<NameValuePair>();  
			 if(sendData != null){
				 for(Map.Entry<String,Object> entry : sendData.entrySet()){
					 	if(entry.getValue() != null){
					 		params.add(new BasicNameValuePair(entry.getKey(),entry.getValue().toString())) ;
					 	}
					}
			 }
			 //�����ַ���  
            HttpEntity httpentity = new UrlEncodedFormEntity(params, "UTF-8"); 
            //����httpRequest  
            HttpPost httpRequest = new HttpPost(url);
            httpRequest.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
            httpRequest.setEntity(httpentity); 
            httpClient = getInstance() ;
            
			HttpResponse httpResponse = httpClient.execute(httpRequest); 
			if(httpResponse != null){
				String result = EntityUtils.toString(httpResponse.getEntity());
				if(httpResponse.getStatusLine().getStatusCode() == 200){
					callback.onSuccess(result);
					return ;
				}
				//����
				JSONObject jsonObj = JSON.parseObject(result);
				if(jsonObj.containsKey("errorObject")){
					JSONObject errObj = jsonObj.getJSONObject("errorObject");
					String errorMsg = errObj.getString("errorText");
					String error = new String(
							errorMsg.getBytes("ISO-8859-1"), "UTF-8");
					callback.onError(error);
				}
			}
		}catch(JSONPathException e){
			e.printStackTrace();
			callback.onError(e.getMessage());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			callback.onError(e.getMessage());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			callback.onError(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			callback.onError(e.getMessage());
		}
	}
	
	//get����
	public static void sendGet(String url, Map<String, Object> sendData, HttpCallback callback){
		try {
			//��װ����
			String oldUrl = url + "?" ;
			if(sendData != null){
				for(Map.Entry<String,Object> entry : sendData.entrySet()){
					oldUrl = oldUrl +  entry.getKey() + "=" + entry.getValue() + "&" ;
				}
			}
			//ȥ�����һ��"&"
			String newUrl = oldUrl.substring(0, (oldUrl.length()-1)) ;
			HttpGet httpGet = new HttpGet(newUrl) ;
			httpGet.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
			httpClient = getInstance() ;
	        
			HttpResponse httpResponse;
			httpResponse = httpClient.execute(httpGet);
			if(httpResponse != null){
				String result = EntityUtils.toString(httpResponse.getEntity());
				if(httpResponse.getStatusLine().getStatusCode() == 200){
					callback.onSuccess(result);
					return ;
				}
				JSONObject jsonObj = JSON.parseObject(result);
				if(jsonObj.containsKey("errorObject")){
					JSONObject errObj = jsonObj.getJSONObject("errorObject");
					String errorMsg = errObj.getString("errorText");
					String error = new String(
							errorMsg.getBytes("ISO-8859-1"), "UTF-8");
					callback.onError(error);
					return ;
				}
				
			}
		} catch (ParseException e) {
			e.printStackTrace();
			callback.onError(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			callback.onError(e.getMessage());
		}
	}
	
	public interface HttpCallback{
		public void onError(String errorText);
		public void onSuccess(String responseText);
	}
}
