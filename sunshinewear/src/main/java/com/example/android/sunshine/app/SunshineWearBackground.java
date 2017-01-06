package com.example.android.sunshine.app;

import android.graphics.Canvas;
import android.graphics.Paint;

public class SunshineWearBackground {

    private Paint mPaint;
    private static final int COLOR_INTERACTIVE_BACKGROUND = 0xFF2196F3;
    private static final int COLOR_LOWBIT_BACKGROUND = 0xFF000000;

    public SunshineWearBackground() {
        mPaint = new Paint();
    }

    public void ambientState() {
        lowBitState();
    }

    public void interactiveState() {
        mPaint.setColor(COLOR_INTERACTIVE_BACKGROUND);
    }

    public void lowBitState() {
        mPaint.setColor(COLOR_LOWBIT_BACKGROUND);
    }

    public void onDraw(Canvas canvas) {
        canvas.drawColor(mPaint.getColor());
    }
}
