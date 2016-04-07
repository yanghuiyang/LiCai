package com.tust.tools.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tust.tools.R;
public class JSMainActivity extends Activity implements OnClickListener{
	Button b1,b2,b3,b4,b5,b6,b7,b8,b9,b0,bdian,bjia,bjian,bcheng,bchu,bdeng,bdel,bclean;
	Button bts[]=new Button[]{b0,b1,b2,b3,b4,b5,b6,b7,b8,b9,bdian,bdel};
	EditText et;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.js_main);
        initNumBt();
        initBt();
	}
    private void initNumBt() {
    	et = (EditText)this.findViewById(R.id.js_content_et);
        int id[] = new int[] { R.id.js_b0, R.id.js_b1, R.id.js_b2, R.id.js_b3, R.id.js_b4, R.id.js_b5, R.id.js_b6, R.id.js_b7, R.id.js_b8, R.id.js_b9,R.id.js_bdian,R.id.js_bdel};
        for (int i = 0; i < bts.length; i++) {
            bts[i] = (Button) this.findViewById(id[i]);
            bts[i].setOnClickListener(new MyClick(et));
        }
    }
    private void initBt(){
    	bjia = (Button)this.findViewById(R.id.js_bjia);
    	bjia.setOnClickListener(this);
    	bjian = (Button)this.findViewById(R.id.js_bjian);
    	bjian.setOnClickListener(this);
    	bcheng = (Button)this.findViewById(R.id.js_bcheng);
    	bcheng.setOnClickListener(this);
    	bchu = (Button)this.findViewById(R.id.js_bchu);
    	bchu.setOnClickListener(this);
    	bdeng = (Button)this.findViewById(R.id.js_bdeng);
    	bdeng.setOnClickListener(this);
    	bclean = (Button)this.findViewById(R.id.js_bclean);
    	bclean.setOnClickListener(this);
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
        case R.id.js_bjia:
        	YunSuan((Button)v);
            break;
        case R.id.js_bjian:
        	YunSuan((Button)v);
        	break;
        case R.id.js_bcheng:
        	YunSuan((Button)v);
        	break;
        case R.id.js_bchu:
        	YunSuan((Button)v);
        	break;
        case R.id.js_bdeng:
        	deng();
        	break;
        case R.id.js_bclean:
        	initNum();
        	break;
        }
    }
    
    public void initNum(){
    	et.setText("0");
    	first_num=0;
        second_num=0;
        fu = '0';
        s = "";
    }
    double first_num=0;
    double second_num=0;
    char fu = '0';
    String s = "";
    public void YunSuan(Button b){
    	String s = et.getText().toString().trim();
       	//��ֹ����ĩβ��С����
       	if(s.contains(".")&&s.indexOf(".")==(s.length()-1)){
       		s= s.substring(0, s.length()-1);
       	}
       	if(s.endsWith("+")||s.endsWith("-")||s.endsWith("*")||s.endsWith("/")){
			String newsString = s.substring(0, s.length()-1);
			s = newsString;
			et.setText(s);
			fu = b.getText().charAt(0);
			et.append(fu+"");
			return;
		}
		if(fu!='0'&&fu!='='){
			deng();
			fu = b.getText().charAt(0);
			et.append(fu+"");
		}else{
			fu = b.getText().charAt(0);
			et.append(fu+"");
			first_num = Double.parseDouble(s);
		}
    }
    
    /*
     * ��ȡ���һ��������� ����ĵ�����
     * */
    public String getLastNum(){
    	String etContent = et.getText().toString().trim();
    	//��ֹ���������ź�������Ⱥ�
     	if(etContent.endsWith("+")||etContent.endsWith("-")||etContent.endsWith("*")||etContent.endsWith("/")){
			String newsString = etContent.substring(0, etContent.length()-1);
			etContent = newsString;
		}
    	int fuIndex = etContent.lastIndexOf(fu+"")+1;
    	String lastnum = etContent.substring(fuIndex);
    	if(lastnum.equals("")||lastnum.equals(null)){
    			lastnum = "0";
    		if(fu=='*'||fu=='/'){
    			lastnum = "1";	
    		}
    	}
    	return lastnum;
    }
    
    double count = 0;
    public void deng(){
    	if(fu=='0'){
    		return;
    	}
    	//��ֹ����ĩβ��С����
       	if(s.contains(".")&&s.indexOf(".")==(s.length()-1)){
       		s= s.substring(0, s.length()-1);
       	}
    	second_num = Double.parseDouble(getLastNum());
    	switch (fu) {
		case '+':
			count = first_num+second_num;
			break;
		case '-':
			count = first_num-second_num;
			break;
		case '*':
			count = first_num*second_num;
			break;
		case '/':
			if(second_num==0||second_num==0.0){
				showMsg("��ѧ��������ʦ�̵İɣ�");
				return;
			}else{
				count = first_num/second_num;
			}
			break;
		}
		String countString = String.valueOf(count);
    	if(countString.contains(".")){
    		String string[]= countString.split("\\.");
	    	if(string[1].equals("0")){
	    		et.setText(string[0]);
	    	}else{
	    		et.setText(countString);	
	    	}
    	}else{
    		et.setText(countString);
    	}
    	first_num = count;
    	second_num= 0;
    	fu = '=';
        if(et.getText().toString().trim().length()>11){
        	et.setTextSize(25);
        }else{
        	et.setTextSize(40);
        }
    }
    
    public boolean onKeyDown(int kCode, KeyEvent kEvent) {
        switch (kCode) {
        case KeyEvent.KEYCODE_BACK: {
                exitDialog();
            return false;
        }
        }
        return super.onKeyDown(kCode, kEvent);
    }

    /*
     * �˳�������
     */
    String fileName;// �Ե�ǰʱ���������ļ���
    public void exitDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String confrimStr = "";
        String cancelStr = "";
        builder.setTitle("�Ƿ�ȷ���˳���");
        confrimStr = "�˳�С����";
        cancelStr = "�˳����׼���";
        builder.setPositiveButton(confrimStr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    System.exit(0);
            }
        });
        
        builder.setNeutralButton(cancelStr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(JSMainActivity.this, ToolsMainActivity.class);
                JSMainActivity.this.startActivity(intent);
                JSMainActivity.this.finish();
            }
        });
        builder.create().show();
    }

    /*
     * �������ּ���
     */
    private class MyClick implements OnClickListener {
        private EditText et;
        public MyClick(EditText et) {
            this.et = et;
        }

        @Override
        public void onClick(View v) {
            Button button = (Button) v;
            String number = et.getText().toString().trim();
            if (v.getId()!=R.id.js_bdel&&!number.contains("+")&&!number.contains("-")&&!number.contains("*")&&!number.contains("/")&&number.length() > 12) {
                return;
            }
            if(number.length()>11){
            	et.setTextSize(25);
            }else{
            	et.setTextSize(40);
            }
            if (v.getId() != R.id.js_bdel) {
                if (number.equals("0")||fu=='='){// ��һ������ʱ,����Ϊ = �����ı�������Ϊ0
                	fu = '0';//�ѷ��Ÿ�Ϊ 0 �����ٴν���öγ���
                    if (button.getText().equals(".")) {
                    	et.setText("0.");
                    }else{
                    	et.setText(button.getText());
                    }
                 } else {
                    if (number.contains(".")) {// ������Ѿ�����С����
                        if (button.getText().equals(".")&&number.indexOf(".", number.lastIndexOf(fu+""))!=-1) {// �����ΪС����
                            showMsg("ûѧ����ѧѽ��");
                            return;
                        }
                        // С����󳬹���λʱ
                        if ((number.length() - number.indexOf(".")) <= 5||(
                        		number.contains("+")||
                        		number.contains("-")||
                        		number.contains("*")||
                        		number.contains("/"))) {
                        	et.append(button.getText());
                        } else {
                            showMsg("��ô��С�����������Ұ���");
                        }
                    } else {
                    	et.append(button.getText());
                    }
                }
            } else {// �����ɾ����
                if (!number.equals("0")) {
                    if (number.length() > 1) {
                        String str = number.substring(0, number.length() - 1);
                        et.setText(str);
                    } else {
                    	et.setText("0");
                    }
                }
            }
        }
    }
    
    public void showMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}