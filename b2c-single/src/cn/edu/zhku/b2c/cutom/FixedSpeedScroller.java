package cn.edu.zhku.b2c.cutom;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

public class FixedSpeedScroller extends Scroller{

	private int mDuration = 1500;
	
	public FixedSpeedScroller(Context context) {
		super(context);
	}
	
	public FixedSpeedScroller(Context context, Interpolator interpolator) {
		super(context, interpolator);
	}

	
	@Override
	public void startScroll(int startX, int startY, int dx, int dy, int duration) {
		// TODO Auto-generated method stub
		super.startScroll(startX, startY, dx, dy, duration);
	}

	@Override
	public void startScroll(int startX, int startY, int dx, int dy) {
		// TODO Auto-generated method stub
		super.startScroll(startX, startY, dx, dy);
	}

	public int getmDuration() {
		return mDuration;
	}

	public void setmDuration(int mDuration) {
		this.mDuration = mDuration;
	}
	
	
}
