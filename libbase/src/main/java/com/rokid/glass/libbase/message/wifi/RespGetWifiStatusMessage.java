package com.rokid.glass.libbase.message.wifi;


import com.rokid.glass.libbase.message.enums.MessageDirection;

public class RespGetWifiStatusMessage extends WifiMessage {

    public static int WIFI_STATUS_OFF = 0x01; // WIFI关闭
    public static int WIFI_STATUS_CONNECTED = 0x02; // 连接成功
    public static int WIFI_STATUS_NOT_CONNECT = 0x03; // 未连接

    private String mSSID;
    private String mBSSID;
    private int mLevel;
    private String mPassword;
    private int mStatus;

    public RespGetWifiStatusMessage() {
        super(MessageDirection.GLASS_TO_MOBILE, WifiMessageType.GET_WIFI_STATUS);;
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

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int Status) {
        this.mStatus = Status;
    }

    @Override
    public String toString() {
        return "RespGetWifiStatusMessage{" +
                "mSSID='" + mSSID + '\'' +
                ", mBSSID='" + mBSSID + '\'' +
                ", mLevel=" + mLevel +
                ", mPassword=" + mPassword +
                ", mStatus=" + mStatus +
                '}';
    }
}
