package cn.edu.zhku.b2c;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import cn.edu.zhku.b2c.cate.CateSubActivity;
import cn.edu.zhku.b2c.cutom.CustomAlertDialog;
import cn.edu.zhku.b2c.cutom.CustomProgressDialog;
import cn.edu.zhku.b2c.db.CategoryDbManager;
import cn.edu.zhku.b2c.i.TabListener;
import cn.edu.zhku.b2c.util.ConfigParser;
import cn.edu.zhku.b2c.util.HttpUtil;
import cn.edu.zhku.b2c.vo.CategoryVo;

public class CategoryFragment extends Fragment implements OnItemClickListener {
	private View view = null;
	private ListView listView = null;
	private CustomProgressDialog progressDialog = null;
	private CustomAlertDialog alertDialog = null;
	private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
	private SimpleAdapter categoryAdapter = null;
	private CategoryDbManager dbMgr = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.category_tab, container, false);
		dbMgr = new CategoryDbManager(getActivity());
		initUi();
		loadData();
		listView.setOnItemClickListener(this);
		
		EditText searchBar = (EditText)view.findViewById(R.id.searchView);
		searchBar.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				TabListener tabListener = (TabListener) getActivity();
				tabListener.setTab(1);
			}
		});
		return view;
	}

	public void initUi() {
		listView = (ListView) view.findViewById(R.id.category);
		if (progressDialog == null) {
			progressDialog = CustomProgressDialog.createProgressDialog(
					getActivity(), false);
		}
	}

	public void loadData() {
		String webRoot = ConfigParser.loadConfig(getActivity(), "webRoot");
		new LoadCategoryTask().execute(webRoot
				+ "/app/product/category.json?rootNode=rootNode");
	}

	public class LoadCategoryTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			if (progressDialog != null) {
				progressDialog.show();
			}
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO �ȴ����ݿ�ȡ��û����ȡ��������

			List<CategoryVo> cateList = dbMgr.getByParentId(4);
			if (cateList.size() != 0) {
				for (CategoryVo cate : cateList) {
					Map<String, Object> item = new HashMap<String, Object>();
					item.put("categoryId", cate.getCategoryId());
					item.put("mainTitle", cate.getTitle());
					item.put("subTitle", cate.getSubTitle());
					data.add(item);
				}
				return null;
			}
			String result = HttpUtil.httpPost(arg0[0], null, null);
			return result;
		}

		@Override
		protected void onPostExecute(String args) {
			progressDialog.hide();
			if (args != null) {
				try {
					JSONObject jsonObj = new JSONObject(args);
					if (jsonObj.has("errorObject")) {
						JSONObject errorObject = jsonObj
								.getJSONObject("errorObject");
						String errorMsg = errorObject.getString("errorText");
						String error = new String(
								errorMsg.getBytes("ISO-8859-1"), "UTF-8");
						if (alertDialog == null) {
							alertDialog = new CustomAlertDialog(getActivity());
							Bundle bundle = new Bundle();
							bundle.putString("message", error);
							alertDialog.setArguments(bundle);
							alertDialog
									.show(getActivity().getFragmentManager(),
											"error");
						}
						return;
					}
					List<CategoryVo> categoryList = new ArrayList<CategoryVo>();
					loadToPOJO(jsonObj, categoryList);
					// ���浽���ݿ�
					dbMgr.batchSave(categoryList);
					dbMgr.close();// �ر����ݿ�
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			// ����ui
			categoryAdapter = new SimpleAdapter(getActivity(), data,
					R.layout.category_list_item, new String[] { "mainTitle",
							"subTitle" }, new int[] { R.id.mainTitle,
							R.id.subTitle });
			listView.setAdapter(categoryAdapter);
			categoryAdapter.notifyDataSetChanged();
		}
	}

	public void loadToPOJO(JSONObject json, List<CategoryVo> categoryList) {
		try {
			if (json.has("rootNode") && !json.isNull("rootNode")) {
				JSONArray jsonArr = json.getJSONArray("rootNode");
				int len = jsonArr.length();
				// ȡ��һ��Ŀ¼
				for (int i = 0; i < len; i++) {
					JSONObject jo = (JSONObject) jsonArr.get(i);
					int categoryId = jo.getInt("id");
					String mainTitle = jo.getString("title");
					String subTitle = jo.getString("subTitle");
					Map<String, Object> item = new HashMap<String, Object>();
					item.put("categoryId", categoryId);
					item.put("mainTitle", mainTitle);
					item.put("subTitle", subTitle);
					data.add(item);
					CategoryVo vo = new CategoryVo();
					vo.setCategoryId(categoryId);
					vo.setTitle(mainTitle);
					vo.setSubTitle(subTitle);
					vo.setParentId(4);
					categoryList.add(vo);
					// �ڶ���Ŀ¼
					if (!jo.isNull("children")) {
						JSONArray secondArr = jo.getJSONArray("children");
						for (int j = 0; j < secondArr.length(); j++) {
							JSONObject secondJo = (JSONObject) secondArr.get(j);
							CategoryVo vo1 = new CategoryVo();
							vo1.setCategoryId(secondJo.getInt("id"));
							vo1.setTitle(secondJo.getString("title"));
							vo1.setSubTitle(secondJo.getString("subTitle"));
							vo1.setParentId(categoryId);
							categoryList.add(vo1);
							// ������Ŀ¼
							if (!secondJo.isNull("children")) {
								JSONArray thirdArr = secondJo
										.getJSONArray("children");
								for (int k = 0; k < thirdArr.length(); k++) {
									JSONObject thirdJo = (JSONObject) thirdArr
											.get(k);
									CategoryVo vo2 = new CategoryVo();
									vo2.setCategoryId(thirdJo.getInt("id"));
									vo2.setTitle(thirdJo.getString("title"));
									vo2.setSubTitle(thirdJo
											.getString("subTitle"));
									vo2.setParentId(secondJo.getInt("id"));
									categoryList.add(vo2);
								}
							}
						}
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (progressDialog != null) {
			progressDialog.dismiss();
		}

		if (alertDialog != null) {
			alertDialog.dismiss();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long id) {
		int categoryId = (Integer) data.get(position).get("categoryId") ;
		String title = (String) data.get(position).get("mainTitle") ;
		Intent intent = new Intent(getActivity(), CateSubActivity.class) ;
		intent.putExtra("categoryId", categoryId) ;
		intent.putExtra("title", title) ;
		getActivity().startActivity(intent);
	}

	@Override	
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

}
