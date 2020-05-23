package com.rokid.glass.libbase.message.push;


import com.rokid.glass.libbase.message.dto.FaceInfoBean;
import com.rokid.glass.libbase.message.enums.MessageDirection;

/**
 * 推送的人脸消息，需要眼镜端添加特征再做识别
 * Author: Zack
 * Email:  newzzack@gmail.com
 * Date:   2019.08.28 11:05
 */
public class ReqFaceMessage extends PushMessage {

    private FaceInfoBean mFaceInfoBean;

    public ReqFaceMessage() {
        super(MessageDirection.MOBILE_TO_GLASS, PushMessageType.FACE);
    }

    public ReqFaceMessage(FaceInfoBean faceInfoBean) {
        super(MessageDirection.MOBILE_TO_GLASS, PushMessageType.FACE);
        mFaceInfoBean = faceInfoBean;
    }

    @Override
    public String toString() {
        return "ReqFaceMessage{" +
                "mFaceInfoBean=" + mFaceInfoBean +
                ", mSubType=" + mSubType +
                ", mId=" + mId +
                ", mMessageType=" + mMessageType +
                ", mDirection=" + mDirection +
                ", mResultCode=" + mResultCode +
                '}';
    }
}
