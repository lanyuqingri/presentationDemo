package com.rokid.glass.libbase.userDao;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;


import java.io.Serializable;


@Entity(tableName = Constant.USER_TABLE_NAME, indices = {@Index(value = {"userName"})})
public class User implements Serializable {
    private long id;
    @PrimaryKey
    @NonNull
    private String userName;
    private String passWord;
    private String userArea;
    private String userId;

    private String accessToken;
    private String refreshToken;
    private Long expiresIn;

    public User(){
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return this.passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return this.refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Long getExpiresIn() {
        return this.expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getUserArea() {
        return this.userArea;
    }

    public void setUserArea(String userArea) {
        this.userArea = userArea;
    }

    public boolean checkUser() {
        if (TextUtils.isEmpty(userName)) {
            return false;
        }
        if (TextUtils.isEmpty(passWord)) {
            return false;
        }
        if (TextUtils.isEmpty(userId)) {
            return false;
        }
        if (TextUtils.isEmpty(accessToken)) {
            return false;
        }

        return true;
    }

}
