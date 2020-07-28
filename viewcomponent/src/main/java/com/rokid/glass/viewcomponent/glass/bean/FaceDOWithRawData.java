package com.rokid.glass.viewcomponent.glass.bean;

import com.rokid.facelib.model.FaceDO;
import com.rokid.facelib.model.FaceModel;

/**
 * Author: heshun
 * Date: 2020/7/7 1:46 PM
 * gmail: shunhe1991@gmail.com
 */
public class FaceDOWithRawData extends FaceModel {

    private byte[] rawData;
    private FaceDO faceDO;

    public byte[] getRawData() {
        return rawData;
    }

    public void setRawData(byte[] rawData) {
        this.rawData = rawData;
    }

    public FaceDO getFaceDO() {
        return faceDO;
    }

    public void setFaceDO(FaceDO faceDO) {
        this.faceDO = faceDO;
    }

    public FaceDOWithRawData(FaceDO faceDO, byte[] rawData){
        this.faceDO = faceDO;
        this.rawData = rawData;
    }
}
