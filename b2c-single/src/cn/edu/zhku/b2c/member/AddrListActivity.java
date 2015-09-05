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
		
		//����titleBar
		HeaderFragment headerFragment = new HeaderFragment() ;
		//��ֵ �� (recommend)
		Bundle args = new Bundle() ;
		args.putString("title", "�ջ���ַ����");
		headerFragment.setArguments(args);
		
		//�����ַ
		AddressFragment addr1 = new AddressFragment() ;
		Bundle addrArgs = new Bundle() ;
		addrArgs.putString("name", "leona");
		addrArgs.putString("tel", "13800138000");
		addrArgs.putString("address", "�㶫ʡ�����л��Ƕ�·");
		addr1.setArguments(addrArgs);
		
		AddressFragment addr2 = new AddressFragment() ;
		Bundle addrArgs1 = new Bundle() ;
		addrArgs1.putString("name", "do you know");
		addrArgs1.putString("tel", "13800138001");
		addrArgs1.putString("address", "�㶫ʡ�����л��Ƕ�·73��");
		addr2.setArguments(addrArgs1);
		
		FragmentManager fragmentManager = getFragmentManager() ;
		FragmentTransaction transaction = fragmentManager.beginTransaction() ;
		transaction.add(R.id.addrList,headerFragment) ;
		transaction.add(R.id.addr_add,addr1) ;
		transaction.add(R.id.addr_add,addr2) ;
		transaction.commit() ;
	}
	
	//�����ջ���ַ
	public void openAddReceiver(View view){
		Intent intent = new Intent(AddrListActivity.this , NewAddressActivity.class);
		startActivity(intent);
	}
}
