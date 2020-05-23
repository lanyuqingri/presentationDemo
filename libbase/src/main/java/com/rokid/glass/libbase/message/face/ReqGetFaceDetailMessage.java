package com.rokid.glass.libbase.message.face;


import com.rokid.glass.libbase.message.dto.FaceInfoBean;
import com.rokid.glass.libbase.message.enums.MessageDirection;

/**
 * Author: Zack
 * Email:  newzzack@gmail.com
 * Date:   2019.07.10 17:11
 */
public class ReqGetFaceDetailMessage extends FaceMessage {

    /**
     * 人脸信息
     */
    protected FaceInfoBean mFaceInfo;


    public ReqGetFaceDetailMessage() {
        super(MessageDirection.MOBILE_TO_GLASS, FaceMessageType.GET_FACE_DETAIL);
    }

    public ReqGetFaceDetailMessage(FaceInfoBean faceInfo) {
        super(MessageDirection.MOBILE_TO_GLASS, FaceMessageType.GET_FACE_DETAIL);
        this.mFaceInfo = faceInfo;
    }

    public FaceInfoBean getFaceInfo() {
        return mFaceInfo;
    }

    public void setFaceInfo(FaceInfoBean faceInfo) {
        this.mFaceInfo = faceInfo;
    }

    @Override
    public String toString() {
        return "ReqGetFaceDetailMessage{" +
                "mFaceInfo=" + mFaceInfo +
                ", mId=" + mId +
                ", mMessageType=" + mMessageType +
                ", mResultCode=" + mResultCode +
                '}';
    }
}
