package cn.edu.zhku.b2c.member;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import cn.edu.zhku.b2c.R;
import cn.edu.zhku.b2c.cate.ProductActivity;
import cn.edu.zhku.b2c.common.HeaderFragment;
import cn.edu.zhku.b2c.constant.App;
import cn.edu.zhku.b2c.cutom.CustomAlertDialog;
import cn.edu.zhku.b2c.cutom.CustomProgressDialog;
import cn.edu.zhku.b2c.model.SysUser;
import cn.edu.zhku.b2c.util.ConfigParser;
import cn.edu.zhku.b2c.util.HttpUtil;
import cn.edu.zhku.b2c.util.JsonUtil;
import cn.edu.zhku.b2c.util.MD5;

public class LoginActivity extends Activity implements CustomAlertDialog.AlertDialogListener{
	private CustomProgressDialog progressDialog = null ;
	private CustomAlertDialog alertDialog = null ;
	private SysUser user = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		addHeader();
		Button goLogin = (Button) findViewById(R.id.loginBtn) ;
		goLogin.setOnClickListener(new GoLoginListener());
	}
	
	public void addHeader(){
		HeaderFragment headerFragment = new HeaderFragment() ;
		Bundle args = new Bundle() ;
		args.putString("title", "登录");
		headerFragment.setArguments(args);
		FragmentManager fragmentManager = getFragmentManager() ;
		FragmentTransaction transaction = fragmentManager.beginTransaction() ;
		transaction.add(R.id.loginPage,headerFragment) ;
		transaction.commit() ;
	}
	
	public class GoLoginListener implements View.OnClickListener{
		@Override
		public void onClick(View v) {
			if (progressDialog == null) {
				progressDialog = CustomProgressDialog.createProgressDialog(
						LoginActivity.this, false);
				progressDialog.setMessage("加载中...");
				progressDialog.show();
			}
			login();
			/**
			 * EditText editText = (EditText) findViewById(R.id.username) ;
			String userName = editText.getText().toString() ;
			
			EditText editText1 = (EditText) findViewById(R.id.pwd) ;
			String pwd = editText1.getText().toString() ;
			
			String password = MD5.stringToMD5(pwd) ;
			
			String webRoot = ConfigParser.loadConfig(LoginActivity.this,"webRoot") ;
			new LoginTask().execute(webRoot+"/app/member/userLogin/save.json",userName,password) ;
			 */
		}
	}
	
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0){
				App app = (App) getApplication() ;
				app.setUserName(user.getUserName());
				app.setIsAdmin(user.getIsAdmin());
				app.setSysUserId(user.getSysUserId());
				app.setLoginId(user.getLoginId());
				app.setEmail(user.getUserEmail());
				app.setMobile(user.getUserMobile());
				app.setTel(user.getUserTel());
				app.setRegOrgId(user.getRegOrgId());
				app.setUserStateCode(user.getUserStatCode());
				app.setUserLogined(true);
				
				
				Intent intent = new Intent() ;
				intent.putExtra("userName", user.getUserName()) ;
				intent.putExtra("sysUserId", user.getSysUserId()) ;
				setResult(201, intent);
				finish();
				
				progressDialog.hide();
			}
		}
	};
	
	
	public void login(){
		EditText editText = (EditText) findViewById(R.id.username) ;
		final String userName = editText.getText().toString() ;
		
		EditText editText1 = (EditText) findViewById(R.id.pwd) ;
		String pwd = editText1.getText().toString() ;
		
		final String password = MD5.stringToMD5(pwd) ;
		
		new Thread(new Runnable(){
			@Override
			public void run() {
				final String webRoot = ConfigParser.loadConfig(LoginActivity.this, "webRoot");
				final Map<String, Object> sendData = new HashMap<String, Object>();
				sendData.put("userAcount", userName);
				sendData.put("userPassword", password);
				HttpUtil.send(webRoot+"/app/member/userLogin/save.json", HttpUtil.POST, sendData, new HttpUtil.HttpCallback() {
					@Override
					public void onSuccess(String responseText) {
						user = JSON.parseObject(JSON.parseObject(responseText).getString("result"), SysUser.class);
						handler.sendEmptyMessage(0);
					}
					
					@Override
					public void onError(String errorText) {
						if(alertDialog == null){
							alertDialog = new CustomAlertDialog(
									LoginActivity.this);
							Bundle bundle = new Bundle();
							bundle.putString("message", errorText);
							alertDialog.setArguments(bundle);
							alertDialog.show(getFragmentManager(),
									"error");
						}
					}
				});
			}
		}).start();
	}
	
	//=====================new login method========================================
	
	
	//++++++++++++++++++++++old login method++++++++++++++++++++++++++++++++++++++++
	
	public class LoginTask extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... arg) {
			Map<String,Object> postData = new HashMap<String,Object>() ;
			postData.put("userAcount",arg[1]) ;
			postData.put("userPassword",arg[2]) ;
			String result = HttpUtil.httpPost(arg[0],postData,"UTF-8") ;
			
			return result;
		}

	    @Override  
	    protected void onPreExecute() { 
	    	if(progressDialog == null){
	    		progressDialog = CustomProgressDialog.createProgressDialog(LoginActivity.this , false) ;
	    	}
			progressDialog.setMessage("加载中...") ;
	    	progressDialog.show() ;
	    } 

		protected void onPostExecute(String result) {
			parseJson(result) ;
	    }
	}
	
	
	public void parseJson(String jsonStr){
		if(progressDialog != null){
			progressDialog.dismiss();
		}
		String error = JsonUtil.parseError(jsonStr);
		if(error != null){
			if(alertDialog != null){
				alertDialog = new CustomAlertDialog(LoginActivity.this) ;
				Bundle args = new Bundle() ;
				args.putString("message", error);
				alertDialog.setArguments(args);
				alertDialog.show(getFragmentManager(), "error");
				return ;
			}
		}
		try {
			
			JSONObject jsonObj = new JSONObject(jsonStr) ;
			
			/*
			if(jsonObj.has("errorObject")){
				JSONObject error = jsonObj.getJSONObject("errorObject") ;
				String errorMsg = error.getString("errorText") ;
				if(alertDialog == null){
					alertDialog = new CustomAlertDialog() ;
				}
				Bundle bundle = new Bundle() ;
				bundle.putString("message", errorMsg);
				alertDialog.setArguments(bundle);
				alertDialog.show(getFragmentManager(), "alertError");
				return ;
			}*/
			String success = jsonObj.getString("success") ;
			if("false".equals(success)){
				return ;
			}
			if(!jsonObj.has("result")){
				return ;
			}
			JSONObject response = jsonObj.getJSONObject("result") ;
			String userName = null ;
			Integer sysUserId = null ;
			if(response != null){
				App app = (App) getApplication() ;
				userName = response.getString("userName") ;
				sysUserId = response.getInt("sysUserId") ;
				app.setUserName(userName);
				app.setIsAdmin(response.getString("isAdmin"));
				app.setSysUserId(sysUserId);
				app.setLoginId(response.getString("loginId"));
				app.setEmail(response.getString("userEmail"));
				app.setMobile(response.getString("userMobile"));
				app.setTel(response.getString("userTel"));
				app.setRegOrgId(response.getInt("regOrgId"));
				app.setUserStateCode(response.getString("userStatCode"));
				app.setUserLogined(true);
			}
			Intent intent = new Intent() ;
			intent.putExtra("userName", userName) ;
			intent.putExtra("sysUserId", sysUserId) ;
			
			setResult(201, intent);
			finish();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(alertDialog != null){
			alertDialog.dismiss();
		}
		if(progressDialog != null){
			progressDialog.dismiss();
		}
	}

	@Override
	public void onPostiveClick(DialogFragment dialog) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNegativeClick(DialogFragment dialog) {
		// TODO Auto-generated method stub
		
	}
	
	
}
