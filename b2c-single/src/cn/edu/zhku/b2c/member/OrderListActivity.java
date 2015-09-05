package cn.edu.zhku.b2c.member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import cn.edu.zhku.b2c.R;
import cn.edu.zhku.b2c.adapter.OrderListAdapter;
import cn.edu.zhku.b2c.common.HeaderFragment;
import cn.edu.zhku.b2c.cutom.CustomAlertDialog;
import cn.edu.zhku.b2c.cutom.CustomProgressDialog;
import cn.edu.zhku.b2c.util.ConfigParser;
import cn.edu.zhku.b2c.util.HttpUtil;
import cn.edu.zhku.b2c.util.JsonUtil;

public class OrderListActivity extends Activity {
	private Integer pageNum = 1 ;//分页
	private CustomProgressDialog progressDialog = null ;
	private CustomAlertDialog alertDialog = null ;
	private ListView listView ;
	private int totalCount = 0;//ListView item个数
	private int lastVisibleItem = 0;//最后可见的Item
	private View footer = null ;//底部刷新footer
	private boolean isFirstLoad = true ;
	private List<Map<String, Object>> dataContainer = new ArrayList<Map<String, Object>>() ;//装载listview数据
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_list);
		initUI() ;
		
		initListView() ;
		
	}
	
	public void initUI(){
		if(progressDialog == null){
    		progressDialog = CustomProgressDialog.createProgressDialog(OrderListActivity.this , false) ;
    	}
		//引入titleBar
		HeaderFragment headerFragment = new HeaderFragment() ;
		//传值 二 (recommend)
		Bundle args = new Bundle() ;
		args.putString("title", "订单列表");
		headerFragment.setArguments(args);
		
		FragmentManager fragmentManager = getFragmentManager() ;
		FragmentTransaction transaction = fragmentManager.beginTransaction() ;
		transaction.add(R.id.recevier_add,headerFragment) ;
		transaction.commit() ;
	}
	
	public void initListView(){
		//listview header
		listView = (ListView) findViewById(R.id.order_listview) ;
		LayoutInflater inflater = LayoutInflater.from(OrderListActivity.this) ;
		View headerView = inflater.inflate(R.layout.order_list_header, null) ;
		
		//listview footer
		footer = inflater.inflate(R.layout.list_refresh_footer, null) ;
		footer.setVisibility(View.GONE);
		listView.addHeaderView(headerView);
		listView.addFooterView(footer);
		
		
		//获取订单列表数据
		String webRoot = ConfigParser.loadConfig(OrderListActivity.this,"webRoot") ;
		new FetchOrderList().execute(webRoot + "/app/member/order.json") ;
		OrderListAdapter adapter = new OrderListAdapter(dataContainer,OrderListActivity.this) ;
		listView.setAdapter(adapter);
		listView.setOnScrollListener(new MyScrollListener()) ;
	}
	
	//AsyncTask获取订单数据
	public class FetchOrderList extends AsyncTask<String, Void, String>{
	    @Override  
	    protected void onPreExecute() { 
	    	if(isFirstLoad){
	    		progressDialog.show() ;
	    		isFirstLoad = false ;
	    	}
	    	
	    } 
	    
		@Override
		protected String doInBackground(String... params) {
			int type = getIntent().getExtras().getInt("type") ;//订单类型参数  全部订单-0, 待付款-1, 待发货-2,  待收货-3, 待评论-4
			Map<String, Object> data = new HashMap<String, Object>() ;
			data.put("orderType", type) ;
			data.put("pageNumber", pageNum) ;
			String result = HttpUtil.httpPost(params[0], data, "UTF-8") ;
			return result ;
		}
		
		protected void onPostExecute(String result) {
			if(progressDialog != null){
				progressDialog.hide();
			}
			//解析json
			String error = JsonUtil.parseError(result);
			if(error != null){
				alertDialog = new CustomAlertDialog(OrderListActivity.this) ;
				Bundle args = new Bundle() ;
				args.putString("message", error);
				alertDialog.setArguments(args);
				alertDialog.show(getFragmentManager(),"error") ;
			}
			//装载listview数据 TODO
			
		}
	}
	
	
	//监听listview的滑动事件，实现分页
	private class MyScrollListener implements AbsListView.OnScrollListener{

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			 lastVisibleItem = firstVisibleItem + visibleItemCount;
		    totalCount = totalItemCount;
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			//当滑动到底端，并滑动状态为 not scrolling
		    if(lastVisibleItem == totalCount && scrollState == SCROLL_STATE_IDLE){
		        //设置可见
		        footer.setVisibility(View.VISIBLE);
		        //加载数据 TODO
		        pageNum ++ ;
		        String webRoot = ConfigParser.loadConfig(OrderListActivity.this,"webRoot") ;
		        new FetchOrderList().execute(webRoot + "/app/member/order.json") ;
		    }
		}
		
	}
}
