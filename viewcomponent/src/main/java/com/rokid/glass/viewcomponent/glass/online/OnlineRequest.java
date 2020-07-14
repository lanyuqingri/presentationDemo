package com.rokid.glass.viewcomponent.glass.online;

import com.rokid.glass.libbase.message.car.ReqCarRecognizeMessage;
import com.rokid.glass.libbase.message.face.ReqOnlineSingleFaceMessage;

public interface OnlineRequest {
    void sendFaceInfo(ReqOnlineSingleFaceMessage faceMessage);
    void sendPlateInfo(ReqCarRecognizeMessage plateMessage);
}
