package com.rokid.glass.libbase.message.enums;

/**
 * Author: Zack
 * Email:  newzzack@gmail.com
 * Date:   2019.07.12 11:19
 */
public enum ResultCode {

    OK(200, "OK"),
    InvalidParam(400, "参数错误"),
    InternalServerError(500, "服务器内部错误"),
    ;

    private int code;
    private String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ResultCode{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
