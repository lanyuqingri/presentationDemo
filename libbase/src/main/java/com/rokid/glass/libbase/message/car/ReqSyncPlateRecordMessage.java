package com.rokid.glass.libbase.message.car;


import com.rokid.glass.libbase.message.dto.CarRecognizeInfoBean;
import com.rokid.glass.libbase.message.enums.MessageDirection;

public class ReqSyncPlateRecordMessage extends PlateInfoMessage {
    private byte[] plateImg;
    private int imageWidth;
    private int imageHeight;
    private CarRecognizeInfoBean carBean;

    public ReqSyncPlateRecordMessage(){
        super(PlateInfoType.SYNC_PLATE_INFO, MessageDirection.GLASS_TO_MOBILE);
    }

    public byte[] getPlateImg() {
        return plateImg;
    }

    public void setPlateImg(byte[] plateImg) {
        this.plateImg = plateImg;
    }

    public CarRecognizeInfoBean getCarBean() {
        return carBean;
    }

    public void setCarBean(CarRecognizeInfoBean carBean) {
        this.carBean = carBean;
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
