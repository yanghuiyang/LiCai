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
		private SQLiteDatabase db;
		private DBOpenHelper dbHelper;// 创建DBOpenHelper对象
		private Context context;

	    public UserData(Context context){
	    	this.context = context;
			dbHelper = new DBOpenHelper(context);// 初始化DBOpenHelper对象
	        db= dbHelper.getWritableDatabase();
	    }

		public void close(){
	        db.close();
	        dbHelper.close();
	    }
	    
	 
	    /*
	     * 判断某条是否存在
	     * */
	    public boolean haveUser(String username){
	    	boolean flag=false;
	    	Cursor cursor=db.query("User", null,"username ='"+username+"'", null, null, null, null);
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
 * 根据用户名查询用户信息
 * */
	public User getUserByUserName(String userName){
		User user = new User();
		Cursor cursor=db.query("User", null,"username ='"+userName+"'", null, null, null, null);
		cursor.moveToFirst();
		user.setUsername(userName);
		user.setPwd(cursor.getString(cursor.getColumnIndex("pwd")));
		user.setSex(cursor.getInt(cursor.getColumnIndex("sex")));
		user.setTel(cursor.getString(cursor.getColumnIndex("tel")));
		user.setBudget(cursor.getInt(cursor.getColumnIndex("budget")));
		return user;
	}
	    /*
	     * 更新用户表的记录
	     * */	
	    public int UpdateUserInfo(User user){
	        ContentValues values = new ContentValues();
	       // values.put("username", user.getUsername());
	        values.put("pwd", user.getPwd());
	        values.put("sex", user.getSex());
	        values.put("tel", user.getTel());
			values.put("budget", user.getBudget());
	        int idupdate= db.update("User", values, "username ='"+user.getUsername()+"'", null);
	        this.close();
	        return idupdate;
	    }
	  
	    /*
	     * 添加用户
	     * */
	    public Long SaveUser(User user){
	        ContentValues values = new ContentValues();
	        values.put("username", user.getUsername());
	        values.put("pwd", user.getPwd());
	        values.put("sex", user.getSex());
	        values.put("tel", user.getTel());
			values.put("budget",user.getBudget());
	        Long uid = db.insert("USER",null,values);
	        this.close();
	        return uid;
	    }
	    
	    
//	    /*
//	     * 删除用户表的记录
//	     * */
//	    public int DelUser(int id){
//	        int iddel=  db.delete("User", "ID ="+id, null);
//	        this.close();
//	        return iddel;
//	    }
	    
	    /*
	     * 删除所有记录
	     * */
	    public int delAll(){
	    	int iddel= db.delete("User", null, null);
	        this.close();
	        return iddel;
	    }
}
