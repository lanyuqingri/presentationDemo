package com.rokid.glass.libbase.plate;

import java.io.Serializable;

public class PlateDeployInfo implements Serializable {
    private String name;
    private long expireTime = -1;
    private long updateTime;
    private int plateNum;
    private boolean newPlateSwitch;
    private String newPlateDesc;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public int getPlateNum() {
        return plateNum;
    }

    public void setPlateNum(int plateNum) {
        this.plateNum = plateNum;
    }

    public boolean isNewPlateSwitch() {
        return newPlateSwitch;
    }

    public void setNewPlateSwitch(boolean newPlateSwitch) {
        this.newPlateSwitch = newPlateSwitch;
    }

    public String getNewPlateDesc() {
        return newPlateDesc;
    }

    public void setNewPlateDesc(String newPlateDesc) {
        this.newPlateDesc = newPlateDesc;
    }
}
