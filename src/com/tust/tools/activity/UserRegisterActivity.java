package com.tust.tools.activity;

import com.tust.tools.R;
import com.tust.tools.bean.ExpenditureType;
import com.tust.tools.bean.User;
import com.tust.tools.db.ExpenditureTypeData;
import com.tust.tools.db.IncomeTypeData;
import com.tust.tools.db.UserData;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UserRegisterActivity extends Activity implements OnClickListener{
	private Button btn_register;
	private EditText account, pwd,tel;
	private Spinner sex_spinner = null;//性别下拉框
	private int sex;
	private UserData userData;
	private User user;
	private ArrayAdapter<CharSequence> sprinnerSex = null;// 要使用的Adapter  
	private ExpenditureTypeData expenditureTypeData;
	private IncomeTypeData incomeTypeData;
	private ExpenditureType type;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		btn_register = (Button) findViewById(R.id.btn_register);
		btn_register.setOnClickListener(this);
		account = (EditText) this.findViewById(R.id.edit_username);
		pwd = (EditText) this.findViewById(R.id.edit_pwd);
		tel = (EditText) this.findViewById(R.id.edit_tel);
		userData = new UserData(this);
		//性别下拉
		sex_spinner = (Spinner) this.findViewById(R.id.spinner_sex);
		sex_spinner.setPrompt("您的性别是:");// 设置Prompt  
		sprinnerSex = ArrayAdapter.createFromResource(this, R.array.sprinnerSex,android.R.layout.simple_spinner_item);// 实例化ArrayAdapter  
		sex_spinner.setAdapter(sprinnerSex);// 设置显示信息


	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		 switch (v.getId()) {
		    case R.id.btn_register: //注册
				if(!isMobileNO(tel.getText().toString())){
					showMsg("请输入合法的手机号码");
//				}else if(account.toString().equals("")) {
				}else if(TextUtils.isEmpty(account.getText())){
					showMsg("账号不能为空，请输入账号");
//				}else if(pwd.toString().equals("")){
				}else if(TextUtils.isEmpty(pwd.getText())){
					showMsg("密码不能为空，请输入密码");
				}else
				{
					if (sex_spinner.getSelectedItem().toString().equals("男")) {
						sex = 1;
					} else {
						sex = 2;
					}
						user = new User();
						user.setUsername(account.getText().toString());
						user.setPwd(pwd.getText().toString());
						user.setSex(sex);
						user.setTel(tel.getText().toString());
						user.setBudget(1200);//设置默认月预算
						Calendar now = Calendar.getInstance();
						user.setYm((now.get(Calendar.YEAR)+"")+(now.get(Calendar.MONTH) + 1) + "");
						long result = userData.SaveUser(user);
						if (result != -1) {
							//为改用户添加初始化的支出类型
							expenditureTypeData = new ExpenditureTypeData(this);
							incomeTypeData = new IncomeTypeData(this);
							expenditureTypeData.initType(user);
							incomeTypeData.initType(user);
							showMsg("注册成功,请登录");
							changeActivity(LoginActivity.class);
						} else {
							showMsg("注册失败");
						}

				}
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
			Intent intent = new Intent(UserRegisterActivity.this, LoginActivity.class);
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
                    sleep(200);
                    Intent intent = new Intent(UserRegisterActivity.this, c);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    UserRegisterActivity.this.startActivity(intent);
                    UserRegisterActivity.this.finish();
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
