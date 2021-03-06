package com.tust.tools.activity;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.tust.tools.R;
import com.tust.tools.bean.ExpenditureType;
import com.tust.tools.db.ExpenditureTypeData;


public class ExpenditureTypeManageActivity extends Activity implements OnClickListener {
    private Button btn_addType,btn_delete;
    private ListView lv;
    private List<String> typenames;//存放类型列表
    private String userName;
    private ExpenditureTypeData expenditureTypeData;
    private ExpenditureType type;
    String inputStr = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expenditure_typemanage);
        btn_addType = (Button) findViewById(R.id.addExpenditureType);
        btn_addType.setOnClickListener(this);
        btn_delete = (Button) findViewById(R.id.deleteExpenditureType);
        btn_delete.setOnClickListener(this);
        lv = (ListView) findViewById(R.id.typelist);// 得到ListView对象
        SharedPreferences preferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        userName =preferences.getString("userName", "");
        expenditureTypeData = new ExpenditureTypeData(this);
        typenames=expenditureTypeData.getTypesByUserName(userName);//获取该用户所有支出类型
        /* 为ListView设置Adapter来绑定数据 */
        lv.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_checked, typenames));

    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.addExpenditureType: //添加类型
                addtype();
                break;
            case R.id.deleteExpenditureType: //删除类型
                deleteDialog();
                break;
            default:
                break;
        }

    }
    //添加类型对话框
    private void addtype() {
        final EditText inputServer = new EditText(this);
        inputServer.setFocusable(true);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("添加类型").setView(inputServer)
                    .setNegativeButton("取消", null);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    inputStr = inputServer.getText().toString();
                    //   int i = (int) ExpenditureTypeData.getCount(userName) + 1;
                    boolean flag = expenditureTypeData.haveExpenditureType(userName,inputStr);
                    if (inputStr.trim().equals("")) {
                        Toast.makeText(getApplicationContext(), "输入内容不能为空！", Toast.LENGTH_LONG).show();
                    } else if(flag){
                        Toast.makeText(getApplicationContext(), "支出类型不能重复哦！", Toast.LENGTH_LONG).show();
                    }
                        else {
                        type = new ExpenditureType();
                        type.setUserName(userName);
                        type.setTypeName(inputStr);
                        expenditureTypeData.SaveExpenditureType(type);
                    }
                    refresh();
                }
            });
            builder.show();
        }

    public void refresh() {
        finish();
        Intent intent = new Intent(ExpenditureTypeManageActivity.this, ExpenditureTypeManageActivity.class);
        startActivity(intent);
    }
    private void deleteDialog() { // 退出程序的方法
        Dialog dialog = null;
        AlertDialog.Builder customBuilder = new AlertDialog.Builder(
                ExpenditureTypeManageActivity.this);
        customBuilder
                .setTitle("删除")
                // 创建标题
                .setMessage("您确定要删除吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        onDeleteClick();
                        Toast.makeText(ExpenditureTypeManageActivity.this, "已删除！",
                                Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                        refresh();
                    }

                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
        dialog = customBuilder.create();// 创建对话框
        dialog.show(); // 显示对话框
    }
    public void onDeleteClick() {
        // 获取选中的行
        SparseBooleanArray checked = lv.getCheckedItemPositions();
        List<String> checkList = new ArrayList<String>();
        for (int i = 0; i < lv.getCount(); i++) {
            if (checked.get(i) == true) {
                // 获取到选择的行的数据
                checkList.add(typenames.get(i).toString());
            }
        }
        if (checkList.size() > 0) {
            for (String lchecked : checkList) {
               expenditureTypeData.deleteByName(userName,lchecked);
            }
        } else {
            Toast.makeText(ExpenditureTypeManageActivity.this, "您未选中任何项,请选择",
                    Toast.LENGTH_LONG).show();
        }
        lv.clearChoices();// 清空listView的选择状态，方便下次选择
    }
    @Override
    public boolean onKeyDown(int kCode, KeyEvent kEvent) {
        switch (kCode) {
            case KeyEvent.KEYCODE_BACK://返回键
                Intent intent = new Intent(ExpenditureTypeManageActivity.this, JZSheZhiActivity.class);
                startActivity(intent);
                this.finish();
                break;
        }
        return super.onKeyDown(kCode, kEvent);
    }
    @Override
    protected void onResume() {
        overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
        super.onResume();
    }
}
