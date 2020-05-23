package com.rokid.glass.libbase.message.file;

public enum FileMessageType {
    /**
     * 新增文件
     */
    UPLOAD_FILE,
    /**
     * 删除文件
     */
    DELETE_FILE,
    /**
     * 获取文件列表
     */
    GET_LIST,
    /**
     * 请求缩略图
     */
    GET_FILE_THUMB,
    /**
     * 请求原图
     */
    GET_FILE_DETAIL
}
