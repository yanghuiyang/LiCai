package com.tust.tools.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.tust.tools.R;
import com.tust.tools.bean.User;
import com.tust.tools.db.UserData;

public class UserEditActivity extends Activity implements OnClickListener{

	private Button btn_save;
	private EditText account, pwd,tel;
	private Spinner sex_spinner = null;//性别下拉框
	private int sex;
	private UserData userData;
	private User user;
	private ArrayAdapter<CharSequence> sprinnerSex = null;// 要使用的Adapter


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.useredit);
        btn_save = (Button) findViewById(R.id.edit_btn_save);
        btn_save.setOnClickListener(this);
		account = (EditText) this.findViewById(R.id.edit_username);
		pwd = (EditText) this.findViewById(R.id.edit_pwd);
		tel = (EditText) this.findViewById(R.id.edit_tel);
		userData = new UserData(this);
		//性别下拉
		sex_spinner = (Spinner) this.findViewById(R.id.spinner_sex);
		sex_spinner.setPrompt("您的性别是:");// 设置Prompt
		sprinnerSex = ArrayAdapter.createFromResource(this, R.array.sprinnerSex,android.R.layout.simple_spinner_item);// 实例化ArrayAdapter
		sex_spinner.setAdapter(sprinnerSex);// 设置显示信息
		//取数 赋值到界面上
		SharedPreferences preferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
		account.setText(preferences.getString("userName", ""));
		user = new User();
		user =userData.getUserByUserName(preferences.getString("userName", ""));
		account.setText(user.getUsername());
		pwd.setText(user.getPwd());
		tel.setText(user.getTel());

		sex_spinner.setSelection(user.getSex()-1);// index从0开始 1 男 2 女


	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.edit_btn_save: //保存 保存用户信息
				if(sex_spinner.getSelectedItem().toString().equals("男")){
					sex = 1;
				}else{
					sex = 2;
				}
				if(account.toString().equals("") || pwd.toString().equals("")){

				}else{
					user = new User();
					user.setUsername(account.getText().toString());
					user.setPwd(pwd.getText().toString());
					user.setSex(sex);
					user.setTel(tel.getText().toString());
					//String a = user.getUsername()+"-"+user.getPwd();
					//待做数据校验 如重复用户等情况
					long result = userData.UpdateUserInfo(user);
					if(result != -1){
						showMsg("更新成功");
						changeActivity(ToolsMainActivity.class);
					}else{
						showMsg("更新失败");
					}
				}
//
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

	@Override
	public boolean onKeyDown(int kCode, KeyEvent kEvent) {
		switch (kCode) {
			case KeyEvent.KEYCODE_BACK://返回键
				Intent intent = new Intent(UserEditActivity.this, ToolsMainActivity.class);
				startActivity(intent);
				this.finish();
				break;
		}
		return super.onKeyDown(kCode, kEvent);
	}
	//跳转Activity方法
	public void changeActivity(final Class<?> c) {
		new Thread() {
			public void run() {
				try {
					Intent intent = new Intent(UserEditActivity.this, c);
					intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					UserEditActivity.this.startActivity(intent);
					UserEditActivity.this.finish();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}


}
