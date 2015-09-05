package cn.edu.zhku.b2c;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import cn.edu.zhku.b2c.common.HeaderFragment.RightNavCallback;
import cn.edu.zhku.b2c.i.TabListener;

public class MainActivity extends FragmentActivity implements RightNavCallback, TabListener{

	private ViewPager myViewPager ;
	private FragmentPagerAdapter adapter ;
	private List<Fragment> fragmentList ;
	
	//�ײ�tab
	private ImageView home ;
	private ImageView search ;
	private ImageView category ;
	private ImageView shoppingCart ;
	private ImageView member ;
	
	private OnLoginListener LoginListener = null;
	private OnLoginListener shoppingCartLoginListener = null;
	private OnRightButtonListener onRightButtonListener = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		myViewPager = (ViewPager)findViewById(R.id.myviewpager) ;
		
		//��ʼ��fragment
		initView() ;
		
		adapter = new FragmentPagerAdapter(getSupportFragmentManager()){

			@Override
			public int getCount()
			{
				return fragmentList.size();
			}

			@Override
			public Fragment getItem(int arg0)
			{
				return fragmentList.get(arg0);
			}
		};
		
		myViewPager.setAdapter(adapter) ;
		
		myViewPager.setOnPageChangeListener(new PageChangeListener()) ;
		
		//tab �¼�����
		home.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				resetTabs() ;
				home.setImageResource(R.drawable.home_selected);
				myViewPager.setCurrentItem(0, true);
			}
		});
		
		search.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				resetTabs() ;
				search.setImageResource(R.drawable.search_selected);
				myViewPager.setCurrentItem(1, true);
			}
		});
		
		category.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				resetTabs() ;
				category.setImageResource(R.drawable.category_selected);
				myViewPager.setCurrentItem(2, true);
			}
		});
		
		shoppingCart.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				resetTabs() ;
				shoppingCart.setImageResource(R.drawable.shoppingcart_selected);
				myViewPager.setCurrentItem(3, true);
			}
		});
		
		
		member.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				resetTabs() ;
				member.setImageResource(R.drawable.member_selected);
				myViewPager.setCurrentItem(4, true);
			}
		});
	}
	
	public class PageChangeListener implements OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageSelected(int position) {
			// TODO Auto-generated method stub
			resetTabs() ;
			switch (position) {
			case 0:
				home.setImageResource(R.drawable.home_selected);
				break;
			case 1:
				search.setImageResource(R.drawable.search_selected);
				break;
			case 2:
				category.setImageResource(R.drawable.category_selected);
				break;
			case 3:
				shoppingCart.setImageResource(R.drawable.shoppingcart_selected);
				break;
			case 4:
				member.setImageResource(R.drawable.member_selected);
				break;
			default:
				break;
			}
		}
		
	}
	
	public void resetTabs(){
		home.setImageResource(R.drawable.home_normal);
		search.setImageResource(R.drawable.search_normal);
		category.setImageResource(R.drawable.category_normal);
		shoppingCart.setImageResource(R.drawable.shoppingcart_normal);
		member.setImageResource(R.drawable.member_normal);
	}
	
	public void initView(){
		fragmentList = new ArrayList<Fragment>() ;
		
		HomeFragment homeFragment = new HomeFragment() ;
		SearchFragment searchFragment = new SearchFragment() ;
		CategoryFragment categoryFragment = new CategoryFragment() ;
		ShoppingcartFragment shoppingcartFragment = new ShoppingcartFragment() ;
		MemberFragment memberFragment = new MemberFragment() ;
		
		LoginListener = memberFragment ;
		shoppingCartLoginListener = shoppingcartFragment;
		onRightButtonListener = shoppingcartFragment;
		
		fragmentList.add(homeFragment) ;
		fragmentList.add(searchFragment) ;
		fragmentList.add(categoryFragment) ;
		fragmentList.add(shoppingcartFragment) ;
		fragmentList.add(memberFragment) ;
		
		home = (ImageView) findViewById(R.id.imageView1) ;
		search = (ImageView) findViewById(R.id.imageView2) ;
		category = (ImageView) findViewById(R.id.imageView3) ;
		shoppingCart = (ImageView) findViewById(R.id.imageView4) ;
		member = (ImageView) findViewById(R.id.imageView5) ;
	}
	
	public  interface OnLoginListener{
		public void onLogin(String userName,Integer sysUserId) ;
	}

	
	public interface OnRightButtonListener{
		public void onRightClick(View v);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == 201 && requestCode == 200){
			Bundle bundle = data.getExtras() ;
			String userName = bundle.getString("userName") ;
			Integer sysUserId= bundle.getInt("sysUserId") ;
			LoginListener.onLogin(userName,sysUserId);
			shoppingCartLoginListener.onLogin(userName, sysUserId);
		}
		
		if(resultCode == 501 && requestCode == 500){
			setTab(3);
		}
	}

	
	@Override
	public void onDestroy(){
		super.onDestroy();
	}

	@Override
	public void onRigNavClick(View v) {
		if(onRightButtonListener != null){
			onRightButtonListener.onRightClick(v);
		}
	}
	
	
	public void setTab(int index){
		resetTabs();
		switch (index) {
		case 0:
			home.setImageResource(R.drawable.home_selected);
			myViewPager.setCurrentItem(0, true);
			break;
		case 1:
			search.setImageResource(R.drawable.search_selected);
			myViewPager.setCurrentItem(1, true);
			break;
		case 2:
			category.setImageResource(R.drawable.category_selected);
			myViewPager.setCurrentItem(2, true);
			break;
		case 3:
			shoppingCart.setImageResource(R.drawable.shoppingcart_selected);
			myViewPager.setCurrentItem(3, true);
			break;
		case 4:
			member.setImageResource(R.drawable.member_selected);
			myViewPager.setCurrentItem(4, true);
			break;

		default:
			break;
		}
	}

	//退出程序
		private long exitTime = 0;
		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			if(keyCode == event.KEYCODE_BACK){
				int currentItemIndex = myViewPager.getCurrentItem();
				if(currentItemIndex != 0){
					resetTabs() ;
					home.setImageResource(R.drawable.home_selected);
					myViewPager.setCurrentItem(0, true);
				}else{
					if(System.currentTimeMillis() - exitTime > 2000){
						Toast.makeText(MainActivity.this, "再按一次返回键，退出程序！", Toast.LENGTH_SHORT).show();
						exitTime = System.currentTimeMillis();
					}else{
						finish();
			            System.exit(0);
					}
				}
				return true;
			}
			return super.onKeyDown(keyCode, event);
		}
	
}
