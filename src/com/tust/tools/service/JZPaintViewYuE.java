package com.tust.tools.service;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

//画图界面
public  class JZPaintViewYuE extends View {
    private Paint mPaints;
    private Paint mFramePaint;
    private boolean mUseCenters;
    private RectF mBigOval;
    private float mStart;
    private float mSweep;
    private int bujin;
    //预算  余额   比率
    private float bi;
    
    public JZPaintViewYuE(Context context,float yusuan,float yue,int color,int bujin) {
        super(context);
        this.bi = yue/(yusuan/360);	//比率  (控制绘图区域)
        this.bujin = bujin;
        mPaints = new Paint();
        mPaints = new Paint(mPaints);
        mPaints.setColor(color);
        mUseCenters = true;
        mBigOval = new RectF(15, 0, 175, 160);//绘图区域 距左， 距上 ，左起点至右距离 ，上起点距下距离
        mFramePaint = new Paint();
        mFramePaint.setAntiAlias(true);
        mFramePaint.setStyle(Paint.Style.STROKE);
        mFramePaint.setStrokeWidth(0);
    }

    private void drawArcs(Canvas canvas, RectF oval, boolean useCenter, Paint paint) {
        canvas.drawRect(oval, mFramePaint);
        canvas.drawArc(oval, mStart, mSweep, useCenter, paint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.drawColor(Color.WHITE);//绘图背景色
        drawArcs(canvas, mBigOval, mUseCenters, mPaints);
        mStart = 0;
        mSweep +=bujin ;
        if (mSweep > bi) {
        	mSweep = bi;
        }
        invalidate();
    }
}