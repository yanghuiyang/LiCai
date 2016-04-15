package com.tust.tools.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tust.tools.R;
import com.tust.tools.db.JZData;
import com.tust.tools.db.JZSqliteHelper;
import com.tust.tools.dialog.DialogAbout;

public class JZSheZhiActivity extends Activity implements OnClickListener {
	//密码编辑框
	private EditText mima1,mima2;
	//设置密码标题
	private TextView jiami_text;
	//密码编辑界面的保存，取消，删除按钮
	private Button saveBt,cancelBt,shanchuBt;
	//设置选项  加密，清楚数据，关于软件
	private RelativeLayout jiami_rl,qingchu_rl,guanyu_rl;
	//编辑加密界面
	private LinearLayout bianjijiami;
	//mi 当前密码  ，  tempmi密码标识
	private int mi=0,tempmi=-1;
	//记账数据库管理
	private JZData jzdh;
	
	public static final String JZMIMA = "JZMIMA";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jz_shezhi);
		jzdh = new JZData(this);
		init();
		initJiaMi();
		jiami_text = (TextView)this.findViewById(R.id.jz_shezhi_jiami_text);
		mi = JZSqliteHelper.readPreferenceFile(this,JZMIMA,JZMIMA);
		if(mi==0){//密码为0则当前没密码
			jiami_text.setText("程序加密");
		}else{
			jiami_text.setText("修改或删除密码");
		}
	}
	
	private void init(){
		jiami_rl = (RelativeLayout)this.findViewById(R.id.jz_shezhi_jiami_rl);	
		jiami_rl.setOnClickListener(this);
		qingchu_rl = (RelativeLayout)this.findViewById(R.id.jz_shezhi_qingchu_rl);	
		qingchu_rl.setOnClickListener(this);
		guanyu_rl = (RelativeLayout)this.findViewById(R.id.jz_shezhi_about_rl);	
		guanyu_rl.setOnClickListener(this);
		bianjijiami = (LinearLayout)this.findViewById(R.id.jz_shezhi_bianjimima_ll);
		bianjijiami.setVisibility(View.GONE);
	}
	
	private void initJiaMi(){
		mima1 = (EditText) this.findViewById(R.id.jz_shezhi_jiami1_et);
		mima2 = (EditText) this.findViewById(R.id.jz_shezhi_jiami2_et);
		saveBt = (Button) this.findViewById(R.id.jz_shezhi_queding_bt);
		saveBt.setOnClickListener(this);
		cancelBt = (Button) this.findViewById(R.id.jz_shezhi_quxiao_bt);
		cancelBt.setOnClickListener(this);
		shanchuBt = (Button)this.findViewById(R.id.jz_shezhi_shanchu_bt);
		shanchuBt.setOnClickListener(this);
		shanchuBt.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.jz_shezhi_jiami_rl://程序加密选项
			mi = JZSqliteHelper.readPreferenceFile(this,JZMIMA,JZMIMA);
			if(bianjijiami.isShown()){
				bianjijiami.setVisibility(View.GONE);
			}else{
				bianjijiami.setVisibility(View.VISIBLE);
				if(mi==0){//当没有密码时
					jiami_text.setText("程序加密");
					shanchuBt.setVisibility(View.INVISIBLE);
				}else{
					tempmi = mi;//当有密码时把密码赋值个tempmi，以便于点击确定按钮时正确判断
					jiami_text.setText("修改或删除密码");
					mima1.setHint("请输入旧密码");
					mima2.setVisibility(View.GONE);//密码确认框不可见
					shanchuBt.setVisibility(View.INVISIBLE);//删除密码按钮不可见
				}
			}
			break;
		case R.id.jz_shezhi_qingchu_rl :
			showDialog();
			break;
		case R.id.jz_shezhi_about_rl :
			String text = "该记账器功能比较简单，使用方便。" +
		            "\r\n欢迎大家提出宝贵意见。" +
        			"\r\nQQ：xxxxx" +
        			"\r\n重庆大学"+
        			"\r\n                      软件版本 v1.0" ;
			new DialogAbout(this,text);
			break;
		case R.id.jz_shezhi_queding_bt :
			if(mi==0||tempmi==-1){//当没有密码时
				if(mima1.getText().toString().length()==0||mima2.getText().toString().length()==0){
					showMsg("输入不能为空");
					return;
				}
				if(mima1.getText().toString().length()!=6||mima2.getText().toString().length()!=6){
					showMsg("密码必须为六位数");
					return;
				}
				if(!mima1.getText().toString().equals(mima2.getText().toString())){
					showMsg("两次输入的密码不匹配");
					return;
				}
				mi = Integer.parseInt(mima2.getText().toString());
				tempmi = mi;
				JZSqliteHelper.saveYuSuan(this,JZMIMA,JZMIMA, mi);
				jiami_text.setText("修改或删除密码");
				bianjijiami.setVisibility(View.GONE);
				showMsg("加密成功");
				mima1.setText("");
				mima2.setText("");
			}else{
				if(mima1.getText().toString().length()==0){
					showMsg("输入不能为空");
					return;
				}
				tempmi= Integer.parseInt(mima1.getText().toString());
				if(mi==tempmi){//当密码输入正确时
					tempmi = -1;//赋值为-1修改密码后可以进入if(mi==0||tempmi==-1)代码块
					shanchuBt.setVisibility(View.VISIBLE);
					mima1.setText("");
					mima2.setText("");
					mima1.setHint("请输入新密码（六位）");
					mima2.setVisibility(View.VISIBLE);
				}else{
					showMsg("与旧密码不匹配");
				}
			}
			break;
		case R.id.jz_shezhi_shanchu_bt :
			JZSqliteHelper.saveYuSuan(this,JZMIMA,JZMIMA,0);
			jiami_text.setText("程序加密");
			bianjijiami.setVisibility(View.GONE);
			shanchuBt.setVisibility(View.INVISIBLE);
			mi = 0;
			mima1.setText("");
			mima2.setText("");
			showMsg("删除成功");
			break;
		case R.id.jz_shezhi_quxiao_bt :
			mima1.setText("");
			mima2.setText("");
			tempmi = -1;
			bianjijiami.setVisibility(View.GONE);
			break;
		}
	}
	
	
	/*
     * 退出弹出框
     * */
    public void showDialog(){
    	final AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle("是否确认清除所有数据？");
    	builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				jzdh.delAll();
				showMsg("数据清除完成");
			}
		});
    	builder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
    	builder.create().show();
    }
    
	@Override
	protected void onResume() {
		overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
		super.onResume();
	}
	
	public void showMsg(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

}
