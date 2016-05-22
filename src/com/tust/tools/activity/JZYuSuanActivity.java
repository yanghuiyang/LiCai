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
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tust.tools.R;
import com.tust.tools.bean.JZzhichu;
import com.tust.tools.bean.User;
import com.tust.tools.db.BudgetData;
import com.tust.tools.db.ExpenditureTypeData;
import com.tust.tools.db.JZData;
import com.tust.tools.db.UserData;
import com.tust.tools.service.GetTime;
import com.tust.tools.service.ListEditorAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JZYuSuanActivity extends Activity implements OnClickListener {
	private EditText et;
	private TextView yusuan,yue;
	private Button saveBt, cancelBt;
	private String userName;
	private User user;
	private UserData userData;
	private BudgetData budgetData;
	private ExpenditureTypeData expenditureTypeData;
	private List<String> typenames;//存放类型列表
	private ListView listView;
	private ListEditorAdapter mAdapter;
	private List<Map<String, String>> mData = new ArrayList<Map<String,String>>();//预算暖类型-实际分配的值
	private List<Map<String, Integer>> recommendation = new ArrayList<Map<String,Integer>>();//预算类型-推荐值
//	private List<Map<String, Double>> typePercent = new ArrayList<Map<String,Double>>();
	private Map<String, Double> typePercent = new HashMap<String, Double>();
	private int yue_int = 0;
	private Handler handler = new Handler();
	private Runnable runnable = new Runnable() {
		public void run() {
			this.update();
			handler.postDelayed(this, 1000 );// 间隔1秒
		}
		void update() {
			Integer count=0;
			for (Map<String, String> m : mData) {
				for (String k : m.keySet()) {
					if (!m.get(k).equals("") && !m.get(k).equals("0")) {
						count = count + Integer.valueOf(m.get(k));
					}
				}
			}
			yue_int = Integer.parseInt(et.getText().toString()) - count; //
			if (yue_int<0){
					yue.setText("-"+yue_int);
			}else {
				yue.setText(yue_int+"");
			}
		}
	};//刷新预算剩余额度
	private JZData jzData ;
	private int n = 0;
	private boolean flag; //判断是否使用默认推荐设置（false）或者取往月个额度算数平均值（true）
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
		yue = (TextView) this.findViewById(R.id.jz_yusuan_yue_text);//预算余额
		//获取当前登陆用户
		handler.postDelayed(runnable, 1000);//定时刷新更新剩余额度
		SharedPreferences preferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
		userName = preferences.getString("userName", "");
		user = new User();
		userData = new UserData(this);
		budgetData = new BudgetData(this);
		user = userData.getUserByUserName(userName);
		yusuan.setText("当前本月预算：" + user.getBudget() + "");
		et.setText(""+user.getBudget());
		listView = (ListView) findViewById(R.id.list);
		mAdapter = new ListEditorAdapter(this);//ListEditorAdapter 预算界面 listview封装
		listView.setAdapter(mAdapter);
		expenditureTypeData = new ExpenditureTypeData(this);
		typenames=expenditureTypeData.getTypesByUserName(userName);//获取该用户所有支出类型
		jzData = new JZData(this);

		setFlag();////设置标志 推荐预算值是自己的默认算法还是根据过去支出情况
		initData();//初始化界面数据
		editTextWatcher();//设置textwathcer 输入预算值时更改推荐值
	}
	//第一次进入 初始化数据
	private void initData() {
			for (int i = 0; i < typenames.size(); i++) {
				Map<String, String> budget = new HashMap<String, String>();//实际预算值
				Map<String, Integer> re = new HashMap<String, Integer>();//推荐值
				int temp = 0;
				temp = budgetData.getUserOneBudget(userName, typenames.get(i), GetTime.getYear(), GetTime.getMonth());
				n += temp;
				budget.put(typenames.get(i), "" + temp);
				if (flag == false){
					re.put(typenames.get(i), budgetData.getTypeBudget(user, user.getBudget(), typenames.get(i)));
				}else{
					re.put(typenames.get(i) ,(int)(user.getBudget()*typePercent.get(typenames.get(i))));
				}
				recommendation.add(re);
				mData.add(budget);
			}
		mAdapter.setData(mData,recommendation);
		yue_int = user.getBudget() - n; //第一次
		yue.setText(yue_int+"");
	}
