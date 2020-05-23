package com.rokid.glass.libbase.message.deploy;


import com.rokid.glass.libbase.message.dto.DeployTaskBean;

/**
 * Author: Zack
 * Email:  newzzack@gmail.com
 * Date:   2020.01.03 11:10
 */
public class DeployTaskMessage extends DeployMessage {

    /**
     * 布控包信息对象
     */
    private DeployTaskBean mTaskBean;

    /**
     * 是否是手动检查更新， true为手动， false为轮询结果
     */
    private boolean isManualCheck = false;

    public DeployTaskMessage() {
        super(DeployMessageType.GET_TASK);
    }

    public DeployTaskMessage(DeployTaskBean taskBean) {
        super(DeployMessageType.GET_TASK);
        mTaskBean = taskBean;
    }

    public DeployTaskMessage(DeployTaskBean taskBean, boolean isManualCheck) {
        super(DeployMessageType.GET_TASK);
        mTaskBean = taskBean;
        this.isManualCheck = isManualCheck;
    }

    public DeployTaskBean getTaskBean() {
        return mTaskBean;
    }

    public void setTaskBean(DeployTaskBean taskBean) {
        mTaskBean = taskBean;
    }

    public boolean isManualCheck() {
        return isManualCheck;
    }

    public void setManualCheck(boolean manualCheck) {
        isManualCheck = manualCheck;
    }

    @Override
    public String toString() {
        return "DeployTaskMessage{" +
                "mTaskBean=" + mTaskBean +
                ", isManualCheck=" + isManualCheck +
                ", mId=" + mId +
                ", mMessageType=" + mMessageType +
                ", mDirection=" + mDirection +
                ", mResultCode=" + mResultCode +
                '}';
    }
}
