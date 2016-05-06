package com.tust.tools.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tust.tools.R;
import com.tust.tools.bean.User;
import com.tust.tools.db.BudgetData;
import com.tust.tools.db.ExpenditureTypeData;
import com.tust.tools.db.UserData;
import com.tust.tools.service.GetTime;
import com.tust.tools.service.ListEditorAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JZYuSuanActivity extends Activity implements OnClickListener {

	private EditText et;
	private TextView yusuan;
	private Button saveBt, cancelBt;
	private String userName;
	private User user;
	private UserData userData;
	private BudgetData budgetData;
	private ExpenditureTypeData expenditureTypeData;
	private List<String> typenames;//存放类型列表
	private ListView listView;
	private ListEditorAdapter mAdapter;

	private List<Map<String, String>> mData = new ArrayList<Map<String,String>>();
	private List<Map<String, Integer>> recommendation = new ArrayList<Map<String,Integer>>();//预算类型-推荐值
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
		budgetData = new BudgetData(this);
		user = userData.getUserByUserName(userName);
		yusuan.setText("当前本月预算：" + user.getBudget() + "");
		et.setText(""+user.getBudget());

		listView = (ListView) findViewById(R.id.list);
		mAdapter = new ListEditorAdapter(this);
		listView.setAdapter(mAdapter);
		expenditureTypeData = new ExpenditureTypeData(this);
		typenames=expenditureTypeData.getTypesByUserName(userName);//获取该用户所有支出类型
		for(int i=0;i<typenames.size();i++){
			Map<String, String> budget = new HashMap<String, String>();//实际预算值
			Map<String, Integer> re = new HashMap<String, Integer>();//推荐值
			int temp = 0;
			temp = budgetData.getUserOneBudget(userName,typenames.get(i), GetTime.getYear(),GetTime.getMonth());
			budget.put(typenames.get(i),""+temp);
			re.put(typenames.get(i),budgetData.getTypeBudget(user,user.getBudget(),typenames.get(i)));
			recommendation.add(re);
			mData.add(budget);
		}
		mAdapter.setData(mData,recommendation);
		editTextWatcher();

	}
/*
 	总预算watcher 输入月度预算 实时刷新推荐值列表
 */
	private void editTextWatcher() {
		et.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				//	textview.setText(edittext.getText());
				//	int total = Integer.valueOf(et.getText().toString());
				String newStr = s.toString().replaceFirst("^0*", "");
				int total =Integer.parseInt(newStr);
				mData.clear();
				recommendation.clear();
				if(total>=0) {
					for (int i = 0; i < typenames.size(); i++) {
						Map<String, String> budget = new HashMap<String, String>();//实际预算值
						Map<String, Integer> re = new HashMap<String, Integer>();//推荐值
						int temp = 0;
						temp = budgetData.getUserOneBudget(userName, typenames.get(i), GetTime.getYear(), GetTime.getMonth());
						budget.put(typenames.get(i), "" + temp);
						re.put(typenames.get(i), budgetData.getTypeBudget(user, total, typenames.get(i)));
						recommendation.add(re);
						mData.add(budget);
					}

					mAdapter.setData(mData, recommendation);
					mAdapter.notifyDataSetChanged();
				}
			}
		});
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
			user.setBudget(num);
			userData.UpdateUserInfo(user);
			budgetData.SaveOrUpdateUserBudget(mData,userName);//注意类型 string 和int转换
			yusuan.setText("当前本月预算：" + user.getBudget() + "");
			et.setText(""+num);
			showMsg("保存成功");
			this.finish();
			break;
		case R.id.jz_yusuan_cancelbt:
			showMsg(mData.get(0).toString()+"---"+mData.get(0).get(0+"")+"\n");
		//	this.finish();
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
