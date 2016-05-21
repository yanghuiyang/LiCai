package com.tust.tools.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tust.tools.bean.JZshouru;
import com.tust.tools.bean.JZzhichu;
import com.tust.tools.service.GetTime;

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
     * 获取某月某类型支出总额
     * */
    public int getTypeMonthSpend(String userName,String typeName,Integer year,Integer month){
        int total=0;
        Cursor cursor = db.rawQuery("select ZC_COUNT from zhichu where ZC_USER=? and ZC_ITEM=? And ZC_YEAR = ? and ZC_MONTH=?",
                new String[]{String.valueOf(userName),String.valueOf(typeName), String.valueOf(year), String.valueOf(month) } );
        while (cursor.moveToNext()) {
            total +=Integer.parseInt(cursor.getString(cursor.getColumnIndex("ZC_COUNT")));
        }
        return total;

    }
    /*
   * 获取某月支出总额
   * */
    public int getMonthSpend(String userName,Integer year,Integer month){
        int total=0;
     //   Cursor cursor = db.rawQuery("select SUM(ZC_COUNT) from zhichu where ZC_USER=? And ZC_YEAR = ? and ZC_MONTH=?",
        Cursor cursor = db.rawQuery("select ZC_COUNT from zhichu where ZC_USER=? And ZC_YEAR = ? AND ZC_MONTH=?",
                new String[]{String.valueOf(userName), String.valueOf(year), String.valueOf(month) } );
        while (cursor.moveToNext()) {
//            total = cursor.getInt(0);
            total +=Integer.parseInt(cursor.getString(cursor.getColumnIndex("ZC_COUNT")));
        }
        return total;
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

    /*
     * 用于判断当月记账是否连续3天超出平均值
     * */
    public boolean isBeyond(JZzhichu zhichu, int budget) {
//        boolean flag = false;
        int flag = 0;
        int average=0;//平均值
        if(GetTime.getMonth()!= zhichu.getZc_Month()||zhichu.getZc_Day()<3){ //不在当前月份的记账不考虑 当月前记账两天不考虑
            return false;
        }
        average=(int)Math.floor(budget/30);//假设一个月30天吧
        int dayCount =0;
        for(int i=2;i>=0;i--) {
            String selectionMonth = JZzhichu.ZC_USER + "='" + zhichu.getZc_User() + "'" + " and " + JZzhichu.ZC_YEAR + "=" + GetTime.getYear() + " and " + JZzhichu.ZC_MONTH + "=" + GetTime.getMonth() + " and " + JZzhichu.ZC_DAY + "=" + (zhichu.getZc_Day() - i);
            List<JZzhichu> zhichuMonthList = GetZhiChuList(selectionMonth);
            if (zhichuMonthList != null) {
                for (JZzhichu zhichu2 : zhichuMonthList) {
                    dayCount += zhichu2.getZc_Count();
                }
                if (i == 0) {
                    dayCount += zhichu.getZc_Count();//加上今天这次记账
                }
                if (dayCount > average) {
//                    flag = flag & true;
                    flag ++;
                }
                dayCount = 0;
            }
        }
        if (flag==3){
            return true;
        }
        return false;
    }
}
