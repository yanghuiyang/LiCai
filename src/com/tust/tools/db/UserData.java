package com.tust.tools.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tust.tools.bean.JZshouru;
import com.tust.tools.bean.JZzhichu;
import com.tust.tools.bean.User;

public class UserData {
	//数据库名称
		private String DB_NAME="user.db";
		//数据库版本
		private static int DB_VERSION=1;
		private SQLiteDatabase db;
		private UserSqliteHelper dbHelper;
		private Context context;

	    public UserData(Context context){
	    	this.context = context;
	        dbHelper=new UserSqliteHelper(context,DB_NAME, null, DB_VERSION);
	        db= dbHelper.getWritableDatabase();
	    }

		public void close(){
	        db.close();
	        dbHelper.close();
	    }
	    
	 
	    /*
	     * 判断某条是否存在
	     * */
	    public boolean haveUser(int id){
	    	boolean flag=false;
	    	Cursor cursor=db.query("User", null,"ID ='"+id+"'", null, null, null, null);
	    	flag=cursor.moveToFirst();
	    	cursor.close();
	    	return flag;
	    }
	    /*
	     * 检查登陆 用户密码
	     * */   
	    public  boolean checklogin(String account,String pwd){
			boolean flag= false;
			Cursor cursor=db.query("User", null,"username ='"+account+"' AND pwd='"+ pwd+"'", null, null, null, null);
	    	flag=cursor.moveToFirst();
	    	cursor.close();
	    	return flag;	    	
	    }
	    
	    /*
	     * 更新用户表的记录
	     * */	
	    public int UpdateUserInfo(User user,int id){
	        ContentValues values = new ContentValues();
	        values.put("username", user.getUsername());
	        values.put("pwd", user.getPwd());
	        values.put("sex", user.getSex());
	        values.put("tel", user.getTel());
	        int idupdate= db.update("User", values, "ID ='"+id+"'", null);
	        this.close();
	        return idupdate;
	    }
	  
	    /*
	     * 添加支出记录
	     * */
	    public Long SaveUser(User user){
	        ContentValues values = new ContentValues();
	        values.put("username", user.getUsername());
	        values.put("pwd", user.getPwd());
	        values.put("sex", user.getSex());
	        values.put("tel", user.getTel());
	        Long uid = db.insert("USER",null,values);
	        this.close();
	        return uid;
	    }
	    
	    
	    /*
	     * 删除用户表的记录
	     * */
	    public int DelUser(int id){
	        int iddel=  db.delete("User", "ID ="+id, null);
	        this.close();
	        return iddel;
	    }
	    
	    /*
	     * 删除所有记录
	     * */
	    public int delAll(){
	    	int iddel= db.delete("User", null, null);
	        this.close();
	        return iddel;
	    }
}
