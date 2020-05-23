package com.rokid.glass.libbase.message.file;



import com.rokid.glass.libbase.message.dto.FileInfoBean;
import com.rokid.glass.libbase.message.enums.MessageDirection;

import java.util.List;

public class RespGetFileListMessage extends FileMessage {

    private List<FileInfoBean> mFileInfoList;

    public RespGetFileListMessage() {
        super(MessageDirection.GLASS_TO_MOBILE, FileMessageType.GET_LIST);
    }

    public RespGetFileListMessage(List<FileInfoBean> faceInfoList) {
        super(MessageDirection.GLASS_TO_MOBILE, FileMessageType.GET_LIST);
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
        return "RespFileListMessage{" +
                "mFileInfoList=" + mFileInfoList +
                ", mId=" + mId +
                ", mMessageType=" + mMessageType +
                '}';
    }
}
