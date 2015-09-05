package cn.edu.zhku.b2c.cutom;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;
import cn.edu.zhku.b2c.R;

public class CustomProgressDialog extends Dialog{
	private Context context = null ;
	private AnimationDrawable loading = null ;
	private static CustomProgressDialog customProgressDialog = null ;
	
	public CustomProgressDialog(Context context) {
		super(context);
		this.context = context ;
	}
	
	public CustomProgressDialog(Context context , int theme) {
		super(context,theme);
	}
	
	public static CustomProgressDialog createProgressDialog(Context context , boolean cancelable){
		customProgressDialog = new CustomProgressDialog(context,R.style.CustomProgressDialog) ;
		customProgressDialog.setContentView(R.layout.custom_progress);
		customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER ;
		
		//默认loading提示
		TextView tvMsg = (TextView)customProgressDialog.findViewById(R.id.loading_msg);
		if (tvMsg != null){
            tvMsg.setText(R.string.common_loading);
        }
		//是否可以取消
		customProgressDialog.setCancelable(cancelable ? false : cancelable);
		return customProgressDialog ;
	}
	
	public void onWindowFocusChanged(boolean hasFocus){
		ImageView loadingImg = (ImageView) customProgressDialog.findViewById(R.id.loading_image) ;
		loading = (AnimationDrawable) loadingImg.getBackground() ;
		loading.start();
	}	
	
	
	public CustomProgressDialog setMessage(String msg){
		TextView tvMsg = (TextView)customProgressDialog.findViewById(R.id.loading_msg);
        if (tvMsg != null){
            tvMsg.setText(msg);
        }
        return customProgressDialog;
	}

}
