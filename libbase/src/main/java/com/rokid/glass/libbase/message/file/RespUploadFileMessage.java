package com.rokid.glass.libbase.message.file;


import com.rokid.glass.libbase.message.dto.FileInfoBean;
import com.rokid.glass.libbase.message.enums.MessageDirection;

public class RespUploadFileMessage extends FileMessage {

    private FileInfoBean mUploadFile;

    public RespUploadFileMessage() {
        super(MessageDirection.GLASS_TO_MOBILE, FileMessageType.UPLOAD_FILE);
    }

    public RespUploadFileMessage(FileInfoBean coverFile) {
        super(MessageDirection.GLASS_TO_MOBILE, FileMessageType.UPLOAD_FILE);
        mUploadFile = coverFile;
    }

    public FileInfoBean getCoverFile() {
        return mUploadFile;
    }

    public void setCoverFile(FileInfoBean coverFile) {
        mUploadFile = coverFile;
    }

    @Override
    public String toString() {
        return "RespAddFileMessage{" +
                "mUploadFile=" + mUploadFile +
                ", mId=" + mId +
                ", mMessageType=" + mMessageType +
                ", mDirection=" + mDirection +
                ", mResultCode=" + mResultCode +
                '}';
    }
}
