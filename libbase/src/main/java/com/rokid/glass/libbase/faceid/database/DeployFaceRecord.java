package com.rokid.glass.libbase.faceid.database;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "tbl_recog_face_deploy", indices = {@Index(value = {"id"}, unique = true)})
public class DeployFaceRecord {
    @PrimaryKey
    @NonNull
    public String id;

    public String name;
    public String cardNo;
    public String birthPlace;
    public String tag;
    public String pose;
    public float faceScore;
    public float userInfoScore;
    public String captureImg;
    public String captureFaceImg;
    public long gmtRecg;
    /**
     * 是否是在线识别
     */
    public boolean isOnline;

    /**
     * 如果是在线识别，则此字段表明是使用那家的识别引擎
     * (此字段作为预留字段，目前无法获取在线识别引擎，如果后续业务可以获取，则使用此字段)
     */
    public String apiFrom;


}
