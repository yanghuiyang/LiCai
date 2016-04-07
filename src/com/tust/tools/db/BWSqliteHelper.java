package com.tust.tools.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.tust.tools.bean.BWcontent;

public class BWSqliteHelper extends SQLiteOpenHelper {
	
	public static final String BEIWANG = "BEIWANG";// ����  ��
	
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
	 * �洢��Ԥ��
	 * */
	public static void saveYuSuan(Context context, String filename, String name, int num) {
		SharedPreferences preference = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
		// ��ȡ�༭��
		Editor editor = preference.edit();
		// ������ʱ������ڴ���
		editor.putInt(name, num);
		// �ύ�޸ģ����ڴ��е����ݱ�����xawx.xml�ļ���
		editor.commit();
	}
	
	/*
	 * ��ȡPreference����
	 */
	public static int readPreferenceFile(Context context,String filename, String name) {
		SharedPreferences preference = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
		int num = preference.getInt(name, 0);
		return num;
	}
}
