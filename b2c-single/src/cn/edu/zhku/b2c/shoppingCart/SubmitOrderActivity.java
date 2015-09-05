package cn.edu.zhku.b2c.shoppingCart;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.edu.zhku.b2c.R;
import cn.edu.zhku.b2c.common.HeaderFragment;
import cn.edu.zhku.b2c.cutom.CustomAlertDialog;
import cn.edu.zhku.b2c.cutom.CustomProgressDialog;
import cn.edu.zhku.b2c.model.CartItem;
import cn.edu.zhku.b2c.model.OrderPreference;
import cn.edu.zhku.b2c.model.ShoppingCart;
import cn.edu.zhku.b2c.util.ConfigParser;
import cn.edu.zhku.b2c.util.HttpUtil;
import cn.edu.zhku.b2c.util.HttpUtil.HttpCallback;
import cn.edu.zhku.b2c.util.ImageLoader;

import com.alibaba.fastjson.JSON;

public class SubmitOrderActivity extends Activity implements View.OnClickListener{

	private CustomProgressDialog progressDialog = null;
	private CustomAlertDialog alertDialog = null;
	private OrderPreference orderPreference = null;
	private ShoppingCart shoppingCart = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_submit_order);
		startLoading();
		addHeader();
		initOrderPreference();
		initShoppingCartInfo();
		
		//提交订单事件
		Button submitBtn = (Button)findViewById(R.id.submit);
		submitBtn.setOnClickListener(this);
	}
	
	public void startLoading(){
		if(progressDialog == null){
			progressDialog = CustomProgressDialog.createProgressDialog(
					SubmitOrderActivity.this, true);
			progressDialog.setMessage("加载中...");
			progressDialog.show();
		}
	}
	
	public void addHeader() {
		HeaderFragment headerFragment = new HeaderFragment();
		Bundle args = new Bundle();
		args.putString("title", "填写订单");
		headerFragment.setArguments(args);
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.add(R.id.submitOrder, headerFragment);
		transaction.commit();
	}
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what == 0){
				TextView receiverName = (TextView)findViewById(R.id.receiverName);
				receiverName.setText(orderPreference.getReceiverName());
				
				TextView receiverPhone = (TextView)findViewById(R.id.receiverPhone);
				receiverPhone.setText(orderPreference.getReceiverMobile());
				
				TextView receiverAddr = (TextView)findViewById(R.id.receiverAddr);
				receiverAddr.setText(orderPreference.getReceiverAddr());
				
				//配送方式
				TextView deliveryWay = (TextView)findViewById(R.id.deliveryWay);
				deliveryWay.setText(orderPreference.getDeliveryRuleNm());
				
				//支付方式
				TextView payway = (TextView)findViewById(R.id.payway);
				payway.setText(orderPreference.getPayWayNm());
				
				progressDialog.hide();
			}
			if(msg.what == 1){
				//账户余额
				TextView prestore = (TextView)findViewById(R.id.prestore);
				prestore.setText("￥" + shoppingCart.getUserAcount());
				
				//商品信息
				TextView productTotal = (TextView)findViewById(R.id.productTotal);
				productTotal.setText(shoppingCart.getCartNum().toString());
				
				LayoutInflater inflater = LayoutInflater.from(SubmitOrderActivity.this);
				for(CartItem item : shoppingCart.getAppCartItems()){
					View view = inflater.inflate(R.layout.fragment_order_item, null);
					
					//商品图片
					final ImageView productImage = (ImageView)view.findViewById(R.id.productImage);
					ImageLoader imageLoader = new ImageLoader();
					imageLoader.loadImage(item.getImage(), new ImageLoader.LoaderCallback(){
						@Override
						public void async(Bitmap bitmap, String imageUrl) {
							productImage.setImageBitmap(bitmap);
						}
					});
					
					//商品名称
					TextView productName = (TextView)view.findViewById(R.id.productName);
					productName.setText(item.getName());
					
					//商品价格
					TextView price = (TextView)view.findViewById(R.id.price);
					price.setText("￥" + item.getProductUnitPrice());
					
					//商品数量
					TextView quantity = (TextView)view.findViewById(R.id.quantity);
					quantity.setText("数量:	X" + item.getQuantity());
					
					RelativeLayout productListWrapper = (RelativeLayout)findViewById(R.id.productListWrapper);
					productListWrapper.addView(view);
				}
				
				//商品总金额
				TextView productPriceTotal = (TextView)findViewById(R.id.productPriceTotal);
				productPriceTotal.setText("￥" + shoppingCart.getProductTotalAmount());
				
				//运费
				TextView logisticsFee = (TextView)findViewById(R.id.logisticsFee);
				logisticsFee.setText("￥" + shoppingCart.getFreightAmount());
				
				//应付金额
				TextView actualPayment = (TextView)findViewById(R.id.actualPayment);
				actualPayment.setText("￥" + (shoppingCart.getOrderTotalAmount() - shoppingCart.getCouponAmount() - shoppingCart.getAccountAmount()));
				progressDialog.hide();
			}
			
			if(msg.what == 2){
				progressDialog.hide();
			}
		}
	};
	
	/**
	 * 获取地址，配送方式，支付方式等信息
	 */
	public void initOrderPreference(){
		new Thread(new Runnable(){
			@Override
			public void run() {
				String webRoot = ConfigParser.loadConfig(SubmitOrderActivity.this, "webRoot");
				Map<String, Object> sendData = new HashMap<String, Object>();
				sendData.put("type", "normal");
				HttpUtil.send(webRoot+"/app/cart/preference.json", HttpUtil.POST, sendData, new HttpCallback() {
					@Override
					public void onSuccess(String responseText) {
						orderPreference = JSON.parseObject(JSON.parseObject(responseText).getString("result"), OrderPreference.class);
						handler.sendEmptyMessage(0);
					}
					
					@Override
					public void onError(String errorText) {
						if(alertDialog == null){
							alertDialog = new CustomAlertDialog(SubmitOrderActivity.this);
						}
						Bundle bundle = new Bundle();
						bundle.putString("message", errorText);
						alertDialog.setArguments(bundle);
						alertDialog.show(getFragmentManager(),
								"error");
					}
				});
			}
		}).start();
	}

	//获取购物车相关
	public void initShoppingCartInfo(){
		new Thread(new Runnable(){
			@Override
			public void run() {
				String webRoot = ConfigParser.loadConfig(SubmitOrderActivity.this, "webRoot");
//				Map<String, Object> sendData = new HashMap<String, Object>();
				HttpUtil.send(webRoot+"/app/cart/list.json", HttpUtil.POST, null, new HttpCallback() {
					@Override
					public void onSuccess(String responseText) {
						shoppingCart = JSON.parseObject(JSON.parseObject(responseText).getString("result"), ShoppingCart.class);
						handler.sendEmptyMessage(1);
					}
					
					@Override
					public void onError(String errorText) {
						if(alertDialog == null){
							alertDialog = new CustomAlertDialog(SubmitOrderActivity.this);
						}
						Bundle bundle = new Bundle();
						bundle.putString("message", errorText);
						alertDialog.setArguments(bundle);
						alertDialog.show(getFragmentManager(),
								"error");
					}
				});
			}
		}).start();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(progressDialog != null){
			progressDialog.dismiss();
		}
		
		if(alertDialog != null){
			alertDialog.dismiss();
		}
	}

	@Override
	public void onClick(View v) {
		//提交订单
		progressDialog.setMessage("订单提交中...").show();
		new Thread(new Runnable(){
			@Override
			public void run() {
				String webRoot = ConfigParser.loadConfig(SubmitOrderActivity.this, "webRoot");
				Map<String, Object> sendData = new HashMap<String, Object>();
				sendData.put("isSupportCod", orderPreference.getIsSupportCod());
				sendData.put("payWayId", orderPreference.getMobilePayWayId());
				sendData.put("type", "normal");
				sendData.put("orderSourceCode", "4");
				sendData.put("processStatCode", "0");
				sendData.put("promotionTypeCode", "0");
				sendData.put("invoiceType", "0");
				sendData.put("isNeedInvoice", "N");
				HttpUtil.send(webRoot+"/app/cart/addOrder.json", HttpUtil.POST, sendData, new HttpUtil.HttpCallback() {
					
					@Override
					public void onSuccess(String responseText) {
						//关闭loading
						handler.sendEmptyMessage(2);
						//订单提交成功界面
						int orderId = JSON.parseObject(JSON.parseObject(responseText).getString("result")).getIntValue("orderId");
						Intent intent = new Intent(SubmitOrderActivity.this, SubmitSuccessActivity.class);
						intent.putExtra("orderId", orderId);
						startActivity(intent);
					}
					
					@Override
					public void onError(String errorText) {
						if(alertDialog == null){
							alertDialog = new CustomAlertDialog(SubmitOrderActivity.this);
						}
						Bundle bundle = new Bundle();
						bundle.putString("message", errorText);
						alertDialog.setArguments(bundle);
						alertDialog.show(getFragmentManager(),
								"error");
					}
				});
			}
		}).start();
	}
}
