package com.rokid.glass.libbase.message.deploy;


/**
 * Author: Zack
 * Email:  newzzack@gmail.com
 * Date:   2020.01.03 11:10
 */
public class DeployCheckUpdateMessage extends DeployMessage {

    public DeployCheckUpdateMessage() {
        super(DeployMessageType.CHECK_UDPATE);
    }

    @Override
    public String toString() {
        return "DeployCheckUpdateMessage{" +
                ", mId=" + mId +
                ", mMessageType=" + mMessageType +
                ", mDirection=" + mDirection +
                ", mResultCode=" + mResultCode +
                '}';
    }
}
