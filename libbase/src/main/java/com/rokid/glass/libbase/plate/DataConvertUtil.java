package com.rokid.glass.libbase.plate;

import android.text.TextUtils;


import com.rokid.glass.libbase.message.dto.CarRecognizeInfoBean;

import java.util.ArrayList;
import java.util.List;

public class DataConvertUtil {

    public static PlateInfo convertCarBeanToPlateInfo(CarRecognizeInfoBean carBean){
        if(carBean == null || TextUtils.isEmpty(carBean.getPlate())){
            return null;
        }
        PlateInfo plateInfo = new PlateInfo();
        plateInfo.setPlate(carBean.getPlate());
        plateInfo.setTag(carBean.getTag());
        plateInfo.setOwner(carBean.getOwner());
        plateInfo.setStatus(carBean.getStatus());
        plateInfo.setPhoneNum(carBean.getPhoneNum());
        plateInfo.setIdcard(carBean.getIdcard());
        plateInfo.setBrand(carBean.getBrand());
        plateInfo.setColor(carBean.getColor());
        plateInfo.setDate(carBean.getDate());
        plateInfo.setAddress(carBean.getAddress());
        return plateInfo;
    }

    public static CarRecognizeInfoBean convertPlateInfoToCarBean(PlateInfo plateInfo){
        if(plateInfo == null || TextUtils.isEmpty(plateInfo.getPlate())){
            return null;
        }
        CarRecognizeInfoBean carBean = new CarRecognizeInfoBean();
        carBean.setPlate(plateInfo.getPlate());
        carBean.setTag(plateInfo.getTag());
        carBean.setOwner(plateInfo.getOwner());
        carBean.setStatus(plateInfo.getStatus());
        carBean.setPhoneNum(plateInfo.getPhoneNum());
        carBean.setIdcard(plateInfo.getIdcard());
        carBean.setBrand(plateInfo.getBrand());
        carBean.setColor(plateInfo.getColor());
        carBean.setDate(plateInfo.getDate());
        carBean.setAddress(plateInfo.getAddress());
        return carBean;
    }

    public static List<CarRecognizeInfoBean> convertPlateListToCarBeanList(List<PlateInfo> plateInfos){
        if(plateInfos == null || plateInfos.size() <= 0){
            return null;
        }
        List<CarRecognizeInfoBean> carRecognizeInfoBeans = new ArrayList<>();
        for(PlateInfo plateInfo: plateInfos){
            CarRecognizeInfoBean bean = convertPlateInfoToCarBean(plateInfo);
            carRecognizeInfoBeans.add(bean);
        }
        return carRecognizeInfoBeans;
    }

    public static List<PlateInfo> convertCarBeanListToPlateList(List<CarRecognizeInfoBean> carBeans){
        if(carBeans == null || carBeans.size() <= 0){
            return null;
        }
        List<PlateInfo> plateInfos = new ArrayList<>();
        for(CarRecognizeInfoBean bean: carBeans){
            PlateInfo info  = convertCarBeanToPlateInfo(bean);
            plateInfos.add(info);
        }
        return plateInfos;
    }
}
