package com.rokid.glass.libbase.message.recogrecord;


import com.rokid.glass.libbase.message.dto.CarRecognizeInfoBean;

/**
 * Author: Zack
 * Email:  newzzack@gmail.com
 * Date:   2019.09.23 17:27
 */
public class CarRecordBean {

    private CarRecognizeInfoBean mCarRecognizeInfoBean;
    
    private byte[] rawImage;

    private byte[] resultImage;

    public CarRecognizeInfoBean getCarRecognizeInfoBean() {
        return mCarRecognizeInfoBean;
    }

    public void setCarRecognizeInfoBean(CarRecognizeInfoBean carRecognizeInfoBean) {
        mCarRecognizeInfoBean = carRecognizeInfoBean;
    }

    public byte[] getRawImage() {
        return rawImage;
    }

    public void setRawImage(byte[] rawImage) {
        this.rawImage = rawImage;
    }

    public byte[] getResultImage() {
        return resultImage;
    }

    public void setResultImage(byte[] resultImage) {
        this.resultImage = resultImage;
    }

    @Override
    public String toString() {
        return "CarRecordBean{" +
                "mCarRecognizeInfoBean=" + mCarRecognizeInfoBean +
                ", rawImage=" + (rawImage == null ? 0 : rawImage.length) +
                ", resultImage=" + (resultImage == null ? 0 : resultImage.length) +
                '}';
    }
}
