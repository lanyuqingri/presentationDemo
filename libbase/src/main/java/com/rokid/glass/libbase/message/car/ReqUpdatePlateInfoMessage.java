package com.rokid.glass.libbase.message.car;


import com.rokid.glass.libbase.message.dto.CarRecognizeInfoBean;

public class ReqUpdatePlateInfoMessage extends ReqPlateInfoMessage {
    private CarRecognizeInfoBean carRecognizeInfoBean;

    public ReqUpdatePlateInfoMessage(){
        super(PlateInfoType.UPDATE_PLATE_INFO);
    }

    public ReqUpdatePlateInfoMessage(CarRecognizeInfoBean carBean){
        super(PlateInfoType.UPDATE_PLATE_INFO);
        carRecognizeInfoBean = carBean;
    }

    public CarRecognizeInfoBean getCarRecognizeInfoBean() {
        return carRecognizeInfoBean;
    }

    public void setCarRecognizeInfoBean(CarRecognizeInfoBean carBean) {
        this.carRecognizeInfoBean = carBean;
    }
}
