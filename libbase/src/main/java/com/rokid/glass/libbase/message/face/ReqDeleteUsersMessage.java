package com.rokid.glass.libbase.message.face;


import com.rokid.glass.libbase.message.dto.FaceInfoBean;
import com.rokid.glass.libbase.message.enums.MessageDirection;

import java.util.List;

/**
 * Author: Zack
 * Email:  newzzack@gmail.com
 * Date:   2019.07.12 11:05
 */
public class ReqDeleteUsersMessage extends FaceMessage {

    private List<FaceInfoBean> mFaceInfoList;

    public ReqDeleteUsersMessage() {
        super(MessageDirection.MOBILE_TO_GLASS, FaceMessageType.DELETE_USERS);
    }

    public ReqDeleteUsersMessage(List<FaceInfoBean> faceInfoList) {
        super(MessageDirection.MOBILE_TO_GLASS, FaceMessageType.DELETE_USERS);
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
        return "ReqDeleteUsersMessage{" +
                "mFaceInfoList=" + mFaceInfoList +
                ", mId=" + mId +
                ", mMessageType=" + mMessageType +
                ", mResultCode=" + mResultCode +
                '}';
    }
}
