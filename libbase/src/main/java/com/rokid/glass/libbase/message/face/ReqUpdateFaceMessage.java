package com.rokid.glass.libbase.message.face;



import com.rokid.glass.libbase.message.dto.FaceInfoBean;
import com.rokid.glass.libbase.message.enums.MessageDirection;

import java.util.List;

/**
 * Author: Zack
 * Email:  newzzack@gmail.com
 * Date:   2019.07.12 11:05
 */
public class ReqUpdateFaceMessage extends FaceMessage {

    /**
     * 人脸信息，这个字段始终都会传，保证非空
     */
    private FaceInfoBean mFaceInfo;

    /**
     * 封面fid
     */
    private String mCoverFid;
    /**
     * 新增的人脸列表
     */
    private List<FaceInfoBean> mAddList;
    /**
     * 删除的人脸列表
     */
    private List<FaceInfoBean> mDeleteList;

    public ReqUpdateFaceMessage() {
        super(MessageDirection.MOBILE_TO_GLASS, FaceMessageType.UPDATE);
    }

    public ReqUpdateFaceMessage(FaceInfoBean faceInfo, String coverFid, List<FaceInfoBean> addList, List<FaceInfoBean> deleteList) {
        super(MessageDirection.MOBILE_TO_GLASS, FaceMessageType.UPDATE);
        mFaceInfo = faceInfo;
        mCoverFid = coverFid;
        mAddList = addList;
        mDeleteList = deleteList;
    }

    public List<FaceInfoBean> getAddList() {
        return mAddList;
    }

    public void setAddList(List<FaceInfoBean> addList) {
        mAddList = addList;
    }

    public List<FaceInfoBean> getDeleteList() {
        return mDeleteList;
    }

    public void setDeleteList(List<FaceInfoBean> deleteList) {
        mDeleteList = deleteList;
    }

    public FaceInfoBean getFaceInfo() {
        return mFaceInfo;
    }

    public void setFaceInfo(FaceInfoBean faceInfo) {
        mFaceInfo = faceInfo;
    }

    public String getCoverFid() {
        return mCoverFid;
    }

    public void setCoverFid(String coverFid) {
        mCoverFid = coverFid;
    }

    @Override
    public String toString() {
        return "ReqUpdateFaceMessage{" +
                "mFaceInfo=" + mFaceInfo +
                ", mCoverFid='" + mCoverFid + '\'' +
                ", mAddList=" + mAddList +
                ", mDeleteList=" + mDeleteList +
                ", mId=" + mId +
                ", mMessageType=" + mMessageType +
                ", mDirection=" + mDirection +
                ", mResultCode=" + mResultCode +
                '}';
    }
}
