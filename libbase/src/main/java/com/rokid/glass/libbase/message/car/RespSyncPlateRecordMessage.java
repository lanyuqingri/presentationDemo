package com.rokid.glass.libbase.message.car;


import com.rokid.glass.libbase.message.enums.MessageDirection;

public class RespSyncPlateRecordMessage extends PlateInfoMessage {
    protected int code;
    protected String message;

    public RespSyncPlateRecordMessage(){
        super(PlateInfoType.SYNC_PLATE_INFO, MessageDirection.MOBILE_TO_GLASS);
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
