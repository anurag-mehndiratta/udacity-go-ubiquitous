package com.example.android.sunshine.app;

import android.graphics.Canvas;
import android.graphics.PointF;

import static com.example.android.sunshine.app.SunshineWearUtils.applyPointVal;

public class SunshineWearLine extends SunshineWearFaceItem {

    public SunshineWearLine(PointF position) {
        super();
        mPosition = position;
    }

    public void onDraw(Canvas canvas, int positionX, int positionY) {
        if (mInteractive) {
            float x = positionX + mPointF.x;
            float y = positionY + mPointF.y;
            canvas.drawLine(x - 20, y, x + 20, y, mPaint);
        }
    }

    public void onLayout(float renderSize) {
        super.onLayout(renderSize);
        applyPointVal(mPointF, mPosition, renderSize);
    }
}
