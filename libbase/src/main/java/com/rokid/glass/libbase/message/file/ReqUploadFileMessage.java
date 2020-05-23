package com.rokid.glass.libbase.message.file;




import com.rokid.glass.libbase.message.dto.FileInfoBean;
import com.rokid.glass.libbase.message.enums.MessageDirection;

import java.util.ArrayList;
import java.util.List;

public class ReqUploadFileMessage extends FileMessage {

    public enum AddState {
        NORMAL,
        START,
        END
    }

    private List<FileInfoBean> mFileInfoList;

    private AddState mState = AddState.NORMAL;

    public ReqUploadFileMessage() {
        super(MessageDirection.MOBILE_TO_GLASS, FileMessageType.UPLOAD_FILE);
    }

    public ReqUploadFileMessage(FileInfoBean faceInfo) {
        super(MessageDirection.MOBILE_TO_GLASS, FileMessageType.UPLOAD_FILE);
        List<FileInfoBean> faceInfoBeanList = new ArrayList<>();
        faceInfoBeanList.add(faceInfo);
        mFileInfoList = faceInfoBeanList;
    }

    public ReqUploadFileMessage(List<FileInfoBean> faceInfoList, AddState state) {
        this(faceInfoList);
        mState = state;
    }

    public ReqUploadFileMessage(List<FileInfoBean> faceInfoList) {
        super(MessageDirection.MOBILE_TO_GLASS, FileMessageType.UPLOAD_FILE);
        mFileInfoList = faceInfoList;
    }

    public List<FileInfoBean> getFileInfoList() {
        return mFileInfoList;
    }

    public void setFileInfoList(List<FileInfoBean> faceInfoList) {
        mFileInfoList = faceInfoList;
    }

    public AddState getState() {
        return mState;
    }

    public void setState(AddState state) {
        mState = state;
    }

    @Override
    public String toString() {
        return "ReqAddFileMessage{" +
                "mFileInfoList=" + mFileInfoList +
                ", mState=" + mState +
                ", mId=" + mId +
                ", mMessageType=" + mMessageType +
                ", mDirection=" + mDirection +
                ", mResultCode=" + mResultCode +
                '}';
    }


}
