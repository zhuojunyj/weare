package cn.edu.zhku.b2c.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import cn.edu.zhku.b2c.R;

public class HeaderFragment extends Fragment{
	private View view = null ;
	private Button rightNavBtn = null;
	private RightNavCallback rightNavCallback = null ;
	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = (View) inflater.inflate(R.layout.header, container, false ) ;
		Activity obj = getActivity();
		if(obj instanceof RightNavCallback){
			rightNavCallback = (RightNavCallback) obj;
		}
		Button back = (Button) view.findViewById(R.id.backBtn) ;
		rightNavBtn = (Button) view.findViewById(R.id.rightNavBtn) ;
		//���ղ�����
		Bundle args = getArguments() ;
		TextView title = (TextView) view.findViewById(R.id.title) ;
		title.setText(args.get("title").toString());
		if(args.get("rigNavTitle") != null){
			rightNavBtn.setVisibility(View.VISIBLE);
			rightNavBtn.setText(args.get("rigNavTitle").toString());
		}
		
		if(!args.getBoolean("showLeftBtn", true)){
			back.setVisibility(View.INVISIBLE);
		}
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getActivity().finish() ; 
			}
		});
		
		rightNavBtn.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				if(rightNavCallback != null){
					rightNavCallback.onRigNavClick(v);
				}
			}
		});
		return view ;
	}

	
	public interface RightNavCallback{
		public void onRigNavClick(View v);
	}
}
