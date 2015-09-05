package cn.edu.zhku.b2c.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cn.edu.zhku.b2c.vo.CategoryVo;

public class CategoryDbManager {
	private DbHelper helper = null ;
	private SQLiteDatabase db = null ;
	
	public CategoryDbManager(Context context){
		helper = new DbHelper(context) ;
		db = helper.getWritableDatabase() ;
	}
	
	//批量保存
	public void batchSave(List<CategoryVo> categoryList){
		for(CategoryVo cate : categoryList){
			ContentValues cv = new ContentValues() ;
			cv.put("categoryId", cate.getCategoryId());
			cv.put("title", cate.getTitle()) ;
			cv.put("subTitle", cate.getSubTitle()) ;
			cv.put("parentId", cate.getParentId()) ;
			db.beginTransaction();
			db.insert("category", null, cv) ;
			db.setTransactionSuccessful();
			db.endTransaction();
		}
	}
	
	//根据parentId取出category
	public List<CategoryVo> getByParentId(int parentId){
		List<CategoryVo> categoryList = new ArrayList<CategoryVo>() ;
		Cursor cursor = db.rawQuery("select * from category where parentId=?", new String[]{parentId+""}) ;
		while(cursor.moveToNext()){
			CategoryVo cate = new CategoryVo() ;
			cate.setCategoryId(cursor.getInt(cursor.getColumnIndex("categoryId")));
			cate.setTitle(cursor.getString(cursor.getColumnIndex("title"))) ;
			cate.setSubTitle(cursor.getString(cursor.getColumnIndex("subTitle")));
			cate.setParentId(parentId);
			categoryList.add(cate) ;
		}
		return categoryList ;
	}
	
	public void close(){
		db.close(); 
	}
}
