package com.jiangdg.usbcamera.hw.listener;

/**
 * Author: heshun
 * Date: 2020/8/19 11:17 AM
 * gmail: shunhe1991@gmail.com
 */
public interface RKKeyListener {

    /**
     * 电源键键事件
     * @param eventType {@link KeyEventType}
     */
    void onPowerKeyEvent(@KeyEventType int eventType);

    /**
     * 回退键事件
     * @param eventType {@link KeyEventType}
     */
    void onBackKeyEvent(@KeyEventType int eventType);

    /**
     * 触控板事件
     * @param eventType {@link KeyEventType}
     */
    void onTouchKeyEvent(@KeyEventType int eventType);

    /**
     * 触控板向后滑动
     */
    void onTouchSlideBack();

    /**
     * 触控板向前滑动
     */
    void onTouchSlideForward();

}
