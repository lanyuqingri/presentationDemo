package com.rokid.glass.libbase.message.face;


import com.rokid.glass.libbase.message.dto.FaceInfoBean;
import com.rokid.glass.libbase.message.enums.MessageDirection;

/**
 * Author: Zack
 * Email:  newzzack@gmail.com
 * Date:   2019.07.10 17:11
 */
public class GetFaceImageMessage extends FaceMessage {

    /**
     * 人脸信息
     */
    protected FaceInfoBean mFaceInfo;


    public GetFaceImageMessage() {
        super(MessageDirection.MOBILE_TO_GLASS, FaceMessageType.GET_IMG);
    }

    public GetFaceImageMessage(FaceInfoBean faceInfo) {
        super(MessageDirection.MOBILE_TO_GLASS, FaceMessageType.GET_IMG);
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
        return "GetFaceImageMessage{" +
                "mFaceInfo=" + mFaceInfo +
                ", mId=" + mId +
                ", mMessageType=" + mMessageType +
                ", mResultCode=" + mResultCode +
                '}';
    }
}
