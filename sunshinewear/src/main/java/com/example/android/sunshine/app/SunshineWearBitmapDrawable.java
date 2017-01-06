package com.example.android.sunshine.app;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PointF;

import static com.example.android.sunshine.app.SunshineWearUtils.getFloatVal;

public class SunshineWearBitmapDrawable extends SunshineWearFaceItem {

    private Bitmap mBitmap;
    private int mScaledSize;
    private float mSize;

    public SunshineWearBitmapDrawable(float size, PointF position) {
        super();

        mBitmap = Bitmap.createBitmap((int) size, (int) size, Bitmap.Config.ALPHA_8);
        mSize = size;
        mPosition = position;
    }

    public void onDraw(Canvas canvas, int positionX, int positionY) {
        if (mInteractive) {
            canvas.drawBitmap(mBitmap, positionX + mPointF.x, positionY + mPointF.y, mPaint);
        }
    }

    public void onLayout(float renderSize) {
        super.onLayout(renderSize);
        mScaledSize = (int) getFloatVal(mSize, renderSize);
    }

    public void setBitmap(Resources resources, int resourceId) {
        mBitmap = BitmapFactory.decodeResource(resources, resourceId);
        mBitmap = Bitmap.createScaledBitmap(mBitmap, mScaledSize, mScaledSize, true);
    }
}
