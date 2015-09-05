package cn.edu.zhku.b2c.member;

import cn.edu.zhku.b2c.R;
import cn.edu.zhku.b2c.common.HeaderFragment;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class NewAddressActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_address);
		
		//����titleBar
		HeaderFragment headerFragment = new HeaderFragment() ;
		//��ֵ �� (recommend)
		Bundle args = new Bundle() ;
		args.putString("title", "������ַ");
		headerFragment.setArguments(args);
		
		FragmentManager fragmentManager = getFragmentManager() ;
		FragmentTransaction transaction = fragmentManager.beginTransaction() ;
		transaction.add(R.id.orderList,headerFragment) ;
		transaction.commit() ;
	}
}
