package com.rokid.glass.libbase.message.push;


import com.rokid.glass.libbase.message.enums.MessageDirection;

/**
 * Author: Zack
 * Email:  newzzack@gmail.com
 * Date:   2019.07.25 10:44
 */
public class ReqPushListMessage extends PushMessage {


    private int mOffset;
    private int mLimit;

    public ReqPushListMessage() {
        super(MessageDirection.GLASS_TO_MOBILE, PushMessageType.GET_LIST);
    }

    public ReqPushListMessage(int offset, int limit) {
        super(MessageDirection.GLASS_TO_MOBILE, PushMessageType.GET_LIST);
        this.mOffset = offset;
        this.mLimit = limit;
    }

    public int getOffset() {
        return mOffset;
    }

    public void setOffset(int offset) {
        this.mOffset = offset;
    }

    public int getLimit() {
        return mLimit;
    }

    public void setLimit(int limit) {
        this.mLimit = limit;
    }

    @Override
    public String toString() {
        return "ReqPushListMessage{" +
                "mOffset=" + mOffset +
                ", mLimit=" + mLimit +
                ", mSubType=" + mSubType +
                ", mId=" + mId +
                ", mMessageType=" + mMessageType +
                ", mDirection=" + mDirection +
                ", mResultCode=" + mResultCode +
                '}';
    }
}
