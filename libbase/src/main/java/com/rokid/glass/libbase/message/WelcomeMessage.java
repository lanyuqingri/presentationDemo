package com.rokid.glass.libbase.message;


import com.rokid.glass.libbase.message.enums.MessageDirection;
import com.rokid.glass.libbase.message.enums.MessageType;

/**
 * Author: Zack
 * Email:  newzzack@gmail.com
 * Date:   2019.07.10 19:31
 */
public class WelcomeMessage extends MessageImpl {

    private String mSN;
    private String mOtaVersion;
    private String mMAC;

    public WelcomeMessage() {
        super(MessageType.WELCOME, MessageDirection.GLASS_TO_MOBILE);
    }


    public WelcomeMessage(String SN, String otaVersion, String MAC) {
        this();
        mSN = SN;
        mOtaVersion = otaVersion;
        mMAC = MAC;
    }

    public String getSN() {
        return mSN;
    }

    public void setSN(String SN) {
        this.mSN = SN;
    }

    public String getOtaVersion() {
        return mOtaVersion;
    }

    public void setOtaVersion(String otaVersion) {
        this.mOtaVersion = otaVersion;
    }

    public String getMAC() {
        return mMAC;
    }

    public void setMAC(String MAC) {
        mMAC = MAC;
    }

    @Override
    public String toString() {
        return "WelcomeMessage{" +
                "mSN='" + mSN + '\'' +
                ", mOtaVersion='" + mOtaVersion + '\'' +
                ", mMAC='" + mMAC + '\'' +
                ", mId=" + mId +
                ", mMessageType=" + mMessageType +
                ", mDirection=" + mDirection +
                ", mResultCode=" + mResultCode +
                '}';
    }
}
