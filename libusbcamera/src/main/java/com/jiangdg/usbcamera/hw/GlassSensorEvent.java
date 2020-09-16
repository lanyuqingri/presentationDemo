package com.jiangdg.usbcamera.hw;

/**
 * Author: heshun
 * Date: 2020/8/10 10:58 PM
 * gmail: shunhe1991@gmail.com
 */
public interface GlassSensorEvent {
    /**
     * 距离传感器你
     * @param status  true可认为是带上眼镜 唤醒光机 false可认为已摘下眼镜 熄灭光机
     */
    void onPSensorUpdate(boolean status);//距离传感器

    /**
     * 前置光线传感器
     * @param lux 最小值为0
     */
    void onLSensorUpdate(int lux);//前置光线传感器
}
