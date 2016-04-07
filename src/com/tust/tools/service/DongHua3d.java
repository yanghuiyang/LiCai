package com.tust.tools.service;



import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;

public class DongHua3d {
    private View v;
    public static void applyRotation(View v, float start, float end, int mode) {
        final float centerX = v.getWidth() / 2.0f;
        final float centerY = v.getHeight() / 2.0f;
        Rotate3dAnimation rotation = null;
        if (mode == 0) {// ��ʼ
            rotation = new Rotate3dAnimation(start, end, centerX, centerY, 310.0f, true);
            rotation.setDuration(400);
            v.startAnimation(rotation);
        } else {// ����
            rotation = new Rotate3dAnimation(end, start, centerX, centerY, 310.0f, false);
            rotation.setDuration(400);
            v.startAnimation(rotation);
        }
    }

    Runnable rs = new Runnable() {
        @Override
        public void run() {
            applyRotation(v, 0, 180, 0);
        }
    };
    Runnable re = new Runnable() {
        @Override
        public void run() {
            applyRotation(v, 0, 180, 1);
        }
    };

   static  Handler handler = new Handler();

    public void oneViewDongHua(View v) {
        this.v = v;
        new Thread() {
            public void run() {
                try {
                    handler.post(rs);
                    sleep(400);
                    handler.post(re);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    
    //���ڶ����л���Ҫʱ�䣬�����ӳ���ʾ�����������֧������
    public static void yanChiShow(final View vShow,final View vHidden){
    	new Thread(){
    		public void run(){
    			try {
    				handler.post(new Runnable(){
        				public void run(){
        					DongHua3d.applyRotation(vHidden, 0, 180, 0);
        				 }
        				});
					sleep(400);
					handler.post(new Runnable(){
        				public void run(){
	    					vHidden.setVisibility(View.GONE);
	    					vShow.setVisibility(View.VISIBLE);
	    					DongHua3d.applyRotation(vShow, 0, 180, 1);
        				 }
    				});
	    			
    			} catch (InterruptedException e) {
					e.printStackTrace();
				}
    		}
    	}.start();
    }
    
    public static LayoutAnimationController listDongHua(){
        AnimationSet set = new AnimationSet(true);
        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(50);
        set.addAnimation(animation);
        animation = new TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0.0f,Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, -1.0f,Animation.RELATIVE_TO_SELF, 0.0f
        );
        animation.setDuration(100);
        set.addAnimation(animation);
        LayoutAnimationController controller = new LayoutAnimationController(set, 0.5f);
        return controller;
    }
    
}
