package com.rokid.glass.libbase.message.car;


import com.rokid.glass.libbase.message.dto.CarRecognizeInfoBean;

public class ReqAddPlateInfoMessage extends ReqPlateInfoMessage {
    private CarRecognizeInfoBean carRecognizeInfoBean;

    public ReqAddPlateInfoMessage(){
        super(PlateInfoType.ADD_PLATE_INFO);
    }

    public ReqAddPlateInfoMessage(CarRecognizeInfoBean carBean){
        super(PlateInfoType.ADD_PLATE_INFO);
        carRecognizeInfoBean = carBean;
    }

    public CarRecognizeInfoBean getCarRecognizeInfoBean() {
        return carRecognizeInfoBean;
    }

    public void setCarRecognizeInfoBean(CarRecognizeInfoBean carBean) {
        this.carRecognizeInfoBean = carBean;
    }
}
