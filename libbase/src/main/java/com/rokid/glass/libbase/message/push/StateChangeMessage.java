package com.rokid.glass.libbase.message.push;


import com.rokid.glass.libbase.message.dto.PushMessageBean;
import com.rokid.glass.libbase.message.enums.MessageDirection;

/**
 * Author: Zack
 * Email:  newzzack@gmail.com
 * Date:   2019.07.25 10:54
 */
public class StateChangeMessage extends PushMessage {

    private String mMessageId;
    private PushMessageBean.Status mStatus;

    public StateChangeMessage() {
    }

    public StateChangeMessage(MessageDirection direction, PushMessageType subType) {
        super(direction, subType);
    }

    public StateChangeMessage(String messageId, PushMessageBean.Status mStatus) {
        super(MessageDirection.GLASS_TO_MOBILE, PushMessageType.STATUS);
        this.mMessageId = messageId;
        this.mStatus = mStatus;
    }

    public String getMessageId() {
        return mMessageId;
    }

    public void setMessageId(String messageId) {
        this.mMessageId = messageId;
    }

    public PushMessageBean.Status getStatus() {
        return mStatus;
    }

    public void setStatus(PushMessageBean.Status status) {
        this.mStatus = status;
    }

    @Override
    public String toString() {
        return "StateChangeMessage{" +
                "mMessageId='" + mMessageId + '\'' +
                ", mStatus=" + mStatus +
                ", mSubType=" + mSubType +
                ", mId=" + mId +
                ", mMessageType=" + mMessageType +
                ", mDirection=" + mDirection +
                ", mResultCode=" + mResultCode +
                '}';
    }
}
