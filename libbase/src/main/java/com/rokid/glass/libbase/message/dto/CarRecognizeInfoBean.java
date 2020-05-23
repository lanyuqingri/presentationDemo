package com.rokid.glass.libbase.message.dto;

/**
 * Author: Zack
 * Email:  newzzack@gmail.com
 * Date:   2019.09.20 10:48
 */
public class CarRecognizeInfoBean extends RecogRecordBean {

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
     * 车牌号
     */
    private String plate;
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

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "CarRecognizeInfoBean{" +
                "owner='" + owner + '\'' +
                ", idcard='" + idcard + '\'' +
                ", address='" + address + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", plate='" + plate + '\'' +
                ", brand='" + brand + '\'' +
                ", color='" + color + '\'' +
                ", status='" + status + '\'' +
                ", date='" + date + '\'' +
                ", tag='" + tag + '\'' +
                '}';
    }
}
