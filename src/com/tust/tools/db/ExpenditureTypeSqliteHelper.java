package com.tust.tools.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * Created by yang on 2016/4/23.
 */
public class ExpenditureTypeSqliteHelper extends SQLiteOpenHelper {
    public Context context;

    public ExpenditureTypeSqliteHelper(Context context, String name,
                                       CursorFactory factory, int version) {
        super(context, name, factory, version);
        // TODO Auto-generated constructor stub
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL("CREATE TABLE IF NOT EXISTS expendituretype " +
                "(id INTEGER PRIMARY KEY,username VARCHAR(20) not null ,typename varchar(50) )");

        //初始化数据 支出类型表格
        db.execSQL("insert into expendituretype(id,username,typename) values(?,?,?)",new String[]{"1","admin","午餐"});
        db.execSQL("insert into expendituretype(id,username,typename) values(?,?,?)",new String[]{"2","admin","晚餐"});
        db.execSQL("insert into expendituretype(id,username,typename) values(?,?,?)",new String[]{"3","admin","夜宵"});
        db.execSQL("insert into expendituretype(id,username,typename) values(?,?,?)",new String[]{"4","admin","生活用品"});
;

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS expendituretype" );
        onCreate(db);
    }
}
