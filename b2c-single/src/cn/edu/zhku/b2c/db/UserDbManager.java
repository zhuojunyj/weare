package cn.edu.zhku.b2c.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cn.edu.zhku.b2c.vo.UserVo;

public class UserDbManager {
	private DbHelper helper = null ;
	private SQLiteDatabase db = null ;
	public UserDbManager(Context context){
		helper = new DbHelper(context) ;
		db = helper.getWritableDatabase() ;
	}
	
	public void save(UserVo user){
		ContentValues cv = new ContentValues() ;
		cv.put("name", user.getName());
		cv.put("loginId", user.getLoginId());
		cv.put("mobile", user.getMobile()) ;
		cv.put("email", user.getEmail()) ;
		db.beginTransaction();
		db.insert("user", null, cv) ;
		db.setTransactionSuccessful();
		db.endTransaction();
		/*
		db.execSQL("insert into user(name,loginId,mobile,email) values(?,?,?,?)",
				new String[]{user.getName(),user.getLoginId(),user.getMobile(),user.getEmail()});
		*/
	}
	
	public UserVo query(){
		UserVo user = null ;
		Cursor c = db.rawQuery("select * from user", null) ;
		if(c.moveToFirst()){
			user = new UserVo() ;
			user.setName(c.getString(c.getColumnIndex("name")));
			user.setLoginId(c.getString(c.getColumnIndex("loginId")));
			user.setMobile(c.getString(c.getColumnIndex("mobile"))) ;
			user.setEmail(c.getString(c.getColumnIndex("emaile")));
		}
		return user ;
	}
	
	public void close(){
		db.close() ; 
	}
}
