package com.rokid.glass.libbase.message.dto;

public class AccountInfoBean {

    private String userName;

    private String userId;

    private String token;

    private String btName;

    public AccountInfoBean() {

    }

    public AccountInfoBean(String userName, String userId, String token, String btName) {
        this.userName = userName;
        this.userId = userId;
        this.token = token;
        this.btName = btName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getBtName() {
        return btName;
    }

    public void setBtName(String btName) {
        this.btName = btName;
    }

    @Override
    public String toString() {
        return "AccountInfoBean{" +
                "userName='" + userName + '\'' +
                ", userId='" + userId + '\'' +
                ", token='" + token + '\'' +
                ", btName='" + btName + '\'' +
                '}';
    }
}
