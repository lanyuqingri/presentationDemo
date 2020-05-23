package com.rokid.glass.libbase.message.file;


import com.rokid.glass.libbase.message.dto.FileInfoBean;
import com.rokid.glass.libbase.message.enums.MessageDirection;

public class GetFileDetailMessage extends FileMessage {

    protected FileInfoBean mFileInfo;

    public GetFileDetailMessage() {
        super(MessageDirection.MOBILE_TO_GLASS, FileMessageType.GET_FILE_DETAIL);
    }

    public GetFileDetailMessage(FileInfoBean FileInfo) {
        super(MessageDirection.MOBILE_TO_GLASS, FileMessageType.GET_FILE_DETAIL);
        this.mFileInfo = FileInfo;
    }

    public FileInfoBean getFileInfo() {
        return mFileInfo;
    }

    public void setFileInfo(FileInfoBean FileInfo) {
        this.mFileInfo = FileInfo;
    }

    @Override
    public String toString() {
        return "GetFileImageMessage{" +
                "mFileInfo=" + mFileInfo +
                ", mId=" + mId +
                ", mMessageType=" + mMessageType +
                ", mResultCode=" + mResultCode +
                '}';
    }
}
