package cn.edu.zhku.b2c;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import cn.edu.zhku.b2c.cate.ProductActivity;
import cn.edu.zhku.b2c.cutom.Gallery;
import cn.edu.zhku.b2c.i.TabListener;
import cn.edu.zhku.b2c.model.Module;
import cn.edu.zhku.b2c.model.Product;
import cn.edu.zhku.b2c.refresh.PullToRefreshBase;
import cn.edu.zhku.b2c.refresh.PullToRefreshBase.OnRefreshListener;
import cn.edu.zhku.b2c.refresh.PullToRefreshScrollView;
import cn.edu.zhku.b2c.util.ConfigParser;
import cn.edu.zhku.b2c.util.HttpUtil;
import cn.edu.zhku.b2c.util.HttpUtil.HttpCallback;
import cn.edu.zhku.b2c.util.ImageLoader;
import cn.edu.zhku.b2c.util.ImageLoader.LoaderCallback;

import com.alibaba.fastjson.JSONObject;

public class HomeFragment extends Fragment implements View.OnClickListener, Gallery.ClickCallback{

 	private ScrollView mScrollView;
    private PullToRefreshScrollView mPullScrollView;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");
    private Context context = null;
    private List<Module> appModules = null;
    private View view = null;
    private Gallery gallery = null;
    private String[] rollMsg = null;
    int count = 0; //计数器
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		view = inflater.inflate(R.layout.home_tab, container, false);
		
		//下拉刷新控件
		mPullScrollView = (PullToRefreshScrollView)view.findViewById(R.id.pullToRefreshScrollView1);
        mScrollView = mPullScrollView.getRefreshableView();
        context = getActivity();
        LayoutInflater inflater1 = LayoutInflater.from(context);
        mScrollView.addView(inflater1.inflate(R.layout.fragment_home_main, null));
        setLastUpdateTime();
        mPullScrollView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                new GetDataTask().execute();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                
            }
        });
        //请求首页配置信息
        init();
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(new Runnable(){
			@Override
			public void run() {
				handler.sendEmptyMessage(1);
			}
		}, 2000, 5000, TimeUnit.MILLISECONDS);
		
		// 限时、团购、订单、积分点击事件
		ImageView snapUp = (ImageView)view.findViewById(R.id.snap_up);
		snapUp.setOnClickListener(this);
		
		ImageView groupBuy = (ImageView)view.findViewById(R.id.group_buy);
		groupBuy.setOnClickListener(this);
		
		ImageView orderQuery = (ImageView)view.findViewById(R.id.order_query);
		orderQuery.setOnClickListener(this);
		
		ImageView integralExchange = (ImageView)view.findViewById(R.id.integral_exchange);
		integralExchange.setOnClickListener(this);
		
		EditText searchText = (EditText)view.findViewById(R.id.search);
		searchText.setOnClickListener(this);
		return view;
	}
	
	//更新UI
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			if(0 == what){
				updateUI();
			}
			if(1 == what){
				TextView info = (TextView)view.findViewById(R.id.msg_roll);
				if(rollMsg != null && rollMsg.length > 0){
					info.setText(rollMsg[count]);
					ObjectAnimator moveIn = ObjectAnimator.ofFloat(info, "y", -100f, 15f);
					moveIn.setDuration(1000);
					moveIn.start();
					
					ObjectAnimator moveOut = ObjectAnimator.ofFloat(info, "y", 15f, -100f);
					moveOut.setStartDelay(3000);
					moveOut.setDuration(2000);
					moveOut.start();
					count ++ ;
					if(count > rollMsg.length - 1){
						count = 0;//计数品置0
					}
				}
			}
		}
		
	};
	
	//获取网络数据
	public void init(){
		new Thread(new Runnable(){
			@Override
			public void run() {
				loadData();
			}
		}).start();
	}
	
	
	public void loadData(){
		String webRoot = ConfigParser.loadConfig(getActivity(), "webRoot");
		Map<String, Object> sendData = new HashMap<String, Object>();
		
		//网络类型 
		ConnectivityManager connManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);  
		NetworkInfo networkinfo = connManager.getActiveNetworkInfo();  
		String networkType = networkinfo.getTypeName();
		sendData.put("networktype", networkType);
		
		 //获取屏幕信息
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		sendData.put("platformWidth", dm.widthPixels);
		sendData.put("platformHeight", dm.heightPixels);
		sendData.put("logicalDensityFactor", dm.density);
		
		//获取 api level
