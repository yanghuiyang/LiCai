//package com.tust.tools.db;
//
//import android.content.Context;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//
///**
// * Created by yang on 2016/4/23.
// */
//public class IncomeTypeSqliteHelper extends SQLiteOpenHelper {
//    public Context context;
//    public IncomeTypeSqliteHelper(Context context, String name,
//                                  SQLiteDatabase.CursorFactory factory, int version) {
//        super(context, name, factory, version);
//        // TODO Auto-generated constructor stub
//        this.context = context;
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        // TODO Auto-generated method stub
//        db.execSQL("CREATE TABLE IF NOT EXISTS incometype "
//                + "(id INTEGER PRIMARY KEY,username varchar not null ,typename varchar(50))");
//
//        //初始化数据 收入类型表格
//        db.execSQL("insert into incometype(id,username,typename) values(?,?,?)",new String[]{"1","admin","工资"});
//        db.execSQL("insert into incometype(id,username,typename) values(?,?,?)",new String[]{"2","admin","奖金"});
//        db.execSQL("insert into incometype(id,username,typename) values(?,?,?)",new String[]{"3","admin","补贴"});;
//
//    }
//
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        // TODO Auto-generated method stub
//        db.execSQL("DROP TABLE IF EXISTS incometype" );
//    }
//
//}
