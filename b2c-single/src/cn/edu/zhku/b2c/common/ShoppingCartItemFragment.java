package cn.edu.zhku.b2c.common;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.edu.zhku.b2c.R;

public class ShoppingCartItemFragment extends Fragment{
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_shoppingcart_item, container, false);
		return view ;
	}


}
