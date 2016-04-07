package com.tust.tools.activity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.tust.tools.R;
import com.tust.tools.bean.JZItem;
import com.tust.tools.bean.JZshouru;
import com.tust.tools.bean.JZzhichu;
import com.tust.tools.db.JZData;
import com.tust.tools.dialog.DialogBeiZhu;
import com.tust.tools.dialog.DialogLeiBie;
import com.tust.tools.service.ChangePic;
import com.tust.tools.service.DongHuaYanChi;
import com.tust.tools.service.GetTime;
import com.tust.tools.service.JZMingXiAdapter;
import com.tust.tools.service.SDrw;

public class JZAddActivity extends Activity implements OnClickListener {
    // TextView金额，类别，时间，备注。
    private TextView jine, leibie, date, time, beizhu;
    // FrameLayout支出，收入，借贷，保存，取消。
    private FrameLayout zhichu_fl, shouru_fl, jiedai_fl, save_fl, cancel_fl, del_fl;
    // LinearLayout图片，底部数字按钮
    private LinearLayout pic_ll, num_ll;
    // 顶部选中标识ImageView支出，收入，借贷，图片
    private ImageView zhichu_iv, shouru_iv, jiedai_iv, pic;
    // 底部数字按钮
    private Button b1, b2, b3, b4, b5, b6, b7, b8, b9, b0, bd, bdel;
    // 按钮集合
    private Button bt[] = new Button[] { b0, b1, b2, b3, b4, b5, b6, b7, b8, b9, bd, bdel };
    public static MessageHandler mh;
    // 当前界面收到的消息表示（msg.what）
    public static final int leibie_msg = 1010, beizhu_msg = 1020;
    // 当前选择的添加类别支出，收入，借贷
    public static final int zhichu_flag = 2010, shouru_flag = 2020, jiedai_flag = 2030;
    // 当前选择的类型
    private int now_flag = zhichu_flag;
    // 数据库操作
    JZData dataHelper;
    // 更改的类型（支处 收入 借贷）
    private int update_type, update_id, update_flag;
    // 判断当前是初始创建还是二次更新
    private boolean isUpdate = false;
    // 顶部支出 收入 借贷 文本，修改时需要改动文本内容
    private TextView zhichu_text, shouru_text, jiedai_text;

