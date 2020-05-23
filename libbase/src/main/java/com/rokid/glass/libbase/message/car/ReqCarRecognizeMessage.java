package com.rokid.glass.libbase.message.car;


import com.rokid.glass.libbase.message.enums.MessageDirection;

import java.util.Arrays;

/**
 * Author: Zack
 * Email:  newzzack@gmail.com
 * Date:   2019.09.20 11:01
 */
public class ReqCarRecognizeMessage extends CarRecognizeMessage {

    private String plate;
    private byte[] plateImage;
    private int width;
    private int height;
    private int requestId;

    public ReqCarRecognizeMessage() {
        super(MessageDirection.GLASS_TO_MOBILE, CarRecognizeMessageType.CAR);
    }

    public ReqCarRecognizeMessage(String plate, byte[] plateImage) {
        super(MessageDirection.GLASS_TO_MOBILE, CarRecognizeMessageType.CAR);
        this.plate = plate;
        this.plateImage = plateImage;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public byte[] getPlateImage() {
        return plateImage;
    }

    public void setPlateImage(byte[] plateImage) {
        this.plateImage = plateImage;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    @Override
    public String toString() {
        return "ReqCarRecognizeMessage{" +
                "plate='" + plate + '\'' +
                ", plateImage=" + Arrays.toString(plateImage) +
                ", width=" + width +
                ", height=" + height +
                ", requestId=" + requestId +
                ", mId=" + mId +
                ", mMessageType=" + mMessageType +
                ", mDirection=" + mDirection +
                ", mResultCode=" + mResultCode +
                '}';
    }
}
