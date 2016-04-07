package com.tust.tools.activity;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.tust.tools.R;
import com.tust.tools.bean.BWcontent;
import com.tust.tools.db.BWData;
import com.tust.tools.dialog.DialogBWSheZhiMiMa;
import com.tust.tools.service.BWAdapter;
import com.tust.tools.service.DongHua3d;
import com.tust.tools.service.DongHuaYanChi;
import com.tust.tools.service.SDrw;

public class BWMainActivity extends Activity implements OnClickListener, OnItemClickListener {
	// 主界面list
	private ListView listView;
	// 添加新备忘按钮
	private ImageButton ib;
	// 主界面list适配器
	private BWAdapter adapter;
	// 主界面底部多选删除界面
	private LinearLayout del_ll;
	// 多选删除三个按钮
	private Button selectAll, delSelect, cancelSelect;
	// 
	private Handler handler;
	// 创建数据库对象
	BWData dataHelper;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bw_main);
		del_ll = (LinearLayout) this.findViewById(R.id.bw_main_select_ll);
		del_ll.setVisibility(View.GONE);
		listView = (ListView) this.findViewById(R.id.bw_list);
		ib = (ImageButton) this.findViewById(R.id.bw_main_addbt);
		ib.setOnClickListener(this);
		adapter = new BWAdapter(this);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		handler = new Handler();
		dataHelper = new BWData(this);
		initDelLL();
	}

	public void initDelLL() {
		selectAll = (Button) this.findViewById(R.id.bw_main_select_quanxuan);
		selectAll.setOnClickListener(this);
		delSelect = (Button) this.findViewById(R.id.bw_main_select_del);
		delSelect.setOnClickListener(this);
		cancelSelect = (Button) this.findViewById(R.id.bw_main_select_quxiao);
		cancelSelect.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		adapter.getList();
		adapter.notifyDataSetChanged();
		overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
		listView.setLayoutAnimation(DongHua3d.listDongHua());
		super.onResume();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// 获取点击的Item的Tag（Tag设置为id）
		int content_id = (Integer) view.getTag();
		Intent intent = new Intent(this, BWNewActivity.class);
		intent.putExtra("id", content_id);// 将id传送到新建的activity
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.bw_main_addbt:
			Intent intent = new Intent(BWMainActivity.this, BWNewActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			BWMainActivity.this.startActivity(intent);
			break;
		case R.id.bw_main_select_quanxuan:
			adapter.isSelectAll = true;
			adapter.notifyDataSetChanged();
			break;
		case R.id.bw_main_select_del:
			delSelect();
			break;
		case R.id.bw_main_select_quxiao:
			adapter.isSelectAll = false;
			adapter.notifyDataSetChanged();
			break;
		}
	}

	public void delSelect() {
		if (adapter.idList != null && adapter.idList.size() > 0) {
			for (int id : adapter.idList) {
				dataHelper.DelBWInfo(id);
			}
			adapter.getList();
			adapter.isShowCheck = false;
			adapter.isSelectAll = false;
			adapter.notifyDataSetChanged();
			DongHuaYanChi.dongHuaEnd(del_ll, BWMainActivity.this, handler, R.anim.jz_menu_down, 300);
		} else {
			showMsg("你还没有选择要删除项");
		}
	}

	public void changeActivity(final Class<?> c) {
		new Thread() {
			public void run() {
				try {
					sleep(300);
					Intent intent = new Intent(BWMainActivity.this, c);
					intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					BWMainActivity.this.startActivity(intent);
					BWMainActivity.this.finish();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	public boolean onKeyDown(int kCode, KeyEvent kEvent) {
		switch (kCode) {
		case KeyEvent.KEYCODE_BACK: {
			if (del_ll.isShown()) {
				DongHuaYanChi.dongHuaEnd(del_ll, BWMainActivity.this, handler, R.anim.jz_menu_down, 300);
				adapter.isShowCheck = false;
				adapter.notifyDataSetChanged();
			} else {
				exitDialog("exit");
			}
			return false;
		}
		}
		return super.onKeyDown(kCode, kEvent);
	}

	/*
	 * 退出弹出框
	 */
	String fileName;// 以当前时间命名的文件名

	public void exitDialog(final String flag) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		String confrimStr = "";
		String cancelStr = "";
		if (flag.equals("exit")) {
			builder.setTitle("是否确认退出？");
			confrimStr = "退出小助手";
			cancelStr = "退出备忘录";
		} else if (flag.equals("out")) {
			fileName = getFileName("all.txt");
			builder.setMessage("确定后导出的文件名为"+fileName+"存放在SD/tust/tools/beiwang/txt/文件夹下");
			//builder.setTitle("确定后导出的文件名为" + fileName + "存放在SD/tust/tools/beiwang/txt/文件夹下");
			confrimStr = "确定";
			cancelStr = "取消";
		}
		builder.setPositiveButton(confrimStr, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (flag.equals("exit")) {
					System.exit(0);
				} else if (flag.equals("out")) {
					String dir ="beiwang/txt/"; 
					SDrw ic = new SDrw(fileName, true,dir);
					String string = "";
					for (BWcontent beiwang : BWAdapter.bWlist) {
						String line1 = "---" + beiwang.getYear() + "年" + beiwang.getMonth() + "月" + beiwang.getDay() + "日" + beiwang.getTime() + "\r\n";
						String line2 = "---" + beiwang.getContent() + "\r\n\r\n";
						string += line1 + line2;
					}
					ic.outWrite(string);
					showMsg("导出成功");
				}
			}
		});
		builder.setNeutralButton(cancelStr, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (flag.equals("exit")) {
					Intent intent = new Intent(BWMainActivity.this, ToolsMainActivity.class);
					BWMainActivity.this.startActivity(intent);
					BWMainActivity.this.finish();
				} else if (flag.equals("out")) {
					builder.create().cancel();
				}
			}
		});
		builder.create().show();
	}

	/*
	 * 通过相机回传图片的文件名
	 */
	public String getFileName(String houzhui) {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		return dateFormat.format(date) + houzhui;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 100, 0, "导出所有");
		menu.add(0, 200, 0, "选择删除");
		menu.add(0, 300, 0, "设置密码");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 100:
			if (BWAdapter.bWlist != null && BWAdapter.bWlist.size() > 0) {
				exitDialog("out");
			} else {
				showMsg("没有可导出条目");
			}
			break;
		case 200:
			if (!del_ll.isShown()) {
				if (BWAdapter.bWlist != null && BWAdapter.bWlist.size() > 0) {
					del_ll.setVisibility(View.VISIBLE);
					del_ll.setAnimation(AnimationUtils.loadAnimation(this, R.anim.jz_menu_up));
					adapter.isShowCheck = true;
					adapter.notifyDataSetChanged();
				} else {
					showMsg("没有可选条目");
				}
			} else {
				DongHuaYanChi.dongHuaEnd(del_ll, BWMainActivity.this, handler, R.anim.jz_menu_down, 300);
				adapter.isShowCheck = false;
				adapter.notifyDataSetChanged();
			}
			break;
		case 300:
			new DialogBWSheZhiMiMa(this);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/*
	 * 点击图片以后的动画效果
	 */
	public void dongHua(View v) {
		v.setAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
	}

	public void showMsg(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

}