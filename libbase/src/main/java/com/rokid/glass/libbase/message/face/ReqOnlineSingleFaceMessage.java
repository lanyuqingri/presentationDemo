package com.rokid.glass.libbase.message.face;


import com.rokid.glass.libbase.message.enums.MessageDirection;

/**
 * Author: Zack
 * Email:  newzzack@gmail.com
 * Date:   2019.08.30 15:55
 */
public class ReqOnlineSingleFaceMessage extends FaceMessage {

    private int mTrackId;
    private byte[] mFaceImage;
    private int width;
    private int height;

    public ReqOnlineSingleFaceMessage() {
        super(MessageDirection.GLASS_TO_MOBILE, FaceMessageType.SINGLE_FACE_RECOGNIZE);
    }

    public ReqOnlineSingleFaceMessage(int trackId, byte[] faceImage) {
        super(MessageDirection.GLASS_TO_MOBILE, FaceMessageType.SINGLE_FACE_RECOGNIZE);
        mTrackId = trackId;
        mFaceImage = faceImage;
    }

    public int getTrackId() {
        return mTrackId;
    }

    public void setTrackId(int trackId) {
        mTrackId = trackId;
    }

    public byte[] getFaceImage() {
        return mFaceImage;
    }

    public void setFaceImage(byte[] faceImage) {
        mFaceImage = faceImage;
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

    @Override
    public String toString() {
        return "ReqOnlineSingleFaceMessage{" +
                "mTrackId=" + mTrackId +
                ", mFaceImage=" + (mFaceImage == null ? 0 : mFaceImage.length) +
                ", width=" + width +
                ", height=" + height +
                ", mId=" + mId +
                ", mMessageType=" + mMessageType +
                ", mDirection=" + mDirection +
                ", mResultCode=" + mResultCode +
                '}';
    }
}
