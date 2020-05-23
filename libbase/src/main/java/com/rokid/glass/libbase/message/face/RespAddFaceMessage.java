package com.rokid.glass.libbase.message.face;


import com.rokid.glass.libbase.message.dto.FaceInfoBean;
import com.rokid.glass.libbase.message.enums.MessageDirection;

/**
 * Author: Zack
 * Email:  newzzack@gmail.com
 * Date:   2019.07.12 11:05
 */
public class RespAddFaceMessage extends FaceMessage {

    private FaceInfoBean mCoverFace;

    private String errorMessage;

    public RespAddFaceMessage() {
        super(MessageDirection.GLASS_TO_MOBILE, FaceMessageType.ADD);
    }

    public RespAddFaceMessage(FaceInfoBean coverFace) {
        super(MessageDirection.GLASS_TO_MOBILE, FaceMessageType.ADD);
        mCoverFace = coverFace;
    }

    public FaceInfoBean getCoverFace() {
        return mCoverFace;
    }

    public void setCoverFace(FaceInfoBean coverFace) {
        mCoverFace = coverFace;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "RespAddFaceMessage{" +
                "mCoverFace=" + mCoverFace +
                ", mId=" + mId +
                ", mMessageType=" + mMessageType +
                ", mDirection=" + mDirection +
                ", mResultCode=" + mResultCode +
                '}';
    }
}
