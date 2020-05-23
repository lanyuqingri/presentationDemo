package com.rokid.glass.libbase.message.push;



import com.rokid.glass.libbase.message.dto.PushMessageBean;
import com.rokid.glass.libbase.message.enums.MessageDirection;

import java.util.List;

/**
 * Author: Zack
 * Email:  newzzack@gmail.com
 * Date:   2019.07.25 10:49
 */
public class RespPushListMessage extends PushMessage {

    private int mTotal;
    private List<PushMessageBean> mMessageBeanList;

    public RespPushListMessage() {
        super(MessageDirection.MOBILE_TO_GLASS, PushMessageType.GET_LIST);
    }

    public RespPushListMessage(int total, List<PushMessageBean> messageBeanList) {
        super(MessageDirection.MOBILE_TO_GLASS, PushMessageType.GET_LIST);
        this.mTotal = total;
        mMessageBeanList = messageBeanList;
    }

    public int getTotal() {
        return mTotal;
    }

    public void setTotal(int total) {
        this.mTotal = total;
    }

    public List<PushMessageBean> getMessageBeanList() {
        return mMessageBeanList;
    }

    public void setMessageBeanList(List<PushMessageBean> messageBeanList) {
        mMessageBeanList = messageBeanList;
    }

    @Override
    public String toString() {
        return "RespPushListMessage{" +
                "mTotal=" + mTotal +
                ", mMessageBeanList=" + mMessageBeanList +
                ", mSubType=" + mSubType +
                ", mId=" + mId +
                ", mMessageType=" + mMessageType +
                ", mDirection=" + mDirection +
                ", mResultCode=" + mResultCode +
                '}';
    }
}
