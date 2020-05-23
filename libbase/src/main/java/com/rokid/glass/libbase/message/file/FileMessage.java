package com.rokid.glass.libbase.message.file;


import com.rokid.glass.libbase.message.MessageImpl;
import com.rokid.glass.libbase.message.enums.MessageDirection;
import com.rokid.glass.libbase.message.enums.MessageType;

public class FileMessage extends MessageImpl {

    /**
     * {@link FileMessageType}
     */
    private FileMessageType mSubType;

    public FileMessage() {
    }

    public FileMessage(MessageDirection direction, FileMessageType subType) {
        super(MessageType.FILE, direction);
        mSubType = subType;
    }

    public FileMessageType getSubType() {
        return mSubType;
    }

    public void setSubType(FileMessageType subType) {
        mSubType = subType;
    }

    @Override
    public String toString() {
        return "FileMessage{" +
                "subType=" + mSubType +
                ", mId=" + mId +
                ", mMessageType=" + mMessageType +
                ", mDirection=" + mDirection +
                ", mResultCode=" + mResultCode +
                '}';
    }
}
