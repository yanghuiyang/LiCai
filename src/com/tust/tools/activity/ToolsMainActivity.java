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
import com.tust.tools.db.JZSqliteHelper;
import com.tust.tools.dialog.DialogAbout;
import com.tust.tools.dialog.DialogBWSheZhiMiMa;
import com.tust.tools.dialog.DialogShuRuMiMa;
import com.tust.tools.service.DongHua3d;
import com.tust.tools.service.DongHuaYanChi;
import com.tust.tools.service.PhoneInfoService;
import com.tust.tools.service.SDrw;

public class ToolsMainActivity extends Activity implements OnClickListener,OnLongClickListener,OnTouchListener, OnGestureListener  {
    //������������  ͼ�겼��
    private LinearLayout jz_ll, bw_ll, js_ll, wz_ll, fy_ll,info_ll,icon_ll,top_bt;
    //�ײ���������
    private SlidingDrawer sd;
    //�ײ�������ť�����������ť ���˳���ť��
    private Button handlerBt,help_bt,hidden_bt,about_bt,exit_bt;
    //3D��ת����
    private DongHua3d dh3;
    //��ȡӲ����Ϣ����
    private PhoneInfoService ps;
    private MessageHandler mh;
    private boolean flag=true;
    //��ʾ��ǰ�ֻ�����Ϣ
    private TextView cpu,cpuhz,rom,sdcard,memory,version,phone,system,time;
    //��������
    private GestureDetector mGestureDetector;
    //�����沼��  �������������¼�
    private FrameLayout mainTouch;
    //��������ȫ������
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
        //����SlidingDrawer�ļ����¼� �� �򿪺͹رգ�
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
        //��ȡ��ǰ�Ƿ���ʾ����
        int i = JZSqliteHelper.readPreferenceFile(this, JZSqliteHelper.ISHIDDEN,JZSqliteHelper.ISHIDDEN);
        if(i==0){
            hidden_bt.setText("��������");
            isShow =true; 
        }else{
            hidden_bt.setText("��ʾ����");
            isShow =false;
        }
    }
    
    String noChange="";
    public void initInfoTextView(){
        cpu = (TextView)this.findViewById(R.id.maintext_cpu);
        String max = ps.getMaxCpu(PhoneInfoService.max);
        cpu.setText("оƬ�ͺţ�"+ps.getCpuName());
        cpuhz = (TextView)this.findViewById(R.id.maintext_maxhz);
        cpuhz.setText("���Ƶ�ʣ�"+changeCpuHZ(max));
        noChange = cpuhz.getText().toString();
        rom =(TextView)this.findViewById(R.id.maintext_rom);
        String roms[]=ps.getRomMemroy();
        rom.setText("ROM��С��"+roms[0]+"GB  ʣ���С��"+roms[1]+"GB");
        //sdcard = (TextView)this.findViewById(R.id.maintext_sd);
        //sdcard.setText("SD����С��"+ps.getSDCardMemory()[0]+"GB  ʣ���С��"+ps.getSDCardMemory()[1]+"GB");
        memory = (TextView)this.findViewById(R.id.maintext_memory);
        memory.setText(ps.getTotalMemory());
        String versions[] = ps.getVersion();
        version = (TextView)this.findViewById(R.id.maintext_version);
        version.setText("��׿�汾��"+versions[1]);
        phone = (TextView)this.findViewById(R.id.maintext_phonename);
        phone.setText("�ֻ��ͺţ�"+versions[2]);
        system = (TextView)this.findViewById(R.id.maintext_system);
        system.setText("ϵͳ�汾��"+versions[3]);
        time = (TextView)this.findViewById(R.id.maintext_time);
        time.setText("����ʱ����"+ps.getTimes());
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
        hidden_bt = (Button)this.findViewById(R.id.main_hidden_bt);
        hidden_bt.setOnClickListener(this);
        about_bt = (Button)this.findViewById(R.id.main_about_bt);
        about_bt.setOnClickListener(this);
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
     * ��ʼ��LinearLayout����
     */
    public void initLinearLayout() {
    	//������3���ӹ���
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
        case R.id.main_ll_1://����
        	int mi1 = JZSqliteHelper.readPreferenceFile(this,JZSheZhiActivity.JZMIMA,JZSheZhiActivity.JZMIMA);
        	dh3.oneViewDongHua(jz_ll);
        	if(mi1==0){
                changeActivity(JZMainActivity.class);
            }else{
            	new DialogShuRuMiMa(this, this,JZSheZhiActivity.JZMIMA);
            }
            break;
        case R.id.main_ll_2://����
        	int mi2 = JZSqliteHelper.readPreferenceFile(this,DialogBWSheZhiMiMa.BWMIMA,DialogBWSheZhiMiMa.BWMIMA);
        	dh3.oneViewDongHua(bw_ll);
        	if(mi2==0){
                changeActivity(BWMainActivity.class);
            }else{
            	new DialogShuRuMiMa(this, this,DialogBWSheZhiMiMa.BWMIMA);
            }
            break;
        case R.id.main_ll_3://����
        	dh3.oneViewDongHua(js_ll);
            changeActivity(JSMainActivity.class);
            break;
        case R.id.main_top_bt://���Ͻ��л���ť
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
            String texth ="�����Ҫ�˽��������ÿ���ӹ��ܵ��ص��ʹ�÷��������ڸù��ܶ�Ӧ��ͼ���ϳ������ţ����򽫻ᵯ����Ӧ����ʾ��Ϣ��\r\n�ó���Ŀǰ������Android2.2�����ϵİ汾";            
            new DialogAbout(this,texth);
            sd.animateClose();
            break;
        case R.id.main_hidden_bt:
            sd.animateClose();
            if(JZSqliteHelper.readPreferenceFile(this, JZSqliteHelper.ISHIDDEN,JZSqliteHelper.ISHIDDEN)==0){
              //��������洢��ǰ�Ƿ���ʾ����   ��ʾΪ0������ʾΪ1
                JZSqliteHelper.saveYuSuan(this,JZSqliteHelper.ISHIDDEN,JZSqliteHelper.ISHIDDEN,1);
                isShow = false;
                hidden_bt.setText("��ʾ����");
            }else{
                JZSqliteHelper.saveYuSuan(this,JZSqliteHelper.ISHIDDEN,JZSqliteHelper.ISHIDDEN,0);
                isShow = true;
                hidden_bt.setText("��������");
            }
            break;
        case R.id.main_about_bt://�ײ����� ���� ��ť
        	String text ="��������ּ�����N��ʵ�ù��ܣ�" +
        			"\r\n1.���˹���     2.�������¡�" +
        	        "\r\n3.���׼���    " +
        			"\r\n��ӭ���������������" +
        			"\r\nQQ��xxxxx" +
        			"\r\n�����ѧ"+
        			"\r\n                      ����汾 v1.0" ;
        	new DialogAbout(this,text);
        	sd.animateClose();
            break;
        case R.id.main_exit_bt://�ײ����� �˳�  ��ť
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
        case R.id.main_ll_1://����
            text ="�á����˹��ߡ�������Ϊ�˷����ճ�����ʹ�ã���������ļ�¼��ƽʱ������Ϣ��" +
            		"\r\n1.�ù��ܿ��Ը���ÿ�µ����Ѻ�����������Ƴ����Ѻ�֧��������ͼ������һĿ��Ȼ�Ŀ�����ÿ�µ����������" +
            		"\r\n2.�ù�����ҳ������ͼ�ڷֱ���Ϊ480*800���µ���Ļ�ϻ����ƫ�ƺͲ��������Ժ�İ汾�����������⡣" +
            		"\r\n3.��Ӽ�¼ʱÿ��ѡ���ǿ���ѡ��ģ����磺�ı䡮��𡯾͵������𡯺����Ӧ��ֵ����ѡ�񣬸ı䡮ʱ�䡯Ҳ��ͬ��" +
            		"\r\n4.�ù��ܿ��Լ��ܣ����ܹ����ڡ����á�ѡ���У����ܺ����ס�������룬Ŀǰû�������һع��ܡ�";            
            break;
        case R.id.main_ll_2://����
            text ="�á��������¡�������Ϊ�˼�¼ƽʱ�����е���Ҫ�������Ƶģ����㲻��Ϊ������Ҫ��������ա�" +
            		"\r\n1.�ù��ܿ��Ա���ͼƬ�����֣������ı���SD���У�����������ɫ�Լ��ı������С�ȣ�������㣬����ʹ�á�" +
            		"\r\n2.�������½������޸�ֻҪ������󰴷��ؼ������Զ�������������ݡ�" +
                    "\r\n3.�ù��ܿ��Լ��ܣ����ܹ����ڲ˵�ѡ���У����ܺ����ס�������룬Ŀǰû�������һع��ܡ�";            
            break;
        case R.id.main_ll_3://����
            text ="�á����׼��㡯������Ϊ�˷����ճ�������ż�������Ƚϸ��ӵĻ���������Ƶġ�" +
            		"\r\n1.Ŀǰ���ܱȽϼ򵥣��Ժ�汾�������Ӹ��๦�ܡ�"+
                    "\r\n2.��׿���������bug���ܻ���ڣ����ȵ��µģ�";            
            break;
        }
        new DialogAbout(this,text);
        return false;
    }

    //��תActivity����
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
     * �˳�������
     */
    public void exitDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String confrimStr = "";
        String cancelStr = "";
        builder.setTitle("�Ƿ�ȷ���˳�С���֣�");
        confrimStr = "ȷ��";
        cancelStr = "ȡ��";
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
        case KeyEvent.KEYCODE_BACK://���ؼ�
            if(sd.isOpened()){//���������Ƿ���ʾ
            	sd.animateClose();
                return false;
            }else{
                exitDialog();
            }
            break;
        case KeyEvent.KEYCODE_MENU://�˵���
           if(sd.isOpened()){//���������Ƿ���ʾ
        	   sd.animateClose();
           }else{
               sd.animateOpen();
           }
            break;
        }
        return super.onKeyDown(kCode, kEvent);
    }
    
    /*
     * ���ͼƬ�Ժ�Ķ���Ч��
     */
    public void dongHua(View v) {
        v.setAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
    }

    public void showMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    //handler���½�����
    class MessageHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case 1:
                String cur = ps.getMaxCpu(PhoneInfoService.cur);
                cpuhz.setText(noChange+"  ��ǰƵ�ʣ�"+changeCpuHZ(cur));
                break;
            case -1:
                break;
            }
            super.handleMessage(msg);
        }
        
    }
    
    //������onTouchListener �� OnGestureListener��ʵ�ַ���  ����ʵ�����һ���
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

    private int click = 0, Min = 10;//��������С����
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1.getX() - e2.getX() > Min && Math.abs(velocityX) > 0) {//Math.abs  float ֵ�ľ���ֵ
            // "��������"
            click++;
            if (click == 1) {
                top_bt.setBackgroundResource(R.drawable.main_top_btl);
                DongHuaYanChi.dongHuaSandE(icon_ll, info_ll, this, handler, R.anim.mainpush_left_out, R.anim.mainpush_left_in, 10);
            }else{
                click=1;
            }
        } else if (e2.getX() - e1.getX() > Min && Math.abs(velocityX) > 0) {
            // "��������"
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