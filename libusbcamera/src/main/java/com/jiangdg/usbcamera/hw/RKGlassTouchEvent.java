package com.jiangdg.usbcamera.hw;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Author: heshun
 * Date: 2020/8/10 10:38 PM
 * gmail: shunhe1991@gmail.com
 */
@Retention(RetentionPolicy.CLASS)
@IntDef(value = {RKGlassTouchEvent.SHORT_PRESS, RKGlassTouchEvent.LONG_PRESS,
        RKGlassTouchEvent.FORWARD_SLIDE, RKGlassTouchEvent.BACKWARD_SLIDE})
public @interface RKGlassTouchEvent {

    /**
     * 短按
     */
    int SHORT_PRESS = 2;

    /**
     * 长按
     */
    int LONG_PRESS = 3;

    /**
     * 向前滑动
     */
    int FORWARD_SLIDE = 4;

    /**
     * 向后滑动
     */
    int BACKWARD_SLIDE = 5;
}
