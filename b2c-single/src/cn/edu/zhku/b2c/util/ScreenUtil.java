package cn.edu.zhku.b2c.util;

import android.content.Context;

public class ScreenUtil {
	//�����ֻ��ֱ��� px to dp
	public static int px2dp(Context context , float pxValue){
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	
	//�����ֻ��ֱ��� dp to px
	public static int dp2px(Context context , float dpValue){
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
}
