package com.tust.tools.service;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tust.tools.R;
import com.tust.tools.activity.JZAddActivity;
import com.tust.tools.bean.JZItem;
import com.tust.tools.dialog.DialogLeiBie;

public class JZLeibieAdapter extends BaseAdapter{
	//����б����������б�ļ���
	private ArrayList<String> leibiearr;
	private Context context;
	private int flag = 0;
	private	String subItem="";

	public JZLeibieAdapter(Context context, int flag,String subitem) {
		this.flag = flag;
		this.context = context;
		//���ݴ���Ĳ�����ȡ��Ӧ�ļ���
		if(flag==JZAddActivity.zhichu_flag){
			getLeibieList(JZItem.leibie_s);
		}else if(flag==JZAddActivity.shouru_flag){
			getLeibieList(JZItem.shoru_s);
		}else if(flag==JZAddActivity.jiedai_flag){
			getLeibieList(JZItem.jiedai_s);
		}else if(flag==DialogLeiBie.flagsubleibie){
			getLeibieSubList(subitem);
		}
	}

	/*
	 * ��ȡ�����б���
	 * */
	public void getLeibieList(String s[]) {
		leibiearr = new ArrayList<String>();
		for (String str : s) {
			leibiearr.add(str);
		}
	}

	/*
	 * ��ȡ���������б��ϣ����ݴ���ĵ�ǰѡ��������жϣ�
	 * */
	public void getLeibieSubList(String subitem) {
		leibiearr = new ArrayList<String>();
		String strs[]=null;
		if(subitem.equals("����")){
			strs=JZItem.cy_s;
		}else if(subitem.equals("��ͨ")){
			strs=JZItem.jt_s;
		}else if(subitem.equals("����")){
			strs=JZItem.gw_s;
		}else if(subitem.equals("����")){
			strs=JZItem.yl_s;
		}else if(subitem.equals("ҽ��")){
			strs=JZItem.yj_s;
		}else if(subitem.equals("�Ӽ�")){
			strs=JZItem.jj_s;
		}else if(subitem.equals("Ͷ��")){
			strs=JZItem.tz_s;
		}else if(subitem.equals("����")){
			strs=JZItem.rq_s;
		}
		this.subItem = subitem;
		for (String str : strs) {
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
		if (flag!=DialogLeiBie.flagsubleibie) {//����ʶ��Ϊ���б�ʱ��������Ľ���
			convertView = LayoutInflater.from(context).inflate(R.layout.jz_leibie_item, null);
			String lb = leibiearr.get(position);
			TextView lb_text = (TextView) convertView.findViewById(R.id.leibie_item_text);
			addsub = (RelativeLayout) convertView.findViewById(R.id.jz_item_addsub_rl);
			convertView.setTag(lb);
			//��ǰѡ�б�ʶ
			addsub.setTag(lb);
			lb_text.setText(lb);
		} else {//����ʶΪsubʱ ��������Ľ���
			convertView = LayoutInflater.from(context).inflate(R.layout.jz_leibie_subitem, null);
			TextView sublb_text = (TextView) convertView.findViewById(R.id.leibie_subitem_text);
			String lb = leibiearr.get(position);
			convertView.setTag(subItem+">"+lb);
			sublb_text.setText(lb);
		}
		return convertView;
	}
}
