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
     * 获取某年某月预算值
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
//        if (user.getSex() == 1) {
//            if (totalBudget >= 600 && totalBudget <= 1000) {
//                if (type.equals("饮食&通讯")) {
//                    temp=350;
//                  //  return 350;
//                } else if (type.equals("购物")) {
//                    temp= 50;
//                } else if (type.equals("娱乐")) {
//                    temp= 50;
//                } else if (type.equals("书本&其它资料")) {
//                    temp= 50;
//                } else if (type.equals("恋爱")) {
//                    temp= 100;
//                }
//
//            } else if (totalBudget > 1000 && totalBudget <= 1500) {
//                if (type.equals("饮食&通讯")) {
//                    temp= 600;
//                } else if (type.equals("购物")) {
//                    temp= 100;
//                } else if (type.equals("娱乐")) {
//                    temp= 100;
//                } else if (type.equals("书本&其它资料")) {
//                    temp= 60;
//                } else if (type.equals("恋爱")) {
//                    temp= 140;
//                }
//            } else if (totalBudget > 1500) {
//                if (type.equals("饮食&通讯")) {
//                    temp= 800;
//                } else if (type.equals("购物")) {
//                    temp=250;
//                } else if (type.equals("娱乐")) {
//                    temp= 150;
//                } else if (type.equals("书本&其它资料")) {
//                    temp= 100;
//                } else if (type.equals("恋爱")) {
//                    temp= 200;
//                }
//            }
//        } else {
//            if (totalBudget >= 600 && totalBudget <= 1000) {
//                if (type.equals("饮食&通讯")) {
//                    temp= 290;
//                } else if (type.equals("购物")) {
//                    temp= 160;
//                } else if (type.equals("娱乐")) {
//                    temp= 50;
//                } else if (type.equals("书本&其它资料")) {
//                    temp=40;
//                } else if (type.equals("恋爱")) {
//                    temp= 60;
//                }
//
//            } else if (totalBudget > 1000 && totalBudget <= 1500) {
//                if (type.equals("饮食&通讯")) {
//                    temp= 480;
//                } else if (type.equals("购物")) {
//                    temp= 300;
//                } else if (type.equals("娱乐")) {
//                    temp= 80;
//                } else if (type.equals("书本&其它资料")) {
//                    temp= 40;
//                } else if (type.equals("恋爱")) {
//                    temp= 100;
//                }
//            } else if (totalBudget > 1500) {
//                if (type.equals("饮食&通讯")) {
//                    temp= 650;
//                } else if (type.equals("购物")) {
//                    temp= 500;
//                } else if (type.equals("娱乐")) {
//                    temp=  100;
//                } else if (type.equals("书本&其它资料")) {
//                    temp= 100;
//                } else if (type.equals("恋爱")) {
//                    temp=  150;
//                }
//            }
//        }


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
