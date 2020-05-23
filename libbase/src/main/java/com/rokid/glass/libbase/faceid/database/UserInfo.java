package com.rokid.glass.libbase.faceid.database;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.rokid.glass.libbase.faceid.FaceConstants;

import java.io.Serializable;

/**
 * zhuohf1 @2019.5.9
 */

@Entity(tableName = FaceConstants.FACE_USER_TABLE, indices = {@Index(value = {"uid"})})
public class UserInfo implements Serializable {
    private static final long serialVersionUID = 4352710447101203901L;

    @PrimaryKey
    @NonNull
    public String uid; //用户ID,主键,uuid生成

    public String name; //用户姓名
    public String cardno; //身份证号
    public String nativeplace; //籍贯

    public String description;
    public boolean isAlarm;

    @Override
    public String toString() {
        return "UserInfo{" +
                "uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", cardno='" + cardno + '\'' +
                ", nativeplace='" + nativeplace + '\'' +
                ", description='" + description + '\'' +
                ", isAlarm=" + isAlarm +
                '}';
    }
}
