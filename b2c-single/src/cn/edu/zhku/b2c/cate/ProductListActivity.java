package cn.edu.zhku.b2c.cate;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.edu.zhku.b2c.R;
import cn.edu.zhku.b2c.common.HeaderFragment;
import cn.edu.zhku.b2c.common.ProductListFragment;

public class ProductListActivity extends Activity implements OnClickListener{
	private int categoryId = 0;
	private FilterCallback callback = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_list);
		addHeader() ;
		addList() ;
		setEventListeners();
	}
	
	//事件
	public void setEventListeners(){
		RelativeLayout defaultRank = (RelativeLayout)findViewById(R.id.default_rank);
		defaultRank.setOnClickListener(this);
		
		RelativeLayout salesRank = (RelativeLayout)findViewById(R.id.salesvolume);
		salesRank.setOnClickListener(this);
		
		RelativeLayout filterRank = (RelativeLayout)findViewById(R.id.filter);
		filterRank.setOnClickListener(this);
	}
	
	public void resetTab(){
		TextView defaultTip = (TextView)findViewById(R.id.defaultTip);
		defaultTip.setTextColor(getResources().getColor(R.color.text_color));
		
		TextView salesvolumeTip = (TextView)findViewById(R.id.salesvolumeTip);
		salesvolumeTip.setTextColor(getResources().getColor(R.color.text_color));
		
		TextView filterTip = (TextView)findViewById(R.id.filterTip);
		filterTip.setTextColor(getResources().getColor(R.color.text_color));
	}
	
	public void addHeader(){
		String title = getIntent().getStringExtra("title") ;
		HeaderFragment headerFragment = new HeaderFragment() ;
		Bundle args = new Bundle() ;
		args.putString("title", title);
		headerFragment.setArguments(args);
		FragmentManager fragmentManager = getFragmentManager() ;
		FragmentTransaction transaction = fragmentManager.beginTransaction() ;
		transaction.add(R.id.product_list,headerFragment) ;
		transaction.commit() ;
	}
	
	public void addList(){
		ProductListFragment productListFragment = new ProductListFragment() ;
		callback = productListFragment;
		categoryId = getIntent().getIntExtra("categoryId", 4) ;
		Bundle args = new Bundle() ;
		args.putInt("categoryId", categoryId);
		String keyword = getIntent().getStringExtra("keyword");
		args.putString("keyword", keyword);
		productListFragment.setArguments(args);
		FragmentManager fragmentManager = getFragmentManager() ;
		FragmentTransaction transaction = fragmentManager.beginTransaction() ;
		transaction.add(R.id.prd_list, productListFragment) ;
		transaction.commit() ;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		//筛选
		if(id == R.id.filter){
			Intent intent = new Intent(ProductListActivity.this, FilterActivity.class);
			intent.putExtra("categoryId", categoryId);
			startActivityForResult(intent, 100);
		}
		
		//综合排行
		if(id == R.id.default_rank){
			resetTab();
			TextView defaultTip = (TextView)findViewById(R.id.defaultTip);
			defaultTip.setTextColor(getResources().getColor(R.color.text_color_red));
			callback.onOrderChange("lastOnSaleDate,desc");
		}
		
		//销量优先
		if(id == R.id.salesvolume){
			resetTab();
			TextView salesvolumeTip = (TextView)findViewById(R.id.salesvolumeTip);
			salesvolumeTip.setTextColor(getResources().getColor(R.color.text_color_red));
			callback.onOrderChange("salesVolume,desc");
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(100 == requestCode && 200 == resultCode){
			resetTab();
			TextView filterTip = (TextView)findViewById(R.id.filterTip);
			filterTip.setTextColor(getResources().getColor(R.color.text_color_red));
			String q = data.getStringExtra("q");
			callback.onFilterFinish(q);
			
		}
	}
	
	//�ص�fragmentˢ���б�ҳ
	public interface FilterCallback{
		public void onFilterFinish(String q);
		
		public void onOrderChange(String order);
	}
	
}
