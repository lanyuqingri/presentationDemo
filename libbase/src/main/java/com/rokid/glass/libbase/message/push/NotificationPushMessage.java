package com.rokid.glass.libbase.message.push;


import com.rokid.glass.libbase.message.dto.PushMessageBean;
import com.rokid.glass.libbase.message.enums.MessageDirection;

/**
 * Author: Zack
 * Email:  newzzack@gmail.com
 * Date:   2019.07.25 10:53
 */
public class NotificationPushMessage extends PushMessage {

    private PushMessageBean mPushMessageBean;

    public NotificationPushMessage() {
        super(MessageDirection.MOBILE_TO_GLASS, PushMessageType.NOTIFICATION);
    }

    public NotificationPushMessage(PushMessageBean pushMessageBean) {
        super(MessageDirection.MOBILE_TO_GLASS, PushMessageType.NOTIFICATION);
        mPushMessageBean = pushMessageBean;
    }

    public PushMessageBean getPushMessageBean() {
        return mPushMessageBean;
    }

    public void setPushMessageBean(PushMessageBean pushMessageBean) {
        mPushMessageBean = pushMessageBean;
    }

    @Override
    public String toString() {
        return "NotificationPushMessage{" +
                "mPushMessageBean=" + mPushMessageBean +
                ", mSubType=" + mSubType +
                ", mId=" + mId +
                ", mMessageType=" + mMessageType +
                ", mDirection=" + mDirection +
                ", mResultCode=" + mResultCode +
                '}';
    }
}
