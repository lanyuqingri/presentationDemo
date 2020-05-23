package com.rokid.glass.libbase.config;

public interface UserConfigConst {
    //用户蓝牙锁开关状态变化
    String ACTION_LOCK_BT_CHANGE = "com.rokid.lock_bt_change";
    /**
     * 神盾是否强制依赖蓝牙连接，true表示不依赖，false表示强依赖
     */
    String KEY_CONFIG_NO_BT = "user_config_no_bt";
}
