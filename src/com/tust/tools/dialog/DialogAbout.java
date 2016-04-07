package com.tust.tools.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.tust.tools.R;
import com.tust.tools.service.DongHuaYanChi;

/*
 * �������dialog
 * */
public class DialogAbout extends Dialog{
	private View diaView ;
	private Context context;
	private Handler handler ;
	// flag��ʶת���������ۣ�id��ʶ��ǰ΢����id��where��ʶ������������dialog�������ط�����������home���棬�����ݽ���
	public DialogAbout(Context context,String text) {
		super(context, R.style.maindialog);
		this.context = context;
		handler = new Handler();
		diaView = View.inflate(context, R.layout.dialog_about, null);
		this.setContentView(diaView);
		TextView textView= (TextView)diaView.findViewById(R.id.dialog_about_text);
		textView.setText(text);
//		textView.setMovementMethod(ScrollingMovementMethod.getInstance());
		//��ӱ�ע�༭��
		this.show();
		diaView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.push_up_in));
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			DongHuaYanChi.dongHuaDialogEnd(this, diaView, context, handler, R.anim.push_up_out,300);
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
}
