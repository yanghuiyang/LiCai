package com.tust.tools.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.tust.tools.bean.JZshouru;
import com.tust.tools.bean.JZzhichu;

public class JZSqliteHelper extends SQLiteOpenHelper {
	
	public static final String ZHICHU = "ZHICHU";// ֧��
	public static final String SHOURU = "SHOURU";// ����
	public static final String YUSUAN_MONTH = "YUSUAN_MONTH";//��Ԥ��
	public static final String ISHIDDEN ="HIDDEN";
	public Context context;
	public JZSqliteHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	    saveYuSuan(context,YUSUAN_MONTH,YUSUAN_MONTH,3000);
	    //��������洢��ǰ�Ƿ���ʾ����   ��ʾΪ1������ʾΪ0
	    saveYuSuan(context,ISHIDDEN,ISHIDDEN,1);
	    db.execSQL("CREATE TABLE IF NOT EXISTS " + 
					ZHICHU + "(" + "ID" + " integer primary key," + 
					JZzhichu.ZC_ITEM + " varchar," + 
					JZzhichu.ZC_SUBITEM + " varchar," + 
					JZzhichu.ZC_YEAR + " Integer," + 
					JZzhichu.ZC_MONTH + " Integer," + 
					JZzhichu.ZC_WEEK + " Integer," + 
					JZzhichu.ZC_DAY + " Integer," + 
					JZzhichu.ZC_TIME + " varchar," + 
					JZzhichu.ZC_PIC + " varchar," + 
					JZzhichu.ZC_COUNT + " REAL," + 
					JZzhichu.ZC_BEIZHU + " varchar" + ");");
			
			db.execSQL("CREATE TABLE IF NOT EXISTS " + 
					SHOURU + "(" + "ID" + " integer primary key," + 
					JZshouru.SR_ITEM + " varchar," + 
					JZshouru.SR_YEAR + " Integer," + 
					JZshouru.SR_MONTH + " Integer," + 
					JZshouru.SR_WEEK + " Integer," + 
					JZshouru.SR_DAY + " Integer," + 
					JZshouru.SR_TIME + " varchar," + 
					JZshouru.SR_COUNT + " REAL," + 
					JZshouru.SR_BEIZHU + " varchar" + ");");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS" + ZHICHU);
			db.execSQL("DROP TABLE IF EXISTS" + SHOURU);
		onCreate(db);
	}

	public void updateColumn(SQLiteDatabase db, String oldColumn, String newColumn, String typeColumn) {
		try {
			db.execSQL("ALTER TABLE " + ZHICHU + " CHANGE " + oldColumn + " " + newColumn + " " + typeColumn);
			db.execSQL("ALTER TABLE " + SHOURU + " CHANGE " + oldColumn + " " + newColumn + " " + typeColumn);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * �洢��Ԥ����߼�������
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
