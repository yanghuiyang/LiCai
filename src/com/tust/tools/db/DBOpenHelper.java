package com.tust.tools.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tust.tools.bean.BWcontent;
import com.tust.tools.bean.JZshouru;
import com.tust.tools.bean.JZzhichu;

/**
 * Created by yang on 2016/4/24.
 */
public class DBOpenHelper extends SQLiteOpenHelper {
    private static final int VERSION=1;	//已经做出更新
    private static final String DBNAME="licai.db";

    public DBOpenHelper(Context context){
        super(context,DBNAME,null,VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL("CREATE TABLE IF NOT EXISTS beiwang" +
                  "(" + "ID" + " integer primary key," + BWcontent.YEAR + " Integer," + BWcontent.MONTH + " Integer," + BWcontent.WEEK + " Integer," + BWcontent.DAY + " Integer," + BWcontent.TIME + " varchar," + BWcontent.CONTENT + " varchar," + BWcontent.PIC + " varchar," + BWcontent.COLOR + " Integer," + BWcontent.SIZE + " REAL," + BWcontent.USER + " varchar" +");");

        db.execSQL("CREATE TABLE IF NOT EXISTS expendituretype " +
                "(id INTEGER PRIMARY KEY,username VARCHAR(20) not null ,typename varchar(50) )");
        //初始化数据 支出类型表格
//        db.execSQL("insert into expendituretype(id,username,typename) values(?,?,?)",new String[]{"1","admin","午餐"});
//        db.execSQL("insert into expendituretype(id,username,typename) values(?,?,?)",new String[]{"2","admin","晚餐"});
//        db.execSQL("insert into expendituretype(id,username,typename) values(?,?,?)",new String[]{"3","admin","夜宵"});
//        db.execSQL("insert into expendituretype(id,username,typename) values(?,?,?)",new String[]{"4","admin","生活用品"});
//        ;

        db.execSQL("CREATE TABLE IF NOT EXISTS incometype "
                + "(id INTEGER PRIMARY KEY,username varchar not null ,typename varchar(50))");

        //初始化数据 收入类型表格
//        db.execSQL("insert into incometype(id,username,typename) values(?,?,?)",new String[]{"1","admin","工资"});
//        db.execSQL("insert into incometype(id,username,typename) values(?,?,?)",new String[]{"2","admin","奖金"});
//        db.execSQL("insert into incometype(id,username,typename) values(?,?,?)",new String[]{"3","admin","补贴"});;

        db.execSQL("CREATE TABLE IF NOT EXISTS zhichu " +
                 "(" + "ID" + " integer primary key," + JZzhichu.ZC_ITEM + " varchar," + JZzhichu.ZC_SUBITEM + " varchar," + JZzhichu.ZC_YEAR + " Integer," + JZzhichu.ZC_MONTH + " Integer," + JZzhichu.ZC_WEEK + " Integer," + JZzhichu.ZC_DAY + " Integer," + JZzhichu.ZC_TIME + " varchar," + JZzhichu.ZC_PIC + " varchar," + JZzhichu.ZC_COUNT + " REAL," + JZzhichu.ZC_BEIZHU + " varchar," + JZzhichu.ZC_USER + " varchar" +");");

        db.execSQL("CREATE TABLE IF NOT EXISTS shouru" +
                "(" + "ID" + " integer primary key," + JZshouru.SR_ITEM + " varchar," + JZshouru.SR_YEAR + " Integer," + JZshouru.SR_MONTH + " Integer," + JZshouru.SR_WEEK + " Integer," + JZshouru.SR_DAY + " Integer," + JZshouru.SR_TIME + " varchar," + JZshouru.SR_COUNT + " REAL," + JZshouru.SR_BEIZHU + " varchar," + JZshouru.SR_USER + " varchar" +");");

        db.execSQL("CREATE TABLE IF NOT EXISTS USER"
                + "(username VARCHAR(20) PRIMARY KEY , pwd VARCHAR(20) not null, sex INTEGER, tel VARCHAR,budget INTEGER)");

        //创建预算表
//       db.execSQL("CREATE TABLE IF NOT EXISTS budget "
//               + "(id INTEGER PRIMARY KEY,username varchar not null ,typename varchar(50),year INTEGER,month INTEGER,money INTEGER)");
//        db.execSQL("CREATE TABLE IF NOT EXISTS budget "
//                + "(username varchar not null ,typename varchar(50) not null,year int not null,month int not null,money int，" +
//                " primary key(username,typename,year,month))");
               db.execSQL("CREATE TABLE IF NOT EXISTS budget "
               + "(username varchar not null ,typename varchar(50),year int,month int,money int DEFAULT 0,primary key (username,typename,year,month))");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS beiwang" );
        db.execSQL("DROP TABLE IF EXISTS expendituretype" );
        db.execSQL("DROP TABLE IF EXISTS incometype" );
        db.execSQL("DROP TABLE IF EXISTS zhichu" );
        db.execSQL("DROP TABLE IF EXISTS shouru" );
        db.execSQL("DROP TABLE IF EXISTS USER" );
        db.execSQL("DROP TABLE IF EXISTS budget" );
        onCreate(db);
    }

}
