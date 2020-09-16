package com.jiangdg.usbcamera.hw;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Author: heshun
 * Date: 2020/8/10 10:52 AM
 * gmail: shunhe1991@gmail.com
 */
@IntDef(value = {RKGlassKeycode.GLASS_KEYCODE_BACK, RKGlassKeycode.GLASS_KEYCODE_POWER})
@Retention(RetentionPolicy.CLASS)
public @interface RKGlassKeycode {
    int GLASS_KEYCODE_BACK = 16;
    int GLASS_KEYCODE_POWER = 4;
}
