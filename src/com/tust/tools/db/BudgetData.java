package com.tust.tools.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class BudgetData {
    private SQLiteDatabase db;
    private DBOpenHelper dbHelper;// 创建DBOpenHelper对象
    private Context context;
    public BudgetData(Context context){
        this.context = context;
        dbHelper = new DBOpenHelper(context);// 初始化DBOpenHelper对象
        db= dbHelper.getWritableDatabase();
    }

    public void close(){
        db.close();
        dbHelper.close();
    }
}
