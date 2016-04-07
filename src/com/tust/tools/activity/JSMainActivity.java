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
       	//防止数字末尾是小数点
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
     * 获取最后一次输入符号 后面的的数字
     * */
    public String getLastNum(){
    	String etContent = et.getText().toString().trim();
    	//防止最后输入符号后再输入等号
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
    	//防止数字末尾是小数点
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
				showMsg("数学是体育老师教的吧？");
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
     * 退出弹出框
     */
    String fileName;// 以当前时间命名的文件名
    public void exitDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String confrimStr = "";
        String cancelStr = "";
        builder.setTitle("是否确认退出？");
        confrimStr = "退出小助手";
        cancelStr = "退出简易计算";
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
     * 输入数字监听
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
                if (number.equals("0")||fu=='='){// 第一次输入时,符号为 = 或者文本框内容为0
                	fu = '0';//把符号改为 0 避免再次进入该段程序
                    if (button.getText().equals(".")) {
                    	et.setText("0.");
                    }else{
                    	et.setText(button.getText());
                    }
                 } else {
                    if (number.contains(".")) {// 金额中已经包含小数点
                        if (button.getText().equals(".")&&number.indexOf(".", number.lastIndexOf(fu+""))!=-1) {// 输入的为小数点
                            showMsg("没学过数学呀？");
                            return;
                        }
                        // 小数点后超过两位时
                        if ((number.length() - number.indexOf(".")) <= 5||(
                        		number.contains("+")||
                        		number.contains("-")||
                        		number.contains("*")||
                        		number.contains("/"))) {
                        	et.append(button.getText());
                        } else {
                            showMsg("这么多小数，想整死我啊？");
                        }
                    } else {
                    	et.append(button.getText());
                    }
                }
            } else {// 如果是删除键
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