package com.rokid.glass.libbase.faceid;


import com.rokid.glass.libbase.faceid.database.FaceMapping;
import com.rokid.glass.libbase.faceid.database.UserInfo;

import java.io.Serializable;

/**
 * zhuohf1 @2019.5.9
 */
public class FaceInfo implements Serializable {
    private static final long serialVersionUID = 1752990013875566870L;
    private UserInfo userInfo;
    private FaceMapping faceMapping;


    public UserInfo getUserInfo() {
        return userInfo;
    }

    public FaceInfo setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
        return this;
    }

    public FaceMapping getFaceMapping() {
        return faceMapping;
    }

    public FaceInfo setFaceMapping(FaceMapping faceMapping) {
        this.faceMapping = faceMapping;
        return this;
    }

    @Override
    public String toString() {
        return "FaceInfo{" +
                "userInfo=" + userInfo +
                ", faceMapping=" + faceMapping +
                '}';
    }
}
