package com.rokid.glass.libbase.message;


import com.rokid.glass.libbase.message.enums.MessageDirection;
import com.rokid.glass.libbase.message.enums.MessageType;

/**
 * Author: Zack
 * Email:  newzzack@gmail.com
 * Date:   2019.12.02 20:15
 */
public class CancelMessage extends MessageImpl {

    private MessageType mCancelType;
    private int mCancelSubType;

    public CancelMessage() {
        super(MessageType.CANCEL, MessageDirection.MOBILE_TO_GLASS);
    }

    public CancelMessage(MessageType cancelType, int cancelSubType) {
        super(MessageType.CANCEL, MessageDirection.MOBILE_TO_GLASS);
        mCancelType = cancelType;
        mCancelSubType = cancelSubType;
    }

    public MessageType getCancelType() {
        return mCancelType;
    }

    public void setCancelType(MessageType cancelType) {
        mCancelType = cancelType;
    }

    public int getCancelSubType() {
        return mCancelSubType;
    }

    public void setCancelSubType(int cancelSubType) {
        mCancelSubType = cancelSubType;
    }

    @Override
    public String toString() {
        return "CancelMessage{" +
                "mCancelType=" + mCancelType +
                ", mCancelSubType=" + mCancelSubType +
                ", mId=" + mId +
                ", mMessageType=" + mMessageType +
                ", mDirection=" + mDirection +
                ", mResultCode=" + mResultCode +
                '}';
    }
}
