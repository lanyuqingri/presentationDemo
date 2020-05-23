package com.rokid.glass.presentationdemo.glass.online;


import com.rokid.glass.libbase.message.car.RespCarRecognizeMessage;
import com.rokid.glass.libbase.message.face.RespOnlineSingleFaceMessage;

public interface OnlineResp {

    void onCarResp(RespCarRecognizeMessage respCarRecognizeMessage);
    void onFaceResp(RespOnlineSingleFaceMessage respOnlineSingleFaceMessage);
}
