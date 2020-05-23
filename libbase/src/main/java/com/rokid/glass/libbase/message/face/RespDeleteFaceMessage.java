package com.rokid.glass.libbase.message.face;


import com.rokid.glass.libbase.message.enums.MessageDirection;

/**
 * Author: Zack
 * Email:  newzzack@gmail.com
 * Date:   2019.07.12 11:05
 */
public class RespDeleteFaceMessage extends FaceMessage {


    public RespDeleteFaceMessage() {
        super(MessageDirection.GLASS_TO_MOBILE, FaceMessageType.DELETE_FACES);
    }
}
