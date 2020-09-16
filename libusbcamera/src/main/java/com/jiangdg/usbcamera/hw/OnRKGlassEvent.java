package com.jiangdg.usbcamera.hw;

/**
 * Author: heshun
 * Date: 2020/8/10 10:48 AM
 * gmail: shunhe1991@gmail.com
 */
interface OnRKGlassEvent {

    void OnKeyPress(int keycode, boolean isPress);

    void OnTouchPress(int position);

    void OnTouchEvent(int event, int value);

    void OnImuUpdate(long timestamp, float[] values);//IMU事件

    void OnPSensorUpdate(boolean status);//距离传感器

    void OnLSensorUpdate(int lux);//前置光线传感器
}
