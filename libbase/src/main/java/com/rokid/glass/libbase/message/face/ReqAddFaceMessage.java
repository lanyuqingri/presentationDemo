package com.rokid.glass.libbase.message.face;


import com.rokid.glass.libbase.message.dto.FaceInfoBean;
import com.rokid.glass.libbase.message.enums.MessageDirection;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Zack
 * Email:  newzzack@gmail.com
 * Date:   2019.07.12 11:05
 */
public class ReqAddFaceMessage extends FaceMessage {

    public enum AddState {
        NORMAL,
        START,
        END
    }

    private List<FaceInfoBean> mFaceInfoList;

    private AddState mState = AddState.NORMAL;

    public ReqAddFaceMessage() {
        super(MessageDirection.MOBILE_TO_GLASS, FaceMessageType.ADD);
    }

    public ReqAddFaceMessage(FaceInfoBean faceInfo) {
        super(MessageDirection.MOBILE_TO_GLASS, FaceMessageType.ADD);
        List<FaceInfoBean> faceInfoBeanList = new ArrayList<>();
        faceInfoBeanList.add(faceInfo);
        mFaceInfoList = faceInfoBeanList;
    }

    public ReqAddFaceMessage(List<FaceInfoBean> faceInfoList, AddState state) {
        this(faceInfoList);
        mState = state;
    }

    public ReqAddFaceMessage(List<FaceInfoBean> faceInfoList) {
        super(MessageDirection.MOBILE_TO_GLASS, FaceMessageType.ADD);
        mFaceInfoList = faceInfoList;
    }

    public List<FaceInfoBean> getFaceInfoList() {
        return mFaceInfoList;
    }

    public void setFaceInfoList(List<FaceInfoBean> faceInfoList) {
        mFaceInfoList = faceInfoList;
    }

    public AddState getState() {
        return mState;
    }

    public void setState(AddState state) {
        mState = state;
    }

    @Override
    public String toString() {
        return "ReqAddFaceMessage{" +
                "mFaceInfoList=" + mFaceInfoList +
                ", mState=" + mState +
                ", mId=" + mId +
                ", mMessageType=" + mMessageType +
                ", mDirection=" + mDirection +
                ", mResultCode=" + mResultCode +
                '}';
    }


}
