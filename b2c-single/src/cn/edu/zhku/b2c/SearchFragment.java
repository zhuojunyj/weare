package cn.edu.zhku.b2c;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import cn.edu.zhku.b2c.cate.ProductListActivity;
import cn.edu.zhku.b2c.common.BrowseHistoryFragment;
import cn.edu.zhku.b2c.common.HotSearchFragment;
import cn.edu.zhku.b2c.common.SearchHistoryFragment;

public class SearchFragment extends Fragment implements View.OnClickListener{

	private View view = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		view = inflater.inflate(R.layout.search_tab, container, false);
		initHotSearch();
		Button hotSearch = (Button)view.findViewById(R.id.hot);
		hotSearch.setOnClickListener(this);
		
		Button history = (Button)view.findViewById(R.id.history);
		history.setOnClickListener(this);
		
		Button recent = (Button)view.findViewById(R.id.recent);
		recent.setOnClickListener(this);
		
		Button goSearch = (Button)view.findViewById(R.id.go_search);
		goSearch.setOnClickListener(this);
		
		return  view;
	}
	
	public void initHotSearch(){
		HotSearchFragment hotSearchFragment = new HotSearchFragment();
		FragmentManager fragmentManager = getActivity().getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.add(R.id.replace, hotSearchFragment);
		fragmentTransaction.commit();
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(R.id.go_search == v.getId()){
			EditText search = (EditText)view.findViewById(R.id.search);
			String keyword = search.getText().toString();
			Intent intent = new Intent(getActivity(), ProductListActivity.class) ;
			intent.putExtra("title", keyword) ;
			intent.putExtra("keyword", keyword) ;
			getActivity().startActivity(intent);
			return;
		}
		resetButton();
		Button b = (Button)v;
		b.setBackgroundResource(R.drawable.search_tab_s);
		b.setTextColor(getResources().getColor(R.color.btn_color_red));
		int id = b.getId();
		if(R.id.hot == id){//��������
			HotSearchFragment hotSearchFragment = new HotSearchFragment();
			FragmentManager fragmentManager = getActivity().getFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.replace(R.id.replace, hotSearchFragment);
			fragmentTransaction.commit();
		}
		if(R.id.history == id){//������ʷ
			SearchHistoryFragment searchHistoryFragment = new SearchHistoryFragment();
			FragmentManager fragmentManager = getActivity().getFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.replace(R.id.replace, searchHistoryFragment);
			fragmentTransaction.commit();
		}
		if(R.id.recent == id){
			BrowseHistoryFragment browseHistoryFragment = new BrowseHistoryFragment();
			FragmentManager fragmentManager = getActivity().getFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.replace(R.id.replace, browseHistoryFragment);
			fragmentTransaction.commit();
		}
	}
	
	public void resetButton(){
		Button hotSearch = (Button)view.findViewById(R.id.hot);
		hotSearch.setBackgroundResource(R.drawable.search_tab);
		hotSearch.setTextColor(getResources().getColor(R.color.text_color_light));
		
		Button history = (Button)view.findViewById(R.id.history);
		history.setBackgroundResource(R.drawable.search_tab);
		history.setTextColor(getResources().getColor(R.color.text_color_light));
		
		Button recent = (Button)view.findViewById(R.id.recent);
		recent.setBackgroundResource(R.drawable.search_tab);
		recent.setTextColor(getResources().getColor(R.color.text_color_light));
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
}
