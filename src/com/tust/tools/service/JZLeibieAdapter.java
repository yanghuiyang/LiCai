package com.tust.tools.service;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tust.tools.R;
import com.tust.tools.activity.JZAddActivity;
import com.tust.tools.db.ExpenditureTypeData;
import com.tust.tools.db.IncomeTypeData;
import com.tust.tools.db.JZData;
import com.tust.tools.dialog.DialogLeiBie;

public class JZLeibieAdapter extends BaseAdapter{
	//类别列表或类别子类列表的集合
	private ArrayList<String> leibiearr;
	private Context context;
	private int flag = 0;
	private	 String subItem="";
	private String userName;
	private List<String> leibielist;
	public JZLeibieAdapter(Context context, int flag,String subitem) {
		this.flag = flag;
		this.context = context;
		//获取当前登陆用户
		SharedPreferences preferences = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
		userName = preferences.getString("userName", "");
		JZData jzData = new JZData(context);
		//根据传入的参数获取相应的集合
		if(flag==JZAddActivity.zhichu_flag){
			ExpenditureTypeData expenditureTypeData = new ExpenditureTypeData(context);
			leibielist=expenditureTypeData.getTypesByUserName(userName);//获取该用户所有收入类型
			leibiearr = new ArrayList<String>();
			leibiearr = new ArrayList(leibielist);
		}else if(flag==JZAddActivity.shouru_flag){
			IncomeTypeData incomeTypeData = new IncomeTypeData(context);
			leibielist=incomeTypeData.getTypesByUserName(userName);//获取该用户所有收入类型
			leibiearr = new ArrayList<String>();
			leibiearr = new ArrayList(leibielist);
		}
	}

	/*
	 * 获取类别的列表集合
	 * */
	public void getLeibieList(String s[]) {
		leibiearr = new ArrayList<String>();
		for (String str : s) {
			leibiearr.add(str);
		}
	}

	@Override
	public int getCount() {
		return leibiearr.size();
	}

	@Override
	public Object getItem(int position) {
		return leibiearr.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private RelativeLayout addsub=null;
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (flag!=DialogLeiBie.flagsubleibie) {//当标识不为子列表时加载下面的界面
			convertView = LayoutInflater.from(context).inflate(R.layout.jz_leibie_item, null);
			String lb = leibiearr.get(position);
			TextView lb_text = (TextView) convertView.findViewById(R.id.leibie_item_text);
			//addsub = (RelativeLayout) convertView.findViewById(R.id.jz_item_addsub_rl);
			convertView.setTag(lb);
			//当前选中标识
		//	addsub.setTag(lb);
			lb_text.setText(lb);
		} else {//当标识为sub时 加载下面的界面
//			convertView = LayoutInflater.from(context).inflate(R.layout.jz_leibie_subitem, null);
//			TextView sublb_text = (TextView) convertView.findViewById(R.id.leibie_subitem_text);
//			String lb = leibiearr.get(position);
//			convertView.setTag(subItem+">"+lb);
//			sublb_text.setText(lb);
		}
		return convertView;
	}
}
