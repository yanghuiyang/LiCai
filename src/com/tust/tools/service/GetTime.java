package com.tust.tools.service;

import java.util.Calendar;

public class GetTime {
	static final Calendar c = Calendar.getInstance();  
    
    public static int getYear(){
    	return c.get(Calendar.YEAR);
    }
    public static int getMonth(){
        //��ȡ���·ݺ��й����һ����
    	return c.get(Calendar.MONTH)+1;
    }
    public static int getWeek(){
    	return c.get(Calendar.DAY_OF_WEEK)-1;
    }
    public static int getDay(){
    	return c.get(Calendar.DAY_OF_MONTH);
    }
    public static int getHour(){
    	return c.get(Calendar.HOUR_OF_DAY);
    }
    public static int getMinute(){
    	return c.get(Calendar.MINUTE);
    }
    public static String getTime(){
    	String hour = getHour()+"";
    	String minute = getMinute()+"";
    	if(hour.length()==1){
    		hour = "0"+hour;
    	}
    	if(minute.length()==1){
    		minute = "0"+minute;
    	}
    	return hour+":"+minute;
    }
    //����ֱ�Ӵ�ϵͳ�õ����·ݺ���ʵ�·���һ�������Ƕ�һ����ŷ�޺��й���ͬ�ģ����������·����õ���ȷ����
    public static int getWeekOfYear(){
    	return getTheWeekOfYear(getYear(), getMonth(),getDay());
    }
    //��ȡָ�����ڵ�����
    public static int getTheWeekOfDay(int year , int month, int day){
        Calendar cd = Calendar.getInstance();
        cd.set(year, month-1, day-1); 
       return cd.get(Calendar.DAY_OF_WEEK); 
    }
    //��ȡָ�������ڵ����еĵڼ�������
    public static int getTheWeekOfYear(int year , int month, int day){
        Calendar cd = Calendar.getInstance();
        cd.set(year, month-1, day-1); 
       return cd.get(Calendar.WEEK_OF_YEAR); 
    }
}
