package com.rokid.glass.libbase.message.car;


import com.rokid.glass.libbase.message.enums.MessageDirection;

public class ReqPlateInfoMessage extends PlateInfoMessage {
    public ReqPlateInfoMessage(PlateInfoType type) {
        super(type, MessageDirection.MOBILE_TO_GLASS);
    }
}

