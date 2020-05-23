package com.rokid.glass.libbase.message.car;


import com.rokid.glass.libbase.message.dto.CarRecognizeInfoBean;
import com.rokid.glass.libbase.message.enums.MessageDirection;

/**
 * Author: Zack
 * Email:  newzzack@gmail.com
 * Date:   2019.09.20 10:53
 */
public class RespCarRecognizeMessage extends CarRecognizeMessage {

    private CarRecognizeInfoBean mCarRecognizeInfoBean;
    private int requestId;
    private int errorCode;
    private byte[] plateImage;
    private int imageWidth;
    private int imageHeight;

    public RespCarRecognizeMessage() {
        super(MessageDirection.MOBILE_TO_GLASS, CarRecognizeMessageType.CAR);
    }

    public RespCarRecognizeMessage(CarRecognizeInfoBean carRecognizeInfoBean) {
        super(MessageDirection.MOBILE_TO_GLASS, CarRecognizeMessageType.CAR);
        mCarRecognizeInfoBean = carRecognizeInfoBean;
    }

    public CarRecognizeInfoBean getCarRecognizeInfoBean() {
        return mCarRecognizeInfoBean;
    }

    public void setCarRecognizeInfoBean(CarRecognizeInfoBean carRecognizeInfoBean) {
        mCarRecognizeInfoBean = carRecognizeInfoBean;
    }

    @Override
    public String toString() {
        return "RespCarRecognizeMessage{" +
                "mCarRecognizeInfoBean=" + mCarRecognizeInfoBean +
                ", requestId=" + requestId +
                ", errorCode=" + errorCode +
                ", plateImage=" + (plateImage == null ? "null" : plateImage.length) +
                ", imageWidth=" + imageWidth +
                ", imageHeight=" + imageHeight +
                ", mId=" + mId +
                ", mMessageType=" + mMessageType +
                ", mDirection=" + mDirection +
                ", mResultCode=" + mResultCode +
                '}';
    }

    public byte[] getPlateImage() {
        return plateImage;
    }

    public void setPlateImage(byte[] plateImage) {
        this.plateImage = plateImage;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }
}
