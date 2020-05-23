package com.rokid.glass.libbase.message.wifi;


import com.rokid.glass.libbase.message.MessageImpl;
import com.rokid.glass.libbase.message.enums.MessageDirection;
import com.rokid.glass.libbase.message.enums.MessageType;

public class WifiMessage extends MessageImpl {

    /**
     * {@link WifiMessageType}
     */
    private WifiMessageType mSubType;

    public WifiMessage() {
    }

    public WifiMessage(MessageDirection direction, WifiMessageType subType) {
        super(MessageType.WIFI, direction);
        mSubType = subType;
    }

    public WifiMessageType getSubType() {
        return mSubType;
    }

    public void setSubType(WifiMessageType subType) {
        mSubType = subType;
    }

    @Override
    public String toString() {
        return "WifiMessage{" +
                "subType=" + mSubType +
                ", mId=" + mId +
                ", mMessageType=" + mMessageType +
                ", mDirection=" + mDirection +
                ", mResultCode=" + mResultCode +
                '}';
    }
}
