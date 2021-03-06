package com.tust.tools.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.tust.tools.bean.ExpenditureType;
import com.tust.tools.bean.User;

import java.util.ArrayList;
import java.util.List;


public class ExpenditureTypeData {

    private SQLiteDatabase db;
    private DBOpenHelper dbHelper;// 创建DBOpenHelper对象
    private Context context;

    public ExpenditureTypeData(Context context){
        this.context = context;
        dbHelper = new DBOpenHelper(context);// 初始化DBOpenHelper对象
        db= dbHelper.getWritableDatabase();
    }

    public void close(){
        db.close();
        dbHelper.close();
    }

    /*
     * 更新支出表类型的记录
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

    public boolean haveExpenditureType(String username, String type){
        boolean flag=false;
        Cursor cursor=db.query("expendituretype", null,"username ='"+username+"' AND typeName ='"+type+"'", null, null, null, null);
        flag=cursor.moveToFirst();
        cursor.close();
        return flag;
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


    public void deleteByName(String username,String typename){
        // 执行删除
        db.execSQL("delete from expendituretype where username =? and typename=?", new Object[] { username, typename});
    }

    public void deleteById(int id){
        // 执行删除操作
        db.execSQL("delete from expendituretype where id =? ", new Object[] { id});
    }

    public void initType(User user) {
            db.execSQL("insert into expendituretype values(null,?,?)", new Object[]{user.getUsername(),"饮食"});
            db.execSQL("insert into expendituretype values(null,?,?)", new Object[]{user.getUsername(),"通讯"});
            db.execSQL("insert into expendituretype values(null,?,?)", new Object[]{user.getUsername(),"购物"});
            db.execSQL("insert into expendituretype values(null,?,?)", new Object[]{user.getUsername(),"娱乐"});
            db.execSQL("insert into expendituretype values(null,?,?)", new Object[]{user.getUsername(),"书本&其它资料"});
     //     db.execSQL("insert into expendituretype values(null,?,?)", new Object[]{user.getUsername(),"恋爱"});
    }
}
