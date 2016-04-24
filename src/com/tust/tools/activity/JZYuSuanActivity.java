package com.tust.tools.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tust.tools.R;
import com.tust.tools.bean.User;

import com.tust.tools.db.UserData;

public class JZYuSuanActivity extends Activity implements OnClickListener {
	private EditText et;
	private TextView yusuan;
	private Button saveBt, cancelBt;
	private String userName;
	private User user;
	private UserData userData;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jz_yusuan);
		et = (EditText) this.findViewById(R.id.jz_yusuan_et);
		saveBt = (Button) this.findViewById(R.id.jz_yusuan_savebt);
		saveBt.setOnClickListener(this);
		cancelBt = (Button) this.findViewById(R.id.jz_yusuan_cancelbt);
		cancelBt.setOnClickListener(this);
		yusuan = (TextView) this.findViewById(R.id.jz_yusuan_txt);
		//获取当前登陆用户
		SharedPreferences preferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
		userName = preferences.getString("userName", "");
		user = new User();
		userData = new UserData(this);
		user = userData.getUserByUserName(userName);
//		int ys = JZSqliteHelper.readPreferenceFile(this,
//				JZSqliteHelper.YUSUAN_MONTH, JZSqliteHelper.YUSUAN_MONTH);
//		yusuan.setText("当前本月预算：" + ys + "");
		yusuan.setText("当前本月预算：" + user.getBudget() + "");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.jz_yusuan_savebt:
			if (et.getText().toString().length() < 1) {
				showMsg("输入不能为空");
				return;
			} else if (et.getText().toString().length() > 7) {
				showMsg("你有这么多钱么？");
				return;
			}
			int num = Integer.parseInt(et.getText().toString());
//			JZSqliteHelper.saveYuSuan(this, JZSqliteHelper.YUSUAN_MONTH,
//					JZSqliteHelper.YUSUAN_MONTH, num);
//			int ys = JZSqliteHelper.readPreferenceFile(this,
//					JZSqliteHelper.YUSUAN_MONTH, JZSqliteHelper.YUSUAN_MONTH);
			user.setBudget(num);
			userData.UpdateUserInfo(user);
			yusuan.setText("当前本月预算：" + user.getBudget() + "");

			this.finish();
			break;
		case R.id.jz_yusuan_cancelbt:
			this.finish();
			break;
		}
	}

	@Override
	protected void onResume() {
		overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
		super.onResume();
	}

	public void showMsg(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	public class MessageHandler extends Handler {
		public void handleMessage(Message msg) {

		}
	}

}
