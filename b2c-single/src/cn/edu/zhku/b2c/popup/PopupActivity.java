package cn.edu.zhku.b2c.popup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import cn.edu.zhku.b2c.R;

public class PopupActivity extends Activity implements OnItemClickListener{

	private ListView listview = null;
	private List<Map<String, Object>> data = null;
	private List<String> list = new ArrayList<String>();
	private String[] array = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_popup);
		LayoutInflater inflater = LayoutInflater.from(PopupActivity.this);
		listview = (ListView) findViewById(R.id.popup_list);
		View headerView = inflater.inflate(R.layout.popup_listview_header, null);
		listview.addHeaderView(headerView, null, false);
		loadData();
		listview.setOnItemClickListener(this);
	}
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			listview.setAdapter(new ArrayAdapter<Object>(PopupActivity.this, 
					android.R.layout.simple_list_item_single_choice, 
					list.toArray()));
			listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		}
	};
	
	public void loadData(){
		new Thread(new Runnable(){
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				Intent intent = getIntent();
				data = (List<Map<String, Object>>) intent.getSerializableExtra("list");
				for(int i = 0 ; i < data.size() ; i ++){
					for(String key : data.get(i).keySet()){
						list.add(key);
					}
				}
				Message message = Message.obtain();
				handler.sendMessage(message);
			}
		}).start();
		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		int location = (int)arg3;
		Map<String, Object> map = data.get(location);
		String key = list.get(location);
		String value = (String)map.get(list.get(location));
		Intent intent = new Intent();
		intent.putExtra("value", value);
		intent.putExtra("key", key);
		setResult(200, intent);
		finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		listview = null;
		data = null;
		list = null;
		array = null;
	}
	
	
}
