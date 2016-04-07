package com.tust.tools.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import android.os.Environment;

public class SDrw {
    private String name;
    private boolean zhuijia;
    private File file;
    private String dir;//除去sd卡路径的文件路径
    public static final String SDPATH = getSDPath()+"/tust/tools/";
    public SDrw(String name, boolean zhuijia,String dir) {
        this.name = name;
        this.zhuijia = zhuijia;
        this.dir = dir;
        creatDir();
    }

    public boolean isInclude() {
        boolean flag = false;
        File file = new File(SDPATH+dir, name);
        if (file.exists() && file.length() > 1) {
            flag = true;
        } else {
            flag = false;
        }
        return flag;
    }

    public void creatDir() {
        file = new File(SDPATH+dir,name);
        File dirPath = new File(SDPATH+dir);
        if (!dirPath.exists()) {
        	dirPath.mkdirs();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
        	if(!zhuijia){
        		file.delete();
        		 try {
                     file.createNewFile();
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
        	}
        }
    }

    public void outWrite(String str) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, zhuijia));
            bw.write(str);
            bw.newLine();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String readIn() {
        String str = "";
        String str1 = "";
        try {
            if (file.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(file));
                while ((str = br.readLine()) != null) {
                    str1 = str.trim() + "\r\n" + str1.trim();
                }
                br.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str1;
    }
    
    /*
     * 获取sd卡路径
     */
    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
            return sdDir.toString();
        } else {
            return "";
        }
    }
}
