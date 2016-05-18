package com.tust.tools.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tust.tools.R;
import com.tust.tools.bean.User;
import com.tust.tools.db.JZData;
import com.tust.tools.db.UserData;

import java.util.Calendar;

public class LoginActivity extends Activity implements OnClickListener{
	/* 注册，登陆按钮 */
	private Button btn_login, btn_register;
	private EditText account, pwd;
	private UserData userData;
	private User user;
	private JZData jzData;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.loginpage);
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_login.setOnClickListener(this);
		btn_register = (Button) findViewById(R.id.btn_register);
		btn_register.setOnClickListener(this);
		account = (EditText) this.findViewById(R.id.login_edit_account);
		pwd = (EditText) this.findViewById(R.id.login_edit_pwd);

		userData = new UserData(this);
		jzData = new JZData(this);
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
					editor.putString("pwd",pwd.getText().toString());
					String tips = preferences.getString("tips", "");//tips 提醒标志 1为提醒 2位不提醒
					if(tips.equals("2")){

					}else {
						editor.putString("tips","1");
					}
					editor.commit();
					user = userData.getUserByUserName(account.getText().toString());
					final Calendar now = Calendar.getInstance();
					if(user.getYm().equals((now.get(Calendar.YEAR)+"")+(now.get(Calendar.MONTH) + 1) + "")){
						changeActivity(ToolsMainActivity.class);
					}else {
						final int count = jzData.getMonthSpend(account.getText().toString(),now.get(Calendar.YEAR),(now.get(Calendar.MONTH)));//bug 上月总支出
						if (count < user.getBudget()){
							//弹出对话框“恭喜你！这个月有结余哟~这笔钱放在哪里呢？ 存入梦想基 转入余额宝
							Dialog dialog = null;
							AlertDialog.Builder customBuilder = new AlertDialog.Builder(
									LoginActivity.this);
							customBuilder
									.setTitle("提示")
									// 创建标题
									.setMessage("恭喜你！这个月有结余哟~这笔钱放在哪里呢？")
									.setPositiveButton("存入梦想基", new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int which) {
											user.setMxjj(user.getMxjj()+(user.getBudget()-count));
											user.setBudget(1200);
											user.setYm((now.get(Calendar.YEAR)+"")+(now.get(Calendar.MONTH) + 1) + "");
											userData.UpdateUserInfo(user);
											Toast.makeText(LoginActivity.this, "已存入梦想基！",
													Toast.LENGTH_LONG).show();

											dialog.dismiss();
											changeActivity(ToolsMainActivity.class);
										}

									})
									.setNegativeButton("转入余额宝", new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int which) {
											user.setBudget(1200);
											user.setYm((now.get(Calendar.YEAR)+"")+(now.get(Calendar.MONTH) + 1) + "");
											userData.UpdateUserInfo(user);
											dialog.dismiss();
											changeActivity(ToolsMainActivity.class);
										}
									});
							dialog = customBuilder.create();// 创建对话框
							dialog.show(); // 显示对话框
						}
//						user.setBudget(1200);
//						user.setYm((now.get(Calendar.YEAR)+"")+(now.get(Calendar.MONTH) + 1) + "");
//						userData.UpdateUserInfo(user);
					}

//					changeActivity(ToolsMainActivity.class);

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
