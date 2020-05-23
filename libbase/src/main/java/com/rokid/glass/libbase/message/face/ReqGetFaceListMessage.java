package com.rokid.glass.libbase.message.face;


import com.rokid.glass.libbase.message.enums.MessageDirection;

/**
 * Author: Zack
 * Email:  newzzack@gmail.com
 * Date:   2019.07.10 17:11
 */
public class ReqGetFaceListMessage extends FaceMessage {

    private int offset;
    private int limit;

    public ReqGetFaceListMessage() {
        super(MessageDirection.MOBILE_TO_GLASS, FaceMessageType.GET_LIST);
    }

    public ReqGetFaceListMessage(int offset, int limit) {
        super(MessageDirection.MOBILE_TO_GLASS, FaceMessageType.GET_LIST);
        this.offset = offset;
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    @Override
    public String toString() {
        return "ReqGetFaceListMessage{" +
                "offset=" + offset +
                ", limit=" + limit +
                ", mId=" + mId +
                ", mMessageType=" + mMessageType +
                ", mDirection=" + mDirection +
                ", mResultCode=" + mResultCode +
                '}';
    }
}
