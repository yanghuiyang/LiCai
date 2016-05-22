package com.tust.tools.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tust.tools.R;
import com.tust.tools.bean.ExpenditureType;
import com.tust.tools.bean.User;
import com.tust.tools.db.JZData;
import com.tust.tools.db.UserData;

import java.util.Calendar;

public class LoginActivity extends Activity implements OnClickListener{
	/* 注册，登陆按钮 */
	private Button btn_login, btn_register,forgetPwd;
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
		forgetPwd=(Button) findViewById(R.id.forgetPwd);
		forgetPwd.setOnClickListener(this);
		account = (EditText) this.findViewById(R.id.login_edit_account);
		pwd = (EditText) this.findViewById(R.id.login_edit_pwd);

		userData = new UserData(this);
		jzData = new JZData(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		 switch (v.getId()) {
		    case R.id.btn_login:  //登陆要做一些判断 包括一些提醒 和 新的记账月额度流转问题
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
					String tips = preferences.getString("tips", "");//tips 提醒标志（每天连续超过3月平均值） 1为提醒 2位不提醒
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
							String s ;
							s = (user.getBudget()-count)+"";
							s = "恭喜你！这个月有结余:"+s+" 哟~这笔钱放在哪里呢？" ;
							AlertDialog.Builder customBuilder = new AlertDialog.Builder(
									LoginActivity.this);
							customBuilder
									.setTitle("提示")
									// 创建标题
									.setMessage(s)
									.setPositiveButton("存入梦想基金", new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int which) {
											user.setMxjj(user.getMxjj()+(user.getBudget()-count));
											user.setBudget(1200);
											user.setYm((now.get(Calendar.YEAR)+"")+(now.get(Calendar.MONTH) + 1) + "");
											userData.UpdateUserInfo(user);
											Toast.makeText(LoginActivity.this, "已存入梦想基金！",
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
			 case R.id.forgetPwd: //忘记密码
				showDialogToGetPwd();
				 break;
		    default:
			      break;
			    }
			  
	}

	private void showDialogToGetPwd() {
			LayoutInflater factory = LayoutInflater.from(this);
			final View textEntryView = factory.inflate(R.layout.forget_pwd_dialog, null);
			final EditText forget_account= (EditText) textEntryView.findViewById(R.id.forget_account);
			final EditText forget_tel = (EditText)textEntryView.findViewById(R.id.forget_tel);
			AlertDialog.Builder ad1 = new AlertDialog.Builder(LoginActivity.this);
			ad1.setTitle("找回密码");
			ad1.setIcon(android.R.drawable.ic_dialog_info);
			ad1.setView(textEntryView);
			ad1.setPositiveButton("找回", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int i) {
					Log.i("111111", forget_account.getText().toString());
					if(forget_account.getText().toString().length()<1||forget_tel.getText().toString().length()<1){
						showMsg("账号或手机号不能为空");
					}else if(!isMobileNO(forget_tel.getText().toString())){
						showMsg("请输入合法的手机号码");
					}else{
						User user2 = userData.getUserByUserName(forget_account.getText().toString());
						if (user2!=null){
							if(user2.getTel().equals(forget_tel.getText().toString())){
								Toast.makeText(LoginActivity.this, "账号："+forget_account.getText().toString()+" 密码为："+user2.getPwd(),
										Toast.LENGTH_LONG).show();
								dialog.dismiss();
							}else{
								Toast.makeText(LoginActivity.this, "账号和手机号不匹配",
										Toast.LENGTH_LONG).show();
							}
						}else{
							Toast.makeText(LoginActivity.this, "账号不存在",
									Toast.LENGTH_LONG).show();
						}
					}

				}
			});
			ad1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int i) {
				}
			});
			ad1.show();// 显示对话框
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
	public static boolean isMobileNO(String mobiles) {
    /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
		String telRegex = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
		if (TextUtils.isEmpty(mobiles)) return false;
		else return mobiles.matches(telRegex);
	}
}
