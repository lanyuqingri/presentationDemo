package com.rokid.glass.libbase.message.enums;

/**
 * Author: Zack
 * Email:  newzzack@gmail.com
 * Date:   2019.07.10 20:43
 */
public enum MessageType {
    /**
     * 握手消息
     */
    WELCOME,
    /**
     * 人脸管理消息
     */
    FACE,
    /**
     * 文件传输消息
     */
    FILE,
    /**
     * 推送消息
     */
    PUSH,
    /**
     * Wifi连接消息
     */
    WIFI,
    /**
     * 车辆识别
     */
    CAR,
    /**
     * 识别记录
     */
    RECORD,
    /**
     * 取消消息，**当前主要场景是取消批处理消息**
     */
    CANCEL,
    /**
     * 眼镜端主动断开消息
     */
    DISCONNECT,
    /**
     * 布控任务
     */
    DEPLOY,
    /**
     * 车牌编辑/添加/删除/查询
     */
    PLATE_INFO
}
