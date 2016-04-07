package com.tust.tools.bean;

import com.tust.tools.service.GetTime;

public class JZshouru {
	public static final String SR_ITEM = "SR_ITEM";
	public static final String SR_YEAR = "SR_YEAR";
	public static final String SR_MONTH = "SR_MONTH";
	public static final String SR_WEEK = "SR_WEEK";
	public static final String SR_DAY = "SR_DAY";
	public static final String SR_TIME = "SR_TIME";
	public static final String SR_COUNT = "SR_COUNT";
	public static final String SR_BEIZHU = "SR_BEIZHU";
	// 收入条目id
	private int sr_Id;
	// 在那方面收入
	private String sr_Item = "";
	//收入年份
	private int sr_Year = GetTime.getYear();;
	//收入月份
	private int sr_Month = GetTime.getMonth();
	//收入周(月中的某周)
	private int sr_Week = GetTime.getWeekOfYear();
	//收入日
	private int sr_Day = GetTime.getDay();
	//收入时间
	private String sr_Time = GetTime.getHour()+":"+GetTime.getMinute();
	//收入总数
	private double sr_Count = 0;
	//收入备注
	private String sr_Beizhu="";
	
	public int getSr_Id() {
		return sr_Id;
	}
	public void setSr_Id(int sr_Id) {
		this.sr_Id = sr_Id;
	}
	public String getSr_Item() {
		return sr_Item;
	}
	public void setSr_Item(String sr_Item) {
		this.sr_Item = sr_Item;
	}
	public int getSr_Year() {
		return sr_Year;
	}
	public void setSr_Year(int sr_Year) {
		this.sr_Year = sr_Year;
	}
	public int getSr_Month() {
		return sr_Month;
	}
	public void setSr_Month(int sr_Month) {
		this.sr_Month = sr_Month;
	}
	public int getSr_Week() {
		return sr_Week;
	}
	public void setSr_Week(int sr_Week) {
		this.sr_Week = sr_Week;
	}
	public int getSr_Day() {
		return sr_Day;
	}
	public void setSr_Day(int sr_Day) {
		this.sr_Day = sr_Day;
	}
	public String getSr_Time() {
		return sr_Time;
	}
	public void setSr_Time(String sr_Time) {
		this.sr_Time = sr_Time;
	}
	public double getSr_Count() {
		return sr_Count;
	}
	public void setSr_Count(double sr_Count) {
		this.sr_Count = sr_Count;
	}
    public String getSr_Beizhu() {
        return sr_Beizhu;
    }
    public void setSr_Beizhu(String sr_Beizhu) {
        this.sr_Beizhu = sr_Beizhu;
    }
}
