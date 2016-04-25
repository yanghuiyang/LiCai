package com.tust.tools.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.tust.tools.bean.ExpenditureType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yang on 2016/4/23.
 */
public class ExpenditureTypeData {
    //数据库名称
    //private String DB_NAME="ExpenditureType.db";

    //数据库版本
   // private static int DB_VERSION=1;
    private SQLiteDatabase db;
 //   private ExpenditureTypeSqliteHelper dbHelper;
    private DBOpenHelper dbHelper;// 创建DBOpenHelper对象
    private Context context;

    public ExpenditureTypeData(Context context){
        this.context = context;
        //dbHelper=new ExpenditureTypeSqliteHelper(context,DB_NAME, null, DB_VERSION);
        dbHelper = new DBOpenHelper(context);// 初始化DBOpenHelper对象
        db= dbHelper.getWritableDatabase();
    }

    public void close(){
        db.close();
        dbHelper.close();
    }

    /*
     * 更新支出表的记录
     * */
    public int UpdateExpenditureType(ExpenditureType type){
        ContentValues values = new ContentValues();
        values.put("username", type.getUserName());
     //   values.put("id", type.getId());
        values.put("typename", type.getTypeName());
        int idupdate= db.update("expendituretype", values, "id ='"+type.getId()+"'", null);
        this.close();
        return idupdate;
    }

    /*
     * 添加支出类型
     * */
    public Long SaveExpenditureType(ExpenditureType type){
        ContentValues values = new ContentValues();
        values.put("username", type.getUserName());
        //values.put("id", type.getId());
        values.put("typename", type.getTypeName());
        Long uid = db.insert("expendituretype",null,values);
        this.close();
        return uid;
    }

    /**
     * 获取类型 随机 仅用于添加记账明细时 设置默认类型
     * param id
     * @return
     * */
    public String getFirstTypeByUserName(String userName){
        Cursor cursor = db.rawQuery("select typename from expendituretype where username=? limit 1",new String[]{String.valueOf(userName) } );
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
        Cursor cursor = db.rawQuery("select typename from expendituretype where username=?",new String[]{String.valueOf(userName) } );
        while (cursor.moveToNext()) {// 访问Cursor中的最后一条数据
            lisString.add(cursor.getString(cursor.getColumnIndex("typename")));
        }
        return lisString;

    }
    /**
     * 删除收入类型 多选
     * @param  ids
     */
    public void delete(Integer... ids){
        if (ids.length > 0)// 判断是否存在要删除的id
        {
            StringBuffer sb = new StringBuffer();// 创建StringBuffer对象
            for (int i = 0; i < ids.length-1; i++)// 遍历要删除的id集合
            {
                sb.append('?').append(',');// 将删除条件添加到StringBuffer对象中
            }
            sb.deleteCharAt(sb.length() - 1);// 去掉最后一个“,“字符
            // 执行删除
            db.execSQL("delete from expendituretype where id in (?) and no in (" + sb + ")",
                    (Object[]) ids);
        }
    }

    public void deleteByName(String username,String typename){
        // 执行删除
        db.execSQL("delete from expendituretype where username =? and typename=?", new Object[] { username, typename});
    }

    public void deleteById(int id){
        // 执行删除操作
        db.execSQL("delete from expendituretype where id =? ", new Object[] { id});
    }

}
