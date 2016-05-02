package com.tust.tools.bean;

/**
 * Created by yang on 2016/5/2.
 */
public class Budget {
  //  private int id;//支出类型id
    private String userName;//用户名 id
    private String typeName;//支出类型名称
    private Integer year;//预算年
    private Integer month;//预算月
    private Integer money;//支出类型名称

//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }
}
