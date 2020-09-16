package com.jiangdg.usbcamera.hw.listener;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Author: heshun
 * Date: 2020/8/19 11:28 AM
 * gmail: shunhe1991@gmail.com
 */
@IntDef(value = {KeyEventType.SINGLE_CLICK, KeyEventType.DOUBLE_CLICK, KeyEventType.LONG_CLICK})
@Retention(RetentionPolicy.SOURCE)
public @interface KeyEventType {

    /**
     * 单击事件
     */
    public static final int SINGLE_CLICK =1;

    /**
     * 双击事件
     */
    public static final int DOUBLE_CLICK =2;

    /**
     * 长按事件
     */
    public static final int LONG_CLICK =3;
}
