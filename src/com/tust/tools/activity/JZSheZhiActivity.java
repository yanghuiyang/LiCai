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
	//����༭��
	private EditText mima1,mima2;
	//�����������
	private TextView jiami_text;
	//����༭����ı��棬ȡ����ɾ����ť
	private Button saveBt,cancelBt,shanchuBt;
	//����ѡ��  ���ܣ�������ݣ��������
	private RelativeLayout jiami_rl,qingchu_rl,guanyu_rl;
	//�༭���ܽ���
	private LinearLayout bianjijiami;
	//mi ��ǰ����  ��  tempmi�����ʶ
	private int mi=0,tempmi=-1;
	//�������ݿ����
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
		if(mi==0){//����Ϊ0��ǰû����
			jiami_text.setText("�������");
		}else{
			jiami_text.setText("�޸Ļ�ɾ������");
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
		case R.id.jz_shezhi_jiami_rl://�������ѡ��
			mi = JZSqliteHelper.readPreferenceFile(this,JZMIMA,JZMIMA);
			if(bianjijiami.isShown()){
				bianjijiami.setVisibility(View.GONE);
			}else{
				bianjijiami.setVisibility(View.VISIBLE);
				if(mi==0){//��û������ʱ
					jiami_text.setText("�������");
					shanchuBt.setVisibility(View.INVISIBLE);
				}else{
					tempmi = mi;//��������ʱ�����븳ֵ��tempmi���Ա��ڵ��ȷ����ťʱ��ȷ�ж�
					jiami_text.setText("�޸Ļ�ɾ������");
					mima1.setHint("�����������");
					mima2.setVisibility(View.GONE);//����ȷ�Ͽ򲻿ɼ�
					shanchuBt.setVisibility(View.INVISIBLE);//ɾ�����밴ť���ɼ�
				}
			}
			break;
		case R.id.jz_shezhi_qingchu_rl :
			showDialog();
			break;
		case R.id.jz_shezhi_about_rl :
			String text = "�ü��������ܱȽϼ򵥣�ʹ�÷��㡣" +
		            "\r\n��ӭ���������������" +
        			"\r\nQQ��xxxxx" +
        			"\r\n�����ѧ"+
        			"\r\n                      ����汾 v1.0" ;
			new DialogAbout(this,text);
			break;
		case R.id.jz_shezhi_queding_bt :
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
				JZSqliteHelper.saveYuSuan(this,JZMIMA,JZMIMA, mi);
				jiami_text.setText("�޸Ļ�ɾ������");
				bianjijiami.setVisibility(View.GONE);
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
					shanchuBt.setVisibility(View.VISIBLE);
					mima1.setText("");
					mima2.setText("");
					mima1.setHint("�����������루��λ��");
					mima2.setVisibility(View.VISIBLE);
				}else{
					showMsg("������벻ƥ��");
				}
			}
			break;
		case R.id.jz_shezhi_shanchu_bt :
			JZSqliteHelper.saveYuSuan(this,JZMIMA,JZMIMA,0);
			jiami_text.setText("�������");
			bianjijiami.setVisibility(View.GONE);
			shanchuBt.setVisibility(View.INVISIBLE);
			mi = 0;
			mima1.setText("");
			mima2.setText("");
			showMsg("ɾ���ɹ�");
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
     * �˳�������
     * */
    public void showDialog(){
    	final AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle("�Ƿ�ȷ������������ݣ�");
    	builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				jzdh.delAll();
				showMsg("����������");
			}
		});
    	builder.setNeutralButton("ȡ��", new DialogInterface.OnClickListener() {
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
