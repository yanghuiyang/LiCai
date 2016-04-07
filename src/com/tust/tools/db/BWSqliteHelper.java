package com.tust.tools.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.tust.tools.bean.BWcontent;

public class BWSqliteHelper extends SQLiteOpenHelper {
	
	public static final String BEIWANG = "BEIWANG";// 备忘  表
	
	public BWSqliteHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE IF NOT EXISTS " + 
					BEIWANG + "(" + "ID" + " integer primary key," + 
					BWcontent.YEAR + " Integer," + 
					BWcontent.MONTH + " Integer," + 
					BWcontent.WEEK + " Integer," + 
					BWcontent.DAY + " Integer," + 
					BWcontent.TIME + " varchar," + 
					BWcontent.CONTENT + " varchar," + 
					BWcontent.PIC + " varchar," +
					BWcontent.COLOR + " Integer," +
					BWcontent.SIZE + " REAL" + ");");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS" + BEIWANG);
		onCreate(db);
	}

	public void updateColumn(SQLiteDatabase db, String oldColumn, String newColumn, String typeColumn) {
		try {
			db.execSQL("ALTER TABLE " + BEIWANG + " CHANGE " + oldColumn + " " + newColumn + " " + typeColumn);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * 存储月预算
	 * */
	public static void saveYuSuan(Context context, String filename, String name, int num) {
		SharedPreferences preference = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
		// 获取编辑器
		Editor editor = preference.edit();
		// 数据暂时存放在内存中
		editor.putInt(name, num);
		// 提交修改，将内存中的数据保存至xawx.xml文件中
		editor.commit();
	}
	
	/*
	 * 读取Preference参数
	 */
	public static int readPreferenceFile(Context context,String filename, String name) {
		SharedPreferences preference = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
		int num = preference.getInt(name, 0);
		return num;
	}
}
