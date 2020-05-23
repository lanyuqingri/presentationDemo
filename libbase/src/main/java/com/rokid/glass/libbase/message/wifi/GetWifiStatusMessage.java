package com.rokid.glass.libbase.message.wifi;


import com.rokid.glass.libbase.message.enums.MessageDirection;

public class GetWifiStatusMessage extends WifiMessage {

    public GetWifiStatusMessage() {
        super(MessageDirection.MOBILE_TO_GLASS, WifiMessageType.GET_WIFI_STATUS);
    }

}
