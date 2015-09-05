package cn.edu.zhku.b2c.shoppingCart;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import cn.edu.zhku.b2c.R;
import cn.edu.zhku.b2c.alipay.Result;
import cn.edu.zhku.b2c.alipay.SignUtils;
import cn.edu.zhku.b2c.common.HeaderFragment;
import cn.edu.zhku.b2c.cutom.CustomAlertDialog;
import cn.edu.zhku.b2c.model.OrderDetail;
import cn.edu.zhku.b2c.util.ConfigParser;
import cn.edu.zhku.b2c.util.HttpUtil;

import com.alibaba.fastjson.JSON;
import com.alipay.sdk.app.PayTask;

public class SubmitSuccessActivity extends Activity implements OnClickListener{

	private CustomAlertDialog alertDialog = null;
	private OrderDetail orderDetail = null;
	
	public static final String PARTNER = "2088701361565282";
	public static final String SELLER = "pay@imall.com.cn";
	public static final String RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAL2bdkR6nb0qssAmSZXAej6GvRXeQEymsvc5vyCwKoSycAdq2rtXhZJxNqH7tySsPQvd6n3yKpHcifRmFKXlzR1cRTPvzCs1kzY1quY7ALL5G4gHIdXXBG9+V24//tjYVb6Bj9pi5YxMas4kPsi7wqLA94UHXEAlLXRkfMRCtubdAgMBAAECgYAyG+vdnSoi3C3xaiz4Tq3/6EwAg4QqvcN+YtyIHwadmyrQSwUTjcXXNeRiWsvD/WEEydk2/9EQS2CKHpBGZ70VGI5kv2SpDBO9c9UZP7yC7J2gy1MSDPY1V0S1ULg+c9W5XN8rCpYF81L5fGYvB3Ym3hubPKLk2odhW1TlD5VaYQJBAO1xM3vHgQs2uQ88mVZJdivXlXpbZy69BYpzO+vkcZhZFVnunbobESkhfCxZ/1jaJ7m245GvHnX4N9o3Bc043esCQQDMbSssGsUdSJnj0kKnJLFf4Xzh2rDfu2DEU6I544zKoFpdiqdKTDAIUBt3fBbfG1m9E48/EyeifkEckS2pP3RXAkEA3vM5NVO6Pq5OjBkJnfTyqe4O5EopE8DXEA5tyGzDoRqcqsocfiBmN6nCb969nk+Rl5c8DZJSVtEQmKyrnOx1qwJBALoKpB6oYjlrSDoQsX4ho5cpxBhbiVqj0cX6gwoB77C8TGES/Xpdad024jhUYxA6eOndMiFqEkkMc79G1HJSAFMCQBgEPTEtBd9xc0Nqqlu8XvidY/+AoBgF5557SCz28u30+OWc5I78YYc5EwvkBsADP8wfXHDOmkJXLd9JVkNq9O4=";
	public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

	private static final int SDK_PAY_FLAG = 1;

