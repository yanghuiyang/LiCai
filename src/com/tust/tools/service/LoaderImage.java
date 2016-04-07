package com.tust.tools.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

/*
 * �첽����ͼƬ
 * */
public class LoaderImage {
	public  Drawable loadDrawable(final String imageUrl, final ImageView imageView, final ImageCallback imageCallback) {
		// ���ݴ���Ĳ�����ͬ��ִ�в�ͬ�Ĳ���
		return isHasImg(imageUrl, imageView, imageCallback);
	}

	private  Drawable isHasImg(final String imageUrl, final ImageView imageView, final ImageCallback imageCallback) {
		// �ԡ�/������ִ�������ӵ�ַ����Ϊ�����Ӹ�ʽ�ǹ̶���
		String imageName = imageUrl.lastIndexOf("/") != 0 ? imageUrl.substring(imageUrl.lastIndexOf("/")+1, imageUrl.length()) : "";
		File file = new File(SDrw.SDPATH+"tianqi/imgcache", imageName);
		Drawable d = null;
		FileInputStream fis = null;
		if (file.exists()) {// �ж������ͼ���Ƿ��ڱ��ش���
			try {
				fis = new FileInputStream(file);
				d = Drawable.createFromStream(fis, "src");
				fis.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return d;
		} else {// ������ز�������ʼ����
			final Handler handler = new Handler() {
				public void handleMessage(Message message) {
					imageCallback.imageLoaded((Drawable) message.obj, imageView, imageUrl);
				}
			};
			// ������һ���µ��߳�����ͼƬ
			new Thread() {
				@Override
				public void run() {
					Drawable drawable = loadImageFromUrl(imageUrl);
					Message message = handler.obtainMessage(0, drawable);
					handler.sendMessage(message);
				}
			}.start();
			return null;
		}
	}

	private  Drawable loadImageFromUrl(final String url) {
		Drawable d = null;
		try {
			d = creatDrawable(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return d;
	}

	/*
	 * ͨ���ṩ���������ظ�ͼƬ��������洢�ڱ���
	 */
	private  Drawable creatDrawable(final String url) throws Exception {
		URL m = new URL(url);
		InputStream i = (InputStream) m.getContent();
		Bitmap b = BitmapFactory.decodeStream(i);
		BitmapDrawable bd = new BitmapDrawable(b);
		String imageName = url.lastIndexOf("/") != 0 ? url.substring(url.lastIndexOf("/")+1, url.length()) : "";
		File file = null;
		if (!SDrw.getSDPath().equals("") && !SDrw.getSDPath().equals(null)) {
			File dir = new File(SDrw.SDPATH+"tianqi/imgcache");
			file = new File(dir, imageName);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			if (!file.exists()) {
				file.createNewFile();
			} else {
				file.delete();
			}
			// �Ѹ�ͼƬд�뵽sd��
			FileOutputStream fos = new FileOutputStream(file, true);
			b.compress(CompressFormat.PNG, 100, fos);
			fos.close();
			i.close();
		}
		return bd;
	}

	// �ص��ӿ� ��������ͼƬ
	public interface ImageCallback {
		public void imageLoaded(Drawable imageDrawable, ImageView imageView, String imageUrl);
	}

}
