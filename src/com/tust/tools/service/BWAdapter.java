package com.tust.tools.service;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.tust.tools.R;
import com.tust.tools.bean.BWcontent;
import com.tust.tools.db.BWData;

public class BWAdapter extends BaseAdapter implements OnCheckedChangeListener{
	//����б����������б�ļ���
	public static ArrayList<BWcontent> bWlist;
	//�洢��ѡitem��id
	public ArrayList<Integer> idList;
	//�Ƿ���ʾ��ѡ��
	public boolean isShowCheck;
	//�Ƿ�ȫѡ
	public boolean isSelectAll;
	private Context context;

	public BWAdapter(Context context) {
		this.context = context;
		bWlist = new ArrayList<BWcontent>();
		idList=new ArrayList<Integer>();
		getList();
		isShowCheck=false;
		isSelectAll=false;
	}

	public void getList(){
		BWData dataHelper = new BWData(context);
		bWlist = dataHelper.GetBWList("");	
	}

	@Override
	public int getCount() {
		return bWlist.size();
	}

	@Override
	public Object getItem(int position) {
		return bWlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
			convertView = LayoutInflater.from(context).inflate(R.layout.bw_list_item, null);
			BWcontent beiwang = new BWcontent();
			beiwang = bWlist.get(position);
			convertView.setTag(beiwang.getId());
			TextView content_text = (TextView) convertView.findViewById(R.id.bw_list_item_content_text);
			TextView date_text = (TextView) convertView.findViewById(R.id.bw_list_item_date_text);
			TextView week_text = (TextView) convertView.findViewById(R.id.bw_list_item_week_text);
			TextView time_text = (TextView) convertView.findViewById(R.id.bw_list_item_time_text);
			content_text.setText(beiwang.getContent());
			content_text.setSingleLine(true);
			date_text.setText(beiwang.getMonth()+"��"+beiwang.getDay()+"��");
			time_text.setText(beiwang.getTime());
			String weekString ="����";
			if(beiwang.getWeek()==1){
				weekString = weekString+"һ";
			}else if(beiwang.getWeek()==2){
				weekString = weekString+"��";
			}else if(beiwang.getWeek()==3){
				weekString = weekString+"��";
			}else if(beiwang.getWeek()==4){
				weekString = weekString+"��";
			}else if(beiwang.getWeek()==5){
				weekString = weekString+"��";
			}else if(beiwang.getWeek()==6){
				weekString = weekString+"��";
			}else if(beiwang.getWeek()==7){
				weekString = weekString+"��";
			}
			week_text.setText(weekString);
			CheckBox cb =(CheckBox)convertView.findViewById(R.id.bw_list_item_check);
			cb.setTag(beiwang.getId());
			cb.setOnCheckedChangeListener(this);
			if(!isShowCheck){
			    cb.setVisibility(View.GONE);
			}else{
			    cb.setVisibility(View.VISIBLE);
			    if(isSelectAll){
			        cb.setChecked(true);
			    }else{
			        idList.clear();
			        cb.setChecked(false); 
			    }
			}
		return convertView;
	}

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){
System.out.println("add(Integer)v.getTag())=="+(Integer)buttonView.getTag());
            idList.add((Integer)buttonView.getTag());    
        }else{
System.out.println("remove(Integer)v.getTag())=="+(Integer)buttonView.getTag());
            idList.remove((Integer)buttonView.getTag());   
        }
    }
}
