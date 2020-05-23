package com.rokid.glass.libbase.message.car;


import com.rokid.glass.libbase.message.dto.CarRecognizeInfoBean;

import java.util.List;

public class RespQueryPlateInfoMessage extends RespPlateInfoMessage {
    private List<CarRecognizeInfoBean> carRecognizeInfoBeanList;

    public RespQueryPlateInfoMessage() {
        super(PlateInfoType.QUERY_PLATE_INFO);
    }
    public List<CarRecognizeInfoBean> getCarRecognizeInfoBeanList() {
        return carRecognizeInfoBeanList;
    }

    public void setCarRecognizeInfoBeanList(List<CarRecognizeInfoBean> carRecognizeInfoBeanList) {
        this.carRecognizeInfoBeanList = carRecognizeInfoBeanList;
    }
}
