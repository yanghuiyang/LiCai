package com.tust.tools.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ChangePic {
	// ת����Ƭ����С�����ϴ�
	public String changePic(String pic, String path) {
		int size = 800;
		String newpic = "";
		Bitmap bb = null;
		try {
			Bitmap b = BitmapFactory.decodeFile(pic);
			int width = b.getWidth();
			int height = b.getHeight();
			if (width > size || height > size) {
				// ͨ�����������СͼƬ
				double bi = ((double) height / (double) width);
				bb = Bitmap
						.createScaledBitmap(b, size, (int) (bi * size), true);
				// ��С��洢��sd����
				if (!SDrw.getSDPath().equals(null)
						&& !SDrw.getSDPath().equals("")) {
					File dir = new File(path);
					if (!dir.exists()) {
						dir.mkdirs();
					}
					// �Ե�ǰʱ�������洢ͼƬ
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
