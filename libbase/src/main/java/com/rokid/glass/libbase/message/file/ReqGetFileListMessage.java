package com.rokid.glass.libbase.message.file;


import com.rokid.glass.libbase.message.enums.MessageDirection;

public class ReqGetFileListMessage extends FileMessage {

    private int offset;
    private int limit;

    public ReqGetFileListMessage() {
        super(MessageDirection.MOBILE_TO_GLASS, FileMessageType.GET_LIST);
    }

    public ReqGetFileListMessage(int offset, int limit) {
        super(MessageDirection.MOBILE_TO_GLASS, FileMessageType.GET_LIST);
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
        return "ReqGetFileListMessage{" +
                "limit=" + limit +
                ", mId=" + mId +
                ", mMessageType=" + mMessageType +
                ", mDirection=" + mDirection +
                ", mResultCode=" + mResultCode +
                '}';
    }
}
