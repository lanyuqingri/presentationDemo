package com.rokid.glass.libbase.message.deploy;


/**
 * Author: Zack
 * Email:  newzzack@gmail.com
 * Date:   2020.01.03 11:06
 */
public class DeployStateChangeMessage extends DeployMessage {

    public enum DeviceStatus {
        /**
         * 已读
         */
        READ,
        /**
         * 已更新
         */
        UPDATED,
    }

    private String mTaskId;
    private DeviceStatus mStatus;

    public DeployStateChangeMessage() {
        super(DeployMessageType.STATUS);
    }

    public DeployStateChangeMessage(String taskId, DeviceStatus status) {
        super(DeployMessageType.STATUS);
        mTaskId = taskId;
        mStatus = status;
    }

    public String getTaskId() {
        return mTaskId;
    }

    public void setTaskId(String taskId) {
        mTaskId = taskId;
    }

    public DeviceStatus getStatus() {
        return mStatus;
    }

    public void setStatus(DeviceStatus status) {
        mStatus = status;
    }

    @Override
    public String toString() {
        return "DeployStateChangeMessage{" +
                "mTaskId='" + mTaskId + '\'' +
                ", mStatus=" + mStatus +
                ", mId=" + mId +
                ", mMessageType=" + mMessageType +
                ", mDirection=" + mDirection +
                ", mResultCode=" + mResultCode +
                '}';
    }
}
