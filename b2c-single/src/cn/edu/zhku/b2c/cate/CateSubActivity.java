package cn.edu.zhku.b2c.cate;

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
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import cn.edu.zhku.b2c.R;
import cn.edu.zhku.b2c.common.HeaderFragment;
import cn.edu.zhku.b2c.db.CategoryDbManager;
import cn.edu.zhku.b2c.vo.CategoryVo;

public class CateSubActivity extends Activity {
	private ListView secListView = null ;
	private ListView thrListView = null ;
	private List<Map<String, Object>> secData = new ArrayList<Map<String, Object>>() ;
	private List<Map<String, Object>> thrData = new ArrayList<Map<String, Object>>() ;
	private SimpleAdapter secAdapter = null ;
	private SimpleAdapter thrAdapter = null ;
	private CategoryDbManager mgr = null ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cate_sub_actvity);
		mgr = new CategoryDbManager(CateSubActivity.this) ;
		int categoryId = getIntent().getIntExtra("categoryId", 0) ;
		addHeader() ;
		initUI() ;
		load(categoryId) ;
		secListView.setOnItemClickListener(new SecListener());
		thrListView.setOnItemClickListener(new ThrListener());
	}
	
	public void addHeader(){
		String title = getIntent().getStringExtra("title") ;
		HeaderFragment headerFragment = new HeaderFragment() ;
		Bundle args = new Bundle() ;
		args.putString("title", title);
		headerFragment.setArguments(args);
		FragmentManager fragmentManager = getFragmentManager() ;
		FragmentTransaction transaction = fragmentManager.beginTransaction() ;
		transaction.add(R.id.cate_sub,headerFragment) ;
		transaction.commit() ;
	}
	
	//��ʼ��UI
	public void initUI(){
		secListView = (ListView) findViewById(R.id.sec_cate) ;
		thrListView = (ListView) findViewById(R.id.thr_cate) ;
	}
	
	//�������
	public void load(final int categoryId){
		new Thread(new Runnable(){
			@Override
			public void run() {
				List<CategoryVo> secCateList = mgr.getByParentId(categoryId) ;
				int defaultCateId = secCateList.get(0).getCategoryId() ;
				//����Ŀ¼
				for(CategoryVo cate : secCateList){
					Map<String, Object> map = new HashMap<String, Object>() ;
					map.put("categoryId", cate.getCategoryId()) ;
					map.put("title", cate.getTitle()) ;
					secData.add(map) ;
				}
				
				//����Ŀ¼
				List<CategoryVo> thrCateList = mgr.getByParentId(defaultCateId) ;
				for(CategoryVo cate : thrCateList){
					Map<String, Object> map = new HashMap<String, Object>() ;
					map.put("categoryId", cate.getCategoryId()) ;
					map.put("title", cate.getTitle()) ;
					thrData.add(map) ;
				}
				
				//����ui
				Message msg = Message.obtain() ;
				msg.what = 0 ;
				handler.sendMessage(msg) ;
			}
		}).start();
	}
	
	private Handler handler = new Handler(){
		public void handleMessage(Message message) { 
			int type = message.what ;
			//��ʼ������
			if(type == 0){
				//����
				secAdapter = new SimpleAdapter(CateSubActivity.this, secData, 
						R.layout.category_sec_list_item, 
						new String[]{"title"}, 
						new int[]{R.id.sec_title}) ;
				
				secListView.setAdapter(secAdapter);
				secAdapter.notifyDataSetChanged();
				//����
				thrAdapter = new SimpleAdapter(CateSubActivity.this, thrData,
						R.layout.category_thr_list_item,
						new String[]{"title"},
						new int[]{R.id.third_title}) ;
				thrListView.setAdapter(thrAdapter);
				thrAdapter.notifyDataSetChanged();
			}
			if(type == 1){
				thrAdapter.notifyDataSetChanged();
			}
		}
	} ;
	
	
	private class SecListener implements OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			int count = secListView.getChildCount() ;
			for(int i = 0 ; i < count ; i ++){
				if(i == position){
					secListView.getChildAt(i).setBackgroundResource(R.drawable.sec_item_selected);
				}else{
					secListView.getChildAt(i).setBackgroundResource(R.drawable.sec_item_normal);
				}
			}
			final int categoryId = (Integer) secData.get(position).get("categoryId") ;
			new Thread(new Runnable(){
				@Override
				public void run() {
					// TODO Auto-generated method stub
					List<CategoryVo> cateList = mgr.getByParentId(categoryId) ;
					//���ԭ��������
					thrData.clear();
					for(CategoryVo cate : cateList){
						Map<String, Object> map = new HashMap<String, Object>() ;
						map.put("title", cate.getTitle()) ;
						map.put("categoryId", cate.getCategoryId()) ;
						thrData.add(map) ;
					}
					Message msg = Message.obtain() ;
					msg.what = 1 ;
					handler.sendMessage(msg) ;
				}
			}).start();
		}
	}
	
	private class ThrListener implements OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Intent intent = new Intent(CateSubActivity.this, ProductListActivity.class) ;
			Map<String, Object> map = thrData.get(arg2) ;
			String title = (String) map.get("title") ;
			int categoryId = (Integer) map.get("categoryId") ;
			intent.putExtra("title", title) ;
			intent.putExtra("categoryId", categoryId) ;
			startActivity(intent);
		}
	}
}