    private String picpath = "";// 文件路径
    private static final int PHOTO_WITH_CAMERA = 1010;// 拍摄照片
    private static final int PHOTO_WITH_DATA = 1020;// 从SD中得到照片
    private File PHOTO_DIR;// 拍摄照片存储的文件夹路径
    private File capturefile;// 拍摄的照片文件

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jz_add);
        mh = new MessageHandler();
        initZhiChu();
        initBt(jine);
        initUpdate();
    }

    /*
     * 如果是以修改的方式打开该界面
     */
    JZzhichu zc = new JZzhichu();
    JZshouru sr = new JZshouru();

    private void initUpdate() {
        Intent intent = this.getIntent();
        if (intent.hasExtra("update")) {
            zhichu_text = (TextView) this.findViewById(R.id.jz_add_zhichu_text);
            shouru_text = (TextView) this.findViewById(R.id.jz_add_shouru_text);
            jiedai_text = (TextView) this.findViewById(R.id.jz_add_jiedai_text);
            del_fl.setVisibility(View.VISIBLE);
            isUpdate = true;
            update_type = intent.getIntExtra("type", 0);
            update_id = intent.getIntExtra("id", 0);
            ArrayList<?> mingXiList = JZMingXiAdapter.mingXiList;
            if (update_type == zhichu_flag) {
                for (Object zhichu : mingXiList) {
                    JZzhichu zc = (JZzhichu) zhichu;
                    if (update_id == zc.getZc_Id()) {
                        this.zc = zc;
                        getZhiChuType(zc);
                        return;
                    }
                }
            } else if (update_type == shouru_flag) {
                for (Object shouru : mingXiList) {
                    JZshouru sr = (JZshouru) shouru;
                    if (update_id == sr.getSr_Id()) {
                        this.sr = sr;
                        getShouRuType(sr);
                        return;
                    }
                }
            }
        }
    }

    /*
     * 判断该支出类型是否为借贷
     */
    public void getZhiChuType(JZzhichu zc) {
        pic_ll.setVisibility(View.GONE);
        if (zc.getZc_Item().equals(JZItem.jiechu) || zc.getZc_Item().equals(JZItem.huankuan)) {
            now_flag = jiedai_flag;
            jiedai_text.setText("修改借贷");
            leibie.setText(zc.getZc_Item());
            setTopBG(now_flag, jiedai_iv);
            zhichu_fl.setVisibility(View.INVISIBLE);
            shouru_fl.setVisibility(View.INVISIBLE);
        } else {
            now_flag = zhichu_flag;
            zhichu_text.setText("修改支出");
            leibie.setText(zc.getZc_Item() + ">" + zc.getZc_SubItem());
            setTopBG(now_flag, zhichu_iv);
            jiedai_fl.setVisibility(View.INVISIBLE);
            shouru_fl.setVisibility(View.INVISIBLE);
            picpath = zc.getZc_Pic();// 获取图片路径
            if (picpath != null && picpath.endsWith("jpg")) {
                File filePic = new File(picpath);
                pic.setImageBitmap(decodeFile(filePic));
            }
        }
        update_flag = zhichu_flag;// 用于删除当前数据功能
        jine.setText(zc.getZc_Count() + "");
        date.setText(zc.getZc_Year() + "-" + zc.getZc_Month() + "-" + zc.getZc_Day());
        time.setText(zc.getZc_Time());
        beizhu.setText(zc.getZc_Beizhu());
    }

    /*
     * 判断该收入类型是否为借贷
     */
    public void getShouRuType(JZshouru sr) {
        pic_ll.setVisibility(View.GONE);
        if (sr.getSr_Item().equals(JZItem.jieru) || sr.getSr_Item().equals(JZItem.shoukuan)) {
            now_flag = jiedai_flag;// 用于判断当前状态
            jiedai_text.setText("修改借贷");
            setTopBG(now_flag, jiedai_iv);
            zhichu_fl.setVisibility(View.INVISIBLE);
            shouru_fl.setVisibility(View.INVISIBLE);
        } else {
            now_flag = shouru_flag;
            shouru_text.setText("修改收入");
            setTopBG(now_flag, shouru_iv);
            jiedai_fl.setVisibility(View.INVISIBLE);
            zhichu_fl.setVisibility(View.INVISIBLE);
        }
        update_flag = shouru_flag;// 用于删除当前数据功能
        jine.setText(sr.getSr_Count() + "");
        leibie.setText(sr.getSr_Item());
        date.setText(sr.getSr_Year() + "-" + sr.getSr_Month() + "-" + sr.getSr_Day());
        time.setText(sr.getSr_Time());
        beizhu.setText(sr.getSr_Beizhu());
    }

    private void initZhiChu() {
        // 金额数量
        jine = (TextView) findViewById(R.id.jz_add_jine_text);
        jine.setOnClickListener(new TextClick());
        // 类别
        leibie = (TextView) findViewById(R.id.jz_add_leibie_text);
        leibie.setOnClickListener(new TextClick());
        // 日期
        date = (TextView) findViewById(R.id.jz_add_date_text);
        date.setText(GetTime.getYear() + "-" + GetTime.getMonth() + "-" + GetTime.getDay());
        date.setOnClickListener(new TextClick());
        // 时间
        time = (TextView) findViewById(R.id.jz_add_time_text);
        time.setText(GetTime.getHour() + ":" + GetTime.getMinute());
        time.setOnClickListener(new TextClick());
        // 备注
        beizhu = (TextView) findViewById(R.id.jz_add_beizhu_text);
        beizhu.setOnClickListener(new TextClick());
        // 图片
        pic = (ImageView) findViewById(R.id.jz_add_zhichu_addpic_iv);
        pic.setOnClickListener(new TextClick());
        // pic linerlayout当选择收入或借贷时隐藏该选项
        pic_ll = (LinearLayout) findViewById(R.id.jz_add_pic_ll);
        // 底部数字按钮
        num_ll = (LinearLayout) findViewById(R.id.jz_add_numbt_ll);
        num_ll.setVisibility(View.GONE);
        DongHuaYanChi.dongHuaStart(num_ll, this, mh, R.anim.jz_menu_up, 400);

        zhichu_iv = (ImageView) findViewById(R.id.jz_add_zhichu_iv);
        shouru_iv = (ImageView) findViewById(R.id.jz_add_shouru_iv);
        jiedai_iv = (ImageView) findViewById(R.id.jz_add_jiedai_iv);

        zhichu_fl = (FrameLayout) findViewById(R.id.jz_add_zhichu_fl);
        zhichu_fl.setOnClickListener(this);
        shouru_fl = (FrameLayout) findViewById(R.id.jz_add_shouru_fl);
        shouru_fl.setOnClickListener(this);
        jiedai_fl = (FrameLayout) findViewById(R.id.jz_add_jiedai_fl);
        jiedai_fl.setOnClickListener(this);
        save_fl = (FrameLayout) findViewById(R.id.jz_add_save_fl);
        save_fl.setOnClickListener(this);
        cancel_fl = (FrameLayout) findViewById(R.id.jz_add_cancel_fl);
        cancel_fl.setOnClickListener(this);
        // 删除当前要修改的数据，只在修改时有效
        del_fl = (FrameLayout) this.findViewById(R.id.jz_add_del_fl);
        del_fl.setOnClickListener(this);
        del_fl.setVisibility(View.INVISIBLE);
    }

    /*
     * 初始化数字按钮
     */
    private void initBt(TextView tv) {
        int id[] = new int[] { R.id.jz_add_bt_0, R.id.jz_add_bt_1, R.id.jz_add_bt_2, R.id.jz_add_bt_3, R.id.jz_add_bt_4, R.id.jz_add_bt_5, R.id.jz_add_bt_6, R.id.jz_add_bt_7, R.id.jz_add_bt_8, R.id.jz_add_bt_9, R.id.jz_add_bt_d, R.id.jz_add_bt_del };
        for (int i = 0; i < bt.length; i++) {
            bt[i] = (Button) this.findViewById(id[i]);
            bt[i].setOnClickListener(new MyClick(tv));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.jz_add_zhichu_fl:// 支出tab
            setTopBG(zhichu_flag, zhichu_iv);
            leibie.setText("餐饮>晚餐");
            break;
        case R.id.jz_add_shouru_fl:// 收入tab
            setTopBG(shouru_flag, shouru_iv);
            leibie.setText("工资");
            break;
        case R.id.jz_add_jiedai_fl:// 借贷tab
            setTopBG(jiedai_flag, jiedai_iv);
            leibie.setText("借出");
            break;
        case R.id.jz_add_save_fl:// 保存按钮
            saveToDB();
            break;
        case R.id.jz_add_cancel_fl:// 取消按钮
            this.finish();
            break;
        case R.id.jz_add_del_fl:// 取消按钮
            dataHelper = new JZData(this);
            switch (update_flag) {
            case zhichu_flag:
                int i1 = dataHelper.DelZhiChuInfo(update_id);
                if (i1 > 0) {
                    showMsg("删除成功");
                    this.finish();
                } else {
                    showMsg("删除失败");
                }
                break;
            case shouru_flag:
                int i2 = dataHelper.DelShouRuInfo(update_id);
                if (i2 > 0) {
                    showMsg("删除成功");
                    this.finish();
                } else {
                    showMsg("删除失败");
                }
                break;
            }
            break;
        }
    }

    /*
     * 存储支出收入借贷到数据库
     */
    public void saveToDB() {
        dataHelper = new JZData(this);
        JZzhichu zhichu = new JZzhichu();
        JZshouru shouru = new JZshouru();
        // 类别
        String leibies = leibie.getText().toString().trim();
        String items[] = leibies.split(">");
        // 日期
        String dateString = date.getText().toString().trim();
        String dates[] = dateString.split("-");
        // 时间
        String timeString = time.getText().toString().trim();
        // 金额
        String jineString = jine.getText().toString().trim();
        // 备注
        String beizhuString = beizhu.getText().toString().trim();
        if (jineString.equals("0.00")) {
            showMsg("金额不能为零");
            return;
        }
        if (now_flag == zhichu_flag) {
            zhichu.setZc_Item(items[0]);
            zhichu.setZc_SubItem(items[1]);
            zhichu.setZc_Year(Integer.parseInt(dates[0]));
            zhichu.setZc_Month(Integer.parseInt(dates[1]));
            zhichu.setZc_Day(Integer.parseInt(dates[2]));
            zhichu.setZc_Time(timeString);
            zhichu.setZc_Week(GetTime.getTheWeekOfYear(Integer.parseInt(dates[0]), Integer.parseInt(dates[1]), Integer.parseInt(dates[2])));
            zhichu.setZc_Pic(picpath);
            zhichu.setZc_Count(Double.parseDouble(jineString));
            zhichu.setZc_Beizhu(beizhuString);
            if (!isUpdate) {
                dataHelper.SaveZhiChuInfo(zhichu);
                showMsg("该条支出存储成功");
                picpath = "";
            } else {
                dataHelper.UpdateZhiChuInfo(zhichu, zc.getZc_Id());
                showMsg("该条支出修改成功");
            }
        } else if (now_flag == shouru_flag) {
            shouru.setSr_Item(leibies);
            shouru.setSr_Year(Integer.parseInt(dates[0]));
            shouru.setSr_Month(Integer.parseInt(dates[1]));
            shouru.setSr_Day(Integer.parseInt(dates[2]));
            shouru.setSr_Time(timeString);
            shouru.setSr_Week(GetTime.getTheWeekOfYear(Integer.parseInt(dates[0]), Integer.parseInt(dates[1]), Integer.parseInt(dates[2])));
            shouru.setSr_Count(Double.parseDouble(jineString));
            shouru.setSr_Beizhu(beizhuString);
            if (!isUpdate) {
                dataHelper.SaveShouRuInfo(shouru);
                showMsg("该条收入存储成功");
            } else {
                dataHelper.UpdateShouRuInfo(shouru, sr.getSr_Id());
                showMsg("该条收入修改成功");
            }
        } else if (now_flag == jiedai_flag) {// 借贷中包含借入和借出 分别存储到支出和收入中
            if (leibies.equals(JZItem.jiechu) || leibies.equals(JZItem.huankuan)) {
                zhichu.setZc_Item(leibies);
                zhichu.setZc_SubItem("");
                zhichu.setZc_Year(Integer.parseInt(dates[0]));
                zhichu.setZc_Month(Integer.parseInt(dates[1]));
                zhichu.setZc_Day(Integer.parseInt(dates[2]));
                zhichu.setZc_Time(timeString);
                zhichu.setZc_Week(GetTime.getTheWeekOfYear(Integer.parseInt(dates[0]), Integer.parseInt(dates[1]), Integer.parseInt(dates[2])));
                zhichu.setZc_Pic("");
                zhichu.setZc_Count(Double.parseDouble(jineString));
                zhichu.setZc_Beizhu(beizhuString);
                if (!isUpdate) {
                    dataHelper.SaveZhiChuInfo(zhichu);
                    showMsg("该条支出存储成功");
                } else {
                    dataHelper.UpdateZhiChuInfo(zhichu, zc.getZc_Id());
                    showMsg("该条支出修改成功");
                }
            } else if (leibies.equals(JZItem.jieru) || leibies.equals(JZItem.shoukuan)) {
                shouru.setSr_Item(leibies);
                shouru.setSr_Year(Integer.parseInt(dates[0]));
                shouru.setSr_Month(Integer.parseInt(dates[1]));
                shouru.setSr_Day(Integer.parseInt(dates[2]));
                shouru.setSr_Time(timeString);
                shouru.setSr_Week(GetTime.getTheWeekOfYear(Integer.parseInt(dates[0]), Integer.parseInt(dates[1]), Integer.parseInt(dates[2])));
                shouru.setSr_Count(Double.parseDouble(jineString));
                shouru.setSr_Beizhu(beizhuString);
                if (!isUpdate) {
                    dataHelper.SaveShouRuInfo(shouru);
                    showMsg("该条借贷存储成功");
                } else {
                    dataHelper.UpdateShouRuInfo(shouru, sr.getSr_Id());
                    showMsg("该条借贷修改成功");
                }
            }
        }
        this.finish();
    }

    /*
     * 设置顶部切换按钮的背景及动画
     */
    private void setTopBG(int now_flag, ImageView iv) {
        this.now_flag = now_flag;// 赋值给全局变量
        if (now_flag == zhichu_flag) {
            if (!pic_ll.isShown()) {
                pic_ll.setVisibility(View.VISIBLE);
                pic_ll.setAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in));
            }
        } else if (now_flag == shouru_flag) {
            if (pic_ll.isShown()) {
                DongHuaYanChi.dongHuaEnd(pic_ll, this, mh, R.anim.push_left_out, 400);
            }
        } else if (now_flag == jiedai_flag) {
            if (pic_ll.isShown()) {
                DongHuaYanChi.dongHuaEnd(pic_ll, this, mh, R.anim.push_left_out, 400);
            }
        }
        shouru_iv.setImageDrawable(null);
        zhichu_iv.setImageDrawable(null);
        jiedai_iv.setImageDrawable(null);
        iv.setImageResource(R.drawable.jz_tab1_bt_bgs);
        iv.setAnimation(AnimationUtils.loadAnimation(this, R.anim.jz_top_right2left));
    }

    @Override
    protected void onResume() {
        overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
        super.onResume();
    }

    /*
     * 日期dialog
     */
    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == R.id.jz_add_date_text) {// 当点击按钮为R.id.button1显示该dialog
            Calendar c = Calendar.getInstance();
            OnDateSetListener osl = new OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    date.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth + " ");
                }
            };
            new DatePickerDialog(this, 0, osl, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
        } else if (id == R.id.jz_add_time_text) {
            Calendar c = Calendar.getInstance();
            OnTimeSetListener otl = new OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    time.setText(hourOfDay + ":" + minute);
                }
            };
            new TimePickerDialog(this, 0, otl, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
        }
        return null;
    }

    /*
     * 输入数字监听
     */
    private class TextClick implements OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
            case R.id.jz_add_jine_text:// 点击num显示数字按键
                if (num_ll.isShown()) {
                    DongHuaYanChi.dongHuaEnd(num_ll, JZAddActivity.this, mh, R.anim.jz_menu_down, 300);
                } else {
                    num_ll.setAnimation(AnimationUtils.loadAnimation(JZAddActivity.this, R.anim.jz_menu_up));
                    num_ll.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.jz_add_leibie_text:// 更改类别
                new DialogLeiBie(JZAddActivity.this, now_flag);
                break;
            case R.id.jz_add_date_text:// 更改日期
                onCreateDialog(R.id.jz_add_date_text);
                break;
            case R.id.jz_add_time_text:// 更改时间
                onCreateDialog(R.id.jz_add_time_text);
                break;
            case R.id.jz_add_beizhu_text:// 添加备注
                String beizhuString = beizhu.getText().toString();
                if(beizhuString.equals("无备注")){
                    new DialogBeiZhu(JZAddActivity.this,"");
                }else{
                    new DialogBeiZhu(JZAddActivity.this,beizhuString);  
                }
                break;
            case R.id.jz_add_zhichu_addpic_iv:
                if (picpath != null && picpath.endsWith("jpg")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Uri uri = Uri.parse("file://" + picpath);
                    intent.setDataAndType(uri, "image/*");
                    startActivity(intent);
                } else {
                    choosePic(JZAddActivity.this);
                }
                break;
            }
        }
    }

    /*
     * 输入数字监听
     */
    private class MyClick implements OnClickListener {
        private TextView tv;

        public MyClick(TextView tv) {
            this.tv = tv;
        }

        @Override
        public void onClick(View v) {
            Button button = (Button) v;
            String jines = tv.getText().toString().trim();
            if (v.getId() != R.id.jz_add_bt_del && jines.length() > 9) {
                showMsg("你有这么多钱吗？");
                return;
            }
            if (v.getId() != R.id.jz_add_bt_del) {
                if (jines.equals("0.00")) {// 第一次输入时
                    if (!button.getText().equals(".") && !button.getText().equals("0")) {
                        tv.setText(button.getText());
                    }
                } else {
                    if (jines.contains(".")) {// 金额中已经包含小数点
                        if (button.getText().equals(".")) {// 输入的为小数点
                            showMsg("没学过数学呀？");
                            return;
                        }
                        // 小数点后超过两位时
                        if ((jines.length() - jines.indexOf(".")) <= 2) {
                            tv.append(button.getText());
                        } else {
                            showMsg("你有那么多零钱吗？");
                        }
                    } else {
                        tv.append(button.getText());
                    }
                }
            } else {// 如果是删除键
                if (!jines.equals("0.00")) {
                    if (jines.length() > 1) {
                        String str = jines.substring(0, jines.length() - 1);
                        tv.setText(str);
                    } else {
                        tv.setText("0.00");
                    }
                }
            }
        }
    }

    public boolean onKeyDown(int kCode, KeyEvent kEvent) {
        switch (kCode) {
        case KeyEvent.KEYCODE_BACK: {
            if (num_ll.isShown()) {
                DongHuaYanChi.dongHuaEnd(num_ll, JZAddActivity.this, mh, R.anim.jz_menu_down, 300);
                return false;
            } else {
                this.finish();
            }
        }
        }
        return super.onKeyDown(kCode, kEvent);
    }

    public void showMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public class MessageHandler extends Handler {
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case leibie_msg:
                leibie.setText((String) msg.obj);
                break;
            case beizhu_msg:
                beizhu.setText((String) msg.obj);
                break;
            default:
                break;
            }
        }
    }

    public void choosePic(Context context) {// 照片选择
        final Context dialogContext = new ContextThemeWrapper(context, android.R.style.Theme_Light);
        PHOTO_DIR = new File(SDrw.SDPATH+ "jizhang/imgcache/");
        if (!PHOTO_DIR.exists()) {
            PHOTO_DIR.mkdirs();
        }
        // 生成当前目录图片不可见的标志文件
        File noMideaFile = new File(PHOTO_DIR, ".nomedia");
        if (!noMideaFile.exists()) {
            try {
                noMideaFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String[] choices;
        choices = new String[2];
        choices[0] = "相机拍摄"; // 拍照
        choices[1] = "本地相册"; // 从相册中选择
        final ListAdapter adapter = new ArrayAdapter<String>(dialogContext, android.R.layout.simple_list_item_1, choices);
        final AlertDialog.Builder builder = new AlertDialog.Builder(dialogContext);
        builder.setTitle("添加图片");
        builder.setSingleChoiceItems(adapter, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                switch (which) {
                case 0: {
                    String status = Environment.getExternalStorageState();
                    if (status.equals(Environment.MEDIA_MOUNTED)) {// 判断是否有SD卡
                        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        capturefile = new File(PHOTO_DIR, getPhotoFileName());
                        try {
                            capturefile.createNewFile();
                            i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(capturefile));// 将拍摄的照片信息存到capturefile中
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        startActivityForResult(i, PHOTO_WITH_CAMERA);// 用户点击了从照相机获取
                    } else {
                        showMsg("没有SD卡");
                    }
                    break;
                }
                case 1:// 从相册中去获取
                    Intent intent = new Intent();
                    /* 开启Pictures画面Type设定为image */
                    intent.setType("image/*");
                    /* 使用Intent.ACTION_GET_CONTENT这个Action */
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    /* 取得相片后返回本画面 */
                    startActivityForResult(intent, PHOTO_WITH_DATA);
                    break;
                }
            }
        });
        builder.create().show();
    }

    /*
     * 通过相机回传图片的文件名
     */
    public String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(date) + ".jpg";
    }

    /*
     * 选择图片的回传处理
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        File file = null;
        Bitmap picb = null;
        ChangePic cp = new ChangePic();// 转换图片类
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
            case PHOTO_WITH_CAMERA:// 获取拍摄的文件
                picpath = capturefile.getAbsolutePath();
                System.out.println(picpath);
                file = new File(picpath);
                picb = decodeFile(file);
                pic.setImageBitmap(picb);
                System.out.println("++++++相机+++++");
                break;

            case PHOTO_WITH_DATA:// 获取从图库选择的文件
                Uri uri = data.getData();
                String scheme = uri.getScheme();
                if (scheme.equalsIgnoreCase("file")) {
                    picpath = uri.getPath();
                    System.out.println(picpath);
                    file = new File(picpath);
                    picb = decodeFile(file);
                    pic.setImageBitmap(picb);
                } else if (scheme.equalsIgnoreCase("content")) {
                    Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                    cursor.moveToFirst();
                    picpath = cursor.getString(1);
                    file = new File(picpath);
                    picb = decodeFile(file);
                    pic.setImageBitmap(picb);
                }
                break;
            }
            // 存放照片的路径
            String savePath = SDrw.SDPATH+"jizhang/imgcache/";
            picpath = cp.changePic(picpath, savePath);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // 压缩图片，避免报错
    private Bitmap decodeFile(File f) {
        Bitmap b = null;
        try {
            Bitmap bb = BitmapFactory.decodeFile(f.getAbsolutePath());
            int width = bb.getWidth();
            int height = bb.getHeight();
            if (width > 100 || height > 100) {
                double bi = ((double) height / (double) width);
                b = Bitmap.createScaledBitmap(bb, 100, (int) (bi * 100), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }

}
