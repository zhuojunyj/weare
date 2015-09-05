package cn.edu.zhku.b2c.member;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import cn.edu.zhku.b2c.R;
import cn.edu.zhku.b2c.common.AddressFragment;
import cn.edu.zhku.b2c.common.HeaderFragment;

public class AddrListActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addr_list);
		
		//引入titleBar
		HeaderFragment headerFragment = new HeaderFragment() ;
		//传值 二 (recommend)
		Bundle args = new Bundle() ;
		args.putString("title", "收货地址管理");
		headerFragment.setArguments(args);
		
		//加入地址
		AddressFragment addr1 = new AddressFragment() ;
		Bundle addrArgs = new Bundle() ;
		addrArgs.putString("name", "leona");
		addrArgs.putString("tel", "13800138000");
		addrArgs.putString("address", "广东省兴宁市环城东路");
		addr1.setArguments(addrArgs);
		
		AddressFragment addr2 = new AddressFragment() ;
		Bundle addrArgs1 = new Bundle() ;
		addrArgs1.putString("name", "do you know");
		addrArgs1.putString("tel", "13800138001");
		addrArgs1.putString("address", "广东省兴宁市环城东路73号");
		addr2.setArguments(addrArgs1);
		
		FragmentManager fragmentManager = getFragmentManager() ;
		FragmentTransaction transaction = fragmentManager.beginTransaction() ;
		transaction.add(R.id.addrList,headerFragment) ;
		transaction.add(R.id.addr_add,addr1) ;
		transaction.add(R.id.addr_add,addr2) ;
		transaction.commit() ;
	}
	
	//新增收货地址
	public void openAddReceiver(View view){
		Intent intent = new Intent(AddrListActivity.this , NewAddressActivity.class);
		startActivity(intent);
	}
}
