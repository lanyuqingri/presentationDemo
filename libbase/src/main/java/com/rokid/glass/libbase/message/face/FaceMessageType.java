package com.rokid.glass.libbase.message.face;

/**
 * Author: Zack
 * Email:  newzzack@gmail.com
 * Date:   2019.07.10 20:43
 */
public enum FaceMessageType {
    /**
     * 新增人脸信息
     */
    ADD,
    /**
     * 删除人脸信息
     */
    DELETE_FACES,
    /**
     * 删除用户信息
     */
    DELETE_USERS,
    /**
     * 获取人脸列表
     */
    GET_LIST,
    /**
     * 搜索人脸
     */
     SEARCH,
    /**
     * 更新人脸信息
     */
    UPDATE,
    /**
     * 根据UUID请求人脸图
     */
    GET_IMG,
    /**
     * 根据UID请求人脸详情
     */
    GET_FACE_DETAIL,

    /**
     * 在线单人识别
     */
    SINGLE_FACE_RECOGNIZE,

    /**
     * 在线多人识别
     */
    MULTI_FACE_RECOGNIZE,

    /**
     * 获取标签列表
     */
    GET_TAG_LIST,

    /**
     * 添加标签
     */
    ADD_TAG,

    /**
     * 删除标签
     */
    DELETE_TAG,
}
