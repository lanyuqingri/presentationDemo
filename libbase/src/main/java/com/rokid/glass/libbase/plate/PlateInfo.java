package com.rokid.glass.libbase.plate;

import android.os.Parcel;
import android.os.Parcelable;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.rokid.glass.libbase.faceid.FaceConstants;

import java.io.Serializable;

@Entity(tableName = FaceConstants.PLATE_TABLE, indices = {@Index(value = {"plate"})})
public class PlateInfo implements Serializable, Parcelable {
    /**
     * 车牌号
     */
    @PrimaryKey
    @NonNull
    private String plate;
    /**
     * 车主
     */
    private String owner;
    /**
     * 车主身份证号
     */
    private String idcard;
    /**
     * 车主地址
     */
    private String address;
    /**
     * 车主电话号码
     */
    private String phoneNum;
    /**
     * 品牌
     */
    private String brand;
    /**
     * 颜色
     */
    private String color;
    /**
     * 车辆状态：
     * 正常、违法未处理 etc.
     */
    private String status;
    /**
     * 日期
     */
    private String date;

    /**
     * 标签
     */
    private String tag;

    public PlateInfo(){}

    protected PlateInfo(Parcel in) {
        owner = in.readString();
        idcard = in.readString();
        address = in.readString();
        phoneNum = in.readString();
        plate = in.readString();
        brand = in.readString();
        color = in.readString();
        status = in.readString();
        date = in.readString();
        tag = in.readString();
    }

    public static final Creator<PlateInfo> CREATOR = new Creator<PlateInfo>() {
        @Override
        public PlateInfo createFromParcel(Parcel in) {
            return new PlateInfo(in);
        }

        @Override
        public PlateInfo[] newArray(int size) {
            return new PlateInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(owner);
        dest.writeString(idcard);
        dest.writeString(address);
        dest.writeString(phoneNum);
        dest.writeString(plate);
        dest.writeString(brand);
        dest.writeString(color);
        dest.writeString(status);
        dest.writeString(date);
        dest.writeString(tag);
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

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
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
}
