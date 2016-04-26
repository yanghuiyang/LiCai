package com.tust.tools.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tust.tools.bean.JZshouru;
import com.tust.tools.bean.JZzhichu;
//记账数据库操作
public class JZData {
	private SQLiteDatabase db;
    private DBOpenHelper dbHelper;// 创建DBOpenHelper对象
	private Context context;
	 
    public JZData(Context context){
    	this.context = context;
        dbHelper = new DBOpenHelper(context);// 初始化DBOpenHelper对象
        db= dbHelper.getWritableDatabase();
    }
    
    public void close(){
        db.close();
        dbHelper.close();
    }


    /*
     * 获取支出表中的所有数据
     * */
    public ArrayList<JZzhichu> GetZhiChuList(String selection){
        ArrayList<JZzhichu> zhichulist=new ArrayList<JZzhichu>();
    	Cursor cursor=db.query("zhichu", null, selection, null, null, null, "ID DESC");
	    	cursor.moveToFirst();
	    	while(!cursor.isAfterLast()&&(cursor.getString(1)!=null)){
	    		JZzhichu zhichu=new JZzhichu();
	    		zhichu.setZc_Id(cursor.getInt(0));
	    		zhichu.setZc_Item(cursor.getString(1));
	    		zhichu.setZc_SubItem(cursor.getString(2));
	    		zhichu.setZc_Year(cursor.getInt(3));
	    		zhichu.setZc_Month(cursor.getInt(4));
	    		zhichu.setZc_Week(cursor.getInt(5));
	    		zhichu.setZc_Day(cursor.getInt(6));
	    		zhichu.setZc_Time(cursor.getString(7));
	    		zhichu.setZc_Pic(cursor.getString(8));
	    		zhichu.setZc_Count(cursor.getDouble(9));
	    		zhichu.setZc_Beizhu(cursor.getString(10));
                zhichu.setZc_User(cursor.getString(11));//add
	    		zhichulist.add(zhichu);
	    		cursor.moveToNext();
    	}
    	cursor.close();
    	return zhichulist;
    }
    
    /*
     * 获取收入表中的所有数据
     * */
    public ArrayList<JZshouru> GetShouRuList(String selection){
        ArrayList<JZshouru>shourulist=new ArrayList<JZshouru>();
    	Cursor cursor=db.query("shouru", null, selection, null, null, null, "ID DESC");
//        Cursor cursor=db.query(JZSqliteHelper.SHOURU, null, null, null, null, null, "ID DESC");
	    	cursor.moveToFirst();
	    	while(!cursor.isAfterLast()&&(cursor.getString(1)!=null)){
	    		JZshouru shouru=new JZshouru();
	    		shouru.setSr_Id(cursor.getInt(0));
	    		shouru.setSr_Item(cursor.getString(1));
	    		shouru.setSr_Year(cursor.getInt(2));
	    		shouru.setSr_Month(cursor.getInt(3));
	    		shouru.setSr_Week(cursor.getInt(4));
	    		shouru.setSr_Day(cursor.getInt(5));
	    		shouru.setSr_Time(cursor.getString(6));
	    		shouru.setSr_Count(cursor.getDouble(7));
	    		shouru.setSr_Beizhu(cursor.getString(8));
                shouru.setSr_User(cursor.getString(9)); //add
	    		shourulist.add(shouru);
	    		cursor.moveToNext();
    	}
    	cursor.close();
    	return shourulist;
    }
    
    /*
     * 判断某条是否存在
     * */
    public boolean haveZhiChuInfo(int id){
    	boolean flag=false;
    	Cursor cursor=db.query("zhichu", null,"ID ='"+id+"'", null, null, null, null);
    	flag=cursor.moveToFirst();
    	cursor.close();
    	return flag;
    }
    
