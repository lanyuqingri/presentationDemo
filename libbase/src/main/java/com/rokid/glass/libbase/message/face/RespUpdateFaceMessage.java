package com.rokid.glass.libbase.message.face;


import com.rokid.glass.libbase.message.enums.MessageDirection;

/**
 * Author: Zack
 * Email:  newzzack@gmail.com
 * Date:   2019.07.12 11:05
 */
public class RespUpdateFaceMessage extends FaceMessage {

    private String errorMessage;

    public RespUpdateFaceMessage() {
        super(MessageDirection.GLASS_TO_MOBILE, FaceMessageType.UPDATE);
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "RespUpdateFaceMessage{" +
                "errorMessage='" + errorMessage + '\'' +
                ", mId=" + mId +
                ", mMessageType=" + mMessageType +
                ", mDirection=" + mDirection +
                ", mResultCode=" + mResultCode +
                '}';
    }
}
