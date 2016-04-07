package com.tust.tools.service;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tust.tools.R;
import com.tust.tools.activity.JZMingXiActivity;
import com.tust.tools.bean.JZshouru;
import com.tust.tools.bean.JZzhichu;
import com.tust.tools.db.JZData;

public class JZMingXiAdapter extends BaseAdapter{
	//类别列表或类别子类列表的集合
	public static ArrayList<?> mingXiList;
	private Context context;
	private int flag = 0;
	//数据库操作
    JZData dataHelper ;
	public JZMingXiAdapter(Context context) {
		this.context = context;
		dataHelper= new JZData(context);
	}

	//根据传入的参数获取相应的集合
	public double[] getList(int year,int month,int day,int flag){
	    this.flag = flag;
	    double countZhiChu = 0,countShouRu = 0;
	    ArrayList<JZzhichu> zhiChuList = new ArrayList<JZzhichu>();
	    String selectionzhichu = JZzhichu.ZC_YEAR+"="+year+" and "+JZzhichu.ZC_MONTH+"="+month;
	    zhiChuList =  dataHelper.GetZhiChuList(selectionzhichu);
    	for(JZzhichu zhichu:zhiChuList){
    		countZhiChu += zhichu.getZc_Count();
    	}
    	ArrayList<JZshouru> shouRuList = new ArrayList<JZshouru>();
        String selectionshouru = JZshouru.SR_YEAR+"="+year+" and "+JZshouru.SR_MONTH+"="+month;
        shouRuList =  dataHelper.GetShouRuList(selectionshouru);
        for(JZshouru shouru:shouRuList){
        	countShouRu += shouru.getSr_Count();
    	}
	    if(flag==JZMingXiActivity.zhichu_flag){
	    	mingXiList = zhiChuList;
	    }else if(flag==JZMingXiActivity.shouru_flag){
            mingXiList = shouRuList;
	    }
	    return new double[]{countZhiChu,countShouRu};
	}
	@Override
	public int getCount() {
		return mingXiList.size();
	}

	@Override
	public Object getItem(int position) {
		return mingXiList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
			convertView = LayoutInflater.from(context).inflate(R.layout.jz_mingxi_item, null);
			JZzhichu zhichu = new JZzhichu();
			JZshouru shouru = new JZshouru();
			//类别
            TextView lb_text = (TextView) convertView.findViewById(R.id.mingxi_leibie_item_text);
            TextView jine_text = (TextView) convertView.findViewById(R.id.mingxi_jine_item_text);
            TextView beizhu_text = (TextView) convertView.findViewById(R.id.mingxi_beizhu_item_text);
            TextView time_text = (TextView) convertView.findViewById(R.id.mingxi_time_item_text);
			if(flag==JZMingXiActivity.zhichu_flag){
			    zhichu = (JZzhichu)mingXiList.get(position);
			    convertView.setTag(zhichu);
			    if(zhichu.getZc_SubItem().length()>0){
			        lb_text.setText(zhichu.getZc_Item()+">"+zhichu.getZc_SubItem());
			    }else{
			        lb_text.setText(zhichu.getZc_Item());
			    }
			    jine_text.setText(zhichu.getZc_Count()+"");
			    beizhu_text.setText(zhichu.getZc_Beizhu());
			    time_text.setText(zhichu.getZc_Day()+"日 "+zhichu.getZc_Time());
			}else if(flag==JZMingXiActivity.shouru_flag){
			    shouru = (JZshouru)mingXiList.get(position);
			    convertView.setTag(shouru);
			    lb_text.setText(shouru.getSr_Item());
			    jine_text.setText(shouru.getSr_Count()+"");
                beizhu_text.setText(shouru.getSr_Beizhu());
                time_text.setText(shouru.getSr_Day()+"日 "+shouru.getSr_Time());
			}
		return convertView;
	}
}
