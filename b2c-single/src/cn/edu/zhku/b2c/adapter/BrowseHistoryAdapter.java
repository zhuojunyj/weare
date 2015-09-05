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

public class BrowseHistoryAdapter extends BaseAdapter{
	private List<Map<String, Object>> data = null ;
	private LayoutInflater inflater = null ;
	private ImageLoader imageLoader = null ;
	private ListView view = null ;
	
	public BrowseHistoryAdapter(List<Map<String, Object>> data, Context context, ListView listView){
		this.data = data ;
		inflater = LayoutInflater.from(context) ;
		imageLoader = new ImageLoader() ;
		view = listView ;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
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
			convertView = inflater.inflate(R.layout.fragment_browse_history_item, null) ;
			holder.productImage = (ImageView)convertView.findViewById(R.id.productImage) ;
			holder.productNm = (TextView)convertView.findViewById(R.id.productNm) ;
			holder.price = (TextView)convertView.findViewById(R.id.price) ;
			holder.sellingPoint = (TextView)convertView.findViewById(R.id.sellingPoint) ;
			convertView.setTag(holder);//��ViewHolder����
		}else{
			holder = (ViewHolder)convertView.getTag() ;
		}
		Map<String, Object> map = data.get(position) ;
		
		holder.productImage.setTag(map.get("image"));
		imageLoader.loadImage((String)map.get("image"), new LoaderCallback(){
			@Override
			public void async(Bitmap bitmap, String imageUrl) {
				ImageView imageView = (ImageView)view.findViewWithTag(imageUrl) ;
				if(imageView != null){
					imageView.setImageBitmap(bitmap);
				}
			}
			
		});
		
		if(map.get("productNm") != null){
			holder.productNm.setText((String)map.get("productNm"));
		}
		
		if(map.get("price") != null){
			holder.price.setText("会员价：￥" + map.get("price"));
		}
		
		if(map.get("sellingPoint") != null){
			holder.sellingPoint.setText((String)map.get("sellingPoint"));
		}
		
		return convertView;
	}

	public class ViewHolder{
		public ImageView productImage ;
		public TextView productNm ;
		public TextView price ;
		public TextView sellingPoint ;
	}
}
