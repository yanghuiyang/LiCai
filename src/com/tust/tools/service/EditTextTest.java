package com.tust.tools.service;

/**
 * Created by yang on 2016/4/27.
 */
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.EditText;

public class EditTextTest extends EditText {
    public EditTextTest(Context context) {
        super(context);
    }

    public EditTextTest(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public EditTextTest(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setTextSize(20);
        paint.setColor(Color.GRAY);
        canvas.drawText("输入提示文本:", 10, getHeight() / 2 + 5, paint);
        super.onDraw(canvas);
    }
}