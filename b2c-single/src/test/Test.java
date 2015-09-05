package test;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import cn.edu.zhku.b2c.util.ConfigParser;
import cn.edu.zhku.b2c.util.HttpUtil;
import cn.edu.zhku.b2c.util.MD5;

public class Test {

	public static void main(String[] args) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
//		Map<String,Object> postData = new HashMap<String,Object>() ;
//		postData.put("userAcount","admin") ;
//		postData.put("userPassword",MD5.stringToMD5("123456")) ;
//		String resp = HttpUtil.httpPost("http://demo.imall.com.cn/app/member/userLogin/save.json",
//				postData,"UTF-8") ;
//		
//		System.out.println(resp) ;
//		if(resp != null){
//			Map<String,Object> postData1 = new HashMap<String,Object>() ;
//			postData1.put("orderType",1) ;
//			postData1.put("pageNumber",1) ;
//			String result = HttpUtil.httpPost("http://demo.imall.com.cn/app/product/category.json?rootNode=rootNode",
//					null,"UTF-8") ;
//			System.out.println(result) ;
//		}
		
		Map<String, Object> params = new HashMap<String, Object>() ;
		params.put("id", 1) ;
		String response = HttpUtil.httpPost("http://demo.imall.com.cn/app/product/productDetail.json", 
				params, 
				"UTF-8") ;
		System.out.println(response);
		
	}

}
