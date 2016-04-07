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
 * �������dialog
 * */
public class DialogShuRuMiMa extends Dialog implements OnClickListener{
	//���������
	private EditText et = null;
	private Context context;
	//Activity����  �û���������ɹ���ر���Ӧ��Activity
	private Activity activity;
	//Dialog��View
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
		//�������whoseΪ����ʱ
		if(whose.equals(JZSheZhiActivity.JZMIMA)){
			et.setHint("��������˹�������");
			mi = JZSqliteHelper.readPreferenceFile(context,JZSheZhiActivity.JZMIMA,JZSheZhiActivity.JZMIMA);
			c = JZMainActivity.class;
		}else if(whose.equals(DialogBWSheZhiMiMa.BWMIMA)){//�������whoseΪ����ʱ
			et.setHint("�����뱸����������");
			mi = JZSqliteHelper.readPreferenceFile(context,DialogBWSheZhiMiMa.BWMIMA,DialogBWSheZhiMiMa.BWMIMA);
			c = BWMainActivity.class;
		}

		//��ӱ�ע�༭��
		this.show();
		//��ȡ���� ����dialogλ��Ϊ�ϲ�
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
				showMsg("���벻��Ϊ��");
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
					showMsg("�������");
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
			//dialog�˳�����
			DongHuaYanChi.dongHuaDialogEnd(this, diaView, context, handler, R.anim.push_up_out,300);
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	public void showMsg(String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}
}
