package com.rokid.glass.libbase.faceid.database;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "tbl_recog_face_single", indices = {@Index(value = {"id"}, unique = true)})
public class SingleFaceRecord {
    @PrimaryKey
    @NonNull
    public String id;

    public String name;
    public String cardNo;
    public String birthPlace;
    public String tag;
    public String pose;
    public float faceScore;
    public float userInfoScore;
    public String captureImg;
    public String captureFaceImg;
    public long gmtRecg;
    /**
     * 是否是在线识别
     */
    public boolean isOnline;

    /**
     * 如果是在线识别，则此字段表明是使用那家的识别引擎
     * (此字段作为预留字段，目前无法获取在线识别引擎，如果后续业务可以获取，则使用此字段)
     */
    public String apiFrom;

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getPose() {
        return pose;
    }

    public void setPose(String pose) {
        this.pose = pose;
    }

    public float getFaceScore() {
        return faceScore;
    }

    public void setFaceScore(float faceScore) {
        this.faceScore = faceScore;
    }

    public float getUserInfoScore() {
        return userInfoScore;
    }

    public void setUserInfoScore(float userInfoScore) {
        this.userInfoScore = userInfoScore;
    }

    public String getCaptureImg() {
        return captureImg;
    }

    public void setCaptureImg(String captureImg) {
        this.captureImg = captureImg;
    }

    public String getCaptureFaceImg() {
        return captureFaceImg;
    }

    public void setCaptureFaceImg(String captureFaceImg) {
        this.captureFaceImg = captureFaceImg;
    }

    public long getGmtRecg() {
        return gmtRecg;
    }

    public void setGmtRecg(long gmtRecg) {
        this.gmtRecg = gmtRecg;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public String getApiFrom() {
        return apiFrom;
    }

    public void setApiFrom(String apiFrom) {
        this.apiFrom = apiFrom;
    }
}
