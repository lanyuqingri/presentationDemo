package com.rokid.glass.libbase.message.face;



import com.rokid.glass.libbase.message.dto.FaceInfoBean;
import com.rokid.glass.libbase.message.enums.MessageDirection;

import java.util.List;

/**
 * Author: Zack
 * Email:  newzzack@gmail.com
 * Date:   2019.07.12 11:05
 */
public class RespGetFaceDetailMessage extends FaceMessage {


    private List<FaceInfoBean> mFaceInfoList;

    public RespGetFaceDetailMessage() {
        super(MessageDirection.GLASS_TO_MOBILE, FaceMessageType.GET_FACE_DETAIL);
    }

    public RespGetFaceDetailMessage(List<FaceInfoBean> faceInfoList) {
        super(MessageDirection.GLASS_TO_MOBILE, FaceMessageType.GET_FACE_DETAIL);
        mFaceInfoList = faceInfoList;
    }

    public List<FaceInfoBean> getFaceInfoList() {
        return mFaceInfoList;
    }

    public void setFaceInfoList(List<FaceInfoBean> faceInfoList) {
        mFaceInfoList = faceInfoList;
    }

    @Override
    public String toString() {
        return "RespGetFaceDetailMessage{" +
                "mFaceInfoList=" + mFaceInfoList +
                ", mId=" + mId +
                ", mMessageType=" + mMessageType +
                ", mDirection=" + mDirection +
                ", mResultCode=" + mResultCode +
                '}';
    }
}
