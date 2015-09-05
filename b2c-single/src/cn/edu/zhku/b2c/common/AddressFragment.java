package cn.edu.zhku.b2c.common;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.edu.zhku.b2c.R;

public class AddressFragment extends Fragment{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = (View) inflater.inflate(R.layout.address, container, false) ;
		Bundle bundle = getArguments() ;
		String name = bundle.getString("name") ;
		String tel = bundle.getString("tel") ;
		String addr = bundle.getString("address") ;
		TextView nameField = (TextView) view.findViewById(R.id.name) ;
		nameField.setText(name);
		TextView telField = (TextView) view.findViewById(R.id.tel) ;
		telField.setText(tel);
		TextView address = (TextView) view.findViewById(R.id.textView1) ;
		address.setText(addr);
		return view ;
	}
	
}
