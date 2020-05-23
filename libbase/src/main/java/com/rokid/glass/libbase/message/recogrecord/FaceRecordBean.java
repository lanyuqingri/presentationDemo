package com.rokid.glass.libbase.message.recogrecord;


import com.rokid.glass.libbase.message.dto.FaceInfoBean;

/**
 * Author: Zack
 * Email:  newzzack@gmail.com
 * Date:   2019.09.23 19:57
 */
public class FaceRecordBean {

    private FaceInfoBean mFaceInfoBean;

    private byte[] mRawImage;

    private boolean isSingleFace;

    public FaceRecordBean() {
    }

    public FaceRecordBean(FaceInfoBean faceInfoBean, byte[] rawImage, boolean isSingleFace) {
        mFaceInfoBean = faceInfoBean;
        mRawImage = rawImage;
        this.isSingleFace = isSingleFace;
    }

    public FaceInfoBean getFaceInfoBean() {
        return mFaceInfoBean;
    }

    public void setFaceInfoBean(FaceInfoBean faceInfoBean) {
        mFaceInfoBean = faceInfoBean;
    }

    public byte[] getRawImage() {
        return mRawImage;
    }

    public void setRawImage(byte[] rawImage) {
        mRawImage = rawImage;
    }

    public boolean getIsSingleFace() {
        return isSingleFace;
    }

    public void setIsSingleFace(boolean isSingleFace) {
        this.isSingleFace = isSingleFace;
    }

    @Override
    public String toString() {
        return "FaceRecordBean{" +
                "mFaceInfoBean=" + mFaceInfoBean +
                ", mRawImage=" + (mRawImage == null ? 0 : mRawImage.length) +
                ", isSingleFace=" + isSingleFace +
                '}';
    }
}
