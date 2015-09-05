package cn.edu.zhku.b2c.cutom;

import cn.edu.zhku.b2c.util.ScreenUtil;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class CustomViewGroup extends ViewGroup{
	private int marginHorizontal = 0;
	private int marginVertical = 0;
	public CustomViewGroup(Context context) {
		super(context);
		marginHorizontal = ScreenUtil.dp2px(context, 15);
		marginVertical = ScreenUtil.dp2px(context, 15);
	}
	
	public CustomViewGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		marginHorizontal = ScreenUtil.dp2px(context, 15);
		marginVertical = ScreenUtil.dp2px(context, 15);
	}
	
	
	public CustomViewGroup(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		marginHorizontal = ScreenUtil.dp2px(context, 15);
		marginVertical = ScreenUtil.dp2px(context, 15);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		int count = getChildCount();
		int xCount = 0;//x���������
		int height = 0;
		int row = 1;
		for(int i = 0 ; i < count ; i ++){
			View view = getChildAt(i);
			if(null == view){
				continue;
			}
			xCount += view.getMeasuredWidth() + marginHorizontal;
			if(xCount >= right){//����
				xCount = 0;
				row ++;
				height = row * (view.getMeasuredHeight() + marginVertical);
				view.layout(xCount + marginHorizontal, 
						height - view.getMeasuredHeight(), 
						xCount + marginHorizontal + view.getMeasuredWidth(), height);
				xCount += marginHorizontal + view.getMeasuredWidth();
			}else{
				height = row * (view.getMeasuredHeight() + marginVertical);
				view.layout(xCount - view.getMeasuredWidth(), 
						height - view.getMeasuredHeight(), 
						xCount, height);
			}
		}
	}

	/**
	 * �����Զ��������������
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int row = 1;//����
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = 0;
		int xCount = 0;
		int count = getChildCount();
		for(int i = 0 ; i < count ; i ++){
			View view = getChildAt(i);
			if(null == view){
				continue;
			}
			view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);	
			xCount += marginHorizontal + view.getMeasuredWidth();
			if(xCount > width){//���button�ۼ������Ŀ�ȴ����Զ��������ȣ�����
				row ++ ;
				xCount = marginHorizontal + view.getMeasuredWidth();//����
			}
			height = row * (view.getMeasuredHeight() + marginVertical);
		}
		setMeasuredDimension(width, height);
	}
	

}
