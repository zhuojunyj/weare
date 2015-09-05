package cn.edu.zhku.b2c.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.edu.zhku.b2c.R;

public class OrderListAdapter extends BaseAdapter{
	private List<Map<String,Object>> list ;
	private LayoutInflater inflater ;
	
	public OrderListAdapter(List<Map<String,Object>> list,Context context){
		this.list = list ;
		inflater = LayoutInflater.from(context) ;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size() ;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position ;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder() ;
			convertView = inflater.inflate(R.layout.order_list_item, null) ;
			holder.orderNum = (TextView)convertView.findViewById(R.id.order_num) ;
			holder.orderState = (TextView)convertView.findViewById(R.id.order_state) ;
			holder.orderTotal = (TextView)convertView.findViewById(R.id.order_total) ;
			holder.orderMoney = (TextView)convertView.findViewById(R.id.order_money) ;
			holder.orderTime = (TextView)convertView.findViewById(R.id.textView7) ;
			convertView.setTag(holder);//绑定ViewHolder对象
		}else{
			holder = (ViewHolder)convertView.getTag() ;
		}
		holder.orderNum.setText(list.get(position).get("orderNum").toString()) ;
		holder.orderState.setText(list.get(position).get("orderState").toString()) ;
		holder.orderTotal.setText(list.get(position).get("orderTotal").toString()) ;
		holder.orderMoney.setText(list.get(position).get("orderMoney").toString()) ;
		holder.orderTime.setText(list.get(position).get("orderTime").toString()) ;
        return convertView;
	}
	
	public final class ViewHolder{//优化listview
		public TextView orderNum ;
		public TextView orderState ;
		public TextView orderTotal ;
		public TextView orderMoney ;
		public TextView orderTime ;
	} 

}
