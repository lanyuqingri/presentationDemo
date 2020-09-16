package com.jiangdg.usbcamera.glasssdk;

/**
 * Author: heshun
 * Date: 2020/9/2 3:54 PM
 * gmail: shunhe1991@gmail.com
 */
public interface OnGlassEvent {

    void OnKeyPress(int var1, boolean var2);

    void OnTouchPress(int var1);

    void OnTouchEvent(int var1, int var2);

    void OnImuUpdate(long var1, float[] var3);

    void OnPsensorUpdate(boolean var1);

    void OnLsensorUpdate(int var1);
}
