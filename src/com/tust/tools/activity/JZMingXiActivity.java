package com.tust.tools.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tust.tools.R;
import com.tust.tools.bean.JZshouru;
import com.tust.tools.bean.JZzhichu;
import com.tust.tools.service.DongHua3d;
import com.tust.tools.service.DongHuaYanChi;
import com.tust.tools.service.GetTime;
import com.tust.tools.service.JZMingXiAdapter;

public class JZMingXiActivity extends Activity implements OnClickListener,OnItemClickListener {
	//顶部时间的上月和下月,支出收入按钮
	private RelativeLayout back_rl,next_rl,zhichu_rl,shouru_rl;
	//底部汇总界面,列表界面
	private LinearLayout huizong_ll,list_ll;
	//list列表
	private ListView listView;
	//月份,汇总
	private TextView time_text,huizong_text,zhichu_text,shouru_text,shengyu_text;
	//当前年月
	private int year,month;
	//当前时间
	private String nowTime;
	//当前类别
	private int flag=0;
	//类别  支出和收入
	public static final int shouru_flag=4010;
	public static final int zhichu_flag=4020;
	public static MessageHandler mh;
	// 数据库操作
//	private DataHelper dataHelper;
	//适配器
	private JZMingXiAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jz_mingxi);
		mh = new MessageHandler();
		adapter = new JZMingXiAdapter(this);
		init();
	}
	
	public void init(){
		back_rl = (RelativeLayout)this.findViewById(R.id.jz_mingxi_back_rl);
		back_rl.setOnClickListener(this);
		next_rl = (RelativeLayout)this.findViewById(R.id.jz_mingxi_next_rl);
		next_rl.setOnClickListener(this);
		zhichu_rl = (RelativeLayout) this.findViewById(R.id.jz_mingxi_zhichu_rl);
		zhichu_rl.setOnClickListener(this);
		shouru_rl = (RelativeLayout) this.findViewById(R.id.jz_mingxi_shouru_rl);
		shouru_rl.setOnClickListener(this);
		huizong_ll = (LinearLayout)this.findViewById(R.id.jz_mingxi_huizong_ll);
		huizong_ll.setVisibility(View.GONE);
		list_ll = (LinearLayout)this.findViewById(R.id.jz_mingxi_list_ll);
		time_text = (TextView)this.findViewById(R.id.jz_mingxi_time_text);
		huizong_text = (TextView)this.findViewById(R.id.jz_mingxi_huizong);
		huizong_text.setOnClickListener(this);
		zhichu_text = (TextView)this.findViewById(R.id.jz_mingxi_benyuezhichu_text);
		shouru_text = (TextView)this.findViewById(R.id.jz_mingxi_benyueshouru_text);
		shengyu_text = (TextView)this.findViewById(R.id.jz_mingxi_benyueshengyu_text);
		
		year = GetTime.getYear();
		month = GetTime.getMonth();
		if(month<10){
			nowTime = year+"年0"+month+"月";
		}else{
			nowTime = year+"年"+month+"月";	
		}
		time_text.setText(nowTime);
		listView  = (ListView) findViewById(R.id.jz_mingxi_list);
		flag = zhichu_flag;
		getHuiZong();
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		listView.setLayoutAnimation(DongHua3d.listDongHua());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.jz_mingxi_back_rl:
			backMonth(flag);
			break;
		case R.id.jz_mingxi_next_rl:
			nextMonth(flag);
			break;
		case R.id.jz_mingxi_zhichu_rl:
			flag = zhichu_flag;
			yanChiShow(list_ll);
			break;
		case R.id.jz_mingxi_shouru_rl:
			flag  =shouru_flag;
			yanChiShow(list_ll);
			break;
		case R.id.jz_mingxi_huizong :
			if(huizong_ll.isShown()){
				DongHuaYanChi.dongHuaEnd(huizong_ll, JZMingXiActivity.this, mh, R.anim.jz_menu_down, 300);
			}else{
				huizong_ll.setAnimation(AnimationUtils.loadAnimation(JZMingXiActivity.this,R.anim.jz_menu_up));
				huizong_ll.setVisibility(View.VISIBLE);
			}
			break;
		}
	}
	
	/*
	 * 获取当前月汇总
	 * */
	public void getHuiZong(){
		double count[] = adapter.getList(year, month, 0, flag);
		zhichu_text.setText(count[0]+"");
		shouru_text.setText(count[1]+"");
		shengyu_text.setText((count[1]-count[0])+"");
		adapter.notifyDataSetChanged();
	}
	/*
	 * 下一个月方法
	 * */
	public void nextMonth(int flag){
		month++;
		if(month>12){
			month=1;
			year++;
		}
		if(month<10){
			nowTime = year+"年0"+month+"月";
		}else{
			nowTime = year+"年"+month+"月";	
		}
		time_text.setText(nowTime);
		yanChiShow(list_ll);
	}
	
	/*
	 * 上一个月方法
	 * */
	public void backMonth(int flag){
		month--;
		if(month<1){
			month=12;
			year--;
		}
		if(month<10){
			nowTime = year+"年0"+month+"月";
		}else{
			nowTime = year+"年"+month+"月";	
		}
		time_text.setText(nowTime);	
		yanChiShow(list_ll);
	}

	/*
	 * 切换动画，每次切换执行
	 * */
    public void yanChiShow(final View v){
    	//顶部支出和收入按下后的背景
    	if(flag == zhichu_flag){
			zhichu_rl.setBackgroundResource(R.drawable.jz_bt_bg_s);
			shouru_rl.setBackgroundResource(R.drawable.blank);
		}else if(flag == shouru_flag){
			shouru_rl.setBackgroundResource(R.drawable.jz_bt_bg_s);
			zhichu_rl.setBackgroundResource(R.drawable.blank);
		}
    	new Thread(){
    		public void run(){
    			try {
    				mh.post(new Runnable(){
        				public void run(){
        					DongHua3d.applyRotation(v, 0, 180, 0);
        				 }
        				});
					sleep(400);
					mh.post(new Runnable(){
        				public void run(){
        					//下面方法封装getHuiZong();为一个线程，否则直接放到这里会出现动画中丢帧现象
        					huiZongThread();
	    					DongHua3d.applyRotation(v, 0, 180, 1);
        				 }
    				});
    			} catch (InterruptedException e) {
					e.printStackTrace();
				}
    		}
    	}.start();
    }
    
    //下面方法封装getHuiZong();为一个线程，否则直接放到这里会出现动画中丢帧现象
    public void huiZongThread(){
    	new Thread(){
    		public void run(){
    			mh.post(new Runnable(){
    				public void run(){
    					getHuiZong();
    				 }
				});
    		}
    	}.start();
    }
    
	@Override
	protected void onResume() {
		getHuiZong();
		overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
		super.onResume();
	}

	public boolean onKeyDown(int kCode, KeyEvent kEvent) {
		switch (kCode) {
		case KeyEvent.KEYCODE_BACK: {
			this.finish();
			return false;
		 }
		}
		return super.onKeyDown(kCode, kEvent);
	}

	public void showMsg(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	public class MessageHandler extends Handler {
		public void handleMessage(Message msg) {
			
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent(this,JZAddActivity.class);
		if(flag == zhichu_flag){
			JZzhichu zc = (JZzhichu)view.getTag();
			intent.putExtra("update", true);
			intent.putExtra("type", JZAddActivity.zhichu_flag);
			intent.putExtra("id", zc.getZc_Id());
			startActivity(intent);
		}else if(flag == shouru_flag){
			JZshouru sr = (JZshouru)view.getTag();
			intent.putExtra("update", true);
			intent.putExtra("type", JZAddActivity.shouru_flag);
			intent.putExtra("id", sr.getSr_Id());
			startActivity(intent);
		}
	}

}
