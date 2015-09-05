package cn.edu.zhku.b2c.common;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cn.edu.zhku.b2c.R;

public class ShoppingCartBarFragment extends Fragment{
	
	private ShoppingcartCallback callback = null;
	private View view = null;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_shoppingcart, container, false);
		setEventListener();
		return view ;
	}

	public void setEventListener(){
		ImageView instantBuy = (ImageView)view.findViewById(R.id.instant_buy);
		instantBuy.setOnClickListener(new InstantBuyClickListener());
		
		ImageView toShoppintCart = (ImageView)view.findViewById(R.id.to_shoppingcart);
		toShoppintCart.setOnClickListener(new ShoppingCartClickListener());
		if(getActivity() instanceof ShoppingcartCallback){
			callback = (ShoppingcartCallback)getActivity();
		}
		
		ImageView cartLogo = (ImageView)view.findViewById(R.id.cart);
		cartLogo.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Activity activity = getActivity();
				activity.setResult(501);
				activity.finish();
			}
		});
		
	}
	
	public void showCartNum(Integer cartNum){
		ImageView cartBgk = (ImageView) view.findViewById(R.id.cart);
		cartBgk.setVisibility(View.VISIBLE);
		
		TextView cartNumText = (TextView) view.findViewById(R.id.cart_num);
		cartNumText.setVisibility(View.VISIBLE);
		cartNumText.setText(cartNum);
	}
	
	public void hideCartNum(){
		ImageView cartBgk = (ImageView) view.findViewById(R.id.cart);
		cartBgk.setVisibility(View.INVISIBLE);
		
		TextView cartNumText = (TextView) view.findViewById(R.id.cart_num);
		cartNumText.setVisibility(View.INVISIBLE);
	}
	
	private class InstantBuyClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			if(callback != null){
				callback.onInstantBuyClick();
			}
		}
	}
	
	private class ShoppingCartClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			if(callback != null){
				callback.onShoppingcartClick();
			}
		}
	}
	
	public interface ShoppingcartCallback{
		public void onInstantBuyClick();
		public void onShoppingcartClick();
	}
}
