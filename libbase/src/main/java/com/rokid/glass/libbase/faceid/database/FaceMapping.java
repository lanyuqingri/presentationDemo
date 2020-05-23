package com.rokid.glass.libbase.faceid.database;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.rokid.glass.libbase.faceid.FaceConstants;

import java.io.Serializable;

/**
 * zhuohf1 @2019.5.9
 */

@Entity(tableName = FaceConstants.FACE_MAPPING_TABLE, indices = {@Index(value = {"fid"})})
public class FaceMapping implements Serializable {
    private static final long serialVersionUID = -2423337939695564614L;

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String fid; //特征库UUID 32 bit, 不能重复
    public String uid;
    public boolean isCover;
    public byte[] faceImg;

    @Override
    public String toString() {
        return "FaceMapping{" +
                "fid='" + fid + '\'' +
                ", uid='" + uid + '\'' +
                ", isCover=" + isCover +
                '}';
    }
}
