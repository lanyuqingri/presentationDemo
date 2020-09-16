package com.jiangdg.usbcamera.hw;

import com.jiangdg.usbcamera.hw.listener.RKKeyListener;

/**
 * Author: heshun
 * Date: 2020/8/10 10:56 PM
 * gmail: shunhe1991@gmail.com
 */
public interface GlassKeyEvent {

    /**
     * 电源键、回退键 按键事件
     * @param keycode {@link RKGlassKeycode}
     * @param isPress true {@link android.view.KeyEvent#ACTION_DOWN}, false {@link android.view.KeyEvent#ACTION_UP}
     */
    void onKeyPress(@RKGlassKeycode int keycode, boolean isPress);

    /**
     * 触控板事件
     * @param position TouchBar当前触控位置，bit0-bit7分别表示8个触摸按键 0表示无触摸事件
     */
    void onTouchPress(int position);

    /**
     * 触控板触发事件 可看作{@link GlassKeyEvent#onTouchPress}默认处理
     * @param event
     * @param value 表示滑动的长度
     */
    void onTouchEvent(@RKGlassTouchEvent int event, int value);

    /**
     * 初始化接口
     * @param config
     * @param listener
     */
    void init(GlassConfig config, RKKeyListener listener);

    /**
     * 设置Glass配置信息 目前为单击、长按延时时间 后续可能扩展
     * @param config {@link GlassConfig}
     */
    void setGlassConfig(GlassConfig config);

    /**
     * 设置按键事件监听器
     * @param listener {@link RKKeyListener}
     */
    void setRKKeyListener(RKKeyListener listener);

}
