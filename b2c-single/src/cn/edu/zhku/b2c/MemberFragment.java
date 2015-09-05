package cn.edu.zhku.b2c;


import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.edu.zhku.b2c.constant.App;
import cn.edu.zhku.b2c.cutom.CustomAlertDialog;
import cn.edu.zhku.b2c.cutom.CustomProgressDialog;
import cn.edu.zhku.b2c.db.UserDbManager;
import cn.edu.zhku.b2c.member.LoginActivity;
import cn.edu.zhku.b2c.member.OrderListActivity;
import cn.edu.zhku.b2c.member.SettingActivity;
import cn.edu.zhku.b2c.model.UserInfo;
import cn.edu.zhku.b2c.util.ConfigParser;
import cn.edu.zhku.b2c.util.HttpUtil;
import cn.edu.zhku.b2c.util.JsonUtil;
import cn.edu.zhku.b2c.vo.UserVo;

public class MemberFragment extends Fragment implements MainActivity.OnLoginListener{
	private View view = null ;
	private CustomProgressDialog progressDialog = null ;
	private CustomAlertDialog alertDialog = null ;
	private UserInfo userInfo = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		view =  inflater.inflate(R.layout.member_tab, container, false);
		Button toSetting = (Button) view.findViewById(R.id.button1) ;
		toSetting.setOnClickListener(new SettingListener());
		RelativeLayout toOrders = (RelativeLayout) view.findViewById(R.id.relativeLayout4) ;
		toOrders.setOnClickListener(new OrderListener(0));
		if(progressDialog == null){
    		progressDialog = CustomProgressDialog.createProgressDialog(getActivity() , false) ;
    		progressDialog.setMessage("加载中...") ;
    	}
		
		
		App app = (App)getActivity().getApplication() ;
		if(app.getUserLogined()){
			showLogin() ;
		}else{
			//Intent intent = new Intent(getActivity(),LoginActivity.class) ;
			//getActivity().startActivityForResult(intent, 200);
		}
		ImageView toLogin = (ImageView) view.findViewById(R.id.imageButton1) ;
		toLogin.setOnClickListener(new LoginListener());
		return view ;
	}
	
	public class SettingListener implements View.OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(getActivity(),SettingActivity.class) ;
			startActivity(intent);
		}
	}

	
	public class LoginListener implements View.OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(getActivity(),LoginActivity.class) ;
			getActivity().startActivityForResult(intent, 200);
		}
		
	}
	
	public class OrderListener implements View.OnClickListener{
		private int type ;
		public OrderListener(int type){
			this.type = type ;
		}
		@Override
		public void onClick(View v) {
			App app = (App)getActivity().getApplication() ;
			if(!app.getUserLogined()){
				Toast.makeText(getActivity(), "您尚未登录！", Toast.LENGTH_LONG).show(); ;
				return ;
			}
			Intent intent = new Intent(getActivity(),OrderListActivity.class) ;
			Bundle args = new Bundle() ;
			args.putInt("type", type);
			intent.putExtras(args) ;
			startActivity(intent);
		}
		
	}

	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(0 == msg.what){
				UserVo user = new UserVo() ;
				user.setName(userInfo.getName());
				user.setLoginId(userInfo.getLoginId()) ;
				user.setEmail(userInfo.getEmail()) ;
				user.setMobile(userInfo.getMobile()) ;
				UserDbManager mgr = new UserDbManager(getActivity()) ;
				mgr.save(user) ;
				
				TextView userName = (TextView) view.findViewById(R.id.username) ;
				userName.setText(userInfo.getEmail());
				TextView interalText = (TextView) view.findViewById(R.id.textView5) ;
				interalText.setText(userInfo.getIntegral().toString());
				TextView prestoreText = (TextView) view.findViewById(R.id.textView6) ;
				prestoreText.setText(userInfo.getPrestore().toString());
				showLogin();
				progressDialog.hide();
			}
		}
	};
	
	@Override
	public void onLogin(String userName , Integer sysUserId) {
		/**
		 * String webRoot = ConfigParser.loadConfig(getActivity(),"webRoot") ;
			new FetchUserInfo().execute(webRoot+"/app/member/userInfo.json") ;
		 */
		progressDialog.show();
		new Thread(new Runnable(){
			@Override
			public void run() {
				String webRoot = ConfigParser.loadConfig(getActivity(),"webRoot");
				HttpUtil.send(webRoot+"/app/member/userInfo.json", HttpUtil.POST, null, new HttpUtil.HttpCallback() {
					@Override
					public void onSuccess(String responseText) {
						userInfo = JSON.parseObject(JSON.parseObject(responseText).getString("result") ,UserInfo.class);
						handler.sendEmptyMessage(0);
					}
					@Override
					public void onError(String errorText) {
						if(alertDialog == null){
							alertDialog = new CustomAlertDialog(getActivity()) ;
							Bundle args = new Bundle() ;
							args.putString("message", errorText);
							alertDialog.setArguments(args);
							alertDialog.show(getActivity().getFragmentManager(),"error") ;
						}
					}
				});
			}
			
		}).start();
	}
	
	public class FetchUserInfo extends AsyncTask<String , Void , String>{

	    @Override  
	    protected void onPreExecute() { 
	    	progressDialog.show() ;
	    } 
	    
		@Override
		protected String doInBackground(String... arg) {
			String result = HttpUtil.httpPost(arg[0],null,"UTF-8") ;
			return result;
		}
		
		protected void onPostExecute(String result) {
			if(progressDialog != null){
				progressDialog.dismiss();
			}
			String error = JsonUtil.parseError(result);
			if(error != null){
				alertDialog = new CustomAlertDialog(getActivity()) ;
				Bundle args = new Bundle() ;
				args.putString("message", error);
				alertDialog.setArguments(args);
				alertDialog.show(getActivity().getFragmentManager(),"error") ;
			}
			saveToDatabase(result) ;
			showLogin() ;
	    }
	}
	
	public void saveToDatabase(String result){
		try {
			JSONObject json = new JSONObject(result) ;
			if(json.has("result")){
				JSONObject respon = json.getJSONObject("result") ;
				Double interal = respon.getDouble("integral") ;
				String loginId = respon.getString("loginId") ;
				String mobile = respon.getString("mobile") ;
				String email = respon.getString("email") ;
				Double preStore = respon.getDouble("prestore") ;
				String name = respon.getString("userName") ;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void showLogin(){
		view.findViewById(R.id.imageButton1).setVisibility(ImageView.INVISIBLE);
		view.findViewById(R.id.textView6).setVisibility(View.VISIBLE);
		view.findViewById(R.id.textView5).setVisibility(View.VISIBLE);
		view.findViewById(R.id.textView4).setVisibility(View.VISIBLE);
		view.findViewById(R.id.textView3).setVisibility(View.VISIBLE);
		view.findViewById(R.id.textView2).setVisibility(View.VISIBLE);
		view.findViewById(R.id.imageView2).setVisibility(View.VISIBLE);
		view.findViewById(R.id.imageView1).setVisibility(View.VISIBLE);
		view.findViewById(R.id.username).setVisibility(View.VISIBLE);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(alertDialog != null){
			alertDialog.dismiss();
		}
		
		if(progressDialog != null){
			progressDialog.dismiss();
		}
	}
	
}
