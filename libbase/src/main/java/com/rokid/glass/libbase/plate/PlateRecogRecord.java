package com.rokid.glass.libbase.plate;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.rokid.glass.libbase.faceid.FaceConstants;

import java.io.Serializable;

@Entity(tableName = FaceConstants.PLATE_DEPLOY_RECOG_TABLE, indices = {@Index(value = {"recordId"})})
public class PlateRecogRecord implements Serializable {
    @PrimaryKey
    @NonNull
    private String recordId;
    private String plate;
    private String owner;
    private String idcard;
    private String address;
    private String phoneNum;
    private String brand;
    private String color;
    private String status;
    private String date;
    private String tag;
    private long gmtTime;
    private String location;
    private String recogFilePath;
    private boolean isOnline;
    private String apiFrom;

    @NonNull
    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(@NonNull String recordId) {
        this.recordId = recordId;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public long getGmtTime() {
        return gmtTime;
    }

    public void setGmtTime(long gmtTime) {
        this.gmtTime = gmtTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRecogFilePath() {
        return recogFilePath;
    }

    public void setRecogFilePath(String recogFilePath) {
        this.recogFilePath = recogFilePath;
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
