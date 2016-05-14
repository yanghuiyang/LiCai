package com.tust.tools.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import com.tust.tools.R;
import com.tust.tools.db.BWData;
import com.tust.tools.service.BWAdapter;
import com.tust.tools.service.DongHua3d;
import com.tust.tools.service.DongHuaYanChi;
import com.tust.tools.service.SDrw;

import android.view.View.OnClickListener;

import android.widget.AdapterView.OnItemClickListener;

import java.io.File;


public class ArticleActivity extends Activity implements OnClickListener {
    //工具箱主界面  图标布局
    private LinearLayout ar1,ar2,ar3, ar4, ar5,ar6,ar7, ar8;
    //3D翻转动画
    private DongHua3d dh3;
    private boolean flag = true;

    // public static boolean isShow = true;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_main);
        initLinearLayout();
    }


    /*
     * 初始化LinearLayout布局
     */
    public void initLinearLayout() {
        ar1 = (LinearLayout) this.findViewById(R.id.linearLayout1);
        ar1.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_selector));
        ar1.setOnClickListener(this);
        ar2 = (LinearLayout) this.findViewById(R.id.linearLayout2);
        ar2.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_selector));
        ar2.setOnClickListener(this);
        ar3 = (LinearLayout) this.findViewById(R.id.linearLayout3);
        ar3.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_selector));
        ar3.setOnClickListener(this);
        ar4 = (LinearLayout) this.findViewById(R.id.linearLayout4);
        ar4.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_selector));
        ar4.setOnClickListener(this);
        ar5 = (LinearLayout) this.findViewById(R.id.linearLayout5);
        ar5.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_selector));
        ar5.setOnClickListener(this);
        ar6 = (LinearLayout) this.findViewById(R.id.linearLayout6);
        ar6.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_selector));
        ar6.setOnClickListener(this);
        ar7 = (LinearLayout) this.findViewById(R.id.linearLayout7);
        ar7.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_selector));
        ar7.setOnClickListener(this);
        ar8 = (LinearLayout) this.findViewById(R.id.linearLayout8);
        ar8.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_selector));
        ar8.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        int i = 0;
        switch (id) {
            case R.id.linearLayout1:
                i = 1;
                break;
            case R.id.linearLayout2:
                i = 2;
                break;
            case R.id.linearLayout3:
                i = 3;
                break;
            case R.id.linearLayout4:
                i = 4;
                break;
            case R.id.linearLayout5:
                i = 5;
                break;
            case R.id.linearLayout6:
                i = 6;
                break;
            case R.id.linearLayout7:
                i = 7;
                break;
            case R.id.linearLayout8:
                i = 8;
                break;
            default:
                break;
        }
        Intent intent = new Intent(this, ArticleDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("id",i);// 将id传送到新建的activity
        startActivity(intent);
    }



    public boolean onKeyDown(int kCode, KeyEvent kEvent) {
        return super.onKeyDown(kCode, kEvent);
    }


    public void showMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}