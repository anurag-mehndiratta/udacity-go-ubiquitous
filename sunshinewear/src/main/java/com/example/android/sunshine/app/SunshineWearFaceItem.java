package com.example.android.sunshine.app;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.annotation.CallSuper;

import static com.example.android.sunshine.app.SunshineWearUtils.applyPointVal;

public abstract class SunshineWearFaceItem {

    protected boolean mInteractive;
    protected Paint mPaint;
    protected PointF mPointF;
    protected PointF mPosition;

    public SunshineWearFaceItem() {
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);

        mPointF = new PointF(0f, 0f);
    }

    @CallSuper
    public void ambientState() {
        lowBitState();
    }

    @CallSuper
    public void interactiveState() {
        mInteractive = true;
    }

    @CallSuper
    public void lowBitState() {
        mInteractive = false;
    }

    public abstract void onDraw(Canvas canvas, int positionX, int positionY);

    @CallSuper
    public void onLayout(float renderSize) {
        applyPointVal(mPointF, mPosition, renderSize);
    }
}