	private static final int SDK_CHECK_FLAG = 2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_submit_success);
		addHeader();
		init();
		
		Button goPayBtn = (Button)findViewById(R.id.goPay);
		goPayBtn.setOnClickListener(this);
	}
	
	public void addHeader() {
		HeaderFragment headerFragment = new HeaderFragment();
		Bundle args = new Bundle();
		args.putString("title", "提交订单成功");
		headerFragment.setArguments(args);
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.add(R.id.submitSuccess, headerFragment);
		transaction.commit();
	}
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			
			if(msg.what == SDK_PAY_FLAG){
				Result resultObj = new Result((String) msg.obj);
				String resultStatus = resultObj.resultStatus;

				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					Toast.makeText(SubmitSuccessActivity.this, "支付成功",
							Toast.LENGTH_SHORT).show();
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000” 代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(SubmitSuccessActivity.this, "支付结果确认中",
								Toast.LENGTH_SHORT).show();

					} else {
						Toast.makeText(SubmitSuccessActivity.this, "支付失败",
								Toast.LENGTH_SHORT).show();

					}
				}
			}
			
			if(msg.what == SDK_CHECK_FLAG){
				Toast.makeText(SubmitSuccessActivity.this, "检查结果为：" + msg.obj,
						Toast.LENGTH_SHORT).show();
			}
			
			if(0 == msg.what){
				TextView orderNum = (TextView)findViewById(R.id.orderNum);
				orderNum.setText(orderDetail.getOrderNumber());
				
				TextView payment = (TextView)findViewById(R.id.payment);
				payment.setText("￥" + orderDetail.getPayAmount());
				
				TextView delivery = (TextView)findViewById(R.id.delivery);
				delivery.setText(orderDetail.getDeliveryWay());
				
				TextView payway = (TextView)findViewById(R.id.payway);
				payway.setText(orderDetail.getPayWay());
				
				TextView actualPayment = (TextView)findViewById(R.id.actualPayment);
				actualPayment.setText("￥" + orderDetail.getPayAmount());
			}
		}
	};
	
	public void init(){
		Intent intent = getIntent();
		final int orderId = intent.getIntExtra("orderId", 1);
		new Thread(new Runnable(){
			@Override
			public void run() {
				String webRoot = ConfigParser.loadConfig(SubmitSuccessActivity.this, "webRoot");
				Map<String, Object> sendData = new HashMap<String, Object>();
				sendData.put("id", orderId);
				HttpUtil.send(webRoot+"/app/member/orderDetail.json", HttpUtil.POST, sendData, new HttpUtil.HttpCallback(){
					@Override
					public void onError(String errorText) {
						if (alertDialog == null) {
							alertDialog = new CustomAlertDialog(
									SubmitSuccessActivity.this);
							Bundle bundle = new Bundle();
							bundle.putString("message", errorText);
							alertDialog.setArguments(bundle);
							alertDialog.show(getFragmentManager(),
									"error");
						}
					}

					@Override
					public void onSuccess(String responseText) {
						orderDetail = JSON.parseObject(JSON.parseObject(responseText).getString("result"), OrderDetail.class);
						handler.sendEmptyMessage(0);
					}
					
				});
				
			}
			
		}).start();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(alertDialog != null){
			alertDialog.dismiss();
		}
	};
	
	/**
	 * 支付宝原生支付
	 */
	@Override
	public void onClick(View v) {
		pay(v);
	}

	
	/**
	 * call alipay sdk pay. 调用SDK支付
	 * 
	 */
	public void pay(View v) {
		String orderInfo = getOrderInfo("测试的商品", "该测试商品的详细描述", "0.01");
		String sign = sign(orderInfo);
		try {
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
				+ getSignType();

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(SubmitSuccessActivity.this);
				// 调用支付接口
				String result = alipay.pay(payInfo);

				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				handler.sendMessage(msg);
			}
		};

		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}
	
	
	/**
	 * check whether the device has authentication alipay account.
	 * 查询终端设备是否存在支付宝认证账户
	 * 
	 */
	public void check(View v) {
		Runnable checkRunnable = new Runnable() {

			@Override
			public void run() {
				PayTask payTask = new PayTask(SubmitSuccessActivity.this);
				boolean isExist = payTask.checkAccountIfExist();

				Message msg = new Message();
				msg.what = SDK_CHECK_FLAG;
				msg.obj = isExist;
				handler.sendMessage(msg);
			}
		};

		Thread checkThread = new Thread(checkRunnable);
		checkThread.start();

	}

	/**
	 * get the sdk version. 获取SDK版本号
	 * 
	 */
	public void getSDKVersion() {
		PayTask payTask = new PayTask(this);
		String version = payTask.getVersion();
		Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
	}

	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	public String getOrderInfo(String subject, String body, String price) {
		// 合作者身份ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";

		// 卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + "http://v.imall.com.cn/notify_url.jsp"
				+ "\"";

		// 接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}

	/**
	 * get the out_trade_no for an order. 获取外部订单号
	 * 
	 */
	public String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
				Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	public String sign(String content) {
		return SignUtils.sign(content, RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	public String getSignType() {
		return "sign_type=\"RSA\"";
	}
}
