package cn.edu.zhku.b2c;


import java.util.HashMap;
import java.util.Map;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.edu.zhku.b2c.MainActivity.OnRightButtonListener;
import cn.edu.zhku.b2c.cate.ProductActivity;
import cn.edu.zhku.b2c.common.HeaderFragment;
import cn.edu.zhku.b2c.constant.App;
import cn.edu.zhku.b2c.cutom.CustomAlertDialog;
import cn.edu.zhku.b2c.cutom.CustomProgressDialog;
import cn.edu.zhku.b2c.i.TabListener;
import cn.edu.zhku.b2c.member.LoginActivity;
import cn.edu.zhku.b2c.model.CartItem;
import cn.edu.zhku.b2c.model.ShoppingCart;
import cn.edu.zhku.b2c.shoppingCart.SubmitOrderActivity;
import cn.edu.zhku.b2c.util.ConfigParser;
import cn.edu.zhku.b2c.util.HttpUtil;
import cn.edu.zhku.b2c.util.ImageLoader;
import cn.edu.zhku.b2c.util.ImageLoader.LoaderCallback;

import com.alibaba.fastjson.JSON;

public class ShoppingcartFragment extends Fragment implements MainActivity.OnLoginListener, OnRightButtonListener{

	private View view = null;
	private ShoppingCart shoppingCart = null;
	private CustomAlertDialog alertDialog = null;
	private CustomProgressDialog progressDialog = null;
	private String itemKeys = "";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		view = inflater.inflate(R.layout.shoppingcart_tab, container, false);
		addHeader();
//		addItems();
		init();
		addEventListeners();
		return view;
	}
	
	public void init(){
		if (progressDialog == null) {
			progressDialog = CustomProgressDialog.createProgressDialog(
					getActivity(), false);
			progressDialog.setMessage("加载中...");
		}
		
		App app = (App)getActivity().getApplication();
		if(app.getUserLogined()){//已登录
			RelativeLayout goLoginLayout = (RelativeLayout)view.findViewById(R.id.toLogin);
			goLoginLayout.setVisibility(View.GONE);
			loadCartItems();//购物车商品
		}
	}
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(1 == msg.what){
				view.findViewById(R.id.cartItems).setVisibility(View.GONE);
				view.findViewById(R.id.no_stuff).setVisibility(View.VISIBLE);
				Toast.makeText(getActivity(), "购物车已清空！", Toast.LENGTH_SHORT).show();
				App app = (App)getActivity().getApplication();
				if(app.getUserLogined()){
					view.findViewById(R.id.toLogin).setVisibility(View.GONE);
				}else{
					view.findViewById(R.id.toLogin).setVisibility(View.VISIBLE);
				}
			}
			
			if(0 == msg.what){
				int cartNum = shoppingCart.getCartNum();
				if(cartNum > 0){
					view.findViewById(R.id.no_stuff).setVisibility(View.GONE);;
					view.findViewById(R.id.cartItems).setVisibility(View.VISIBLE);
					ImageLoader loader = new ImageLoader();
					LayoutInflater inflater = LayoutInflater.from(getActivity());
					View cartItemView = inflater.inflate(R.layout.fragment_shoppingcart_item, null);
					for(CartItem cartItem : shoppingCart.getAppCartItems()){
						itemKeys += cartItem.getItemKey() + ",";
						final ImageView productImage = (ImageView)cartItemView.findViewById(R.id.product_image);
						loader.loadImage(cartItem.getImage(), new LoaderCallback() {
							@Override
							public void async(Bitmap bitmap, String imageUrl) {
								productImage.setImageBitmap(bitmap);
							}
						});
						
						TextView productName = (TextView)cartItemView.findViewById(R.id.product_name);
						productName.setText(cartItem.getName());
						
						TextView productSpec = (TextView)cartItemView.findViewById(R.id.product_spec);
						productSpec.setText(cartItem.getSpecName());
						
						TextView productPrice = (TextView)cartItemView.findViewById(R.id.product_price);
						productPrice.setText("￥" + cartItem.getProductUnitPrice());
						
						TextView productQuantity = (TextView)cartItemView.findViewById(R.id.product_quantity);
						productQuantity.setText("数量:X" + cartItem.getQuantity());
						
						LinearLayout cartItemWrapper = (LinearLayout)view.findViewById(R.id.items);
						cartItemWrapper.addView(cartItemView);
					}
					
					TextView orderTotal = (TextView)view.findViewById(R.id.heji);
					orderTotal.setText("合计：￥" + shoppingCart.getOrderTotalAmount());
					
					TextView discountAmount = (TextView)view.findViewById(R.id.youhui);
					discountAmount.setText("已优惠：￥" + shoppingCart.getDiscountAmount());
				}
			}
			progressDialog.hide();
		}
		
	};
			
	public void loadCartItems(){
		progressDialog.show();
		new Thread(new Runnable(){

			@Override
			public void run() {
				String webRoot = ConfigParser.loadConfig(getActivity(), "webRoot");
				HttpUtil.send(webRoot+"/app/cart/list.json", HttpUtil.POST, null, new HttpUtil.HttpCallback() {
					
					@Override
					public void onSuccess(String responseText) {
						shoppingCart = JSON.parseObject(JSON.parseObject(responseText).getString("result"), ShoppingCart.class);
						handler.sendEmptyMessage(0);
					}
					
					@Override
					public void onError(String errorText) {
						if(alertDialog == null){
							alertDialog = new CustomAlertDialog(getActivity());
							Bundle bundle = new Bundle();
							bundle.putString("message", errorText);
							alertDialog.setArguments(bundle);
							alertDialog.show(getActivity().getFragmentManager(),
									"error");
						}
					}
				});
			}
			
		}).start();
	}
	
	//添加事件
	public void addEventListeners(){
		//登录
		RelativeLayout goLogin = (RelativeLayout)view.findViewById(R.id.toLogin);
		goLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), LoginActivity.class) ;
				getActivity().startActivityForResult(intent, 200);
			}
		});
		
		//去逛逛
		ImageView goTakeALook = (ImageView)view.findViewById(R.id.go);
		goTakeALook.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				TabListener tabListener = (TabListener) getActivity();
				tabListener.setTab(0);
			}
		});
		
		//去结算
		Button goPayBtn = (Button)view.findViewById(R.id.gopay);
		goPayBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), SubmitOrderActivity.class);
				getActivity().startActivity(intent);
			}
		});
	}
	
	public void addHeader() {
		HeaderFragment headerFragment = new HeaderFragment();
		Bundle args = new Bundle();
		args.putString("title", "购物车");
		args.putString("rigNavTitle", "清空");
		args.putBoolean("showLeftBtn", false);
		headerFragment.setArguments(args);
		FragmentManager fragmentManager = getActivity().getFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.add(R.id.shoppingCart, headerFragment);
		transaction.commit();
	}

	//登录事件
	@Override
	public void onLogin(String userName, Integer sysUserId) {
		App app = (App)getActivity().getApplication();
		if(app.getUserLogined()){//已登录
			RelativeLayout goLoginLayout = (RelativeLayout)view.findViewById(R.id.toLogin);
			goLoginLayout.setVisibility(View.GONE);
			loadCartItems();//购物车商品
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(progressDialog != null){
			progressDialog.dismiss();
		}
		if(alertDialog != null){
			alertDialog.dismiss();
		}
	}

	@Override
	public void onRightClick(View v) {
		//清空购物车(批量删除)
		if(shoppingCart != null && shoppingCart.getAppCartItems().size() > 0){
			new Thread(new Runnable(){
				@Override
				public void run() {
					String webRoot = ConfigParser.loadConfig(getActivity(), "webRoot");
					Map<String, Object> sendData = new HashMap<String, Object>();
					sendData.put("type", "normal");
					sendData.put("handler", "sku");
					sendData.put("itemKeys", itemKeys);
					HttpUtil.send(webRoot+"/app/cart/cartItems/delete.json", HttpUtil.POST, sendData, new HttpUtil.HttpCallback() {
						
						@Override
						public void onSuccess(String responseText) {
							handler.sendEmptyMessage(1);
						}
						
						@Override
						public void onError(String errorText) {
							
						}
					});
				}
			}).start();
		}else{
			Toast.makeText(getActivity(), "购物车暂无商品！", Toast.LENGTH_SHORT).show();
		}
	}
	
	
}
