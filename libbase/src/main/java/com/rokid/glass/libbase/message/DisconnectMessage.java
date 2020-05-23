package com.rokid.glass.libbase.message;


import com.rokid.glass.libbase.message.enums.MessageDirection;
import com.rokid.glass.libbase.message.enums.MessageType;

public class DisconnectMessage extends MessageImpl {

    public DisconnectMessage() {
        super(MessageType.DISCONNECT, MessageDirection.GLASS_TO_MOBILE);
    }

}
