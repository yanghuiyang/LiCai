package com.tust.tools.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tust.tools.R;
import com.tust.tools.activity.JZAddActivity;

/*
 * 备注dialog,主要为一个带有编辑框的弹出框，用来编辑备注
 * */
public class DialogBeiZhu extends Dialog implements OnClickListener {
	private Button queding, quxiao;
	private EditText et;
	private Context context;
	private View v;

	// flag标识转发还是评论，id标识当前微博的id，where标识从哪里启动该dialog，两个地方可以启动，home界面，和内容界面
	public DialogBeiZhu(Context context,String beizhuString) {
		super(context, R.style.maindialog);
		this.context = context;
		View diaView = View.inflate(context, R.layout.dialog_beizhu, null);
		this.setContentView(diaView);
		this.v = diaView;
		//添加备注编辑框
		et = (EditText) diaView.findViewById(R.id.jz_add_beizhu_et);
		//确定按钮
		queding = (Button) diaView.findViewById(R.id.jz_add_beizhu_queding);
		//取消按钮
		quxiao = (Button) diaView.findViewById(R.id.jz_add_beizhu_quxiao);
		et.clearFocus();
		queding.setOnClickListener(this);
		quxiao.setOnClickListener(this);
		this.show();
		diaView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.push_left_in));
		if("".equals(beizhuString)){
		    et.setHint("请输入备注内容...");
		}else{
		    et.setText(beizhuString);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.jz_add_beizhu_queding:
			// 转发和评论度需要用到此dialog所以这里dialog中的按键也作用不同
				String content = et.getText().toString().trim();
				if (content.length() > 0) {
					hidden();
					Message msg  =Message.obtain();
					msg.what=JZAddActivity.beizhu_msg;
					msg.obj = content;
					JZAddActivity.mh.sendMessage(msg);
				} else {
					Toast.makeText(context, "输入不能为空", 1000).show();
				}
			break;
		case R.id.jz_add_beizhu_quxiao:
			et.setText("");
			// 取消后的动画效果，需要启动一个线程延迟播放结束动画
			hidden();
			break;
		}
	}
	
	/*
	 * 隐藏dialog界面
	 * */
	public void hidden(){
		this.v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.push_left_out));
		new Thread() {
			public void run() {
				try {
					Thread.sleep(250);//延迟为了显示退出动画
					DialogBeiZhu.this.dismiss();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

}
