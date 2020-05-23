package com.rokid.glass.libbase.message.file;


import com.rokid.glass.libbase.message.enums.MessageDirection;

public class RespDeleteFileMessage extends FileMessage {

    public RespDeleteFileMessage() {
        super(MessageDirection.GLASS_TO_MOBILE, FileMessageType.DELETE_FILE);
    }
}
