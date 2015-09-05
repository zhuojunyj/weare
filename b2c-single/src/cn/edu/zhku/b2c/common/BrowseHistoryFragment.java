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
import android.view.ViewGroup;
import android.widget.ListView;
import cn.edu.zhku.b2c.R;
import cn.edu.zhku.b2c.adapter.BrowseHistoryAdapter;
import cn.edu.zhku.b2c.db.BrowseHistoryDbManager;
import cn.edu.zhku.b2c.vo.ProductVo;

public class BrowseHistoryFragment extends Fragment{

	private View view = null;
	private ListView listView = null;
	private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
	private BrowseHistoryAdapter adapter = null;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_browse_history, container, false);
		listView = (ListView)view.findViewById(R.id.browseHistory);
		init();
		return view;
	}
	
	public Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			adapter = new BrowseHistoryAdapter(data, getActivity(), listView);
			listView.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		}
	};
	
	public void init(){
		new Thread(new Runnable(){

			@Override
			public void run() {
				BrowseHistoryDbManager manager = new BrowseHistoryDbManager(getActivity());
				List<ProductVo> productVoList = manager.query();
				if(productVoList.isEmpty()){
					return;
				}
				for(ProductVo productVo : productVoList){
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("productId", productVo.getProductId());
					map.put("image", productVo.getImage());
					map.put("productNm", productVo.getProductNm());
					map.put("price", productVo.getPrice());
					map.put("sellingPoint", productVo.getSellingPoint());
					data.add(map);
				}
				handler.sendEmptyMessage(1);
			}
			
		}).start();
	}

	
}
