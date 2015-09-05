package cn.edu.zhku.b2c.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper{
	
	private static final String DATABASE_NAME = "imark.db";  
    private static final int DATABASE_VERSION = 1; 
    
	public DbHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION) ;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String cateSql = "create table if not exists category(categoryId int(11) not null,"
				+ "title varchar(30),"
				+ "subTitle varchar(30),"
				+ "parentId int(11));" ;
		db.execSQL(cateSql) ;
		
		String browseSql = "create table if not exists product(productId int(11) not null,"
				+ "image varchar(255),"
				+ "productNm varchar(255),"
				+ "sellingPoint varchar(255),"
				+ "price varchar(50))" ;
		db.execSQL(browseSql) ;
		
		String userSql = "create table if not exists user(name varchar(30),"
				+ "loginId varchar(30),"
				+ "mobile varchar(30),"
				+ "email varchar(30));" ;
		db.execSQL(userSql) ;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
}
