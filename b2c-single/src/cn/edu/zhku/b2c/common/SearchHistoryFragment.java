package cn.edu.zhku.b2c.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import cn.edu.zhku.b2c.R;

public class SearchHistoryFragment extends Fragment{

	private ListView listView = null;
	private SimpleAdapter adapter = null;
	private List<Map<String, String>> data = new ArrayList<Map<String, String>>();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_search_history, container, false);
		listView = (ListView)view.findViewById(R.id.listView1);
		init();
		return view;
	}
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			adapter = new SimpleAdapter(getActivity(), data, R.layout.fragment_search_history_item, 
					new String[]{"key"}, new int[]{R.id.searchHistory});
			listView.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		}
	};
	
	public void init(){
		new Thread(new Runnable(){
			@Override
			public void run() {
				SharedPreferences searchHistory = getActivity().getSharedPreferences("searchHistory", 0);
				String history = searchHistory.getString("searchHistory", "");
				if(history.isEmpty()){
					return;
				}
				String[] keywords = history.split(",");
				for(String keyword : keywords){
					Map<String, String> map = new HashMap<String, String>();
					map.put("key", keyword);
					data.add(map);
				}
				handler.sendEmptyMessage(1);
			}
		}).start();
	}
	
	
	
}
