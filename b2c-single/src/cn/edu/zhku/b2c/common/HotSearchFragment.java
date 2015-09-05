package cn.edu.zhku.b2c.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import cn.edu.zhku.b2c.R;
import cn.edu.zhku.b2c.cutom.CustomViewGroup;
import cn.edu.zhku.b2c.util.ConfigParser;
import cn.edu.zhku.b2c.util.HttpUtil;
import cn.edu.zhku.b2c.util.HttpUtil.HttpCallback;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class HotSearchFragment extends Fragment{
	
	private View view = null;
	
	private List<String> hotSearchList = new ArrayList<String>();
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		view = inflater.inflate(R.layout.fragment_hot_search, container, false);
		initData();
		return view;
	}
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			for(String keyword : hotSearchList){
				Button btn = new Button(getActivity());
				btn.setText(keyword);
				btn.setBackgroundResource(android.R.color.transparent);
				CustomViewGroup viewGroup = (CustomViewGroup) view.findViewById(R.id.hotSearch);
				btn.setOnClickListener(new ClickListener());
				viewGroup.addView(btn);
			}
		}
		
	};
	
	public void initData(){
		new Thread(new Runnable(){
			@Override
			public void run() {
				String webRoot = ConfigParser.loadConfig(getActivity(), "webRoot");
				Map<String, Object> sendData = new HashMap<String, Object>();
				sendData.put("pageNumber", 1);
				sendData.put("pageSize", 1000);
				HttpUtil.send(webRoot+"/app/product/hotKeywordPage.json", HttpUtil.GET, sendData, new HttpCallback(){
					@Override
					public void onError(String errorText) {
						System.out.println("*************************");
						System.out.println(errorText);
					}
					@Override
					public void onSuccess(String responseText) {
						JSONObject obj = JSON.parseObject(responseText);
						JSONArray array = obj.getJSONArray("result");
						for(int i = 0 ; i < array.size() ; i ++){
							hotSearchList.add(array.getString(i));
						}
						//¸üÐÂUI
						handler.sendEmptyMessage(1);
					}
					
				});
			}
		}).start();
	}
	
	public class ClickListener implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			System.out.println("===========================");
		}
		
	}
}
