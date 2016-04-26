package com.tust.tools.activity;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.TextView;
import android.widget.Toast;
import com.tust.tools.R;
import com.tust.tools.dialog.DialogAbout;
//import com.tust.tools.dialog.DialogBWSheZhiMiMa;
//import com.tust.tools.dialog.DialogShuRuMiMa;
import com.tust.tools.service.DongHua3d;
import com.tust.tools.service.DongHuaYanChi;
import com.tust.tools.service.PhoneInfoService;
import com.tust.tools.service.SDrw;

public class ToolsMainActivity extends Activity implements OnClickListener {
    //工具箱主界面  图标布局
    private LinearLayout jz_ll, bw_ll, js_ll, info_ll, icon_ll, user_manage;
    //3D翻转动画
    private DongHua3d dh3;
    private boolean flag = true;

    // public static boolean isShow = true;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initLinearLayout();
        initPath();
        dh3 = new DongHua3d();
    }

    public void initPath() {
        File dirJZ = new File(SDrw.SDPATH + "jizhang");
        File dirBW = new File(SDrw.SDPATH + "beiwang");
//        File dirTQ = new File(SDrw.SDPATH+"weather");
        File dirWZ = new File(SDrw.SDPATH + "weizhi");
//        File dirs[] = new File[]{dirJZ,dirBW,dirTQ,dirWZ};
        File dirs[] = new File[]{dirJZ, dirBW, dirWZ};
        for (int i = 0; i < 3; i++) {
            if (!dirs[i].exists()) {
                dirs[i].mkdirs();
            }
        }
    }

    /*
     * 初始化LinearLayout布局
     */
    public void initLinearLayout() {
        //主界面3个子功能
        jz_ll = (LinearLayout) this.findViewById(R.id.main_ll_1);
        jz_ll.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_selector));
        jz_ll.setOnClickListener(this);

        bw_ll = (LinearLayout) this.findViewById(R.id.main_ll_2);
        bw_ll.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_selector));
        bw_ll.setOnClickListener(this);

        js_ll = (LinearLayout) this.findViewById(R.id.main_ll_3);
        js_ll.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_selector));
        js_ll.setOnClickListener(this);


        user_manage = (LinearLayout) this.findViewById(R.id.main_ll_4);
        user_manage.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_selector));
        user_manage.setOnClickListener(this);

        info_ll = (LinearLayout) this.findViewById(R.id.main_infolayout);
        info_ll.setVisibility(View.GONE);
        icon_ll = (LinearLayout) this.findViewById(R.id.main_iconlayout);
    }

    @Override
    protected void onResume() {
        overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.main_ll_1://记账
                dh3.oneViewDongHua(jz_ll);
                changeActivity(JZMainActivity.class);
                break;
            case R.id.main_ll_2://记事
                dh3.oneViewDongHua(bw_ll);
                changeActivity(BWMainActivity.class);
                break;
            case R.id.main_ll_3://计算
                dh3.oneViewDongHua(js_ll);
                changeActivity(JSMainActivity.class);
                break;
            case R.id.main_ll_4://用户管理
                dh3.oneViewDongHua(user_manage);
                changeActivity(UserEditActivity.class);
                break;
            default:
                break;
        }
    }


    //跳转Activity方法
    public void changeActivity(final Class<?> c) {
        new Thread() {
            public void run() {
                try {
                    sleep(300);
                    Intent intent = new Intent(ToolsMainActivity.this, c);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    ToolsMainActivity.this.startActivity(intent);
                    ToolsMainActivity.this.finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /*
     * 退出弹出框
     */
    public void exitDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String confrimStr = "";
        String cancelStr = "";
        builder.setTitle("是否确认退出小助手？");
        confrimStr = "确定";
        cancelStr = "取消";
        builder.setPositiveButton(confrimStr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ToolsMainActivity.this.finish();
                System.exit(0);
            }
        });
        builder.setNeutralButton(cancelStr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create().show();
    }

    public boolean onKeyDown(int kCode, KeyEvent kEvent) {
        return super.onKeyDown(kCode, kEvent);
    }

    /*
     * 点击图片以后的动画效果
     */
    public void dongHua(View v) {
        v.setAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
    }

    public void showMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
