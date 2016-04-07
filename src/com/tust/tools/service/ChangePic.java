package com.tust.tools.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ChangePic {
	// 转换照片，变小利于上传
	public String changePic(String pic, String path) {
		int size = 800;
		String newpic = "";
		Bitmap bb = null;
		try {
			Bitmap b = BitmapFactory.decodeFile(pic);
			int width = b.getWidth();
			int height = b.getHeight();
			if (width > size || height > size) {
				// 通过长宽比来缩小图片
				double bi = ((double) height / (double) width);
				bb = Bitmap
						.createScaledBitmap(b, size, (int) (bi * size), true);
				// 缩小后存储在sd卡上
				if (!SDrw.getSDPath().equals(null)
						&& !SDrw.getSDPath().equals("")) {
					File dir = new File(path);
					if (!dir.exists()) {
						dir.mkdirs();
					}
					// 以当前时间命名存储图片
					Date date = new Date(System.currentTimeMillis());
					SimpleDateFormat dateFormat = new SimpleDateFormat(
							"yyyyMMddHHmmss");
					File newfile = new File(path + dateFormat.format(date)
							+ ".jpg");
					FileOutputStream out = new FileOutputStream(newfile);
					bb.compress(Bitmap.CompressFormat.JPEG, 70, out);
					newpic = newfile.getAbsolutePath();
				} else {
					newpic = "";
				}
			} else {
				newpic = pic;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return newpic;
	}

}
