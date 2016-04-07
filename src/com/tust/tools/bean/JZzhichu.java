package com.tust.tools.bean;

import com.tust.tools.service.GetTime;

public class JZzhichu {
	public static final String ZC_ITEM = "ZC_ITEM";
	public static final String ZC_SUBITEM = "ZC_SUBITEM";
	public static final String ZC_YEAR = "ZC_YEAR";
	public static final String ZC_MONTH = "ZC_MONTH";
	public static final String ZC_WEEK = "ZC_WEEK";
	public static final String ZC_DAY = "ZC_DAY";
	public static final String ZC_TIME = "ZC_TIME";
	public static final String ZC_COUNT = "ZC_COUNT";
	public static final String ZC_BEIZHU = "ZC_BEIZHU";
	public static final String ZC_PIC = "ZC_PIC";
	// 支出条目ID
	private int zc_Id;
	// 在那方面支出
	private String zc_Item = "";
	//项目的子项目
	private String zc_SubItem = "";
	// 支出年份
	private int zc_Year = GetTime.getYear();
	// 支出月份
	private int zc_Month = GetTime.getMonth();
	// 支出星期（一月中的某一周）
	private int zc_Week = GetTime.getWeekOfYear();
	// 支出天
	private int zc_Day = GetTime.getDay();
	// 支出时间
	private String zc_Time = GetTime.getHour()+":"+GetTime.getMinute();
	// 支出数量
	private double zc_Count = 0;
	// 支出备注
	private String zc_Beizhu ="";
	//照片位置
	private String zc_Pic = "";
	
	public int getZc_Id() {
		return zc_Id;
	}
	public void setZc_Id(int zc_Id) {
		this.zc_Id = zc_Id;
	}
	public String getZc_Item() {
		return zc_Item;
	}
	public void setZc_Item(String zc_Item) {
		this.zc_Item = zc_Item;
	}
	public String getZc_SubItem() {
		return zc_SubItem;
	}
	public void setZc_SubItem(String zc_SubItem) {
		this.zc_SubItem = zc_SubItem;
	}
	public int getZc_Year() {
		return zc_Year;
	}
	public void setZc_Year(int zc_Year) {
		this.zc_Year = zc_Year;
	}
	public int getZc_Month() {
		return zc_Month;
	}
	public void setZc_Month(int zc_Month) {
		this.zc_Month = zc_Month;
	}
	public int getZc_Week() {
		return zc_Week;
	}
	public void setZc_Week(int zc_Week) {
		this.zc_Week = zc_Week;
	}
	public int getZc_Day() {
		return zc_Day;
	}
	public void setZc_Day(int zc_Day) {
		this.zc_Day = zc_Day;
	}
	public String getZc_Time() {
		return zc_Time;
	}
	public void setZc_Time(String zc_Time) {
		this.zc_Time = zc_Time;
	}
	public double getZc_Count() {
		return zc_Count;
	}
	public void setZc_Count(double zc_Count) {
		this.zc_Count = zc_Count;
	}
	public String getZc_Pic() {
		return zc_Pic;
	}
	public void setZc_Pic(String zc_Pic) {
		this.zc_Pic = zc_Pic;
	}
    public String getZc_Beizhu() {
        return zc_Beizhu;
    }
    public void setZc_Beizhu(String zc_Beizhu) {
        this.zc_Beizhu = zc_Beizhu;
    }

}
