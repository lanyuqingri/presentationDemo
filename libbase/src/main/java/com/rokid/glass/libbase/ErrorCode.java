package com.rokid.glass.libbase;

/**
 * Author: Zack
 * Email:  newzzack@gmail.com
 * Date:   2020.02.21 12:03
 */
public enum ErrorCode {

    OK(0, "返回正确"),
    INVALID_PARAM(201, "参数错误"),
    STORAGE_NOT_AVAILABLE(202, "存储不可用"),


    /************************* 人脸特征 ***************************/
    FACE_ADD_FEAT_NO_PERSON(10001, "不存在对应人员"),
    FACE_ADD_FEAT_NO_FACE_DETECT(10002, "未检测到人脸"),
    FACE_ADD_FEAT_DB_ERROR(10003, "添加人脸特征数据出错"),
    FACE_ADD_FEAT_FILE_NOT_EXIST(10004, "文件不存在"),
    FACE_DELETE_FEAT_FAILED(10005, "删除特征失败"),
    FACE_DELETE_PERSON_FAILED(10006, "删除人员失败"),
    FACE_UPDATE_PERSON_FAILED(10007, "更新人员信息失败"),
    FACE_ADD_PERSON_CLEAR_FAILED(10008, "添加人员清除人脸库失败"),
    FACE_ADD_PERSONS_CLEAR_FAILED(10009, "批量添加清除人脸库失败"),
    FACE_ADD_PERSONS_DECOMPRESS_FAILED(10010, "批量添加解压zip包出错"),
    FACE_ADD_PERSONS_PAUSE_FAILED(10011, "暂停失败"),
    FACE_ADD_PERSONS_RESUME_FAILED(10012, "恢复失败"),


    DEPLOY_DELETE_DATA_FAILED(20001, "删除布控包数据失败"),


    USER_CONFIG_FAILED(30001, "用户配置失败"),

    RECORD_DELETE(40001,"删除识别记录失败"),

    PLATE_HAS_EXIST(50001, "车牌已存在"),
    PLATE_MANAGER_NOT_INIT(50002,"管理服务未初始化"),
    PLATE_DATABASE_NOT_INIT(50003,"数据库未初始化");


    private int code;
    private String msg;

    ErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static String getMsg(int code){
        ErrorCode[] errorCodes = values();
        for(ErrorCode errorCode : errorCodes){
            if(errorCode.code == code){
                return errorCode.msg;
            }
        }
        return null;
    }
}
