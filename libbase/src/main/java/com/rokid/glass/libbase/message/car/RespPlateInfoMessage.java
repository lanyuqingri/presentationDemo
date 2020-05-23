package com.rokid.glass.libbase.message.car;


import com.rokid.glass.libbase.message.enums.MessageDirection;

public class RespPlateInfoMessage extends PlateInfoMessage {
    protected int code;
    protected String message;

    public RespPlateInfoMessage(PlateInfoType type){
        super(type, MessageDirection.GLASS_TO_MOBILE);
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
}
