package com.tust.tools.service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class ImageDownloadHandler {

	
	public  Bitmap loadImageFromUrl(String url) throws Exception  {
		HttpEntity httpEntity = getHttpEntity(url);
		ByteArrayOutputStream imageBytesStream = getImageBytesStream(httpEntity);
		return convertBytesToBitmap(imageBytesStream);
		
    }

	private Bitmap convertBytesToBitmap(ByteArrayOutputStream baos) {
		byte[] imageArray = baos.toByteArray();
		return BitmapFactory.decodeByteArray(
				imageArray, 0, imageArray.length);
	}

	private ByteArrayOutputStream getImageBytesStream(HttpEntity entity) 
		throws Exception {
		InputStream is = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			is = entity.getContent();
			byte[] buf = new byte[1024];
			int readBytes = -1;
			while ((readBytes = is.read(buf)) != -1) {
				baos.write(buf, 0, readBytes);
			}
		} finally {
			if (baos != null) {
				baos.close();
			}
			if (is != null) {
				is.close();
			}
		}
		
		return baos;
	}

	private HttpEntity getHttpEntity(String url) throws Exception {
		final DefaultHttpClient client = new DefaultHttpClient();
		final HttpGet getRequest = new HttpGet(url);

		HttpResponse response = client.execute(getRequest);
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode != HttpStatus.SC_OK)  {
			Log.e("PicShow", "Request URL failed, error code =" + statusCode);
		}
		
		HttpEntity entity = response.getEntity();
		if (entity == null) {
			Log.e("PicShow", "HttpEntity is null");
		}
		
		return entity;
	}
}
