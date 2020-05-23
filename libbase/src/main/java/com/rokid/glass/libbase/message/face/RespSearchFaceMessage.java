package com.rokid.glass.libbase.message.face;



import com.rokid.glass.libbase.message.dto.FaceInfoBean;
import com.rokid.glass.libbase.message.enums.MessageDirection;

import java.util.List;

/**
 * Author: Zack
 * Email:  newzzack@gmail.com
 * Date:   2019.07.10 17:11
 */
public class RespSearchFaceMessage extends FaceMessage {

    private List<FaceInfoBean> mFaceInfoList;

    /**
     * 人脸信息总数
     */
    protected int mTotalCount;

    public RespSearchFaceMessage() {
        super(MessageDirection.GLASS_TO_MOBILE, FaceMessageType.SEARCH);
    }

    public RespSearchFaceMessage(int totalCount, List<FaceInfoBean> faceInfoList) {
        super(MessageDirection.GLASS_TO_MOBILE, FaceMessageType.SEARCH);
        mTotalCount = totalCount;
        mFaceInfoList = faceInfoList;
    }

    public int getTotalCount() {
        return mTotalCount;
    }

    public void setTotalCount(int totalCount) {
        mTotalCount = totalCount;
    }

    public List<FaceInfoBean> getFaceInfoList() {
        return mFaceInfoList;
    }

    public void setFaceInfoList(List<FaceInfoBean> faceInfoList) {
        mFaceInfoList = faceInfoList;
    }

    @Override
    public String toString() {
        return "RespSearchFaceMessage{" +
                "mFaceInfoList=" + mFaceInfoList +
                ", mTotalCount=" + mTotalCount +
                ", mId=" + mId +
                ", mMessageType=" + mMessageType +
                '}';
    }
}
