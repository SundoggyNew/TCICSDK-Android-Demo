package com.tencent.tcic.h5;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.ArrayList;

public class ShakeUtils implements SensorEventListener {

    private ArrayList<OnShakeListener> mOnShakeListeners = null;

    // 触发Shake的最小时间间隔
    private static final int MIN_SHAKE_INTERVAL = 1024;
    // 上次触发Shake操作的时间
    private long mLastShakeTime = 0L;

    private long mLastUpdateTime = 0L;
    // 摇晃的速度
    private static int SPEED_SHAKE_MILLSECONDS = 400;
    // 两次摇晃的最小时间间隔
    private static final int SHAKE_INTERVAL_MILLSECOND = 55;
    // 上次摇晃的重力坐标位置
    private float mLastX = 0;
    private float mLastY = 0;
    private float mLastZ = 0;

    public interface OnShakeListener {
        void onShake(double speed);
    }

    public ShakeUtils(Context context) {
        SensorManager mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);

        mOnShakeListeners = new ArrayList<>();
    }

    public void bindShakeListener(OnShakeListener listener) {
        if (listener != null) {
            mOnShakeListeners.add(listener);
        }
    }

    public void unBindShakeListener(OnShakeListener listener) {
        mOnShakeListeners.remove(listener);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event == null) {
            return;
        }

        long curUpdateTime = System.currentTimeMillis();
        // 两次位置改变的时间间隔
        long timeInterval = curUpdateTime - mLastUpdateTime;

        if (timeInterval < SHAKE_INTERVAL_MILLSECOND) {
            return;
        }

        if (event.values.length < 3) {
            return;
        }

        mLastUpdateTime = curUpdateTime;

        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        float deltaX = x - mLastX;
        float deltaY = y - mLastY;
        float deltaZ = z - mLastZ;

        mLastX = x;
        mLastY = y;
        mLastZ = z;

        double speed = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) * 1000.0 / timeInterval;

        if (speed >= SPEED_SHAKE_MILLSECONDS) {
            startShake(speed);
        }
    }

    private void startShake(double speed) {
        long curShakeTime = System.currentTimeMillis();

        if (curShakeTime - mLastShakeTime < MIN_SHAKE_INTERVAL) {
            return;
        }

        mLastShakeTime = curShakeTime;

        for (int i = 0; i < mOnShakeListeners.size(); i++) {
            mOnShakeListeners.get(i).onShake(speed);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
