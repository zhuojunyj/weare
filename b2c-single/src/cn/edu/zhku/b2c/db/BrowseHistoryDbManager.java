package cn.edu.zhku.b2c.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cn.edu.zhku.b2c.vo.ProductVo;

public class BrowseHistoryDbManager {
	private DbHelper helper = null ;
	private SQLiteDatabase db = null ;
	
	public BrowseHistoryDbManager(Context context){
		helper = new DbHelper(context) ;
		db = helper.getWritableDatabase() ;
	}
	
	public List<ProductVo> query(){
		List<ProductVo> productList = new ArrayList<ProductVo>();
		Cursor cursor = db.rawQuery("select * from product", null) ;
		while(cursor.moveToNext()){
			ProductVo productVo = new ProductVo();
			productVo.setProductId(cursor.getInt(cursor.getColumnIndex("productId")));
			productVo.setImage(cursor.getString(cursor.getColumnIndex("image")));
			productVo.setProductNm(cursor.getString(cursor.getColumnIndex("productNm")));
			productVo.setSellingPoint(cursor.getString(cursor.getColumnIndex("sellingPoint")));
			productVo.setPrice(cursor.getString(cursor.getColumnIndex("price")));
			productList.add(productVo);
		}
		return productList;
	}
	
	//±£´æ
	public void saveHistory(ProductVo product){
		Cursor cursor = db.rawQuery("select * from product where productId=?", new String[]{""+product.getProductId()}) ;
		if(cursor.moveToFirst()){
			return;
		}
		ContentValues cv = new ContentValues() ;
		cv.put("productId", product.getProductId());
		cv.put("image", product.getImage());
		cv.put("productNm", product.getProductNm());
		cv.put("sellingPoint", product.getSellingPoint());
		cv.put("price", product.getPrice());
		db.beginTransaction();
		db.insert("product", null, cv) ;
		db.setTransactionSuccessful();
		db.endTransaction();
	}
	
	public void close(){
		db.close(); 
	}
}
