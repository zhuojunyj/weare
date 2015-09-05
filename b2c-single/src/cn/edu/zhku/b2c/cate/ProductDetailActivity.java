package cn.edu.zhku.b2c.cate;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import cn.edu.zhku.b2c.R;
import cn.edu.zhku.b2c.R.id;
import cn.edu.zhku.b2c.R.layout;
import cn.edu.zhku.b2c.common.HeaderFragment;
import cn.edu.zhku.b2c.util.ConfigParser;

public class ProductDetailActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_detail);
		addHeader();
		int productId = getIntent().getIntExtra("productId", 1);
		WebView webView = (WebView)findViewById(R.id.webView);
		
		//自适应屏幕
		WebSettings webSettings =   webView.getSettings();       
		webSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
		webSettings.setLoadWithOverviewMode(true);
		
		//缩放
		webSettings.setJavaScriptEnabled(true);  
		webSettings.setBuiltInZoomControls(true);
		webSettings.setSupportZoom(true);
		
		String webRoot = ConfigParser.loadConfig(ProductDetailActivity.this, "webRoot");
		webView.loadUrl(webRoot+"/app/productDetail.jsp?productId=" + productId);
	}
	
	public void addHeader() {
		HeaderFragment headerFragment = new HeaderFragment();
		Bundle args = new Bundle();
		args.putString("title", "商品详情");
		headerFragment.setArguments(args);
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.add(R.id.productDetail, headerFragment);
		transaction.commit();
	}
}
