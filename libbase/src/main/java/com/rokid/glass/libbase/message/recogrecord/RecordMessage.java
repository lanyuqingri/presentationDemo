package com.rokid.glass.libbase.message.recogrecord;


import com.rokid.glass.libbase.message.MessageImpl;
import com.rokid.glass.libbase.message.enums.MessageDirection;
import com.rokid.glass.libbase.message.enums.MessageType;

/**
 * Author: Zack
 * Email:  newzzack@gmail.com
 * Date:   2019.09.23 10:55
 */
public class RecordMessage extends MessageImpl {

    /**
     * 标识请求时间戳
     */
    private long mTimestamp;

    /**
     * 标识是否是在线识别
     */
    private boolean isOnline;

    /**
     * 是否是人脸识别记录
     */
    private boolean isFaceRecord;

    /**
     * 车辆识别信息
     */
    private CarRecordBean mCarInfo;

    /**
     * 人脸识别信息
     */
    private FaceRecordBean mFaceInfo;


    public RecordMessage() {
        super(MessageType.RECORD, MessageDirection.GLASS_TO_MOBILE);
    }

    public long getTimestamp() {
        return mTimestamp;
    }

    public void setTimestamp(long timestamp) {
        mTimestamp = timestamp;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public boolean isFaceRecord() {
        return isFaceRecord;
    }

    public void setFaceRecord(boolean faceRecord) {
        isFaceRecord = faceRecord;
    }

    public CarRecordBean getCarInfo() {
        return mCarInfo;
    }

    public void setCarInfo(CarRecordBean carInfo) {
        mCarInfo = carInfo;
    }

    public FaceRecordBean getFaceInfo() {
        return mFaceInfo;
    }

    public void setFaceInfo(FaceRecordBean faceInfo) {
        mFaceInfo = faceInfo;
    }

    @Override
    public String toString() {
        return "RecordMessage{" +
                "mTimestamp=" + mTimestamp +
                ", isOnline=" + isOnline +
                ", isFaceRecord=" + isFaceRecord +
                ", mCarInfo=" + mCarInfo +
                ", mFaceInfo=" + mFaceInfo +
                ", mId=" + mId +
                ", mMessageType=" + mMessageType +
                ", mDirection=" + mDirection +
                ", mResultCode=" + mResultCode +
                '}';
    }
}
