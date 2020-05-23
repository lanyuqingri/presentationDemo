package com.rokid.glass.libbase.message;


import com.rokid.glass.libbase.message.enums.MessageDirection;
import com.rokid.glass.libbase.message.enums.MessageType;
import com.rokid.glass.libbase.message.enums.ResultCode;

/**
 * Author: Zack
 * Email:  newzzack@gmail.com
 * Date:   2019.07.10 15:46
 */
public abstract class MessageImpl implements IMessage {

    /**
     * 消息id
     */
    protected int mId;

    /**
     * 消息类型
     */
    protected MessageType mMessageType;

    /**
     *
     */
    protected MessageDirection mDirection;


    /**
     * 返回码
     */
    protected ResultCode mResultCode = ResultCode.OK;

    public MessageImpl() {
    }

    public MessageImpl(MessageType messageType, MessageDirection direction) {
        mMessageType = messageType;
        mDirection = direction;
    }

    public MessageImpl(int id, MessageType messageType, MessageDirection direction) {
        mId = id;
        mMessageType = messageType;
        mDirection = direction;
    }

    public MessageImpl(int id, MessageType messageType, ResultCode resultCode) {
        mId = id;
        mMessageType = messageType;
        mResultCode = resultCode;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public MessageType getMessageType() {
        return mMessageType;
    }

    public void setMessageType(MessageType messageType) {
        this.mMessageType = messageType;
    }

    public MessageDirection getDirection() {
        return mDirection;
    }

    public void setDirection(MessageDirection direction) {
        mDirection = direction;
    }

    public ResultCode getResultCode() {
        return mResultCode;
    }

    public MessageImpl setResultCode(ResultCode resultCode) {
        mResultCode = resultCode;
        return this;
    }

    public boolean isResultOK() {
        return mResultCode == ResultCode.OK;
    }

}
