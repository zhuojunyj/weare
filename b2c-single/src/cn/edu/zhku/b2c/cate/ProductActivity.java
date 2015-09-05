package cn.edu.zhku.b2c.cate;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.edu.zhku.b2c.R;
import cn.edu.zhku.b2c.common.HeaderFragment;
import cn.edu.zhku.b2c.common.ShoppingCartBarFragment;
import cn.edu.zhku.b2c.common.ShoppingCartBarFragment.ShoppingcartCallback;
import cn.edu.zhku.b2c.constant.App;
import cn.edu.zhku.b2c.cutom.CustomAlertDialog;
import cn.edu.zhku.b2c.cutom.CustomProgressDialog;
import cn.edu.zhku.b2c.cutom.Gallery;
import cn.edu.zhku.b2c.cutom.Gallery.ClickCallback;
import cn.edu.zhku.b2c.db.BrowseHistoryDbManager;
import cn.edu.zhku.b2c.shoppingCart.SubmitOrderActivity;
import cn.edu.zhku.b2c.util.ConfigParser;
import cn.edu.zhku.b2c.util.HttpUtil;
import cn.edu.zhku.b2c.util.HttpUtil.HttpCallback;
import cn.edu.zhku.b2c.vo.ProductVo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class ProductActivity extends Activity implements ClickCallback,
		ShoppingcartCallback {

	private CustomProgressDialog ProgressDialog = null;
	private CustomAlertDialog alertDialog = null;
	private Map<String, Object> data = null;
	private BrowseHistoryDbManager manager = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product);
		manager = new BrowseHistoryDbManager(getApplicationContext());
		if (ProgressDialog == null) {
			ProgressDialog = CustomProgressDialog.createProgressDialog(
					ProductActivity.this, false);
			ProgressDialog.setMessage("加载中...");
			ProgressDialog.show();
		}
		
		initUI();
		final int productId = getIntent().getIntExtra("productId", 0);
		loadData(productId);
		
		//打开商品详情
		RelativeLayout layout = (RelativeLayout)findViewById(R.id.product_detail);
		layout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ProductActivity.this, ProductDetailActivity.class);
				intent.putExtra("productId", productId);
				startActivity(intent);
			}
		});
	}

	public void initUI() {
		addShoppingCartBar();
	}

	public void addShoppingCartBar() {
		ShoppingCartBarFragment shoppingCartBarFragment = new ShoppingCartBarFragment();
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.add(R.id.shopping_cart, shoppingCartBarFragment);
		transaction.commit();
	}

	public void addHeader() {
		HeaderFragment headerFragment = new HeaderFragment();
		Bundle args = new Bundle();
		args.putString("title", "商品详情");
		headerFragment.setArguments(args);
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.add(R.id.product, headerFragment);
		transaction.commit();
	}
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what == 1){
				TextView cartNumText = (TextView)findViewById(R.id.cart_num);
				cartNumText.setVisibility(View.VISIBLE);
				String cartNum = (String)msg.obj;
				cartNumText.setText(cartNum);
				Toast.makeText(ProductActivity.this, "该商品已加入购物车", Toast.LENGTH_SHORT).show();
				return;
			}
			Gallery gallery = (Gallery) findViewById(R.id.gallery);
			gallery.startPlaying((String[]) data.get("images"), 2000, 2000);

			// ��Ʒ��Ϣ
			TextView productNm = (TextView) findViewById(R.id.product_nm);
			productNm.setText((String) data.get("productNm"));
			addHeader();
			TextView sellingPoint = (TextView) findViewById(R.id.product_sellingPoint);
			sellingPoint.setText((String) data.get("sellingPoint"));

			TextView price = (TextView) findViewById(R.id.product_price);
			price.setText("会员价:" + data.get("price"));

			ImageView isCollection = (ImageView) findViewById(R.id.is_collection);
			if ((Boolean) data.get("isCollection")) {
				isCollection.setBackgroundResource(R.drawable.collect02);
			} else {
				isCollection.setBackgroundResource(R.drawable.collect01);
			}
			ProgressDialog.hide();
			
			//���������¼
			ProductVo product = new ProductVo();
			product.setImage((String)data.get("image"));
			product.setProductId((Integer)data.get("id"));
			product.setProductNm((String)data.get("productNm"));
			product.setSellingPoint((String)data.get("sellingPoint"));
			product.setPrice(data.get("price").toString());
			manager.saveHistory(product);
			
		}
	};

	public void loadData(final int productId) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String webRoot = ConfigParser.loadConfig(ProductActivity.this,
						"webRoot");
				Map<String, Object> sendData = new HashMap<String, Object>();
				sendData.put("id", productId);
				HttpUtil.sendPost(webRoot + "/app/product/productDetail.json",
						sendData, new HttpCallback() {
							@Override
							public void onSuccess(String responseText) {
								data = new HashMap<String, Object>();
								JSONObject json = JSON.parseArray(
										JSON.parseObject(responseText)
												.getString("result"))
										.getJSONObject(0);
								data.put("id", json.get("id"));
								data.put("comment", json.get("comment"));
								data.put("productNm", json.get("title"));
								data.put("salesVolume", json.get("salesVolume"));
								data.put("price", json.get("price"));
								// ȡͼƬ����
								JSONArray array = JSON.parseArray(json
										.getString("images"));
								int len = array.size();
								String[] urls = new String[len];
								for (int i = 0; i < array.size(); i++) {
									urls[i] = (String) array.get(i);
								}
								data.put("images", urls);
								data.put("image", json.get("image"));
								data.put("isEnableMultiSpec",
										json.get("isEnableMultiSpec"));
								data.put("marketPrice", json.get("marketPrice"));
								data.put("sellingPoint",
										json.get("sellingPoint"));
								data.put("goodCommentNum",
										json.get("goodCommentNum"));
								data.put("normalCommentNum",
										json.get("normalCommentNum"));
								data.put("badCommentNum",
										json.get("badCommentNum"));
								data.put("isCollection",
										json.get("isCollection"));
								data.put("grate", json.get("grate"));
								data.put("goodRate", json.get("goodRate"));
								data.put("appSKUs", json.get("appSKUs"));

								// ����UI
								Message message = Message.obtain();
								// message.obj = data;
								handler.sendMessage(message);
							}

							@Override
							public void onError(String errorText) {
								if (alertDialog == null) {
									alertDialog = new CustomAlertDialog(
											ProductActivity.this);
									Bundle bundle = new Bundle();
									bundle.putString("message", errorText);
									alertDialog.setArguments(bundle);
									alertDialog.show(getFragmentManager(),
											"error");
								}
							}
						});
			}
		}).start();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(ProgressDialog != null){
			ProgressDialog.dismiss();
		}
		
		if(alertDialog != null){
			alertDialog.dismiss();
		}
	}

	// pager����¼�
	@Override
	public void onImageClick(int index, String imageUrl) {
		// TODO Auto-generated method stub
		System.out.println("*******************************");
		System.out.println(imageUrl);
	}

	//立即购买 
	@Override
	public void onInstantBuyClick() {
		if("N".equals((String) data.get("isEnableMultiSpec"))){
			App app = (App) getApplication() ;
			if(!app.getUserLogined() || !app.getUserLogined()){
				Toast.makeText(ProductActivity.this, "您尚未登录！", Toast.LENGTH_SHORT).show();
				return;
			}
			addToCart(true);
		}else{
			openMultiSpec();
		}
	}

	//加入购物车
	@Override
	public void onShoppingcartClick() {
		if("N".equals((String) data.get("isEnableMultiSpec"))){
			App app = (App) getApplication() ;
			if(!app.getUserLogined() || !app.getUserLogined()){
				Toast.makeText(ProductActivity.this, "您尚未登录！", Toast.LENGTH_SHORT).show();
				return;
			}
			addToCart(false);
		}else{
			openMultiSpec();
		}
	}
	
	public void addToCart(final boolean isInstantBuy){
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				String webRoot = ConfigParser.loadConfig(ProductActivity.this,
						"webRoot");
				Map<String, Object> sendData = new HashMap<String, Object>();
				sendData.put("type", "normal");
				sendData.put("handler", "sku");
				sendData.put("quantity", 1);
				Integer skuId = (Integer)JSON.parseArray(data.get("appSKUs").toString()).getJSONObject(0).get("skuId");
				sendData.put("objectId", skuId);
				HttpUtil.send(webRoot+"/app/cart/save.json", HttpUtil.POST, sendData, new HttpUtil.HttpCallback() {
					@Override
					public void onSuccess(String responseText) {
						if(!isInstantBuy){
							String cartNum = JSON.parseObject(JSON.parseObject(responseText).getString("result")).getString("cartNum");
							Message msg = handler.obtainMessage();
							msg.what = 1;
							msg.obj = cartNum;
							handler.sendMessage(msg);
						}else{
							//立即购买，打开订单提交页面
							Intent intent = new Intent(ProductActivity.this, SubmitOrderActivity.class);
							startActivity(intent);
						}
					}
					
					@Override
					public void onError(String errorText) {
						
					}
				});
			}
			
		}).start();
	}
	
	//�򿪶���
	public void openMultiSpec(){
		Intent intent = new Intent(ProductActivity.this, MultiSpecActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("id", (Integer)data.get("id"));
		bundle.putString("image", (String)data.get("image"));
		bundle.putString("productNm", (String)data.get("productNm"));
		bundle.putString("sellingPoint", (String)data.get("sellingPoint"));
		bundle.putString("price", data.get("price").toString());
		bundle.putString("marketPrice", (String)data.get("marketPrice").toString());
		//bundle.putString("appSKUs", data.get("appSKUs"));
		intent.putExtra("product", bundle);
		startActivity(intent);
	}

}