//		int apiLevel = 0;
//		try {
//			apiLevel = android.provider.Settings.System.getInt(context
//				     .getContentResolver(),
//				     android.provider.Settings.System.SYS_PROP_SETTING_VERSION);
//		} catch (SettingNotFoundException e1) {
//			e1.printStackTrace();
//		}
		sendData.put("apiLevel", 19);
		sendData.put("osname", "android");
		sendData.put("platformDeviceTypeCode", "aos-hand");
		
		//获取当前系统的android版本号
		int currentapiVersion=android.os.Build.VERSION.SDK_INT;
		sendData.put("osversion", currentapiVersion);
		
		//获取包信息
        PackageManager pm = context.getPackageManager();  
        PackageInfo pi = null;
		try {
			pi = pm.getPackageInfo(context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}  
        String versionName = pi.versionName;  
		sendData.put("appversion", versionName);
		HttpUtil.send(webRoot + "/app/page.json", HttpUtil.POST, sendData, new HttpCallback() {
			@Override
			public void onSuccess(String responseText) {
				JSONObject obj = JSONObject.parseObject(responseText);
				appModules = JSONObject.parseArray(obj.getString("result"), Module.class);
				handler.sendEmptyMessage(0);
			}
			@Override
			public void onError(String errorText) {
//				System.out.println(errorText);
			}
		});
	}
	
	//渲染首页数据
	public void updateUI(){
		if(appModules == null || appModules.isEmpty()){
			return;
		}
		ImageLoader loader = new ImageLoader();
		//logo
		if(!appModules.get(0).getModuleObjects().isEmpty()){
			loader.loadImage(appModules.get(0).getModuleObjects().get(0).getPicUrl(), new LoaderCallback(){
				@Override
				public void async(Bitmap bitmap, String imageUrl) {
					ImageView logo = (ImageView)view.findViewById(R.id.logo);
					if(logo != null){
						logo.setImageBitmap(bitmap);
					}
				}
			});
		}
		
		//广告轮换
		if(appModules.size() > 2 && !appModules.get(1).getModuleObjects().isEmpty()){
			int len = appModules.get(1).getModuleObjects().size();
			String[] images = new String[len];
			for(int i = 0 ; i < len ; i ++){
				images[i] = appModules.get(1).getModuleObjects().get(i).getPicUrl();
			}
			gallery = (Gallery) view.findViewById(R.id.adv_roll);
			gallery.startPlaying(images, 2000, 2000);
		}
		
		
		//资讯轮换
		if(appModules.size() > 3 && !appModules.get(2).getModuleObjects().isEmpty()){
			TextView msg = (TextView)view.findViewById(R.id.msg_roll);
			msg.setOnClickListener(this);
			int len = appModules.get(2).getModuleObjects().size();
			rollMsg = new String[len]; 
			for(int i = 0 ; i < len ; i ++){
				rollMsg[i] = appModules.get(2).getModuleObjects().get(i).getTitle();
			}
		}
		
		//f1楼层标题
		if(appModules.size() > 4 && !appModules.get(3).getModuleObjects().isEmpty()){
			TextView f1Title = (TextView)view.findViewById(R.id.f1_title);
			f1Title.setText(appModules.get(3).getModuleObjects().get(0).getTitle());
		}
		
		//f1楼层内容
		if(appModules.size() > 5 && !appModules.get(4).getModuleObjects().isEmpty()){
			final ImageView f1_img1 = (ImageView)view.findViewById(R.id.f1_img1);
			f1_img1.setOnClickListener(this);
			TextView f1_prd1 = (TextView)view.findViewById(R.id.f1_prd1);
			TextView f1_price1 = (TextView)view.findViewById(R.id.f1_price1);
			TextView f1_unitPrice1 = (TextView)view.findViewById(R.id.f1_unitPrice1);
			Product prd1 = appModules.get(4).getModuleObjects().get(0).getProduct();
			loader.loadImage(prd1.getImage(), new LoaderCallback(){
				@Override
				public void async(Bitmap bitmap, String imageUrl) {
					f1_img1.setImageBitmap(bitmap);
				}
			});
			f1_prd1.setText(prd1.getTitle());
			f1_price1.setText("￥" + prd1.getPrice());
			f1_unitPrice1.setText("￥" + prd1.getMarketPrice());
			f1_unitPrice1.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
			
			final ImageView f1_img2 = (ImageView)view.findViewById(R.id.f1_img2);
			f1_img2.setOnClickListener(this);
			TextView f1_prd2 = (TextView)view.findViewById(R.id.f1_prd2);
			TextView f1_price2 = (TextView)view.findViewById(R.id.f1_price2);
			TextView f1_unitPrice2 = (TextView)view.findViewById(R.id.f1_unitPrice2);
			Product prd2 = appModules.get(4).getModuleObjects().get(1).getProduct();
			loader.loadImage(prd2.getImage(), new LoaderCallback(){
				@Override
				public void async(Bitmap bitmap, String imageUrl) {
					f1_img2.setImageBitmap(bitmap);
				}
			});
			f1_prd2.setText(prd2.getTitle());
			f1_price2.setText("￥" + prd2.getPrice());
			f1_unitPrice2.setText("￥" + prd2.getMarketPrice());
			f1_unitPrice2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
			
			final ImageView f1_img3 = (ImageView)view.findViewById(R.id.f1_img3);
			f1_img3.setOnClickListener(this);
			TextView f1_prd3 = (TextView)view.findViewById(R.id.f1_prd3);
			TextView f1_price3 = (TextView)view.findViewById(R.id.f1_price3);
			TextView f1_unitPrice3 = (TextView)view.findViewById(R.id.f1_unitPrice3);
			Product prd3 = appModules.get(4).getModuleObjects().get(2).getProduct();
			loader.loadImage(prd3.getImage(), new LoaderCallback(){
				@Override
				public void async(Bitmap bitmap, String imageUrl) {
					f1_img3.setImageBitmap(bitmap);
				}
			});
			f1_prd3.setText(prd3.getTitle());
			f1_price3.setText("￥" + prd3.getPrice());
			f1_unitPrice3.setText("￥" + prd3.getMarketPrice());
			f1_unitPrice3.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		}
		
		//f2楼层标题
		if(appModules.size() > 6 && !appModules.get(5).getModuleObjects().isEmpty()){
			TextView f2_title = (TextView)view.findViewById(R.id.f2_title);
			f2_title.setText(appModules.get(5).getModuleObjects().get(0).getTitle());
		}
				
		//f2楼层内容
		if(appModules.size() > 7 && !appModules.get(6).getModuleObjects().isEmpty()){
			final ImageView f2_img1 = (ImageView)view.findViewById(R.id.f2_img1);
			String img1 = appModules.get(6).getModuleObjects().get(0).getPicUrl();
			f2_img1.setOnClickListener(this);
			loader.loadImage(img1, new LoaderCallback(){
				@Override
				public void async(Bitmap bitmap, String imageUrl) {
					f2_img1.setImageBitmap(bitmap);
				}
			});
		}
		
		if(appModules.size() > 8 && !appModules.get(7).getModuleObjects().isEmpty()){
			final ImageView f2_img2 = (ImageView)view.findViewById(R.id.f2_img2);
			f2_img2.setOnClickListener(this);
			String img2 = appModules.get(7).getModuleObjects().get(0).getPicUrl();
			
			loader.loadImage(img2, new LoaderCallback(){
				@Override
				public void async(Bitmap bitmap, String imageUrl) {
					f2_img2.setImageBitmap(bitmap);
				}
			});
			
		}
		
		if(appModules.size() > 9 && !appModules.get(8).getModuleObjects().isEmpty()){
			final ImageView f2_img3 = (ImageView)view.findViewById(R.id.f2_img3);
			f2_img3.setOnClickListener(this);
			String img3 = appModules.get(8).getModuleObjects().get(0).getPicUrl();
			
			
			loader.loadImage(img3, new LoaderCallback(){
				@Override
				public void async(Bitmap bitmap, String imageUrl) {
					f2_img3.setImageBitmap(bitmap);
				}
			});
			
		}
		
		if(appModules.size() > 10 && !appModules.get(9).getModuleObjects().isEmpty()){
			final ImageView f2_img4 = (ImageView)view.findViewById(R.id.f2_img4);
			f2_img4.setOnClickListener(this);
			String img4 = appModules.get(9).getModuleObjects().get(0).getPicUrl();
			
			loader.loadImage(img4, new LoaderCallback(){
				@Override
				public void async(Bitmap bitmap, String imageUrl) {
					f2_img4.setImageBitmap(bitmap);
				}
			});
		}
		
		
		if(appModules.size() > 11 && !appModules.get(10).getModuleObjects().isEmpty()){
			final ImageView f2_img5 = (ImageView)view.findViewById(R.id.f2_img5);
			f2_img5.setOnClickListener(this);
			String img5 = appModules.get(10).getModuleObjects().get(0).getPicUrl();
			
			loader.loadImage(img5, new LoaderCallback(){
				@Override
				public void async(Bitmap bitmap, String imageUrl) {
					f2_img5.setImageBitmap(bitmap);
				}
			});
		}
		
		//f3楼层标题
		if(appModules.size() > 12 && !appModules.get(11).getModuleObjects().isEmpty()){
			TextView f3_title = (TextView)view.findViewById(R.id.f3_title);
			f3_title.setText(appModules.get(11).getModuleObjects().get(0).getTitle());
		}
				
		//f3楼层内容
		if(appModules.size() > 13 && !appModules.get(12).getModuleObjects().isEmpty()){
			final ImageView f3_img1 = (ImageView)view.findViewById(R.id.f3_img1);
			f3_img1.setOnClickListener(this);
			TextView f3_prd1 = (TextView)view.findViewById(R.id.f3_prd1);
			TextView f3_price1 = (TextView)view.findViewById(R.id.f3_price1);
			TextView f3_unitPrice1 = (TextView)view.findViewById(R.id.f3_unitPrice1);
			Product prd1 = appModules.get(12).getModuleObjects().get(0).getProduct();
			loader.loadImage(prd1.getImage(), new LoaderCallback(){
				@Override
				public void async(Bitmap bitmap, String imageUrl) {
					f3_img1.setImageBitmap(bitmap);
				}
			});
			f3_prd1.setText(prd1.getTitle());
			f3_price1.setText("￥" + prd1.getPrice());
			f3_unitPrice1.setText("￥" + prd1.getMarketPrice());
			f3_unitPrice1.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
			
			final ImageView f3_img2 = (ImageView)view.findViewById(R.id.f3_img2);
			f3_img2.setOnClickListener(this);
			TextView f3_prd2 = (TextView)view.findViewById(R.id.f3_prd2);
			TextView f3_price2 = (TextView)view.findViewById(R.id.f3_price2);
			TextView f3_unitPrice2 = (TextView)view.findViewById(R.id.f3_unitPrice2);
			Product prd2 = appModules.get(12).getModuleObjects().get(1).getProduct();
			loader.loadImage(prd2.getImage(), new LoaderCallback(){
				@Override
				public void async(Bitmap bitmap, String imageUrl) {
					f3_img2.setImageBitmap(bitmap);
				}
			});
			f3_prd2.setText(prd2.getTitle());
			f3_price2.setText("￥" + prd2.getPrice());
			f3_unitPrice2.setText("￥" + prd2.getMarketPrice());
			f3_unitPrice2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
			
			final ImageView f3_img3 = (ImageView)view.findViewById(R.id.f3_img3);
			f3_img3.setOnClickListener(this);
			TextView f3_prd3 = (TextView)view.findViewById(R.id.f3_prd3);
			TextView f3_price3 = (TextView)view.findViewById(R.id.f3_price3);
			TextView f3_unitPrice3 = (TextView)view.findViewById(R.id.f3_unitPrice3);
			Product prd3 = appModules.get(12).getModuleObjects().get(2).getProduct();
			loader.loadImage(prd3.getImage(), new LoaderCallback(){
				@Override
				public void async(Bitmap bitmap, String imageUrl) {
					f3_img3.setImageBitmap(bitmap);
				}
			});
			f3_prd3.setText(prd3.getTitle());
			f3_price3.setText("￥" + prd3.getPrice());
			f3_unitPrice3.setText("￥" + prd3.getMarketPrice());
			f3_unitPrice3.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		}
	}
	
	
	/*====================================下拉刷新===========================================*/
	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
			// Simulates a background job.
        	if(appModules != null){
        		appModules.clear();//清空数据
        	}
			loadData();
			return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            mPullScrollView.onPullDownRefreshComplete();
            setLastUpdateTime();
        	updateUI();
        	
            super.onPostExecute(result);
        }
    }
    private void setLastUpdateTime() {
        String text = formatDateTime(System.currentTimeMillis());
        mPullScrollView.setLastUpdatedLabel(text);
    }
    
    private String formatDateTime(long time) {
        if (0 == time) {
            return "";
        }
        
        return mDateFormat.format(new Date(time));
    }


	@Override
	public void onClick(View v) {//首页点击事件
		//搜索事件
		if(v.getId() == R.id.search){
			TabListener tabListener = (TabListener) getActivity();
			tabListener.setTab(1);
		}
		
		//打开商品详情 f1
		if(v.getId() == R.id.f1_img1){
			Product product = appModules.get(4).getModuleObjects().get(0).getProduct();
			Intent intent = new Intent(getActivity(), ProductActivity.class);
			intent.putExtra("productId", product.getId());
			getActivity().startActivityForResult(intent, 500);
			return;
		}
		if(v.getId() == R.id.f1_img2){
			Product product = appModules.get(4).getModuleObjects().get(1).getProduct();
			Intent intent = new Intent(getActivity(), ProductActivity.class);
			intent.putExtra("productId", product.getId());
			startActivity(intent);
			return;
		}
		if(v.getId() == R.id.f1_img3){
			Product product = appModules.get(4).getModuleObjects().get(2).getProduct();
			Intent intent = new Intent(getActivity(), ProductActivity.class);
			intent.putExtra("productId", product.getId());
			startActivity(intent);
			return;
		}
		
		//打开商品详情 f3
		if(v.getId() == R.id.f3_img1){
			Product product = appModules.get(12).getModuleObjects().get(0).getProduct();
			Intent intent = new Intent(getActivity(), ProductActivity.class);
			intent.putExtra("productId", product.getId());
			startActivity(intent);
			return;
		}
		
		if(v.getId() == R.id.f3_img2){
			Product product = appModules.get(12).getModuleObjects().get(1).getProduct();
			Intent intent = new Intent(getActivity(), ProductActivity.class);
			intent.putExtra("productId", product.getId());
			startActivity(intent);
			return;
		}
		
		if(v.getId() == R.id.f3_img3){
			Product product = appModules.get(12).getModuleObjects().get(2).getProduct();
			Intent intent = new Intent(getActivity(), ProductActivity.class);
			intent.putExtra("productId", product.getId());
			startActivity(intent);
			return;
		}
		Toast.makeText(getActivity(), "开发中...", Toast.LENGTH_SHORT).show();
	}


	//广告轮换点击事件
	@Override
	public void onImageClick(int index, String imageUrl) {
		Toast.makeText(getActivity(), "开发中...", Toast.LENGTH_SHORT).show();
	}


	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	
	
}