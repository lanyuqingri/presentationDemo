package com.rokid.glass.libbase.message.wifi;


import com.rokid.glass.libbase.message.enums.MessageDirection;

public class RespSendWifiMessage extends WifiMessage {

    public static int WIFI_SEND_SUCCESS = 0x200; // 发送成功
    public static int WIFI_SEND_FAIL = 0x400; // 发送失败

    private String mSSID;
    private String mBSSID;
    private int mLevel;
    private String mPassword;
    private int mResult;

    public RespSendWifiMessage() {
        super(MessageDirection.GLASS_TO_MOBILE, WifiMessageType.SEND_WIFI);
    }

    public RespSendWifiMessage(String ssid, int result) {
        super(MessageDirection.GLASS_TO_MOBILE, WifiMessageType.SEND_WIFI);
        mSSID = ssid;
        mResult = result;
    }

    public String getBSSID() {
        return mBSSID;
    }

    public void setBSSID(String BSSID) {
        this.mBSSID = BSSID;
    }

    public int getLevel() {
        return mLevel;
    }

    public void setLevel(int Level) {
        this.mLevel = Level;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String Password) {
        this.mPassword = Password;
    }

    public String getSSID() {
        return mSSID;
    }

    public void setSSID(String SSID) {
        this.mSSID = SSID;
    }

    public int getResult() {
        return mResult;
    }

    public void setResult(int Result) {
        this.mResult = Result;
    }

    @Override
    public String toString() {
        return "RespSendWifiMessage{" +
                "mSSID='" + mSSID + '\'' +
                ", mBSSID='" + mBSSID + '\'' +
                ", mLevel=" + mLevel +
                ", mPassword=" + mPassword +
                ", mResult=" + mResult +
                '}';
    }
}
