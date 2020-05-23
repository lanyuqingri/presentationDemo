package com.rokid.glass.libbase.message.deploy;


import com.rokid.glass.libbase.message.MessageImpl;
import com.rokid.glass.libbase.message.enums.MessageDirection;
import com.rokid.glass.libbase.message.enums.MessageType;

/**
 * Author: Zack
 * Email:  newzzack@gmail.com
 * Date:   2020.01.03 11:00
 */
public class DeployMessage extends MessageImpl {

    private DeployMessageType mSubType;

    public DeployMessage() {
        super(MessageType.DEPLOY, MessageDirection.MOBILE_TO_GLASS);
    }

    public DeployMessage(DeployMessageType subType) {
        super(MessageType.DEPLOY, MessageDirection.MOBILE_TO_GLASS);
        mSubType = subType;
    }


    public DeployMessageType getSubType() {
        return mSubType;
    }

    public void setSubType(DeployMessageType subType) {
        mSubType = subType;
    }

    @Override
    public String toString() {
        return "DeployMessage{" +
                "mSubType=" + mSubType +
                ", mId=" + mId +
                ", mMessageType=" + mMessageType +
                ", mDirection=" + mDirection +
                ", mResultCode=" + mResultCode +
                '}';
    }
}