//设置标志 推荐预算值是自己的默认算法还是根据过去支出情况 判断依据前几个月有记账数据是flag = true
	private void setFlag() {
	Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH,-3); //前3个月 日历 JANUARY，它为 0
		int count = 0;
		count = jzData.getMonthSpend(userName,c.get(Calendar.YEAR),c.get(Calendar.MONTH)+1);
		if (0 == count){
			flag = false;
		}else {
			flag = true;
			getTypesPercent();
		}
	}
	//获取个类型的近几个月的所占支出总额的百分比 用来计算推荐预算值
	private void getTypesPercent() {
		for (int i = 0; i < typenames.size(); i++) {
			Double temp = 0.00;
//			Map<String, Double> per = new HashMap<String, Double>();
			for (int j= 1 ;j<4;j++){
				double a , b = 0.00; //a 分母 每月支出总额 b 分子 每月某类型支出总额 必须是double 如果是int 整数相除还是为整数
				Calendar c = Calendar.getInstance();
				c.add(Calendar.MONTH,-j);
				int year =c.get(Calendar.YEAR);
				int month=c.get(Calendar.MONTH)+1;
				a = jzData.getMonthSpend(userName,year,month);//获得该用户某年某月支出总额
				String test = typenames.get(i);
				b = jzData.getTypeMonthSpend(userName,typenames.get(i),year,month);//获得某用户某年某月某支出类型的支出总额
				if (a != 0){
					temp += b/a;
				}
			}
			temp = temp/3;
			typePercent.put(typenames.get(i),temp);

//			per.put(typenames.get(i),temp);
//			typePercent.add(per);
		}
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
				String newStr = s.toString().replaceFirst("^0*", "");
				if(!newStr.equals("")){
				int total =Integer.parseInt(newStr);
				mData.clear();
				recommendation.clear();
				if(total>=0) {
					for (int i = 0; i < typenames.size(); i++) {
						Map<String, String> budget = new HashMap<String, String>();//实际预算值
						Map<String, Integer> re = new HashMap<String, Integer>();//推荐值
						int temp = 0;
						temp = budgetData.getUserOneBudget(userName, typenames.get(i), GetTime.getYear(), GetTime.getMonth());;
						budget.put(typenames.get(i), "" + temp);
						if (flag == false){
							re.put(typenames.get(i), budgetData.getTypeBudget(user, total, typenames.get(i)));
						}else{
							re.put(typenames.get(i) ,(int)(total*typePercent.get(typenames.get(i))));
						}
						recommendation.add(re);
						mData.add(budget);
					}
					mAdapter.setData(mData, recommendation);
					mAdapter.notifyDataSetChanged();
				}
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
			int inputTotal=0;
			for (Map<String, String> m : mData) {
				for (String k : m.keySet()) {
					String newStr = m.get(k).toString().replaceFirst("^0*", "");
					if(!newStr.equals("")) {
						inputTotal += Integer.parseInt(newStr);
					}
				}
			}
			if(inputTotal>num){
				showMsg("各分类预算超过了总预算哟");
			}else {
				user.setBudget(num);
				Calendar now = Calendar.getInstance();
				user.setYm((now.get(Calendar.YEAR)+"")+(now.get(Calendar.MONTH) + 1) + "");

				userData.UpdateUserInfo(user);
				budgetData.SaveOrUpdateUserBudget(mData, userName);//注意类型 string 和int转换
				yusuan.setText("当前本月预算：" + user.getBudget() + "");
				et.setText("" + num);
				showMsg("保存成功");

				//this.finish();
			}
			break;
		case R.id.jz_yusuan_cancelbt:
//			showMsg(mData.get(0).toString()+"---"+mData.get(0).get(0+"")+"\n");
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
	@Override
	protected void onDestroy() {
		handler.removeCallbacks(runnable); //停止刷新
		super.onDestroy();
	}

}
