package com.rokid.glass.libbase.message.face;


import com.rokid.glass.libbase.message.MessageImpl;
import com.rokid.glass.libbase.message.enums.MessageDirection;
import com.rokid.glass.libbase.message.enums.MessageType;

/**
 * Author: Zack
 * Email:  newzzack@gmail.com
 * Date:   2019.07.10 17:10
 */
public class FaceMessage extends MessageImpl {

    /**
     * {@link FaceMessageType}
     */
    private FaceMessageType mSubType;

    public FaceMessage() {
    }

    public FaceMessage(MessageDirection direction, FaceMessageType subType) {
        super(MessageType.FACE, direction);
        mSubType = subType;
    }

    public FaceMessageType getSubType() {
        return mSubType;
    }

    public void setSubType(FaceMessageType subType) {
        mSubType = subType;
    }

    @Override
    public String toString() {
        return "FaceMessage{" +
                "subType=" + mSubType +
                ", mId=" + mId +
                ", mMessageType=" + mMessageType +
                ", mDirection=" + mDirection +
                ", mResultCode=" + mResultCode +
                '}';
    }
}
