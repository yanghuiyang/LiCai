package com.tust.tools.bean;

/**
 * Created by yang on 2016/4/23.
 */
public class IncomeType {

    private String userName;//用户名 id
    private int id;//收入类型id
    private String typeName;//收入类型名称

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
