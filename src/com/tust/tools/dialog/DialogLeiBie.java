package com.tust.tools.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.tust.tools.R;
import com.tust.tools.activity.JZAddActivity;
import com.tust.tools.db.JZData;
import com.tust.tools.service.DongHua3d;
import com.tust.tools.service.DongHuaYanChi;
import com.tust.tools.service.JZLeibieAdapter;

/*
 * 类别选择dialog
 * */
public class DialogLeiBie extends Dialog implements OnClickListener{
	//类别列表和类别子类列表
	private ListView lbList,lbsubList;
	//顶部背景 
	private RelativeLayout rLayout;
	private Context context;
	private View lbView;
	//类别列表适配器
	private JZLeibieAdapter adapter;
	//类别和类别子类标识
	public static final int flagsubleibie=3020;
	//当前选择的选项，收入，支出，借贷
	private int now_flag=0;
	private String userName;
	private JZData jzData;
	public DialogLeiBie(Context context,int now_flag) {
		super(context, R.style.leibiedialog);
		this.context = context;
		this.now_flag = now_flag;
		lbView = View.inflate(context, R.layout.dialog_leibie, null);
		this.setContentView(lbView);
		//获取当前登陆用户
		SharedPreferences preferences = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
		userName = preferences.getString("userName", "");
		jzData = new JZData(context);


		//类别适配器
		adapter = new JZLeibieAdapter(context,now_flag,null);
		//类别列表
		lbList = (ListView) lbView.findViewById(R.id.leibie_dialog_list);
		lbList.setAdapter(adapter);
		lbList.setOnItemClickListener(new clickItem());
		rLayout  = (RelativeLayout) lbView.findViewById(R.id.leibie_dialog_rl);
		rLayout.setOnClickListener(this);
		this.show();
		lbView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.push_up_in));
	}

	@Override
	public void onClick(View v) {
	}
	
	String flagShow;//判断当前该列表是否已显示
	Handler handler = new Handler();
	private class clickItem implements OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if(now_flag==JZAddActivity.zhichu_flag){
				getTextandSend(view);
			}else if(now_flag==JZAddActivity.shouru_flag){
				getTextandSend(view);
			}
		}
	}
	
	/*
	 * 子条目点击事件
	 * */
	private class clickSubItem implements OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			getTextandSend(view);
		}
	}
	
	public void getTextandSend(View view){
		String string = (String)view.getTag();
		//dialog退出动画
		DongHuaYanChi.dongHuaDialogEnd(this, lbView, context, handler, R.anim.push_up_out, 300);
		Message msg  =Message.obtain();
		msg.what=JZAddActivity.leibie_msg;
		msg.obj = string;//发送当前选择给ToolsJiZhangAddActivity，让其界面作出相应改变
		JZAddActivity.mh.sendMessage(msg);
	}
	
	public boolean onKeyDown(int kCode, KeyEvent kEvent) {
		switch (kCode) {
		case KeyEvent.KEYCODE_BACK: {
			if (lbView.isShown()) {
				DongHuaYanChi.dongHuaDialogEnd(this, lbView, context, handler, R.anim.push_up_out, 300);
				return false;
			} else {
				this.cancel();
			}
		}
		}
		return super.onKeyDown(kCode, kEvent);
	}
	
}
