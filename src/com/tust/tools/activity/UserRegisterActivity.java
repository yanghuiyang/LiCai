package com.tust.tools.activity;

import com.tust.tools.R;
import com.tust.tools.bean.User;
import com.tust.tools.db.UserData;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class UserRegisterActivity extends Activity implements OnClickListener{
	private Button btn_register;
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
		setContentView(R.layout.register);
		btn_register = (Button) findViewById(R.id.btn_register);
		btn_register.setOnClickListener(this);
		account = (EditText) this.findViewById(R.id.register_account);
		pwd = (EditText) this.findViewById(R.id.register_pwd);
		tel = (EditText) this.findViewById(R.id.register_tel);
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
			    	String a = user.getUsername()+"-"+user.getPwd();
			    	//待做数据校验 如重复用户等情况
			    	long result = userData.SaveUser(user); 
			    	if(result != -1){
			    		showMsg("注册成功");
			    		changeActivity(LoginActivity.class);
			    	}else{
			    		showMsg("注册失败");
			    	}
		    	}
//		    	changeActivity(LoginActivity.class);
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

}
