package com.rokid.glass.libbase.message.wifi;


import com.rokid.glass.libbase.message.enums.MessageDirection;

public class SendWifiMessage extends WifiMessage {

    private String mSSID;
    private String mBSSID;
    private String mPassword;

    public SendWifiMessage() {
        super(MessageDirection.MOBILE_TO_GLASS, WifiMessageType.SEND_WIFI);
    }

    public SendWifiMessage(String ssid, String bssid, String password) {
        super(MessageDirection.MOBILE_TO_GLASS, WifiMessageType.SEND_WIFI);
        mSSID = ssid;
        mBSSID = bssid;
        mPassword = password;
    }

    public String getSSID() {
        return mSSID;
    }

    public void setSSID(String SSID) {
        this.mSSID = SSID;
    }

    public String getBSSID() {
        return mBSSID;
    }

    public void setBSSID(String BSSID) {
        this.mBSSID = BSSID;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String Password) {
        this.mPassword = Password;
    }

    @Override
    public String toString() {
        return "WifiInfoMessage{" +
                "mSSID='" + mSSID + '\'' +
                ", mBSSID='" + mBSSID + '\'' +
                ", mPassword='" + mPassword + '\'' +
                ", mId=" + mId +
                ", mMessageType=" + mMessageType +
                ", mDirection=" + mDirection +
                ", mResultCode=" + mResultCode +
                '}';
    }
}
