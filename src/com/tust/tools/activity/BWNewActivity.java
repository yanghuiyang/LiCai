package com.tust.tools.activity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tust.tools.R;
import com.tust.tools.bean.BWcontent;
import com.tust.tools.db.BWData;
import com.tust.tools.service.BWAdapter;
import com.tust.tools.service.ChangePic;
import com.tust.tools.service.DongHuaYanChi;
import com.tust.tools.service.GetTime;
import com.tust.tools.service.SDrw;

public class BWNewActivity extends Activity implements OnClickListener {
	//文本编辑框
	private EditText et;
	//更换颜色和字体LinerLayout
	LinearLayout color_ll,size_ll;
	//顶部日期
	private TextView time;
	private ImageButton picbt,colorbt;
	private BWData dataHelper;
	//是否为更新
	private Boolean isUpdate;
	private BWcontent beiwang;
	private String updateString;
	//字体大小
	private float textSize=24;
	private float updateTextSize=0;
	private float sizeFloats[] = new float[]{20,24,29,34}; 
	//背景颜色
	private int bgColorId=R.drawable.bw_new_et_bg_1;
	private int updateBgColorId=R.drawable.bw_new_et_bg_1;
	private int ids[] = new int[]{R.drawable.bw_new_et_bg_1,R.drawable.bw_new_et_bg_2,R.drawable.bw_new_et_bg_3,R.drawable.bw_new_et_bg_4,R.drawable.bw_new_et_bg_5}; 
    private Handler handler;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bw_new);
        time = (TextView)this.findViewById(R.id.bw_new_time_text);
        et = (EditText)this.findViewById(R.id.bw_et);
        et.setOnClickListener(this);
        et.setTextSize(sizeFloats[1]);
        picbt = (ImageButton)this.findViewById(R.id.bw_pic_bt);
        picbt.setOnClickListener(this);
        colorbt = (ImageButton)this.findViewById(R.id.bw_new_color_ib);
        colorbt.setOnClickListener(this);
        isUpdate = false;
        beiwang = new BWcontent();
        handler = new Handler();
        dataHelper= new BWData(this);
        initUpdate();
        initBGColor();
        initTextSize();
    }
    
    /*
     * 初始化背景颜色
     * */
    public void initBGColor(){
    	 color_ll = (LinearLayout)this.findViewById(R.id.bw_new_bgcolor_ll);
         color_ll.setVisibility(View.GONE);
         RelativeLayout color1 = (RelativeLayout)this.findViewById(R.id.bw_new_bgcolor_1_rl);
         RelativeLayout color2 = (RelativeLayout)this.findViewById(R.id.bw_new_bgcolor_2_rl);
         RelativeLayout color3 = (RelativeLayout)this.findViewById(R.id.bw_new_bgcolor_3_rl);
         RelativeLayout color4 = (RelativeLayout)this.findViewById(R.id.bw_new_bgcolor_4_rl);
         RelativeLayout color5 = (RelativeLayout)this.findViewById(R.id.bw_new_bgcolor_5_rl);
         RelativeLayout colors[] = new RelativeLayout[]{color1,color2,color3,color4,color5};
         for(RelativeLayout color :colors){
        	 color.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					switch (v.getId()) {
					case R.id.bw_new_bgcolor_1_rl:
					    bgColorId = ids[0];
						break;
					case R.id.bw_new_bgcolor_2_rl:
					    bgColorId = ids[1];
						break;
					case R.id.bw_new_bgcolor_3_rl:
					    bgColorId = ids[2];
						break;
					case R.id.bw_new_bgcolor_4_rl:
					    bgColorId = ids[3];
						break;
					case R.id.bw_new_bgcolor_5_rl:
					    bgColorId = ids[4];
						break;
					}
					et.setBackgroundResource(bgColorId);
					DongHuaYanChi.dongHuaEnd(color_ll, BWNewActivity.this, handler, R.anim.picpush_right_out, 300);
				}
			});
         }
    }
    
    /*
     * 初始化文本大小
     * */
    public void initTextSize(){
    	size_ll = (LinearLayout)this.findViewById(R.id.bw_new_textsize_ll);
        size_ll.setVisibility(View.GONE);
        RelativeLayout size1 = (RelativeLayout)this.findViewById(R.id.bw_new_textsize_1_rl);
        RelativeLayout size2 = (RelativeLayout)this.findViewById(R.id.bw_new_textsize_2_rl);
        RelativeLayout size3 = (RelativeLayout)this.findViewById(R.id.bw_new_textsize_3_rl);
        RelativeLayout size4 = (RelativeLayout)this.findViewById(R.id.bw_new_textsize_4_rl);
        RelativeLayout sizes[] = new RelativeLayout[]{size1,size2,size3,size4};
        for(RelativeLayout size :sizes){
       	size.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						switch (v.getId()) {
						case R.id.bw_new_textsize_1_rl:
							textSize = sizeFloats[0];
							break;
						case R.id.bw_new_textsize_2_rl:
							textSize = sizeFloats[1];
							break;
						case R.id.bw_new_textsize_3_rl:
							textSize = sizeFloats[2];
							break;
						case R.id.bw_new_textsize_4_rl:
							textSize = sizeFloats[3];
							break;
						}
					et.setTextSize(textSize);
					DongHuaYanChi.dongHuaEnd(size_ll, BWNewActivity.this, handler, R.anim.jz_menu_down, 300);
				}
			});
        }
    }
    
    /*
     * 初始化更新信息
     * */
    public void initUpdate(){
    	Intent intent = this.getIntent();
    	if(intent.hasExtra("id")){
    		int id = intent.getIntExtra("id", 0);
    		for(BWcontent beiwang : BWAdapter.bWlist){
    			if(beiwang.getId() == id){
    				time.setText(beiwang.getYear()+"年"+beiwang.getMonth()+"月"+beiwang.getDay()+"日"+" "+beiwang.getTime());
    				isUpdate = true;
    				updateString = beiwang.getContent();
    				et.setText(updateString);
    				Editable ea = et.getText();//设置光标在文字末尾
    				Selection.setSelection((Spannable) ea, ea.length());
    				//初始化文字的大小
    				textSize = beiwang.getSize();
    				updateTextSize =textSize;
    				if(textSize==0){
    					textSize = 24;
    					updateTextSize = 24;
    				}
    				et.setTextSize(textSize);
    				//初始化图片 存在图片就设置图片
    				if(beiwang.getPic()!=null&&!beiwang.getPic().equals("")&&!SDrw.getSDPath().equals("")){
    				    picpath = beiwang.getPic();
    				    File picFile = new File(picpath);
    				    picbt.setImageBitmap(decodeFile(picFile));
    				}else{
    				    beiwang.setPic("");//防止修改时空指针异常
    				}
    				
    				//初始化背景颜色
    				bgColorId=beiwang.getColor();
    				updateBgColorId = bgColorId;
    				et.setBackgroundResource(beiwang.getColor());
    				this.beiwang = beiwang;
    				return;
    			}
    		}
    	}else{
    		time.setText(GetTime.getYear()+"年"+GetTime.getMonth()+"月"+GetTime.getDay()+"日"+" "+GetTime.getTime());	
    	}
    }

    @Override
    protected void onResume() {
        overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
        super.onResume();
    }
    
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
        case R.id.bw_pic_bt:
            if(isUpdate){
                if(beiwang.getPic()!=null&&new File(beiwang.getPic()).exists()){
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Uri uri = Uri.parse("file://"+beiwang.getPic());
                    intent.setDataAndType(uri, "image/*");
                    startActivity(intent);
                }else{
                    choosePic(this);  
                }
            }else{
                choosePic(this);   
            }
        	break;
        case R.id.bw_new_color_ib:
            if(color_ll.isShown()){
                DongHuaYanChi.dongHuaEnd(color_ll, BWNewActivity.this, handler, R.anim.picpush_right_out, 300);
            }else{
                color_ll.setVisibility(View.VISIBLE);
                color_ll.setAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in));
            }
        	break;
        case R.id.bw_et:
            if(color_ll.isShown()){
                DongHuaYanChi.dongHuaEnd(color_ll, BWNewActivity.this, handler, R.anim.picpush_right_out, 300);
            }
            if(size_ll.isShown()){
                DongHuaYanChi.dongHuaEnd(size_ll, BWNewActivity.this, handler, R.anim.jz_menu_down, 300);
            }
            break;
        }
    }
    /*
	 * 存储支出收入借贷到数据库
	 * */
	public void saveToDB(){
		BWcontent beiwang = new BWcontent();
		int year = GetTime.getYear();
		int month = GetTime.getMonth();
		int week = GetTime.getWeek();
		int day = GetTime.getDay();
		String time = GetTime.getTime();
		//内容
		String content=et.getText().toString().trim();
		int color = bgColorId;
		if(content.equals(null)||content.equals("")){
			showMsg("输入不能为空");
			return;
		}
		beiwang.setYear(year);
		beiwang.setMonth(month);
		beiwang.setWeek(week);
		beiwang.setDay(day);
		beiwang.setTime(time);
		beiwang.setContent(content);
		beiwang.setColor(color);
		beiwang.setSize(textSize);
		if(!isUpdate){//判断当前是新建还是修改
		    if(!"".equals(picpath)&&!picpath.equals(null)){
		        beiwang.setPic(picpath);
		    }else{
		        beiwang.setPic(""); 
		    }
		    System.out.println("beiwang.getPic()"+beiwang.getPic());
			dataHelper.SaveBWInfo(beiwang);
			showMsg("存储成功");
		}else{
			if(!updateString.equals(content)||
			        updateBgColorId!=bgColorId||
			        updateTextSize!=textSize||
			        !picpath.equals(this.beiwang.getPic())){
			    if(!picpath.equals(this.beiwang.getPic())){
			        beiwang.setPic(picpath);
			    }
				dataHelper.UpdateBWInfo(beiwang, this.beiwang.getId());
				showMsg("修改成功");
			}
		}
		this.finish();
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
	    menu.add(0, 100, 0, "字体大小");
	    menu.add(0, 200, 0, "删除该条");
	    menu.add(0, 300, 0, "导出文本");
	    menu.add(0, 400, 0, "返回列表");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case 100:
            if(size_ll.isShown()){
                DongHuaYanChi.dongHuaEnd(size_ll, BWNewActivity.this, handler, R.anim.jz_menu_down, 300);
            }else{
                size_ll.setVisibility(View.VISIBLE);
                size_ll.setAnimation(AnimationUtils.loadAnimation(this, R.anim.jz_menu_up));
            }
            break;
        case 200:
            showDialog("del");
            break;
        case 300:
            if(et.getText().toString().trim().length()<1){
                showMsg("导出内容不能为空");
                return false;
            }
            showDialog("out");
            break;
        case 400:
            this.finish();
            break;
        }
        return super.onOptionsItemSelected(item);
    }
    
    /*
     * 删除弹出框
     * */
    String fileName;//以当前时间命名的文件名
    public void showDialog(final String flag){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(flag.equals("del")){//如果标识为删除
            builder.setTitle("是否确认删除该条记录？");
        }else if(flag.equals("out")){
            fileName = getFileName("txt");
            builder.setMessage("确定后导出的文件名为"+fileName+"存放在SD/tust/tools/beiwang/txt/文件夹下");
            //builder.setTitle("确定后导出的文件名为"+fileName+"存放在SD/tust/tools/beiwang/txt/文件夹下");  
        }
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               if(flag.equals("del")){
                    if(isUpdate){
                        dataHelper.DelBWInfo(beiwang.getId()); 
                    }else{
                        et.setText("");
                    }
                    BWNewActivity.this.finish();
               }else if(flag.equals("out")){
                   String line1 = "---"+time.getText().toString()+"\r\n";
                   String line2 = "---"+et.getText().toString();
                   String dir ="beiwang/txt/"; 
                   SDrw ic = new SDrw(fileName, false,dir);
                   ic.outWrite(line1+line2);
                   showMsg("导出成功");
               }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create().show();
    }

    public boolean onKeyDown(int kCode, KeyEvent kEvent) {
		switch (kCode) {
		case KeyEvent.KEYCODE_BACK: {
		    if(color_ll.isShown()||size_ll.isShown()){
		    	//当选择颜色和选择字体大小界面显示时按下返回键先关闭显示的界面
		        if(color_ll.isShown()){
                DongHuaYanChi.dongHuaEnd(color_ll, BWNewActivity.this, handler, R.anim.picpush_right_out, 300);
		        }
		        if(size_ll.isShown()){
                DongHuaYanChi.dongHuaEnd(size_ll, BWNewActivity.this, handler, R.anim.jz_menu_down, 300);
		        }
                return false;
            }
		    //如果文本的内容长度大于一则存储到数据库中
			if (!"".equals(et.getText().toString().trim())||!"".equals(picpath)) {
				saveToDB();
				this.finish();
				return false;
			} else {
					if(isUpdate){//如果当前状态为更新且为空则删除该条
						dataHelper.DelBWInfo(beiwang.getId());
				}
				this.finish();
			}
		}
		}
		return super.onKeyDown(kCode, kEvent);
	}

    public void showMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    
    private String picpath="";// 文件路径
    private static final int PHOTO_WITH_CAMERA = 1010;// 拍摄照片
    private static final int PHOTO_WITH_DATA = 1020;// 从SD中得到照片
    private File PHOTO_DIR;// 拍摄照片存储的文件夹路径
    private File capturefile;// 拍摄的照片文件
    public void choosePic(Context context) {// 照片选择
        final Context dialogContext = new ContextThemeWrapper(context, android.R.style.Theme_Light);
        PHOTO_DIR = new File(SDrw.SDPATH+ "beiwang/imgcache/");
        if(!PHOTO_DIR.exists()){
            PHOTO_DIR.mkdirs();
        }
        //生成当前目录图片不可见的标志文件
        File noMideaFile = new File(PHOTO_DIR,".nomedia");
        if(!noMideaFile.exists()){
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
                        capturefile = new File(PHOTO_DIR, getFileName("jpg"));
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
    public String getFileName(String houzhui) {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(date) + "."+houzhui;
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
                picbt.setImageBitmap(picb);
                System.out.println("++++++相机+++++");
                break;

            case PHOTO_WITH_DATA:// 获取从图库选择的文件
                Uri uri = data.getData();
                String scheme = uri.getScheme();
                if (scheme.equalsIgnoreCase("file")) {
                    picpath = uri.getPath();//从uri中获得路径
                    file = new File(picpath);
                    picb = decodeFile(file);//转换图片大小
                    picbt.setImageBitmap(picb);
                } else if (scheme.equalsIgnoreCase("content")) {
                    Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                    cursor.moveToFirst();
                    picpath = cursor.getString(1);
                    file = new File(picpath);
                    picb = decodeFile(file);
                    picbt.setImageBitmap(picb);
                }
                break;
            }
            //存放照片的路径
            String savePath =SDrw.SDPATH+"beiwang/imgcache/";
            picpath = cp.changePic(picpath,savePath);
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
            if (width > 60 || height > 60) {//压缩到指定大小
                double bi = ((double) height / (double) width);
                b = Bitmap.createScaledBitmap(bb, 60, (int) (bi * 60), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }
}