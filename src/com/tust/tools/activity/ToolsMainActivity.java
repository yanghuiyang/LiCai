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

public class ToolsMainActivity extends Activity implements OnClickListener,OnLongClickListener,OnTouchListener, OnGestureListener  {
    //工具箱主界面  图标布局
    private LinearLayout jz_ll, bw_ll, js_ll, wz_ll, fy_ll,info_ll,icon_ll,top_bt, user_manage;

    //底部上拉界面
    private SlidingDrawer sd;
    //底部上拉按钮，关于软件按钮 ，退出按钮。
    private Button handlerBt,help_bt,hidden_bt,about_bt,exit_bt;
    //3D翻转动画
    private DongHua3d dh3;
    //获取硬件信息服务
    private PhoneInfoService ps;
    private MessageHandler mh;
    private boolean flag=true;
    //显示当前手机的信息
    private TextView cpu,cpuhz,rom,sdcard,memory,version,phone,system,time;
    //滑动监听
    private GestureDetector mGestureDetector;
    //主界面布局  用来监听滑动事件
    private FrameLayout mainTouch;
    //用来控制全局提醒
    public static boolean isShow = true;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ps = new PhoneInfoService();
        mh = new MessageHandler();
        initLinearLayout();
        initMenuBT();
        initPath();
        initInfoTextView();
        mGestureDetector = new GestureDetector((OnGestureListener) this);
        mainTouch = (FrameLayout)this.findViewById(R.id.main_frame);
        mainTouch.setOnTouchListener(this);
        mainTouch.setLongClickable(true);
        icon_ll.setOnTouchListener(this);
        icon_ll.setLongClickable(true);
        sd = (SlidingDrawer)this.findViewById(R.id.main_slidingDrawer);
        handlerBt=(Button)sd.getHandle();
        //设置SlidingDrawer的监听事件 （ 打开和关闭）
        sd.setOnDrawerOpenListener(new OnDrawerOpenListener(){
            @Override
            public void onDrawerOpened() {
                handlerBt.setBackgroundResource(R.drawable.main_down_pressed);
            }
        });
        
