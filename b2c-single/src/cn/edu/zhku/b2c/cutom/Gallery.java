package cn.edu.zhku.b2c.cutom;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import cn.edu.zhku.b2c.R;
import cn.edu.zhku.b2c.util.ImageLoader;
import cn.edu.zhku.b2c.util.ImageLoader.LoaderCallback;
import cn.edu.zhku.b2c.util.ScreenUtil;

public class Gallery extends FrameLayout implements OnPageChangeListener{
	
	private ViewPager viewPager = null;
	private List<ImageView> images = new ArrayList<ImageView>();;
	private Context context = null;
	private MyPagerAdapter adapter = null;
	private LinearLayout indicatorWrapper = null;
	private int currentItemIndex = 0;
	private ClickCallback callback = null;
	private String[] urls = null;
	
	public Gallery(Context context) {
		super(context);
		this.context = context;
		init();
	}
	
	public Gallery(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}
	
	public Gallery(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init();
	}
	
	public void init(){
		LayoutInflater.from(context).inflate(R.layout.custom_gallery, this, true);
		indicatorWrapper = (LinearLayout) findViewById(R.id.indicators);
		if(context instanceof ClickCallback){
			callback = (ClickCallback) context;
		}
	}
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			viewPager.setCurrentItem(currentItemIndex);
			redrawIndicators();
			currentItemIndex ++;
			if(currentItemIndex >= images.size()){
				currentItemIndex = 0;
			}
		}
		
	};
	
	public void redrawIndicators(){
		int childCount = indicatorWrapper.getChildCount();
		for(int i = 0 ; i < childCount ; i ++){
			View view = indicatorWrapper.getChildAt(i);
			view.setBackgroundResource(R.drawable.custom_gallery_indicator_normal);
			if(currentItemIndex == i){
				view.setBackgroundResource(R.drawable.custom_gallery_indicator_selected);
			}
		}
	}
	//�����ֲ�ͼƬ
	public void startPlaying(String[] urls, int duration, int speed){
		if(urls == null || urls.length == 0){
			return;
		}
		this.urls = urls;
		viewPager = (ViewPager)findViewById(R.id.photoGallery);
		setContent(urls);
		drawIndicators(urls.length);
		//�л��ٶ�
		try{
			Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(context,
                    new AccelerateInterpolator());
            field.set(viewPager, scroller);
            scroller.setmDuration(speed);
		}catch(Exception e){
			e.printStackTrace();
		}
		adapter = new MyPagerAdapter();
		viewPager.setAdapter(adapter);
		viewPager.setOnPageChangeListener(this);//�����¼�
		//��ʱ�ֲ�
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(new Runnable(){
			@Override
			public void run() {
				try{
					startRolling();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}, 3000, duration, TimeUnit.MILLISECONDS);
	}
	
	//�Զ��ֲ�
	public void startRolling(){
		//TODO
		if(images.size() < urls.length){
			return;
		}
		handler.sendEmptyMessage(currentItemIndex);
	}
	
	//��СԲ��
	public void drawIndicators(int len){
		if(len <= 1){
			return;
		}
		indicatorWrapper.removeAllViews();
		for(int i = 0 ; i < len ; i ++){
			View view = new View(context);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ScreenUtil.dp2px(context, 8), ScreenUtil.dp2px(context, 8));
			params.setMargins(ScreenUtil.dp2px(context, 4), 0, 0, 0);
			view.setLayoutParams(params);
			if(i == 0){
				view.setBackgroundResource(R.drawable.custom_gallery_indicator_selected);
			}else{
				view.setBackgroundResource(R.drawable.custom_gallery_indicator_normal);
			}
			indicatorWrapper.addView(view);
		}
	}
	
	//������
	public void setContent(String[] urls){
		final int len = urls.length;
		if(0 == len){
			return;
		}
		images.clear();
		ImageLoader loader = new ImageLoader();
		for(int i = 0 ; i < len ; i ++){
			//�첽����ͼƬ
			loader.loadImage(urls[i], new LoaderCallback() {
				@Override
				public void async(Bitmap bitmap, String imageUrl) {
					ImageView imageView = new ImageView(context);
					LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
					imageView.setLayoutParams(params);
					imageView.setImageBitmap(bitmap);
					images.add(imageView);
					adapter.notifyDataSetChanged();
				}
			});
		}
	}

	public class MyPagerAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return images.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			if(images.size() > position){
				container.removeView(images.get(position));
			}else{
				container.removeView((View)object);
			}
		}

		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			// TODO Auto-generated method stub
			container.addView(images.get(position));
			//��ͼƬ����¼�
			images.get(position).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					System.out.println("---->" + callback);
					if(callback != null){
						callback.onImageClick(position, urls[position]);
					}
				}
			});
			return images.get(position);
		}
		
		
	}

	//pager�����¼�
	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		currentItemIndex = arg0;
		redrawIndicators();
		viewPager.setCurrentItem(arg0, true);
	}

	//ͼƬ����ص��ӿ�
	public interface ClickCallback{
		public void onImageClick(int index, String imageUrl);
	}
	
}
