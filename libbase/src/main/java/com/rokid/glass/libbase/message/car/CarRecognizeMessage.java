package com.rokid.glass.libbase.message.car;


import com.rokid.glass.libbase.message.MessageImpl;
import com.rokid.glass.libbase.message.enums.MessageDirection;
import com.rokid.glass.libbase.message.enums.MessageType;

/**
 * Author: Zack
 * Email:  newzzack@gmail.com
 * Date:   2019.09.20 10:27
 */
public class CarRecognizeMessage extends MessageImpl {

    private CarRecognizeMessageType mSubType;

    public CarRecognizeMessageType getSubType() {
        return mSubType;
    }

    public void setSubType(CarRecognizeMessageType subType) {
        mSubType = subType;
    }

    public CarRecognizeMessage() {
    }

    public CarRecognizeMessage(MessageDirection direction, CarRecognizeMessageType subType) {
        super(MessageType.CAR, direction);
        mSubType = subType;
    }

    @Override
    public String toString() {
        return "CarRecognizeMessage{" +
                "mSubType=" + mSubType +
                ", mId=" + mId +
                ", mMessageType=" + mMessageType +
                ", mDirection=" + mDirection +
                ", mResultCode=" + mResultCode +
                '}';
    }
}
