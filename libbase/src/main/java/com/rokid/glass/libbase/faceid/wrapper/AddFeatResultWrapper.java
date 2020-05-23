package com.rokid.glass.libbase.faceid.wrapper;


import com.rokid.glass.libbase.ErrorCode;

/**
 * Author: Zack
 * Email:  newzzack@gmail.com
 * Date:   2020.02.23 15:51
 */
public class AddFeatResultWrapper {

    private String mFeatId;
    private ErrorCode mErrorCode;

    public String getFeatId() {
        return mFeatId;
    }

    public void setFeatId(String featId) {
        mFeatId = featId;
    }

    public ErrorCode getErrorCode() {
        return mErrorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        mErrorCode = errorCode;
    }

    @Override
    public String toString() {
        return "AddFeatResultWrapper{" +
                "mFeatId='" + mFeatId + '\'' +
                ", mErrorCode=" + mErrorCode +
                '}';
    }
}
