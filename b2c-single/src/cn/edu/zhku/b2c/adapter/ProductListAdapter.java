package cn.edu.zhku.b2c.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.edu.zhku.b2c.R;
import cn.edu.zhku.b2c.util.ImageLoader;
import cn.edu.zhku.b2c.util.ImageLoader.LoaderCallback;

public class ProductListAdapter extends BaseAdapter{
	private List<Map<String, Object>> productList = null ;
	private LayoutInflater inflater = null ;
	private ImageLoader imageLoader = null ;
	private ListView view = null ;
	
	public ProductListAdapter(List<Map<String, Object>> productList, Context context, ListView listView){
		this.productList = productList ;
		inflater = LayoutInflater.from(context) ;
		imageLoader = new ImageLoader() ;
		view = listView ;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return productList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return productList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder ;
		if(convertView == null){
			holder = new ViewHolder() ;
			convertView = inflater.inflate(R.layout.product_list_item, null) ;
			holder.image = (ImageView)convertView.findViewById(R.id.prd_img) ;
			holder.title = (TextView)convertView.findViewById(R.id.prd_title) ;
			holder.descr = (TextView)convertView.findViewById(R.id.prd_descr) ;
			holder.price = (TextView)convertView.findViewById(R.id.prd_price) ;
			holder.volume = (TextView)convertView.findViewById(R.id.buy_times) ;
			convertView.setTag(holder);//��ViewHolder����
		}else{
			holder = (ViewHolder)convertView.getTag() ;
		}
		Map<String, Object> prdMap = productList.get(position) ;
		
		
		holder.image.setTag(prdMap.get("image"));
		imageLoader.loadImage((String)prdMap.get("image"), new LoaderCallback(){
			@Override
			public void async(Bitmap bitmap, String imageUrl) {
				ImageView imageView = (ImageView)view.findViewWithTag(imageUrl) ;
				if(imageView != null){
					imageView.setImageBitmap(bitmap);
				}
			}
			
		});
//		Bitmap cacheImage = imageLoader.loadBitmap((String)prdMap.get("image"), new ImageCallback(){
//			@Override
//			public void imageLoaded(Bitmap bitMap, String imageUrl) {
//				ImageView imageView = (ImageView)view.findViewWithTag(imageUrl) ;
//				if(imageView != null){
//					imageView.setImageBitmap(bitMap);
//				}
//			}
//			
//		});
//		if(cacheImage != null){
////			holder.image.setImageDrawable(cacheImage);
//			holder.image.setImageBitmap(cacheImage);
//		}
		
		if(prdMap.get("title") != null){
			holder.title.setText((String)prdMap.get("title"));
		}
		
		if(prdMap.get("descr") != null){
			holder.descr.setText((String)prdMap.get("descr"));
		}
		
		if(prdMap.get("marketPrice") != null){
			holder.price.setText("￥" + prdMap.get("marketPrice"));
		}
		
		if(prdMap.get("salesVolume") != null){
			holder.volume.setText("已有" + prdMap.get("salesVolume") + "人购买");
		}
		
		return convertView;
	}

	public class ViewHolder{
		public ImageView image ;
		public TextView title ;
		public TextView descr ;
		public TextView price ;
		public TextView volume ;
	}
}
