package com.tust.tools.bean;

public class BWcontent {
	public static final String YEAR = "YEAR";
	public static final String MONTH = "MONTH";
	public static final String WEEK = "WEEK";
	public static final String DAY = "DAY";
	public static final String TIME = "TIME";
	public static final String CONTENT = "CONTENT";
	public static final String PIC = "PIC";
	public static final String COLOR = "COLOR";
	public static final String SIZE = "SIZE";
	
	private int id;
	private int year;//������
	private int month;//������
	private int week;//�����ܣ����ڼ���
	private int day;//������
	private String time;//����ʱ��
	private String content;//��������
	private String pic;//����ͼƬ·��
	private int color;//���±���ɫ
	private float size;//���������С
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getWeek() {
		return week;
	}
	public void setWeek(int week) {
		this.week = week;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public float getSize() {
		return size;
	}
	public void setSize(float size) {
		this.size = size;
	}
	
}
