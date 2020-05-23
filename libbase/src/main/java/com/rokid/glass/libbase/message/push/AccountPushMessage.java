package com.rokid.glass.libbase.message.push;


import com.rokid.glass.libbase.message.dto.AccountInfoBean;
import com.rokid.glass.libbase.message.enums.MessageDirection;

public class AccountPushMessage extends PushMessage {

    private AccountInfoBean mAccount;

    public AccountPushMessage() {
        super(MessageDirection.MOBILE_TO_GLASS, PushMessageType.ACCOUNT);
    }

    public AccountPushMessage(AccountInfoBean account) {
        super(MessageDirection.MOBILE_TO_GLASS, PushMessageType.ACCOUNT);
        this.mAccount = account;
    }

    public AccountInfoBean getAccount() {
        return mAccount;
    }

    public void setAccount(AccountInfoBean account) {
        this.mAccount = account;
    }

    @Override
    public String toString() {
        return "ReqFaceMessage{" +
                "mAccount=" + mAccount +
                ", mSubType=" + mSubType +
                ", mId=" + mId +
                ", mMessageType=" + mMessageType +
                ", mDirection=" + mDirection +
                ", mResultCode=" + mResultCode +
                '}';
    }
}