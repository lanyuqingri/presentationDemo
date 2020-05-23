package com.rokid.glass.libbase.message.file;




import com.rokid.glass.libbase.message.dto.FileInfoBean;
import com.rokid.glass.libbase.message.enums.MessageDirection;

import java.util.List;

public class ReqDeleteFileMessage extends FileMessage {

    private List<FileInfoBean> mFileInfoList;

    public ReqDeleteFileMessage() {
        super(MessageDirection.MOBILE_TO_GLASS, FileMessageType.DELETE_FILE);
    }

    public ReqDeleteFileMessage(List<FileInfoBean> faceInfoList) {
        super(MessageDirection.MOBILE_TO_GLASS, FileMessageType.DELETE_FILE);
        mFileInfoList = faceInfoList;
    }

    public List<FileInfoBean> getFileInfoList() {
        return mFileInfoList;
    }

    public void setFileInfoList(List<FileInfoBean> faceInfoList) {
        mFileInfoList = faceInfoList;
    }

    @Override
    public String toString() {
        return "ReqDeleteFilesMessage{" +
                "mFileInfoList=" + mFileInfoList +
                ", mId=" + mId +
                ", mMessageType=" + mMessageType +
                ", mResultCode=" + mResultCode +
                '}';
    }
}
