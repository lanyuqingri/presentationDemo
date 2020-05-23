package com.rokid.glass.libbase.message.push;


import com.rokid.glass.libbase.message.MessageImpl;
import com.rokid.glass.libbase.message.enums.MessageDirection;
import com.rokid.glass.libbase.message.enums.MessageType;

/**
 * Author: Zack
 * Email:  newzzack@gmail.com
 * Date:   2019.07.25 10:41
 */
public class PushMessage extends MessageImpl {

    /**
     * {@link PushMessageType}
     */
    protected PushMessageType mSubType;

    public PushMessage() {
    }

    public PushMessage(MessageDirection direction, PushMessageType subType) {
        super(MessageType.PUSH, direction);
        this.mSubType = subType;
    }

    public PushMessageType getSubType() {
        return mSubType;
    }

    public void setSubType(PushMessageType subType) {
        this.mSubType = subType;
    }

    @Override
    public String toString() {
        return "PushMessage{" +
                "mSubType=" + mSubType +
                ", mId=" + mId +
                ", mMessageType=" + mMessageType +
                ", mDirection=" + mDirection +
                ", mResultCode=" + mResultCode +
                '}';
    }
}
