package com.rokid.glass.libbase.message.car;


import com.rokid.glass.libbase.message.MessageImpl;
import com.rokid.glass.libbase.message.enums.MessageDirection;
import com.rokid.glass.libbase.message.enums.MessageType;

public class PlateInfoMessage extends MessageImpl {
    private PlateInfoType subType;
    public PlateInfoMessage(){ }
    public PlateInfoMessage(PlateInfoType type, MessageDirection direction) {
        super(MessageType.PLATE_INFO, direction);
        subType = type;
    }

    public PlateInfoType getSubType() {
        return subType;
    }

    public void setSubType(PlateInfoType subType) {
        this.subType = subType;
    }
}