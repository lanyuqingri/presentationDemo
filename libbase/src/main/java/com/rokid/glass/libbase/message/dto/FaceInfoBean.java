package com.rokid.glass.libbase.message.dto;


import java.util.Arrays;

/**
 * Author: Zack
 * Email:  newzzack@gmail.com
 * Date:   2019.07.10 19:58
 */
public class FaceInfoBean {

    private String uid;

    private String fid;

    private String name;

    private String cardno;

    private String nativeplace;

    private String tag;

    private boolean isAlarm;

    private boolean isCover;

    /**
     *  图像数据
     */
    protected byte[] faceImage;

    private String fileName;

    public FaceInfoBean() {
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCardno() {
        return cardno;
    }

    public void setCardno(String cardno) {
        this.cardno = cardno;
    }

    public String getNativeplace() {
        return nativeplace;
    }

    public void setNativeplace(String nativeplace) {
        this.nativeplace = nativeplace;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public boolean isAlarm() {
        return isAlarm;
    }

    public void setAlarm(boolean alarm) {
        isAlarm = alarm;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public boolean isCover() {
        return isCover;
    }

    public void setCover(boolean cover) {
        isCover = cover;
    }

    public byte[] getFaceImage() {
        return faceImage;
    }

    public void setFaceImage(byte[] faceImage) {
        this.faceImage = faceImage;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "FaceInfoBean{" +
                "uid='" + uid + '\'' +
                ", fid='" + fid + '\'' +
                ", name='" + name + '\'' +
                ", cardno='" + cardno + '\'' +
                ", nativeplace='" + nativeplace + '\'' +
                ", tag='" + tag + '\'' +
                ", isAlarm=" + isAlarm +
                ", isCover=" + isCover +
                ", faceImage=" + Arrays.toString(faceImage) +
                ", fileName='" + fileName + '\'' +
                '}';
    }

}
