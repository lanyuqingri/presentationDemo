package com.rokid.glass.libbase.message.file;


import com.rokid.glass.libbase.message.dto.FileInfoBean;
import com.rokid.glass.libbase.message.enums.MessageDirection;

public class GetFileThumbMessage extends FileMessage {

    protected FileInfoBean mFileInfo;

    public GetFileThumbMessage() {
        super(MessageDirection.MOBILE_TO_GLASS, FileMessageType.GET_FILE_THUMB);
    }

    public GetFileThumbMessage(FileInfoBean FileInfo) {
        super(MessageDirection.MOBILE_TO_GLASS, FileMessageType.GET_FILE_THUMB);
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
