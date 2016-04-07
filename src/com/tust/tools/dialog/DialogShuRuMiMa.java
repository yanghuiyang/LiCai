package com.tust.tools.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.InputType;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tust.tools.R;
import com.tust.tools.activity.BWMainActivity;
import com.tust.tools.activity.JZMainActivity;
import com.tust.tools.activity.JZSheZhiActivity;
import com.tust.tools.db.JZSqliteHelper;
import com.tust.tools.service.DongHuaYanChi;

/*
 * 密码进入dialog
 * */
public class DialogShuRuMiMa extends Dialog implements OnClickListener{
	//密码输入框
	private EditText et = null;
	private Context context;
	//Activity对象  用户密码输入成功后关闭相应的Activity
	private Activity activity;
	//Dialog的View
	private View diaView;
	private Handler handler;
	private int mi = 0;
	@SuppressWarnings("rawtypes")
	private Class c;
	public DialogShuRuMiMa(Context context,Activity activity,String whose) {
		super(context, R.style.maindialog);
		this.context = context;
		this.activity = activity;
		handler = new Handler();
		diaView = View.inflate(context, R.layout.dialog_mima, null);
		this.setContentView(diaView);
		et = (EditText)diaView.findViewById(R.id.dialog_mima_et);
		Button quding= (Button)diaView.findViewById(R.id.dialog_mima_queding_bt);
		quding.setOnClickListener(this);
		Button quxiao= (Button)diaView.findViewById(R.id.dialog_mima_quxiao_bt);
		quxiao.setOnClickListener(this);
		//当传入的whose为记账时
		if(whose.equals(JZSheZhiActivity.JZMIMA)){
			et.setHint("请输入记账工具密码");
			mi = JZSqliteHelper.readPreferenceFile(context,JZSheZhiActivity.JZMIMA,JZSheZhiActivity.JZMIMA);
			c = JZMainActivity.class;
		}else if(whose.equals(DialogBWSheZhiMiMa.BWMIMA)){//当传入的whose为备忘时
			et.setHint("请输入备忘记事密码");
			mi = JZSqliteHelper.readPreferenceFile(context,DialogBWSheZhiMiMa.BWMIMA,DialogBWSheZhiMiMa.BWMIMA);
			c = BWMainActivity.class;
		}

		//添加备注编辑框
		this.show();
		//获取窗口 设置dialog位置为上部
		Window window = this.getWindow();
		window.setGravity(Gravity.TOP);
		diaView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.push_up_in));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dialog_mima_queding_bt:
			String etString = et.getText().toString();
			if(etString.equals(null)||etString.equals("")){
				showMsg("输入不能为空");
				return;
			}
			if(mi!=0){
				int mima= Integer.parseInt(etString);
				if(mima == mi){
					this.cancel();
					Intent intent = new Intent(context,c);
	                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
	                context.startActivity(intent);
	                DongHuaYanChi.dongHuaDialogEnd(this, diaView, context, handler, R.anim.push_up_out,300);
	                if(activity!=null){
	                	activity.finish();
	                }
				}else{
					showMsg("密码错误");
				}
			}
			break;
		case R.id.dialog_mima_quxiao_bt:
			DongHuaYanChi.dongHuaDialogEnd(this, diaView, context, handler, R.anim.push_up_out,300);
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			//dialog退出动画
			DongHuaYanChi.dongHuaDialogEnd(this, diaView, context, handler, R.anim.push_up_out,300);
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	public void showMsg(String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}
}
