package com.tust.tools.service;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
<<<<<<< HEAD

=======
import android.graphics.Rect;
>>>>>>> a63a26fcf9f5e1b09b2e016b7a158df387edddd8
//跑马灯TextView 重写是为了防止当TextView失去焦点时跑马灯停止。
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
