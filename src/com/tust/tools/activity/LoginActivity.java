package com.tust.tools.activity;

import com.tust.tools.R;
import com.tust.tools.bean.User;
import com.tust.tools.db.UserData;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener{
	private Button btn_login, btn_register;
	private EditText account, pwd;
	private UserData userData;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loginpage);
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_login.setOnClickListener(this);
		btn_register = (Button) findViewById(R.id.btn_register);
		btn_register.setOnClickListener(this);
		account = (EditText) this.findViewById(R.id.login_edit_account);
		pwd = (EditText) this.findViewById(R.id.login_edit_pwd);
		
		userData = new UserData(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		 switch (v.getId()) {
		    case R.id.btn_login: 
//		    	Intent intent =new Intent(LoginActivity.this,JZMainActivity.class);
//		        startActivity(intent);
		    	
				if (account.getText().toString().length() < 1 || pwd.getText().toString().length()<1) {
					showMsg("账号、密码不能为空");
					return;
				} else if(userData.checklogin(account.getText().toString(),pwd.getText().toString())){

					//保存登陆用户信息 修改用户信息，查记录数据需要
					SharedPreferences preferences=getSharedPreferences("userInfo", Context.MODE_PRIVATE);
					SharedPreferences.Editor editor=preferences.edit();
					editor.putString("userName",account.getText().toString());
					editor.commit();
					changeActivity(ToolsMainActivity.class);

				}else{
					showMsg("账号密码错误");
					return;
				}
		      break;
		    case R.id.btn_register: //跳转注册页面
		    	changeActivity(UserRegisterActivity.class);
		    	break;
		    default:
			      break;
			    }
			  
	}
	public void showMsg(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}
	@Override
	protected void onResume() {
		overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
		super.onResume();
	}
    //跳转Activity方法
    public void changeActivity(final Class<?> c) {
        new Thread() {
            public void run() {
                try {
                    sleep(200);
                    Intent intent = new Intent(LoginActivity.this, c);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    LoginActivity.this.startActivity(intent);
                    LoginActivity.this.finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
