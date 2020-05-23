package com.rokid.glass.libbase.message.push;

/**
 * Author: Zack
 * Email:  newzzack@gmail.com
 * Date:   2019.07.25 10:47
 */
public enum PushMessageType {

    /**
     * 眼镜从手机获取消息列表
     */
    GET_LIST,

    /**
     * 实时消息推送到眼镜
     */
    NOTIFICATION,

    /**
     * 已读未读
     */
    STATUS,

    /**
     * 人脸信息
     */
    FACE,

    /**
     * 账号信息
     */
    ACCOUNT
}
