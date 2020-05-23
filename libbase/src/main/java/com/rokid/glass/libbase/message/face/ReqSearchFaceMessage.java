package com.rokid.glass.libbase.message.face;


import com.rokid.glass.libbase.message.dto.FaceInfoBean;
import com.rokid.glass.libbase.message.enums.MessageDirection;

/**
 * Author: Zack
 * Email:  newzzack@gmail.com
 * Date:   2019.07.10 17:11
 */
public class ReqSearchFaceMessage extends FaceMessage {

    private int offset;
    private int limit;

    private FaceInfoBean mSearchFaceInfo;

    public ReqSearchFaceMessage() {
        super(MessageDirection.MOBILE_TO_GLASS, FaceMessageType.SEARCH);
    }

    public ReqSearchFaceMessage(int offset, int limit) {
        super(MessageDirection.MOBILE_TO_GLASS, FaceMessageType.SEARCH);
        this.offset = offset;
        this.limit = limit;
    }

    public ReqSearchFaceMessage(int offset, int limit, FaceInfoBean searchFaceInfo) {
        this();
        this.offset = offset;
        this.limit = limit;
        mSearchFaceInfo = searchFaceInfo;
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

    public FaceInfoBean getSearchFaceInfo() {
        return mSearchFaceInfo;
    }

    public void setSearchFaceInfo(FaceInfoBean searchFaceInfo) {
        mSearchFaceInfo = searchFaceInfo;
    }

    @Override
    public String toString() {
        return "ReqSearchFaceMessage{" +
                "offset=" + offset +
                ", limit=" + limit +
                ", mSearchFaceInfo=" + mSearchFaceInfo +
                ", mId=" + mId +
                ", mMessageType=" + mMessageType +
                ", mDirection=" + mDirection +
                ", mResultCode=" + mResultCode +
                '}';
    }
}
