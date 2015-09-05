package cn.edu.zhku.b2c.cate;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import cn.edu.zhku.b2c.R;
import cn.edu.zhku.b2c.common.ShoppingCartBarFragment;
import cn.edu.zhku.b2c.common.SpecItemFragment;

public class MultiSpecActivity extends Activity {
	private Map<String, Object> data = new HashMap<String, Object>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_multi_spec);
		init();
	}
	
	public void init(){
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		data.put("id", bundle.getInt("id"));
		data.put("productNm", bundle.getString("productNm"));
		data.put("sellingPoint", bundle.getString("sellingPoint"));
		data.put("image", bundle.getString("image"));
		data.put("price", bundle.getString("price"));
		data.put("marketPrice", bundle.getString("marketPrice"));
		data.put("appSKUs", bundle.getString("appSKUs"));
		System.out.println("************************");
		System.out.println(bundle.getString("appSKUs"));
		addSpecs();
		addShoppingCartBar();
	}
	
	public void addSpecs(){
		SpecItemFragment item = new SpecItemFragment();
		SpecItemFragment item1 = new SpecItemFragment();
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.add(R.id.specs,item);
		transaction.add(R.id.specs, item1);
		transaction.commit();
	}
	
	public void addShoppingCartBar(){
		ShoppingCartBarFragment shoppingCartBarFragment = new ShoppingCartBarFragment();
		FragmentManager fragmentManager = getFragmentManager() ;
		FragmentTransaction transaction = fragmentManager.beginTransaction() ;
		transaction.add(R.id.shopping_cart,shoppingCartBarFragment) ;
		transaction.commit() ;
	}
}
