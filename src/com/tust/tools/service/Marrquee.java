package com.tust.tools.service;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

//�����TextView ��д��Ϊ�˷�ֹ��TextViewʧȥ����ʱ�����ֹͣ��
public class Marrquee extends TextView {
    public Marrquee(Context context) {
        super(context);
    }

    public Marrquee(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Marrquee(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean isFocused() {
        return true;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
    }
}
