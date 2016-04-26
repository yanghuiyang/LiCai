package com.tust.tools.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.tust.tools.bean.IncomeType;
import android.content.ContentValues;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yang on 2016/4/23.
 */
public class IncomeTypeData {

    private SQLiteDatabase db;
    private DBOpenHelper dbHelper;// 创建DBOpenHelper对象
    private Context context;

    public IncomeTypeData(Context context){
        this.context = context;
      //  dbHelper=new IncomeTypeSqliteHelper(context,DB_NAME, null, DB_VERSION);
        dbHelper = new DBOpenHelper(context);// 初始化DBOpenHelper对象
        db= dbHelper.getWritableDatabase();
    }

    public void close(){
        db.close();
        dbHelper.close();
    }

    /*
     * 更新收入表的记录
     * */
    public int UpdateIncomeType(IncomeType type){
        ContentValues values = new ContentValues();
        values.put("username", type.getUserName());
        values.put("id", type.getId());
        values.put("typename", type.getTypeName());
        int idupdate= db.update("incometype", values, "id ='"+type.getId()+"'", null);
        this.close();
        return idupdate;
    }

    /*
     * 添加支出类型
     * */
    public Long SaveIncomeType(IncomeType type){
        ContentValues values = new ContentValues();
        values.put("username", type.getUserName());
     //   values.put("id", type.getId());
        values.put("typename", type.getTypeName());
        Long uid = db.insert("incometype",null,values);
        this.close();
        return uid;
    }
    /**
     * 获取类型 随机 仅用于添加记账明细时 设置默认类型
     * param id
     * @return
     * */
    public String getFirstTypeByUserName(String userName){
        Cursor cursor = db.rawQuery("select typename from incometype where username=?",new String[]{String.valueOf(userName) } );
        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndex("typename"));
    }
    /**
     * 获取类型名数组
     * param id
     * @return
     * */
    public List<String> getTypesByUserName(String userName){
        List<String> lisString = new ArrayList<String>();// 创建集合对象
        Cursor cursor = db.rawQuery("select typename from incometype where username=?",new String[]{String.valueOf(userName) } );
        while (cursor.moveToNext()) {// 访问Cursor中的最后一条数据
            lisString.add(cursor.getString(cursor.getColumnIndex("typename")));
        }
        return lisString;

    }

    public void deleteByName(String username,String typename){
        // 执行rr   删除操作
        db.execSQL("delete from incometype where username =? and typename=?", new Object[] { username, typename});
    }

    public void deleteById(int id){
        // 执行删除操作
        db.execSQL("delete from incometype where id =? ", new Object[] { id});
    }
}