    /*
     * 更新支出表的记录
     * */
    public int UpdateZhiChuInfo(JZzhichu zhichu,int id){
        ContentValues values = new ContentValues();
        values.put(JZzhichu.ZC_ITEM, zhichu.getZc_Item());
        values.put(JZzhichu.ZC_SUBITEM, zhichu.getZc_SubItem());
        values.put(JZzhichu.ZC_YEAR, zhichu.getZc_Year());
        values.put(JZzhichu.ZC_MONTH, zhichu.getZc_Month());
        values.put(JZzhichu.ZC_WEEK, zhichu.getZc_Week());
        values.put(JZzhichu.ZC_DAY, zhichu.getZc_Day());
        values.put(JZzhichu.ZC_TIME, zhichu.getZc_Time());
        values.put(JZzhichu.ZC_PIC, zhichu.getZc_Pic());
        values.put(JZzhichu.ZC_COUNT, zhichu.getZc_Count());
        values.put(JZzhichu.ZC_BEIZHU, zhichu.getZc_Beizhu());

        values.put(JZzhichu.ZC_USER, zhichu.getZc_User());//add

        int idupdate= db.update("zhichu", values, "ID ='"+id+"'", null);
        this.close();
        return idupdate;
    }
    /*
     * 更新收入表的记录
     * */
    public int UpdateShouRuInfo(JZshouru shouru,int id){
        ContentValues values = new ContentValues();
        values.put(JZshouru.SR_ITEM, shouru.getSr_Item());
        values.put(JZshouru.SR_YEAR, shouru.getSr_Year());
        values.put(JZshouru.SR_MONTH, shouru.getSr_Month());
        values.put(JZshouru.SR_WEEK, shouru.getSr_Week());
        values.put(JZshouru.SR_DAY, shouru.getSr_Day());
        values.put(JZshouru.SR_TIME, shouru.getSr_Time());
        values.put(JZshouru.SR_COUNT, shouru.getSr_Count());
        values.put(JZshouru.SR_BEIZHU, shouru.getSr_Beizhu());

        values.put(JZshouru.SR_USER, shouru.getSr_User());//add

        int idupdate= db.update("shouru", values, "ID ='"+id+"'", null);
        this.close();
        return idupdate;
    }
    /*
     * 添加支出记录
     * */
    public Long SaveZhiChuInfo(JZzhichu zhichu){
        ContentValues values = new ContentValues();
        values.put(JZzhichu.ZC_ITEM, zhichu.getZc_Item());
        values.put(JZzhichu.ZC_SUBITEM, zhichu.getZc_SubItem());
        values.put(JZzhichu.ZC_YEAR, zhichu.getZc_Year());
        values.put(JZzhichu.ZC_MONTH, zhichu.getZc_Month());
        values.put(JZzhichu.ZC_WEEK, zhichu.getZc_Week());
        values.put(JZzhichu.ZC_DAY, zhichu.getZc_Day());
        values.put(JZzhichu.ZC_TIME, zhichu.getZc_Time());
        values.put(JZzhichu.ZC_PIC, zhichu.getZc_Pic());
        values.put(JZzhichu.ZC_COUNT, zhichu.getZc_Count());
        values.put(JZzhichu.ZC_BEIZHU, zhichu.getZc_Beizhu());

        values.put(JZzhichu.ZC_USER, zhichu.getZc_User());//add

        Long uid = db.insert("zhichu", JZzhichu.ZC_YEAR, values);
        this.close();
        return uid;
    }
    
    /*
     * 添加收入记录
     * */
    public Long SaveShouRuInfo(JZshouru shouru){
        ContentValues values = new ContentValues();
        values.put(JZshouru.SR_ITEM, shouru.getSr_Item());
        values.put(JZshouru.SR_YEAR, shouru.getSr_Year());
        values.put(JZshouru.SR_MONTH, shouru.getSr_Month());
        values.put(JZshouru.SR_WEEK, shouru.getSr_Week());
        values.put(JZshouru.SR_DAY, shouru.getSr_Day());
        values.put(JZshouru.SR_TIME, shouru.getSr_Time());
        values.put(JZshouru.SR_COUNT, shouru.getSr_Count()); 
        values.put(JZshouru.SR_BEIZHU, shouru.getSr_Beizhu());

        values.put(JZshouru.SR_USER, shouru.getSr_User());

        Long uid = db.insert("shouru", JZshouru.SR_YEAR, values);
        this.close();
        return uid;
    }
    
    /*
     * 删除支出表的记录
     * */
    public int DelZhiChuInfo(int id){
        int iddel=  db.delete("zhichu", "ID ="+id, null);
        this.close();
        return iddel;
    }
    
    /*
     * 删除所有记录
     * */
    public void delAll(String userName){
    	//JZSqliteHelper.saveYuSuan(context,JZSqliteHelper.YUSUAN_MONTH,JZSqliteHelper.YUSUAN_MONTH, 0);

    	db.delete("zhichu","ZC_USER ="+"'"+userName+"'", null);
    	db.delete("shouru", "SR_USER ="+"'"+userName+"'", null);
    }
    
    /*
     * 删除收入表的记录
     * */
    public int DelShouRuInfo(int id){
        int iddel=  db.delete("shouru", "ID ="+id, null);
        this.close();
        return iddel;
    }


}
