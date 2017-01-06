package com.example.android.sunshine.app;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.wearable.watchface.WatchFaceStyle;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.WindowInsets;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.ustwo.clockwise.ConnectedWatchFace;
import com.ustwo.clockwise.WatchFaceTime;
import com.ustwo.clockwise.WatchMode;
import com.ustwo.clockwise.WatchShape;

import java.util.Date;

import static com.example.android.sunshine.app.SunshineWearUtils.getIcon;

public class SunshineWearService extends ConnectedWatchFace {

    private static final String FORECAST_INFO_PATH = "/forecast/info";
    private static final String FORECAST_PATH = "/forecast";
    private static final String KEY_HIGH = "high";
    private static final String KEY_LOW = "low";
    private static final String KEY_WEATHER_ID = "weatherId";

    private SunshineWearBackground background;
    private SunshineWearText text;
    private SunshineWearLine divider;
    private SunshineWearText highText;
    private SunshineWearText lowText;
    private SunshineWearText seconds;
    private SunshineWearText time;
    private PointF mWatchFaceCenter = new PointF(0f, 0f);
    private SunshineWearBitmapDrawable mWeatherIcon;

    private static final int COLOR_WHITE_100 = 0xFFFFFFFF;
    private static final int COLOR_WHITE_80 = 0xCCFFFFFF;

    private static final PointF DATE_POSITION = new PointF(160f, 160f);
    private static final float DATE_TEXT_SIZE = 24f;
    private static final PointF DIVIDER_POSITION = new PointF(160f, 190f);
    private static final PointF HIGH_POSITION = new PointF(165f, 240f);
    private static final float HIGH_TEXT_SIZE = 40f;
    private static final PointF LOW_POSITION = new PointF(235f, 240f);
    private static final float LOW_TEXT_SIZE = 40f;
    private static final PointF SECONDS_POSITION = new PointF(225f, 120f);
    private static final float SECONDS_TEXT_SIZE = 24f;

