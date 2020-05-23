package com.rokid.glass.libbase.message.face;


import com.rokid.glass.libbase.message.enums.MessageDirection;

/**
 * Author: Zack
 * Email:  newzzack@gmail.com
 * Date:   2019.07.12 11:05
 */
public class RespDeleteUsersMessage extends FaceMessage {


    public RespDeleteUsersMessage() {
        super(MessageDirection.GLASS_TO_MOBILE, FaceMessageType.DELETE_USERS);
    }
}
