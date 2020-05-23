package com.rokid.glass.libbase.router;

/**
 * @date 2019-07-02
 */

public interface RouterConfig {
    interface BOOT {
        String INDEX = "/boot/index";
    }

    interface HOME {
        String INDEX = "/home/index";
    }
    interface FaceID {
        String INDEX = "/faceid/index";
    }

    interface ACCOUNT {
        String LOGIN = "/account/login";
        String CHECK_SCODE = "/account/check_scode";
        String EDIT_PHONE_NUM = "/account/edit_phone_num";
        String LOGIN_CHECK = "/account/login_check";
        String REGISTER = "/account/register";
        String RESET_PWD = "/account/reset_pwd";
    }

    interface Settings {
        String INDEX = "/settings/index";
        String BLUETOOTH = "/settings/bluetooth";
        String WIFI = "/settings/wifi";
        String VOICE = "/settings/voice";
        String OFFLINE = "/settings/offline";
        String FACEQUALITY = "/settings/facequality";
        String BRIGHTNESS = "/settings/brightness";
        String VOLUME = "/settings/volume";
        String OTA = "/settings/ota";
        String TIME = "/settings/time";
        String RESET = "/settings/reset";
        String ABOUT = "/settings/about";
        String BT_SECURITY_LOCK = "/settings/btsecuritylock";
        String SMART_CONFIG = "/settings/smartconfig";
        String SINGLE_FACE_CONFIG = "/settings/singlefaceconfig";
        String PLATE_CONFIG = "/settings/plateconfig";
    }

    interface Gallery {
        String INDEX = "/gallery/index";
        String GALLERY_LIST = "/gallery/gallerylist";
        String GALLERY = "/gallery/gallery";
        String VIDEO_LIST = "/gallery/videolist";
        String VIDEO = "/gallery/video";
    }

    interface Dialog {
        String INDEX = "/dialog/index";
    }

    interface Message {
        String INDEX = "/message/index";
    }

    interface Deploy {
        String INDEX = "/deploy/index";
    }

    //Key params
    interface Keys {
        String DIALOG_TITLE = "dialog_title";
        String DIALOG_TIP = "dialog_tip";
        String DIALOG_TIP_RES = "dialog_tip_res";
        String DIALOG_CONFIRM = "dialog_confirm";
        String DIALOG_CANCEL = "dialog_cancel";
        String DIALOG_CONFIRM_CALLBACK = "dialog_confirm_callback";
        String DIALOG_CANCEL_CALLBACK = "dialog_cancel_callback";

        String GALLERY_CURRENT_POSITION = "gallery_current_position";
        String GALLERY_TYPE = "gallery_type";
    }
}
