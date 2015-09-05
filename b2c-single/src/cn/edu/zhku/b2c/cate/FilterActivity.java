package cn.edu.zhku.b2c.cate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import cn.edu.zhku.b2c.R;
import cn.edu.zhku.b2c.common.HeaderFragment;
import cn.edu.zhku.b2c.common.HeaderFragment.RightNavCallback;
import cn.edu.zhku.b2c.cutom.CustomAlertDialog;
import cn.edu.zhku.b2c.cutom.CustomProgressDialog;
import cn.edu.zhku.b2c.popup.PopupActivity;
import cn.edu.zhku.b2c.util.ConfigParser;
import cn.edu.zhku.b2c.util.HttpUtil;
import cn.edu.zhku.b2c.util.HttpUtil.HttpCallback;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class FilterActivity extends Activity implements RightNavCallback, OnItemClickListener, OnClickListener{
	private ListView listview = null ;
	private List<Map<String, Object>> data = null ;
	private CustomAlertDialog dialog = null ;
	private CustomProgressDialog loadingDialog = null;
	private SimpleAdapter adapter = null ;
	private Map<String,List<Map<String, Object>>> paramMap = new HashMap<String,List<Map<String, Object>>>();
	private Map<String, Object> resultMap = new HashMap<String, Object>();
	private String id = null;
	private int index = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filter);
		addHeader() ;
		listview = (ListView) findViewById(R.id.filter_list);
		
		LayoutInflater inflater = LayoutInflater.from(FilterActivity.this);
		View footerView = inflater.inflate(R.layout.filter_listview_footer, null);
		listview.addFooterView(footerView);
		
		Button resetBtn = (Button)footerView.findViewById(R.id.reset);
		resetBtn.setOnClickListener(this);
		
		data = new ArrayList<Map<String, Object>>();
		dialog = new CustomAlertDialog(FilterActivity.this);
		if(loadingDialog == null){
			loadingDialog = CustomProgressDialog.createProgressDialog(FilterActivity.this, false) ;
			loadingDialog.setMessage("加载中...") ;
    	}
		loadingDialog.show();
		loadData();
		
		listview.setOnItemClickListener(this);
	}
	
	public void addHeader(){
		HeaderFragment headerFragment = new HeaderFragment() ;
		Bundle args = new Bundle() ;
		args.putString("title", "筛选");
		args.putString("rigNavTitle", "完成");
		headerFragment.setArguments(args);
		FragmentManager fragmentManager = getFragmentManager() ;
		FragmentTransaction transaction = fragmentManager.beginTransaction() ;
		transaction.add(R.id.filter,headerFragment) ;
		transaction.commit() ;
	}
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(msg.what ==0){
				adapter = new SimpleAdapter(FilterActivity.this, data, 
						R.layout.filter_list_item, 
						new String[]{"name"}, 
						new int[]{R.id.facet_nm}) ;
				listview.setAdapter(adapter);
				adapter.notifyDataSetChanged();
			}
			if(msg.what == 1){
				View view = listview.getChildAt(msg.arg1);
				TextView textView = (TextView)view.findViewById(R.id.haha);
				textView.setText((CharSequence) msg.obj);
				textView.setTextColor(getResources().getColor(R.color.text_color_red));
			}
			loadingDialog.hide();
		}
		
	};
	public void loadData(){
		
		new Thread(new Runnable(){
			@Override
			public void run() {
				int categoryId = getIntent().getIntExtra("categoryId", 4);
				String webRoot = ConfigParser.loadConfig(FilterActivity.this, "webRoot");
				Map<String, Object> sendData = new HashMap<String, Object>();
				sendData.put("categoryId", categoryId);
				sendData.put("rootNode", "root");
				HttpUtil.send(webRoot+"/app/product/facet.json", "post", sendData, new HttpCallback(){

					@Override
					public void onError(String errorText) {
						Bundle bundle = new Bundle();
						bundle.putString("message", errorText);
						dialog.setArguments(bundle);
						dialog.show(getFragmentManager(), "error");
					}

					@Override
					public void onSuccess(String responseText) {
						JSONObject result = JSON.parseObject(responseText);
						JSONArray jsonArr = JSON.parseArray(result.getString("root"));
						int len = jsonArr.size();
						for(int i = 0 ; i < len ; i++){
							JSONObject obj = jsonArr.getJSONObject(i);
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("name", obj.getString("name"));
							map.put("id", obj.getString("id"));
							data.add(map);
							
							JSONArray array = JSON.parseArray(obj.getString("values"));
							int arrLen = array.size();
							List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();;
							for(int j = 0 ; j < arrLen ; j ++ ){
								JSONObject valueObj = array.getJSONObject(j);
								Map<String, Object> valueMap = new HashMap<String, Object>();
								valueMap.put(valueObj.getString("name"), valueObj.getString("value"));
								list.add(valueMap);
							}
							paramMap.put(obj.getString("id"), list);
							list = null;
						}
						Message msg = Message.obtain();
						msg.what = 0;
						handler.sendMessage(msg);
					}
				});
			}
		}).start();
	}

	/**
	 * �ش�ɸѡ������productlist
	 */
	@Override
	public void onRigNavClick(View v) {
		int len = resultMap.size();
		if(len == 0){
			finish();
		}else{
			String q = "";
			for(Map.Entry<String, Object> entry : resultMap.entrySet()){
				q = q + entry.getKey() + ":" + entry.getValue() + ";";
			}
			Intent data = new Intent();
			data.putExtra("q", q);
			setResult(200, data);
			finish();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
//		if(dialog != null){
//			dialog.dismiss();
//		}
		if(loadingDialog != null){
			loadingDialog.dismiss();
		}
		listview = null ;
		data = null ;
//		dialog = null ;
//		loadingDialog = null;
		adapter = null ;
		paramMap = null;
		resultMap = null;
		id = null;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		id = (String) data.get(arg2).get("id");
		index = (int)arg3;
		List<Map<String, Object>> values = paramMap.get(id);
		Intent intent = new Intent(FilterActivity.this, PopupActivity.class);
		intent.putExtra("list", (Serializable)values);//������С������ֵ�Ƚ�ֱ��;��������Ӧ�û��浽applicationContext�У��ٴ�����һ��activity��ȡ����
		startActivityForResult(intent, 100);
	}

	//�ش�ֵ����ui
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 100 && resultCode == 200){
			String value = data.getStringExtra("value");
			final String selectedItem = data.getStringExtra("key");
			resultMap.put(id, value);
			new Thread(new Runnable(){
				@Override
				public void run() {
					int firstVisibleIndex = listview.getFirstVisiblePosition();
					if(index - firstVisibleIndex >= 0){
						Message msg = Message.obtain();
						msg.what = 1;
						msg.obj = selectedItem;
						msg.arg1 = index - firstVisibleIndex;
						handler.sendMessage(msg);
					}
					
				}
			}).start();
		}
	}

	//��������
	@Override
	public void onClick(View v) {
		int len = adapter.getCount() - 1 ;
		for(int i = 0 ; i < len ; i ++ ){
			View view = listview.getChildAt(i);
			TextView textView = (TextView)view.findViewById(R.id.haha);
			if(textView != null){
				textView.setText("全部");
				textView.setTextColor(getResources().getColor(R.color.text_color));
			}
		}
//		adapter.notifyDataSetChanged();
	}
	
	
	
}
