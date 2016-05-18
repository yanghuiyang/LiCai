package com.tust.tools.bean;

public class User {
//	private int id ; //ID主键
	private String username;//用户名 主键
	private String pwd ;//密码
	private int sex; //性别 男 1 女 2
	private String tel;//电话号码
	private int budget;//预算
	private String ym;
	private int mxjj;

	public String getYm() {
		return ym;
	}

	public void setYm(String ym) {
		this.ym = ym;
	}

	public int getMxjj() {
		return mxjj;
	}

	public void setMxjj(int mxjj) {
		this.mxjj = mxjj;
	}

	public int getBudget() {
		return budget;
	}
	public void setBudget(int budget) {
		this.budget = budget;
	}


//	public int getId() {
//		return id;
//	}
//	public void setId(int id) {
//		this.id = id;
//	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}

}
