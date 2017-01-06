package com.example.android.sunshine.app;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Typeface;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.android.sunshine.app.SunshineWearUtils.getFloatVal;

public class SunshineWearText extends SunshineWearFaceItem {

    private int mLowBitOffset;
    private boolean mLowBitVisible;
    private String mText;
    private float mTextSize;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("EEE, MMM dd yyyy");
    private static final SimpleDateFormat SECONDS_FORMAT = new SimpleDateFormat("ss");
    private static final SimpleDateFormat TIME_FORMAT_12 = new SimpleDateFormat("h:mm");
    private static final SimpleDateFormat TIME_FORMAT_24 = new SimpleDateFormat("HH:mm");

    public SunshineWearText(Typeface typeface, int color, float textSize, PointF position, int lowBitOffset, boolean lowBitVisible) {
        super();

        mPaint.setColor(color);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTypeface(typeface);

        mText = "";
        mTextSize = textSize;
        mPointF = new PointF(0f, 0f);
        mPosition = position;
        mLowBitOffset = lowBitOffset;
        mLowBitVisible = lowBitVisible;
    }

    public void interactiveState() {
        super.interactiveState();
        mPaint.setAntiAlias(true);
    }

    public void lowBitState() {
        super.lowBitState();
        mPaint.setAntiAlias(false);
    }

    public void onDraw(Canvas canvas, int positionX, int positionY) {
        if (mInteractive) {
            canvas.drawText(mText, positionX + mPointF.x, positionY + mPointF.y, mPaint);
        } else if (mLowBitVisible) {
            canvas.drawText(mText, positionX + mPointF.x + mLowBitOffset, positionY + mPointF.y, mPaint);
        }
    }

    public void onLayout(float renderSize) {
        super.onLayout(renderSize);
        mPaint.setTextSize(getFloatVal(mTextSize, renderSize));
    }

    public void setDate(Date date) {
        mText = DATE_FORMAT.format(date).toUpperCase();
    }

    public void setSeconds(Date date) {
        mText = SECONDS_FORMAT.format(date);
    }

    public void setText(String text) {
        mText = text;
    }

    public void setTime(Date date, boolean is24HourFormat) {
        String time = is24HourFormat ? TIME_FORMAT_24.format(date) : TIME_FORMAT_12.format(date);
        mText = time.length() == 4 ? "0" + time : time;
    }
}