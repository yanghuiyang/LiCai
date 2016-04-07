package com.tust.tools.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tust.tools.R;
import com.tust.tools.db.JZSqliteHelper;
import com.tust.tools.service.DongHuaYanChi;

/*
 * �������dialog
 * */
public class DialogBWSheZhiMiMa extends Dialog implements OnClickListener{
	//���������
	private EditText mima1,mima2;
	//�����������
	private TextView title;
	//ȷ��  ȡ��  ɾ����ť
	private Button queding,quxiao,shanchu;
	//�����Ķ���
	private Context context;
	//Dialog��View
	private View diaView;
	private Handler handler;
	public static final String BWMIMA = "BWMIMA";
	//����  �������ʶ
	int mi=0,tempmi=0;
	public DialogBWSheZhiMiMa(Context context) {
		super(context, R.style.maindialog);
		this.context = context;
		handler = new Handler();
		diaView = View.inflate(context, R.layout.dialog_bwmima, null);
		this.setContentView(diaView);
		title = (TextView)diaView.findViewById(R.id.dialog_bwtittle_text);
		mima1 = (EditText)diaView.findViewById(R.id.dialog_bwmima_et1);
		mima2 = (EditText)diaView.findViewById(R.id.dialog_bwmima_et2);
		queding= (Button)diaView.findViewById(R.id.dialog_bwmima_queding_bt);
		queding.setOnClickListener(this);
		shanchu= (Button)diaView.findViewById(R.id.dialog_bwmima_shanchu_bt);
		shanchu.setOnClickListener(this);
		quxiao= (Button)diaView.findViewById(R.id.dialog_bwmima_quxiao_bt);
		quxiao.setOnClickListener(this);
		mi = JZSqliteHelper.readPreferenceFile(context,BWMIMA,BWMIMA);
		if(mi==0){//����Ϊ0��ǰû����
			title.setText("���ó�������");
			shanchu.setVisibility(View.INVISIBLE);
		}else{
			title.setText("�޸Ļ�ɾ������");
			tempmi = mi;
			mima1.setHint("�����������");
			mima2.setVisibility(View.GONE);//����ȷ�Ͽ򲻿ɼ�
			shanchu.setVisibility(View.INVISIBLE);//ɾ�����밴ť���ɼ�
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
		case R.id.dialog_bwmima_queding_bt ://ȷ��
			if(mi==0||tempmi==-1){//��û������ʱ
				if(mima1.getText().toString().length()==0||mima2.getText().toString().length()==0){
					showMsg("���벻��Ϊ��");
					return;
				}
				if(mima1.getText().toString().length()!=6||mima2.getText().toString().length()!=6){
					showMsg("�������Ϊ��λ��");
					return;
				}
				if(!mima1.getText().toString().equals(mima2.getText().toString())){
					showMsg("������������벻ƥ��");
					return;
				}
				mi = Integer.parseInt(mima2.getText().toString());
				tempmi = mi;
				JZSqliteHelper.saveYuSuan(context,BWMIMA,BWMIMA, mi);
				title.setText("�޸Ļ�ɾ������");
				DongHuaYanChi.dongHuaDialogEnd(this, diaView, context, handler, R.anim.push_up_out,300);
				showMsg("���ܳɹ�");
				mima1.setText("");
				mima2.setText("");
			}else{
				if(mima1.getText().toString().length()==0){
					showMsg("���벻��Ϊ��");
					return;
				}
				tempmi= Integer.parseInt(mima1.getText().toString());
				if(mi==tempmi){//������������ȷʱ
					tempmi = -1;//��ֵΪ-1�޸��������Խ���if(mi==0||tempmi==-1)�����
					shanchu.setVisibility(View.VISIBLE);
					mima1.setText("");
					mima2.setText("");
					mima1.setHint("�����������루��λ��");
					mima2.setVisibility(View.VISIBLE);
				}else{
					showMsg("������벻ƥ��");
				}
			}
			break;
		case R.id.dialog_bwmima_shanchu_bt :
			JZSqliteHelper.saveYuSuan(context,BWMIMA,BWMIMA,0);
			title.setText("�������");
			shanchu.setVisibility(View.INVISIBLE);
			mi = 0;
			mima1.setText("");
			mima2.setText("");
			showMsg("ɾ���ɹ�");
			DongHuaYanChi.dongHuaDialogEnd(this, diaView, context, handler, R.anim.push_up_out,300);
			break;
		case R.id.dialog_bwmima_quxiao_bt :
			mima1.setText("");
			mima2.setText("");
			tempmi = -1;
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
