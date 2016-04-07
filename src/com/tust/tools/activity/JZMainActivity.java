package com.tust.tools.activity;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tust.tools.R;
import com.tust.tools.bean.JZshouru;
import com.tust.tools.bean.JZzhichu;
import com.tust.tools.db.JZData;
import com.tust.tools.db.JZSqliteHelper;
import com.tust.tools.service.DongHua3d;
import com.tust.tools.service.GetTime;
import com.tust.tools.service.JZPaintViewYuE;
import com.tust.tools.service.JZPaintViewZandS;

public class JZMainActivity extends Activity implements OnClickListener {
    // 支出界面的TextView
    TextView zhichu_week, zhichu_month, zhichu_shouru_month, yusuan_month, yusuanyue_month;
    // 收入界面的TextView
    TextView shouru_year, shouru_month, shouru_day;
    // 顶部framelayout支出和收入
    private FrameLayout zhihcu_fl, shouru_fl;
    // 绘图区域
    private RelativeLayout zhichu_shang_rl, zhichu_xia_rl, shouru_shang_rl;
    // 底部环形按钮 添加， 明细，报表，预算，设置。
    private Button tianjia, mingxi, baobiao, yusuan, shezhi;
    // tab1顶部支出和收入按钮选中后背景图像
    private ImageView ivZhichu, ivShuru, menu;
    // tab1顶部支出和收入按钮选中后下面显示的内容
    private LinearLayout zhichull, shourull;
    // 底部环形按钮是否显示
    private boolean isShown = false;
    // button集合 方便管理按钮显示隐藏
    private Button bts[] = null;
    // 数据库操作
    JZData dataHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jz_main);
        dataHelper = new JZData(this);
        init();
        initButton();
        TextView gg=(TextView)this.findViewById(R.id.jz_gg_text);
        if(!ToolsMainActivity.isShow){
            gg.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onResume() {
    	zhichu_shang_rl.removeAllViews();
    	zhichu_xia_rl.removeAllViews();
    	shouru_shang_rl.removeAllViews();
        overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
        getZhiChu();
        getShouRu();
        super.onResume();
    }

    @Override
    protected void onPause() {
    	hiddenView();
        menu.setImageResource(R.drawable.jz_main_more);
        super.onPause();
    }

    /*
     * 初始化组件
     */
    public void init() {
        ivZhichu = (ImageView) findViewById(R.id.jz_tab1_ivzhichu);
        ivShuru = (ImageView) findViewById(R.id.jz_tab1_ivshouru);
        zhichull = (LinearLayout) findViewById(R.id.jz_ll_zhichu);
        shourull = (LinearLayout) findViewById(R.id.jz_ll_shouru);
        zhichull.setVisibility(View.VISIBLE);
        shourull.setVisibility(View.GONE);
        ivZhichu.setImageResource(R.drawable.jz_tab1_bt_bgs);
        zhichu_shang_rl = (RelativeLayout) this.findViewById(R.id.jz_main_zhichu_pic_shang_rl);
        zhichu_xia_rl = (RelativeLayout) this.findViewById(R.id.jz_main_zhichu_pic_xia_rl);
        shouru_shang_rl = (RelativeLayout) this.findViewById(R.id.jz_main_shouru_pic_shang_rl);
        zhichu_week = (TextView) findViewById(R.id.jz_main_zhichu_week_text);
        zhichu_month = (TextView) findViewById(R.id.jz_main_zhichu_month_text);
        zhichu_shouru_month = (TextView) findViewById(R.id.jz_main_zhichu_shouru_text);
        yusuan_month = (TextView) findViewById(R.id.jz_main_zhichu_yusuan_text);
        yusuanyue_month = (TextView) findViewById(R.id.jz_main_zhichu_yusuanyue_text);
        shouru_year = (TextView) findViewById(R.id.jz_main_shouru_year_text);
        shouru_month = (TextView) findViewById(R.id.jz_main_shouru_month_text);
        shouru_day = (TextView) findViewById(R.id.jz_main_shouru_day_text);
    }

    /*
     * 初始化底部环形按钮
     */
    public void initButton() {
        // 顶部收入支出按钮
        zhihcu_fl = (FrameLayout) findViewById(R.id.jz_main_zhichu_fl);
        zhihcu_fl.setOnClickListener(this);
        shouru_fl = (FrameLayout) findViewById(R.id.jz_main_shouru_fl);
        shouru_fl.setOnClickListener(this);
        tianjia = (Button) findViewById(R.id.jz_main_bt_add);
        tianjia.setOnClickListener(this);
        mingxi = (Button) findViewById(R.id.jz_main_bt_mingxi);
        mingxi.setOnClickListener(this);
        baobiao = (Button) findViewById(R.id.jz_main_bt_baobiao);
        baobiao.setOnClickListener(this);
        yusuan = (Button) findViewById(R.id.jz_main_bt_yusuan);
        yusuan.setOnClickListener(this);
        shezhi = (Button) findViewById(R.id.jz_main_bt_setting);
        shezhi.setOnClickListener(this);
        menu = (ImageView) findViewById(R.id.jz_mian_menuiv);
        menu.setOnClickListener(this);
        bts = new Button[] { tianjia, mingxi, baobiao, yusuan, shezhi };
        hiddenView();
    }

    /*
     * 底部环形按钮显示动画
     */
    public void btDonghua() {
        isShown = true;
        for (Button bt : bts) {
            dongHua(bt);
        }
    }

    public void dongHua(Button b) {
        b.setAnimation(AnimationUtils.loadAnimation(this, R.anim.jz_menu_up));
        b.setVisibility(View.VISIBLE);
    }

    /*
     * 隐藏底部环形按钮动画
     */
    public void btHiddenDonghua() {
    	isShown = false;
        final Handler handler = new Handler();
        for (Button bt : bts) {
            bt.setAnimation(AnimationUtils.loadAnimation(this, R.anim.jz_menu_down));
        }
        new Thread() {
            public void run() {
                try {
                    sleep(300);
                    handler.post(new Runnable() {
                        public void run() {
                            hiddenView();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /*
     * 隐藏底部环形按钮
     */
    private void hiddenView() {
        isShown = false;
        for (Button bt : bts) {
            bt.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.jz_main_zhichu_fl:// 顶部支出按钮
            ivZhichu.setImageResource(R.drawable.jz_tab1_bt_bgs);
            ivShuru.setImageDrawable(null);
            ivZhichu.setAnimation(AnimationUtils.loadAnimation(this, R.anim.jz_top_right2left));
            DongHua3d.yanChiShow(zhichull, shourull);
            break;
        case R.id.jz_main_shouru_fl:// 顶部收入按钮
            ivShuru.setImageResource(R.drawable.jz_tab1_bt_bgs);
            ivZhichu.setImageDrawable(null);
            ivShuru.setAnimation(AnimationUtils.loadAnimation(this, R.anim.jz_top_left2right));
            DongHua3d.yanChiShow(shourull, zhichull);
            break;
        case R.id.jz_mian_menuiv:// 底部环形按钮
            if (!isShown) {
                btDonghua();
                menu.setImageResource(R.drawable.jz_main_more_s);
            } else {
                btHiddenDonghua();
                menu.setImageResource(R.drawable.jz_main_more);
            }
            break;
        case R.id.jz_main_bt_add:// 记账按钮
            changeActivity(JZAddActivity.class);
            break;
        case R.id.jz_main_bt_mingxi:// 明细
            changeActivity(JZMingXiActivity.class);
            break;
        case R.id.jz_main_bt_yusuan:// 预算
            changeActivity(JZYuSuanActivity.class);
            break;
        case R.id.jz_main_bt_baobiao://报表
        	changeActivity(JZBaoBiaoActivity.class);
            break;
        case R.id.jz_main_bt_setting://设置
        	changeActivity(JZSheZhiActivity.class);
            break;
        }
    }

    /*
     * 切换界面
     */
    public void changeActivity(Class<?> c) {
        Intent intent = new Intent(JZMainActivity.this, c);
        startActivity(intent);
    }

    public void showMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public boolean onKeyDown(int kCode, KeyEvent kEvent) {
        switch (kCode) {
        case KeyEvent.KEYCODE_BACK:
            if (isShown) {
                btHiddenDonghua();
                menu.setImageResource(R.drawable.jz_main_more);
                return false;
            } else {
            	exitDialog();
            }
            break;
        case KeyEvent.KEYCODE_MENU:
            if (isShown) {
                btHiddenDonghua();
                menu.setImageResource(R.drawable.jz_main_more);
            } else {
                btDonghua();
                menu.setImageResource(R.drawable.jz_main_more_s);
            }
            break;
        }
        return super.onKeyDown(kCode, kEvent);
    }
    
    /*
     * 退出弹出框
     * */
    public void exitDialog(){
    	final AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle("是否确认退出？");
    	builder.setPositiveButton("退出小助手", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				System.exit(0);
			}
		});
    	builder.setNeutralButton("退出记账器", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				 Intent intent = new Intent(JZMainActivity.this,ToolsMainActivity.class);
                 JZMainActivity.this.startActivity(intent);
				 JZMainActivity.this.finish();
			}
		});
    	builder.create().show();
    }
    /*
     * 获取支出 页面总数
     */
    float count_sr_yue;
    public void getZhiChu() {
    	count_sr_yue = 0;
    	float count_zc_week = 0,count_zc_yue = 0;
        String selectionWeek = JZzhichu.ZC_WEEK + "=" + GetTime.getWeekOfYear();
        List<JZzhichu> zhichuWeekList = dataHelper.GetZhiChuList(selectionWeek);
        if (zhichuWeekList != null) {
            for (JZzhichu zhichu : zhichuWeekList) {
            	count_zc_week += zhichu.getZc_Count();
            }
            zhichu_week.setText(count_zc_week + "");
        } else {
            zhichu_week.setText(0 + "");
        }

        String selectionMonth = JZzhichu.ZC_YEAR + "=" + GetTime.getYear() + " and " + JZzhichu.ZC_MONTH + "=" + GetTime.getMonth();
        List<JZzhichu> zhichuMonthList = dataHelper.GetZhiChuList(selectionMonth);
        if (zhichuMonthList != null) {
            for (JZzhichu zhichu : zhichuMonthList) {
            	count_zc_yue += zhichu.getZc_Count();
            }
            zhichu_month.setText(count_zc_yue + "");
        } else {
            zhichu_month.setText(0 + "");
        }

        String selectionShouRuMonth = JZshouru.SR_YEAR + "=" + GetTime.getYear() + " and " + JZshouru.SR_MONTH + "=" + GetTime.getMonth();
        List<JZshouru> shouruMonthList = dataHelper.GetShouRuList(selectionShouRuMonth);
        if (shouruMonthList != null) {
            for (JZshouru shouru : shouruMonthList) {
            	count_sr_yue += shouru.getSr_Count();
            }
            zhichu_shouru_month.setText(count_sr_yue + "");
            shouru_month.setText(count_sr_yue + "");
        } else {
            zhichu_shouru_month.setText(0 + "");
            shouru_month.setText(0 + "");
        }
        //判断当前状态确定是否绘图
        if(count_zc_week>0||count_zc_yue>0||count_sr_yue>0){
        	// 创建绘图区域
        	zhichu_shang_rl.setBackgroundDrawable(null);
            zhichu_shang_rl.addView(new JZPaintViewZandS(this,Color.BLUE,30,count_zc_week/20,"本周支出"));
            zhichu_shang_rl.addView(new JZPaintViewZandS(this,Color.BLACK,100,count_zc_yue/20,"本月支出"));
            zhichu_shang_rl.addView(new JZPaintViewZandS(this,Color.CYAN,170,count_sr_yue/20,"本月收入"));
        }else{
        	zhichu_shang_rl.setBackgroundResource(R.drawable.jz_empty_zhichu_zhuxing);
        }

        final float yusuan_yue = JZSqliteHelper.readPreferenceFile(this, JZSqliteHelper.YUSUAN_MONTH, JZSqliteHelper.YUSUAN_MONTH);
        // 月预算
        yusuan_month.setText(yusuan_yue + "");
        // 月预算余额
        final float zhichu_yue = Float.parseFloat(zhichu_month.getText().toString().trim());
        yusuanyue_month.setText((yusuan_yue - zhichu_yue) + "");
        
        int bujin=0;//根据不同的剩余调整步进速度
        if((yusuan_yue/zhichu_yue)<0.3){
        	bujin = 1;
        }else if((yusuan_yue/zhichu_yue)>=0.3&&(yusuan_yue/zhichu_yue)<=0.6){
        	bujin = 3;
        }else {
        	bujin = 6;
        }
        if(yusuan_yue>0||zhichu_yue>0){
        	zhichu_xia_rl.setBackgroundDrawable(null);
	        zhichu_xia_rl.addView(new JZPaintViewYuE(this,0,0,Color.BLUE,50));
	        zhichu_xia_rl.addView(new JZPaintViewYuE(JZMainActivity.this,yusuan_yue,zhichu_yue,Color.RED,bujin));	
        }else{
        	zhichu_xia_rl.setBackgroundResource(R.drawable.jz_empty_yusuan);
        }
    }
    /*
     * 获取收入页面各个金额总数
     */
    public void getShouRu() {
    	float count_sr_year = 0,count_sr_day = 0;
        String selectionShouRuYear = JZshouru.SR_YEAR + "=" + GetTime.getYear();
        List<JZshouru> shouruYearList = dataHelper.GetShouRuList(selectionShouRuYear);
        if (shouruYearList != null) {
        	count_sr_year = 0;
            for (JZshouru shouru : shouruYearList) {
            	count_sr_year += shouru.getSr_Count();
            }
            shouru_year.setText(count_sr_year + "");
        } else {
            shouru_year.setText(0 + "");
        }

        String selectionShouRuDay = JZshouru.SR_YEAR + "=" + GetTime.getYear() + " and " +JZshouru.SR_MONTH + "=" + GetTime.getMonth()+" and "+ JZshouru.SR_DAY + "=" + GetTime.getDay();
        List<JZshouru> shouruDayList = dataHelper.GetShouRuList(selectionShouRuDay);
        if (shouruDayList != null) {
        	count_sr_day = 0;
            for (JZshouru shouru : shouruDayList) {
            	count_sr_day += shouru.getSr_Count();
            }
            shouru_day.setText(count_sr_day + "");
        } else {
            shouru_day.setText(0 + "");
        }
        //判断当前状态确定是否绘图
        if(count_sr_year>0||count_sr_yue>0||count_sr_day>0){
        	// 创建绘图区域
        	shouru_shang_rl.setBackgroundDrawable(null);
        	shouru_shang_rl.addView(new JZPaintViewZandS(this,Color.BLUE,30,count_sr_year/40,"本年收入"));
        	shouru_shang_rl.addView(new JZPaintViewZandS(this,Color.BLACK,100,count_sr_yue/40,"本月收入"));
        	shouru_shang_rl.addView(new JZPaintViewZandS(this,Color.CYAN,170,count_sr_day/40,"今天收入"));
        }else{
        	shouru_shang_rl.setBackgroundResource(R.drawable.jz_empty_zhichu_zhuxing);
        }
    }
}
