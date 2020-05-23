package com.rokid.glass.libbase.config;

/**
 * Author: Zack
 * Email:  newzzack@gmail.com
 * Date:   2020.01.06 15:01
 */
public interface DeployTaskConfig {

    interface KEYS {
        String DEPLOY_RECEIVE_PKG = "deploy_receive_pkg";
        String DEPLOY_DOWNLOADING_PKG = "deploy_downloading_pkg";
        String DEPLOY_DOWNLOAD_PKG_COMPLETE = "deploy_download_pkg_complete";
        String DEPLOY_PKG_INFO = "deploy_pkg_info";
        String DEPLOY_CHECK_TIMEOUT = "deploy_check_timeout";


        /**
         * sharedPreference 字段名
         */
        String SP_DEPLOY_NAME = "deploy_name";
        String SP_DEPLOY_UPDATE_TIME = "deploy_update_count";
        String SP_DEPLOY_FEATURE_COUNT = "deploy_feature_count";
        String SP_DEPLOY_PERSON_COUNT = "deploy_person_count";
        String SP_DEPLOY_UPDATE_WAY = "deploy_update_way";

        String SP_PLATE_DEPLOY_NAME = "plate_deploy";

        /**
         * 数据库 字段名
         */
        String DB_DEPLOY_NAME = "name";
        String DB_DEPLOY_EXPIRE_TIME = "expireTime";
        String DB_DEPLOY_UPDATE_TIME = "updateTime";
        String DB_DEPLOY_NEW_FRIEND_SWITCH = "newFriendSwitch";
        String DB_DEPLOY_NEW_FRIEND_DESC = "newFriendDesc";
        String DB_DEPLOY_NEW_FRIEND_ALARM = "newFriendAlarm";
    }

    interface UpdateWays {
        int ONLINE = 0;
        int USB = 1;
    }
}