    private static final PointF TIME_POSITION = new PointF(150f, 120f);
    private static final float TIME_TEXT_SIZE = 48f;
    private static final Typeface TYPEFACE_SERIF = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL);
    private static final Typeface TYPEFACE_SERIF_LIGHT = Typeface.create("sans-serif-light", Typeface.NORMAL);
    private static final PointF WEATHER_ICON_POSITION = new PointF(65f, 210f);
    private static final float WEATHER_ICON_SIZE = 40f;

    @Override
    public void onCreate() {
        super.onCreate();

        background = new SunshineWearBackground();
        time = new SunshineWearText(TYPEFACE_SERIF, COLOR_WHITE_100, TIME_TEXT_SIZE, TIME_POSITION, 10, true);
        seconds = new SunshineWearText(TYPEFACE_SERIF, COLOR_WHITE_80, SECONDS_TEXT_SIZE, SECONDS_POSITION, 0, false);
        text = new SunshineWearText(TYPEFACE_SERIF_LIGHT, COLOR_WHITE_80, DATE_TEXT_SIZE, DATE_POSITION, 0, false);
        divider = new SunshineWearLine(DIVIDER_POSITION);
        highText = new SunshineWearText(TYPEFACE_SERIF, COLOR_WHITE_100, HIGH_TEXT_SIZE, HIGH_POSITION, -30, true);
        lowText = new SunshineWearText(TYPEFACE_SERIF_LIGHT, COLOR_WHITE_80, LOW_TEXT_SIZE, LOW_POSITION, -30, true);
        mWeatherIcon = new SunshineWearBitmapDrawable(WEATHER_ICON_SIZE, WEATHER_ICON_POSITION);
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        for (DataEvent dataEvent : dataEvents) {
            if (dataEvent.getType() == DataEvent.TYPE_CHANGED &&
                    FORECAST_INFO_PATH.equals(dataEvent.getDataItem().getUri().getPath())) {
                DataMap dataMap = DataMapItem.fromDataItem(dataEvent.getDataItem()).getDataMap();
                if (dataMap.containsKey(KEY_HIGH)) {
                    highText.setText(dataMap.getString(KEY_HIGH));
                }
                if (dataMap.containsKey(KEY_LOW)) {
                    lowText.setText(dataMap.getString(KEY_LOW));
                }
                if (dataMap.containsKey(KEY_WEATHER_ID)) {
                    mWeatherIcon.setBitmap(getResources(), getIcon(dataMap.getInt(KEY_WEATHER_ID)));
                }
            }
        }
    }

    @Override
    public void onWatchModeChanged(WatchMode watchMode) {
        refreshCurrentState();
    }

    @Override
    protected long getInteractiveModeUpdateRate() {
        return DateUtils.SECOND_IN_MILLIS;
    }

    @Override
    protected WatchFaceStyle getWatchFaceStyle() {
        return new WatchFaceStyle.Builder(this)
                .setCardPeekMode(WatchFaceStyle.PEEK_MODE_VARIABLE)
                .setAmbientPeekMode(WatchFaceStyle.AMBIENT_PEEK_MODE_VISIBLE)
                .setPeekOpacityMode(WatchFaceStyle.PEEK_OPACITY_MODE_TRANSLUCENT)
                .setCardProgressMode(WatchFaceStyle.PROGRESS_MODE_NONE)
                .setBackgroundVisibility(WatchFaceStyle.BACKGROUND_VISIBILITY_INTERRUPTIVE)
                .setViewProtectionMode(WatchFaceStyle.PROTECT_HOTWORD_INDICATOR | WatchFaceStyle.PROTECT_STATUS_BAR)
                .setHotwordIndicatorGravity(Gravity.TOP | Gravity.START)
                .setStatusBarGravity(Gravity.TOP | Gravity.START)
                .setShowSystemUiTime(false)
                .build();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int backgroundX = -getWidth() / 2;
        int backgroundY = -getHeight() / 2;

        canvas.save();

        canvas.translate(mWatchFaceCenter.x, mWatchFaceCenter.y);

        background.onDraw(canvas);
        time.onDraw(canvas, backgroundX, backgroundY);
        seconds.onDraw(canvas, backgroundX, backgroundY);
        text.onDraw(canvas, backgroundX, backgroundY);
        divider.onDraw(canvas, backgroundX, backgroundY);
        highText.onDraw(canvas, backgroundX, backgroundY);
        lowText.onDraw(canvas, backgroundX, backgroundY);
        mWeatherIcon.onDraw(canvas, backgroundX, backgroundY);
        canvas.restore();
    }

    @Override
    protected void onLayout(WatchShape watchShape, Rect rect, WindowInsets windowInsets) {
        // Convert spec dimensions to current screen size
        float renderSize = Math.min(getWidth(), getHeight());

        mWatchFaceCenter.set(getWidth() * 0.5f, getHeight() * 0.5f);

        time.onLayout(renderSize);
        seconds.onLayout(renderSize);
        text.onLayout(renderSize);
        divider.onLayout(renderSize);
        highText.onLayout(renderSize);
        lowText.onLayout(renderSize);
        mWeatherIcon.onLayout(renderSize);

        refreshCurrentState();

        updateDateTime(true, true);
        updateForecast(true);
    }

    @Override
    protected void onTimeChanged(WatchFaceTime oldTime, WatchFaceTime newTime) {
        updateDateTime(newTime.hasDateChanged(oldTime), newTime.hasMinuteChanged(oldTime));
        updateForecast(newTime.hasMinuteChanged(oldTime));
    }

    /**
     * Ambient Light Handling
     */
    private void applyAmbientState() {
        background.ambientState();
        time.ambientState();
        seconds.ambientState();
        text.ambientState();
        divider.ambientState();
        mWeatherIcon.ambientState();
        highText.ambientState();
        lowText.ambientState();
    }

    /**
     * Interactive State Handling
     */
    private void applyInteractiveState() {
        background.interactiveState();
        time.interactiveState();
        seconds.interactiveState();
        text.interactiveState();
        divider.interactiveState();
        mWeatherIcon.interactiveState();
        highText.interactiveState();
        lowText.interactiveState();
    }

    /**
     * LowBitState Handling
     */
    private void applyLowBitState() {
        background.lowBitState();
        time.lowBitState();
        seconds.lowBitState();
        text.lowBitState();
        divider.lowBitState();
        mWeatherIcon.lowBitState();
        highText.lowBitState();
        lowText.lowBitState();
    }

    private void refreshCurrentState() {
        switch (getCurrentWatchMode()) {
            case INTERACTIVE:
                applyInteractiveState();
                break;
            case AMBIENT:
                applyAmbientState();
                break;
            default:
                // (LOW_BIT, BURN_IN, LOW_BIT_BURN_IN)
                applyLowBitState();
                break;
        }
    }

    private void updateDateTime(boolean updateDate, boolean updateTime) {
        Date date = new Date();
        if (updateDate) {
            text.setDate(date);
        }
        if (updateTime) {
            time.setTime(date, is24HourFormat());
        }
        seconds.setSeconds(date);
    }

    private void updateForecast(boolean update) {
        if (update) {
            putMessage(FORECAST_PATH, new byte[0], null);
        }
    }
}
