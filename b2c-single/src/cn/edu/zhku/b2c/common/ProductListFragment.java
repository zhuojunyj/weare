package cn.edu.zhku.b2c.common;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import cn.edu.zhku.b2c.R;
import cn.edu.zhku.b2c.adapter.ProductListAdapter;
import cn.edu.zhku.b2c.cate.ProductActivity;
import cn.edu.zhku.b2c.cate.ProductListActivity;
import cn.edu.zhku.b2c.cate.ProductListActivity.FilterCallback;
import cn.edu.zhku.b2c.cutom.CustomAlertDialog;
import cn.edu.zhku.b2c.cutom.CustomProgressDialog;
import cn.edu.zhku.b2c.model.ProductList;
import cn.edu.zhku.b2c.util.ConfigParser;
import cn.edu.zhku.b2c.util.HttpUtil;

public class ProductListFragment extends Fragment implements OnItemClickListener, OnScrollListener, FilterCallback{
	private ListView listView = null ;
	private List<Map<String, Object>> productList = new ArrayList<Map<String, Object>>() ;
	private ProductListAdapter adapter = null ;
	private int page = 1 ;
	private int visibleCount = 0 ;  
    private int visibleLastIndex = 0 ;
    private int categoryId ;
	private CustomAlertDialog dialog = null ;
	private CustomProgressDialog progressDialog = null ;
	private int totalCount = 0 ;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.product_list_listview, null) ;
		if (progressDialog == null) {
			progressDialog = CustomProgressDialog.createProgressDialog(
					getActivity(), true);
			progressDialog.setMessage("加载中...");
			progressDialog.show();
		}
		
		Bundle args = getArguments() ;
		categoryId = args.getInt("categoryId") ;
		
		listView = (ListView) view.findViewById(R.id.prd_listView) ;
		loadData(categoryId, null, null, null, false) ;
		listView.setOnItemClickListener(this);
		listView.setOnScrollListener(this);
		return view ;
	}

	private Handler handler = new Handler(){
		public void handleMessage(Message message) {
			if(message.what == 0){
				adapter = new ProductListAdapter(productList, getActivity(), listView) ;
				listView.setAdapter(adapter);
				adapter.notifyDataSetChanged();
			}
			
			if(message.what == 1){
				adapter.notifyDataSetChanged();
			}
			progressDialog.hide();
		}
	} ;
	public void loadData(final int categoryId, final String keyword, final String q, final String order, final boolean isRefresh){
		progressDialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("categoryId", categoryId);
				params.put("keyword", keyword);
				params.put("q", q);
				params.put("order", order);
				params.put("page", page);
				String webRoot = ConfigParser.loadConfig(getActivity(),
						"webRoot");
				HttpUtil.send(webRoot+"/app/product/list.json?rootNode=rootNode", HttpUtil.POST, params, new HttpUtil.HttpCallback() {
					
					@Override
					public void onSuccess(String responseText) {
						productList.clear();
						String result = JSON.parseObject(responseText).getString("rootNode");
						List<ProductList> productArray = JSON.parseArray(result, ProductList.class);
						for(ProductList product : productArray){
							Map<String, Object> map = new HashMap<String, Object>() ;
							map.put("image", product.getImage()) ;
							map.put("id", product.getId()) ;
							map.put("title", product.getTitle()) ;
							map.put("salesVolume", product.getSalesVolume()) ;
							map.put("descr", product.getSellingPoint()) ;
							map.put("marketPrice", product.getMarketPrice()) ;
							productList.add(map) ;
							if(isRefresh){
								handler.sendEmptyMessage(1);
							}else{
								handler.sendEmptyMessage(0);
							}
						}
						
					}
					
					@Override
					public void onError(String errorText) {
						if (dialog == null) {
							dialog = new CustomAlertDialog(getActivity());
							Bundle bundle = new Bundle();
							bundle.putString("message", errorText);
							dialog.setArguments(bundle);
							dialog.show(getActivity().getFragmentManager(),
											"error");
						}
					}
				});
				
				//loadDataToList(response, isRefresh);
			}
		}).start();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		listView = null ;
		productList = null ;
		adapter = null ;
		if(progressDialog != null){
			progressDialog.dismiss();
		}
		if(dialog != null){
			dialog.dismiss();
		}
	}
	
	//listview event handlers
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(getActivity(), ProductActivity.class);
		Map<String, Object> temp = productList.get((int)arg3);
		int productId = (Integer)temp.get("id");
		intent.putExtra("productId", productId);
		startActivity(intent);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		visibleCount = visibleItemCount;  
        visibleLastIndex = firstVisibleItem + visibleItemCount ; 
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		int itemsLastIndex = adapter.getCount() ; 
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
				&& visibleLastIndex == itemsLastIndex && visibleLastIndex < totalCount) {
			page ++ ;
			loadData(categoryId, null, null, null, true) ;
		}
		
	}

	@Override
	public void onFilterFinish(String q) {
		loadData(categoryId, null, q, null, true) ;
	}

	@Override
	public void onOrderChange(String order) {
		loadData(categoryId, null, order, null, true) ;
	}
	
}
