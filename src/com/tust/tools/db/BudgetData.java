package com.tust.tools.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tust.tools.bean.User;
import com.tust.tools.service.GetTime;

import java.util.List;
import java.util.Map;

public class BudgetData {
    private SQLiteDatabase db;
    private DBOpenHelper dbHelper;// 创建DBOpenHelper对象
    private Context context;

    public BudgetData(Context context) {
        this.context = context;
        dbHelper = new DBOpenHelper(context);// 初始化DBOpenHelper对象
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        db.close();
        dbHelper.close();
    }

    //更新用户分类预算
    public void SaveOrUpdateUserBudget(List<Map<String, String>> mData, String userName) {
        Integer year = GetTime.getYear();
        Integer month = GetTime.getMonth();
        Integer count=0;
        for (Map<String, String> m : mData) {
            for (String k : m.keySet()) {
                if(m.get(k).equals("")||m.get(k).equals("0")){
                    count = 0;
                }else {
                    count = Integer.valueOf(m.get(k));
                }

                db.execSQL("insert or replace into budget values(?,?,?,?,?)",
                        new Object[]{String.valueOf(userName), String.valueOf(k), year, month, count});
            }
        }
    }

    /**
     * 获取某年某月某类型预算值
     *
     */
    public int getUserOneBudget(String userName, String typeName, Integer year, Integer month) {
        Cursor cursor = db.rawQuery("select money from budget where username=? and typename =? and year = ? and month =?",
                new String[]{String.valueOf(userName), String.valueOf(typeName), String.valueOf(year), String.valueOf(month)});
        cursor.moveToFirst();
        if (cursor.getCount() == 0) {
            return 0; //还未设置过预算 直接返回0
        }
        return cursor.getInt(cursor.getColumnIndex("money"));
    }

    /**
     * 计算推荐值
     */
    public Integer getTypeBudget(User user,int budget, String type) {
        int temp = 0;
        int totalBudget =  budget;
        if (user.getSex() == 1) { //man
            if (totalBudget >= 500 && totalBudget <= 1000) {
                if (type.equals("饮食")) {
                    temp= (int)(totalBudget*0.6);
                } else if (type.equals("通讯")) {
                    temp= (int)(totalBudget*0.08);
                }else if (type.equals("购物")) {
                    temp= (int)(totalBudget*0.16);
                } else if (type.equals("娱乐")) {
                    temp= (int)(totalBudget*0.1);
                } else if (type.equals("书本&其它资料")) {
                    temp= (int)(totalBudget*0.06);
                }

            } else if (totalBudget > 1000 && totalBudget <= 1500) {
                if (type.equals("饮食")) {
                    temp= (int)(totalBudget*0.55);
                } else if (type.equals("通讯")) {
                    temp= (int)(totalBudget*0.08);
                }else if (type.equals("购物")) {
                    temp= (int)(totalBudget*0.2);
                } else if (type.equals("娱乐")) {
                    temp= (int)(totalBudget*0.12);
                } else if (type.equals("书本&其它资料")) {
                    temp= (int)(totalBudget*0.05);
                }
            } else if (totalBudget > 1500) {
                if (type.equals("饮食")) {
                    temp= (int)(totalBudget*0.4);
                    //  return 350;
                } else if (type.equals("通讯")) {
                    temp= (int)(totalBudget*0.07);
                }else if (type.equals("购物")) {
                    temp= (int)(totalBudget*0.27);
                } else if (type.equals("娱乐")) {
                    temp= (int)(totalBudget*0.2);
                } else if (type.equals("书本&其它资料")) {
                    temp= (int)(totalBudget*0.06);
                }
            }
        } else {
            if (totalBudget >= 500 && totalBudget <= 1000) {
                if (type.equals("饮食")) {
                    temp= (int)(totalBudget*0.5);
                } else if (type.equals("通讯")) {
                    temp= (int)(totalBudget*0.07);
                }else if (type.equals("购物")) {
                    temp= (int)(totalBudget*0.25);
                } else if (type.equals("娱乐")) {
                    temp= (int)(totalBudget*0.12);
                } else if (type.equals("书本&其它资料")) {
                    temp= (int)(totalBudget*0.06);
                }

            } else if (totalBudget > 1000 && totalBudget <= 1500) {
                    if (type.equals("饮食")) {
                        temp= (int)(totalBudget*0.48);
                    } else if (type.equals("通讯")) {
                        temp= (int)(totalBudget*0.06);
                    }else if (type.equals("购物")) {
                        temp= (int)(totalBudget*0.3);
                    } else if (type.equals("娱乐")) {
                        temp= (int)(totalBudget*0.12);
                    } else if (type.equals("书本&其它资料")) {
                        temp= (int)(totalBudget*0.04);
                    }
            } else if (totalBudget > 1500) {
                    if (type.equals("饮食")) {
                        temp= (int)(totalBudget*0.36);
                    } else if (type.equals("通讯")) {
                        temp= (int)(totalBudget*0.07);
                    }else if (type.equals("购物")) {
                        temp= (int)(totalBudget*0.4);
                    } else if (type.equals("娱乐")) {
                        temp= (int)(totalBudget*0.13);
                    } else if (type.equals("书本&其它资料")) {
                        temp= (int)(totalBudget*0.04);
                    }
            }
        }
        return temp;
    }
}