        sd.setOnDrawerCloseListener(new OnDrawerCloseListener(){
            @Override
            public void onDrawerClosed() {
                handlerBt.setBackgroundResource(R.drawable.main_up_pressed);  
            }
        });
        dh3 = new DongHua3d();
        //获取当前是否显示提醒
//        int i = JZSqliteHelper.readPreferenceFile(this, JZSqliteHelper.ISHIDDEN,JZSqliteHelper.ISHIDDEN);
//        if(i==0){
//            hidden_bt.setText("隐藏提醒");
//            isShow =true;
//        }else{
//            hidden_bt.setText("显示提醒");
//            isShow =false;
//        }
    }
    
    String noChange="";
    public void initInfoTextView(){
        cpu = (TextView)this.findViewById(R.id.maintext_cpu);
        String max = ps.getMaxCpu(PhoneInfoService.max);
        cpu.setText("芯片型号："+ps.getCpuName());
        cpuhz = (TextView)this.findViewById(R.id.maintext_maxhz);
        cpuhz.setText("最大频率："+changeCpuHZ(max));
        noChange = cpuhz.getText().toString();
        rom =(TextView)this.findViewById(R.id.maintext_rom);
        String roms[]=ps.getRomMemroy();
        rom.setText("ROM大小："+roms[0]+"GB  剩余大小："+roms[1]+"GB");
        //sdcard = (TextView)this.findViewById(R.id.maintext_sd);
        //sdcard.setText("SD卡大小："+ps.getSDCardMemory()[0]+"GB  剩余大小："+ps.getSDCardMemory()[1]+"GB");
        memory = (TextView)this.findViewById(R.id.maintext_memory);
        memory.setText(ps.getTotalMemory());
        String versions[] = ps.getVersion();
        version = (TextView)this.findViewById(R.id.maintext_version);
        version.setText("安卓版本："+versions[1]);
        phone = (TextView)this.findViewById(R.id.maintext_phonename);
        phone.setText("手机型号："+versions[2]);
        system = (TextView)this.findViewById(R.id.maintext_system);
        system.setText("系统版本："+versions[3]);
        time = (TextView)this.findViewById(R.id.maintext_time);
        time.setText("开机时长："+ps.getTimes());
        new Thread(){
            public void run(){
                while(flag){
                    try {
                        Message msg = Message.obtain();
                        msg.what=1;
                        mh.sendMessage(msg);
                        sleep(3000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
        
        top_bt = (LinearLayout)this.findViewById(R.id.main_top_bt);
        top_bt.setOnClickListener(this);
    }
    
    public String changeCpuHZ(String str){
        try {
            if(str.length()>7){
//            str = str.substring(0,str.length()-4);
                str = str.substring(0,str.length()-6)+"00";
                double d= Double.parseDouble(str)/1000;
                str = d+"GHZ";
            }else{
//            str = str.substring(0,str.length()-4)+"HZ";
                str = str.substring(0,str.length()-6)+"00HZ";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }
  
    public void initMenuBT(){
        help_bt = (Button)this.findViewById(R.id.main_help_bt);
        help_bt.setOnClickListener(this);
//        hidden_bt = (Button)this.findViewById(R.id.main_hidden_bt);
//        hidden_bt.setOnClickListener(this);

        exit_bt = (Button)this.findViewById(R.id.main_exit_bt);
        exit_bt.setOnClickListener(this);
    }
    
    public void initPath(){
        File dirJZ = new File(SDrw.SDPATH+"jizhang");
        File dirBW = new File(SDrw.SDPATH+"beiwang");
//        File dirTQ = new File(SDrw.SDPATH+"weather");
        File dirWZ = new File(SDrw.SDPATH+"weizhi");
//        File dirs[] = new File[]{dirJZ,dirBW,dirTQ,dirWZ};
        File dirs[] = new File[]{dirJZ,dirBW,dirWZ};
//        for(int i=0;i<4;i++){
//            if(!dirs[i].exists()){
//              dirs[i].mkdirs();  
//            }
//        }
        for(int i=0;i<3;i++){
            if(!dirs[i].exists()){
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
        jz_ll.setOnLongClickListener(this);
        bw_ll = (LinearLayout) this.findViewById(R.id.main_ll_2);
        bw_ll.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_selector));
        bw_ll.setOnClickListener(this);
        bw_ll.setOnLongClickListener(this);
        js_ll = (LinearLayout) this.findViewById(R.id.main_ll_3);
        js_ll.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_selector));
        js_ll.setOnClickListener(this);
        js_ll.setOnLongClickListener(this);

        user_manage =(LinearLayout) this.findViewById(R.id.main_ll_4);
        user_manage.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_selector));
        user_manage.setOnClickListener(this);

        info_ll = (LinearLayout)this.findViewById(R.id.main_infolayout);
        info_ll.setVisibility(View.GONE);
        icon_ll = (LinearLayout)this.findViewById(R.id.main_iconlayout);
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
        case R.id.main_top_bt://右上角切换按钮
            if(click==0){
                top_bt.setBackgroundResource(R.drawable.main_top_btl);
                DongHuaYanChi.dongHuaSandE(icon_ll, info_ll, this, handler, R.anim.mainpush_left_out, R.anim.mainpush_left_in, 10);
                click=1;
            }else{
                top_bt.setBackgroundResource(R.drawable.main_top_btr);
                DongHuaYanChi.dongHuaSandE(info_ll, icon_ll, this, handler, R.anim.mainpush_right_out, R.anim.mainpush_right_in, 10);
                click=0;
            }
            break;
        case R.id.main_help_bt:
            String texth ="如果您要了解理财助手每个子功能的特点和使用方法，请在该功能对应的图标上长按不放，程序将会弹出相应的提示信息。\r\n该程序目前适用于Android2.2及以上的版本";            
            new DialogAbout(this,texth);
            sd.animateClose();
            break;
        case R.id.main_exit_bt://底部上拉 退出  按钮
        	sd.animateClose(); 
        	this.finish();
            break;
        default:
            break;
        }
    }
    

    @Override
    public boolean onLongClick(View v) {
        int id = v.getId();
        String text = "";
        dongHua(v);
        switch (id) {
        case R.id.main_ll_1://记账
            text ="该‘记账工具’功能是为了方便日常生活使用，清清楚楚的记录您平时消费信息。" +
            		"\r\n1.该功能可以根据每月的消费和输入情况绘制出消费和支出的走线图，让您一目了然的看到您每月的消费情况。" +
            		"\r\n2.该功能首页的柱形图在分辨率为480*800以下的屏幕上会出现偏移和不完整。以后的版本将会解决该问题。" +
            		"\r\n3.添加记录时每个选项是可以选择的，例如：改变‘类别’就点击‘类别’后面对应的值进行选择，改变‘时间’也是同理。" +
            		"\r\n4.该功能可以加密，加密功能在‘设置’选项中，加密后请记住您的密码，目前没有密码找回功能。";            
            break;
        case R.id.main_ll_2://记事
            text ="该‘备忘记事’功能是为了记录平时生活中的重要事情而设计的，让你不再为忘记重要事情而烦恼。" +
            		"\r\n1.该功能可以保存图片和文字，导出文本到SD卡中，更换背景颜色以及改变字体大小等，操作简便，易于使用。" +
            		"\r\n2.无论是新建或者修改只要输入完后按返回键程序将自动保存输入的内容。" +
                    "\r\n3.。";
            break;
        case R.id.main_ll_3://计算
            text ="该‘简易计算’功能是为了方便日常生活中偶尔遇到比较复杂的混合运算而设计的。" +
            		"\r\n1.目前功能比较简单，以后版本将会增加更多功能。";
            break;
        }
        new DialogAbout(this,text);
        return false;
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
    Handler handler = new Handler();
    public boolean onKeyDown(int kCode, KeyEvent kEvent) {
        switch (kCode) {
        case KeyEvent.KEYCODE_BACK://返回键
            if(sd.isOpened()){//上拉界面是否显示
            	sd.animateClose();
                return false;
            }else{
                exitDialog();
            }
            break;
        case KeyEvent.KEYCODE_MENU://菜单键
           if(sd.isOpened()){//上拉界面是否显示
        	   sd.animateClose();
           }else{
               sd.animateOpen();
           }
            break;
        }
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
    //handler更新界面类
    class MessageHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case 1:
                String cur = ps.getMaxCpu(PhoneInfoService.cur);
                cpuhz.setText(noChange+"  当前频率："+changeCpuHZ(cur));
                break;
            case -1:
                break;
            }
            super.handleMessage(msg);
        }
        
    }
    
    //下面是onTouchListener 和 OnGestureListener的实现方法  用来实现左右滑动
    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    private int click = 0, Min = 10;//划过的最小长度
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1.getX() - e2.getX() > Min && Math.abs(velocityX) > 0) {//Math.abs  float 值的绝对值
            // "向左手势"
            click++;
            if (click == 1) {
                top_bt.setBackgroundResource(R.drawable.main_top_btl);
                DongHuaYanChi.dongHuaSandE(icon_ll, info_ll, this, handler, R.anim.mainpush_left_out, R.anim.mainpush_left_in, 10);
            }else{
                click=1;
            }
        } else if (e2.getX() - e1.getX() > Min && Math.abs(velocityX) > 0) {
            // "向右手势"
            click--;
                if (click == 0) {
                    top_bt.setBackgroundResource(R.drawable.main_top_btr);
                    DongHuaYanChi.dongHuaSandE(info_ll, icon_ll, this, handler, R.anim.mainpush_right_out, R.anim.mainpush_right_in, 10);
                } else {
                    click=0;
                }
        }
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }
}