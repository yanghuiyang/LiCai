package com.tust.tools.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.tust.tools.dialog.DialogAbout;

public class JZSheZhiActivity extends Activity implements OnClickListener {

	//密码编辑界面的保存，取消，删除按钮
	//private Button saveBt,cancelBt,shanchuBt;
	//设置选项  加密，清楚数据，关于软件
	private RelativeLayout incomeType_Manage,expenditureType_Manage,qingchu_rl,guanyu_rl;
	//记账数据库管理
	private JZData jzdh;
	//public static final String JZMIMA = "JZMIMA";
	private String userName;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jz_shezhi);
		jzdh = new JZData(this);
		init();
		SharedPreferences preferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
		userName = preferences.getString("userName", "");

	}
	
	private void init(){
		incomeType_Manage = (RelativeLayout)this.findViewById(R.id.jz_shezhi_incomeType_Manage);
		incomeType_Manage.setOnClickListener(this);
		expenditureType_Manage = (RelativeLayout)this.findViewById(R.id.jz_shezhi_expenditureType_Manage);
		expenditureType_Manage.setOnClickListener(this);
		qingchu_rl = (RelativeLayout)this.findViewById(R.id.jz_shezhi_qingchu_rl);	
		qingchu_rl.setOnClickListener(this);
		guanyu_rl = (RelativeLayout)this.findViewById(R.id.jz_shezhi_about_rl);	
		guanyu_rl.setOnClickListener(this);

	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.jz_shezhi_incomeType_Manage:
				changeActivity(IncomeTypeManageActivity.class);
				break;
			case R.id.jz_shezhi_expenditureType_Manage :
				changeActivity(ExpenditureTypeManageActivity.class);
				break;



			case R.id.jz_shezhi_qingchu_rl :
				showDialog();
				break;
			case R.id.jz_shezhi_about_rl :
				String text = "理财-记账" +
        			"\r\n   软件版本 v1.0" ;
				new DialogAbout(this,text);
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
				jzdh.delAll(userName);
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
	//跳转Activity方法
	//跳转Activity方法
	public void changeActivity(final Class<?> c) {
		new Thread() {
			public void run() {
				try {
					sleep(200);
					Intent intent = new Intent(JZSheZhiActivity.this, c);
					intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					JZSheZhiActivity.this.startActivity(intent);
					JZSheZhiActivity.this.finish();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
}
