package com.tust.tools.db;

import com.tust.tools.bean.JZzhichu;
import com.tust.tools.bean.User;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class UserSqliteHelper extends SQLiteOpenHelper {
	public Context context;
	
	public UserSqliteHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE IF NOT EXISTS USER"
		 		+ "(id INTEGER PRIMARY KEY AUTOINCREMENT, username VARCHAR(20) not null, pwd VARCHAR(20) not null, sex INTEGER, tel VARCHAR)");
		//添加一个默认账号
		//User user = new User();
		ContentValues values = new ContentValues();
        values.put("username", "admin");
        values.put("pwd", "123456");
        values.put("sex", "1");
        values.put("tel", "15922771234");
        db.insert("USER",null,values);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS USER" );
	}

}