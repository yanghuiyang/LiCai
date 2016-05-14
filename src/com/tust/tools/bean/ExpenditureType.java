package com.tust.tools.bean;


public class ExpenditureType {
    private int id;//支出类型id
    private String userName;//用户名 id
    private String typeName;//支出类型名称


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
