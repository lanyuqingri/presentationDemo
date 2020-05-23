package com.rokid.glass.libbase.faceid.database;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.rokid.glass.libbase.faceid.FaceConstants;

import java.io.Serializable;

/**
 * Author: Zack
 * Email:  newzzack@gmail.com
 * Date:   2020.02.25 20:50
 */
@Entity(tableName = FaceConstants.DEPLOY_TASK_INFO_TABLE, indices = {@Index(value = {"id"})})
public class DeployDbInfo implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    private String keyStr;

    private String valueStr;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKeyStr() {
        return keyStr;
    }

    public void setKeyStr(String keyStr) {
        this.keyStr = keyStr;
    }

    public String getValueStr() {
        return valueStr;
    }

    public void setValueStr(String valueStr) {
        this.valueStr = valueStr;
    }

    @Override
    public String toString() {
        return "DeployDbInfo{" +
                "id=" + id +
                ", keyStr='" + keyStr + '\'' +
                ", valueStr='" + valueStr + '\'' +
                '}';
    }
}
