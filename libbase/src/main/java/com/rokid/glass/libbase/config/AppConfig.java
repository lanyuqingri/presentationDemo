package com.rokid.glass.libbase.config;

/**
 * @author jian.yang
 * @date 2019-09-10
 */

public interface AppConfig {
    interface Settings {
        String DB_VOICE_WAKEUP_KEY = "rokid_wakeup_setting";
        int VOICE_OFF = 0;
        int VOICE_ON = 1;
    }

    interface Dialog {
        int CONFIRM = 2;
        int CANCEL = 1;
        int CLOSE = 3;
    }

    interface Bluetooth {
        String ACTION_GET_USER_ACCOUNT = "glasses_get_user_account";
        String ACTION_BIND_DEVICE_SUCCESS = "glasses_bind_device_success";
    }

    interface OfflineVoice {
        String GLOBAL_BACK_HOME = "回到首页";
        String GLOBAL_BACK = "回到上一级";
        String GLOBAL_START_RECORD = "开始录像";
        String GLOBAL_STOP_RECORD = "停止录像";
        String GLOBAL_WIRELESS_SCREEN = "无线投屏";

        String DELETE_VIDEO = "删除视频";
        String DELETE_PHOTO = "删除照片";
        String BACK = "返回";
        String DELETE_PROJECT = "删除项目";

        String OPEN_DB_RECOG = "开启离线识别";
        String CLOSE_DB_RECOG = "关闭离线识别";

        String HOME_MENU_DEPLOY = "多人布控";
        String HOME_MENU_RECOG = "单人核查";
        String HOME_MENU_PLATE = "车牌识别";
        String HOME_OPEN_MENU = "打开菜单";
        String HOME_RE_RECOG = "重新识别";
        String HOME_CONTINUE_RECOG = "继续识别";

        String VIDEO_START_PLAY = "播放视频";
        String VIDEO_STOP_PLAY = "停止播放";

        //Setting
        String SETTING_SELECT_PROJECT = "选择项目";
        String SETTING_SELECT_LEVEL = "选择级别";

        String BT_DISCONNECT = "断开蓝牙";
        String WIFI_OPEN = "打开无线网";
        String WIFI_CLOSE = "关闭无线网";

        String OPEN_MULTI_FACE_RECOG = "开启多人布控";
        String CLOSE_MULTI_FACE_RECOG = "关闭多人布控";

        String OPEN_SINGLE_FACE_RECOG = "开启单人核查";
        String CLOSE_SINGLE_FACE_RECOG = "关闭单人核查";

        String OPEN_PLATE_RECOG = "开启车牌识别";
        String CLOSE_PLATE_RECOG = "关闭车牌识别";

        String OPEN_ONLINE_SINGLE_FACE__RECOG = "开启在线人脸";
        String CLOSE_ONLINE_SINGLE_FACE_RECOG = "关闭在线人脸";

        String OPEN_ONLINE_PLATE_RECOG = "开启在线车牌";
        String CLOSE_ONLINE_PLATE_RECOG = "关闭在线车牌";
    }

    interface Deploy {
        int CHECK_UPDATE = 0;
        int DOWNLOADING = 1;
    }


    interface Keys {
        String DIALOG_VIDEO = "dialog_video";
        String DEPLOY_TASK_STATE = "deploy_download_state";
        String KEY_USER_NAME = "key_user_name";
        String KEY_CUSTOMER_CODE = "key_customer_code";
        String KEY_TOKEN = "key_token";
    }
}
