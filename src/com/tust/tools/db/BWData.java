package com.tust.tools.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tust.tools.bean.BWcontent;
public class BWData {
	//数据库名称
	private String DB_NAME="beiwang.db";
	//数据库版本
	private static int DB_VERSION=1;
	private SQLiteDatabase db;
	private BWSqliteHelper dbHelper;
	 
    public BWData(Context context){
        dbHelper=new BWSqliteHelper(context,DB_NAME, null, DB_VERSION);
        db= dbHelper.getWritableDatabase();
    }
    
    public void close(){
        db.close();
        dbHelper.close();
    }
    
    /*
     * 获取备忘表中的所有数据
     * */
    public ArrayList<BWcontent> GetBWList(String selection){
        ArrayList<BWcontent> beiwanglist=new ArrayList<BWcontent>();
    	Cursor cursor=db.query(BWSqliteHelper.BEIWANG, null, selection, null, null, null, "ID DESC");
	    	cursor.moveToFirst();
	    	while(!cursor.isAfterLast()&&(cursor.getString(1)!=null)){
	    		BWcontent beiwang=new BWcontent();
	    		beiwang.setId(cursor.getInt(0));
	    		beiwang.setYear(cursor.getInt(1));
	    		beiwang.setMonth(cursor.getInt(2));
	    		beiwang.setWeek(cursor.getInt(3));
	    		beiwang.setDay(cursor.getInt(4));
	    		beiwang.setTime(cursor.getString(5));
	    		beiwang.setContent(cursor.getString(6));
	    		beiwang.setPic(cursor.getString(7));
	    		beiwang.setColor(cursor.getInt(8));
	    		beiwang.setSize(cursor.getFloat(9));
	    		beiwanglist.add(beiwang);
	    		cursor.moveToNext();
    	}
    	cursor.close();
    	close();
    	return beiwanglist;
    }
    
    /*
     * 判断某条是否存在
     * */
    public boolean haveBeiWangInfo(int id){
    	boolean flag=false;
    	Cursor cursor=db.query(BWSqliteHelper.BEIWANG, null,"ID ='"+id+"'", null, null, null, null);
    	flag=cursor.moveToFirst();
    	cursor.close();
    	return flag;
    }
    
    /*
     * 更新备忘表的记录
     * */
    public int UpdateBWInfo(BWcontent beiwang,int id){
        ContentValues values = new ContentValues();
        values.put(BWcontent.YEAR, beiwang.getYear());
        values.put(BWcontent.MONTH, beiwang.getMonth());
        values.put(BWcontent.WEEK, beiwang.getWeek());
        values.put(BWcontent.DAY, beiwang.getDay());
        values.put(BWcontent.TIME, beiwang.getTime());
        values.put(BWcontent.CONTENT, beiwang.getContent());
        values.put(BWcontent.PIC, beiwang.getPic());
        values.put(BWcontent.COLOR, beiwang.getColor());
        values.put(BWcontent.SIZE, beiwang.getSize());
        int idupdate= db.update(BWSqliteHelper.BEIWANG, values, "ID ='"+id+"'", null);
        this.close();
        return idupdate;
    }
    /*
     * 添加备忘记录
     * */
    public Long SaveBWInfo(BWcontent beiwang){
        ContentValues values = new ContentValues();
        values.put(BWcontent.YEAR, beiwang.getYear());
        values.put(BWcontent.MONTH, beiwang.getMonth());
        values.put(BWcontent.WEEK, beiwang.getWeek());
        values.put(BWcontent.DAY, beiwang.getDay());
        values.put(BWcontent.TIME, beiwang.getTime());
        values.put(BWcontent.CONTENT, beiwang.getContent());
        values.put(BWcontent.PIC, beiwang.getPic());
        values.put(BWcontent.COLOR, beiwang.getColor());
        values.put(BWcontent.SIZE, beiwang.getSize());
        Long uid = db.insert(BWSqliteHelper.BEIWANG, BWcontent.YEAR, values);
//        this.close();
        return uid;
    }
    
    /*
     * 删除备忘表的记录
     * */
    public int DelBWInfo(int id){
        int iddel=  db.delete(BWSqliteHelper.BEIWANG, "ID ="+id, null);
//        this.close();
        return iddel;
    }
}
