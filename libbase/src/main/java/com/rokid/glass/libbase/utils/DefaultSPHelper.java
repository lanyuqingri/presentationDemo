package com.rokid.glass.libbase.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.rokid.glass.libbase.logger.Logger;


public class DefaultSPHelper {

    private static final String PREFERENCE_NAME = "rokid_glass";
    private static volatile DefaultSPHelper instance;
    private SharedPreferences mPreferences;

    private DefaultSPHelper() {
        mPreferences = ContextUtil.getApplicationContext().getSharedPreferences(PREFERENCE_NAME,
                Context.MODE_PRIVATE);
    }

    public static DefaultSPHelper getInstance() {
        if (instance == null) {
            synchronized (DefaultSPHelper.class) {
                if (instance == null) {
                    instance = new DefaultSPHelper();
                }
            }
        }
        return instance;
    }

    /**
     * 将String信息存入Preferences
     */
    public boolean put(String key, String value) {
        Logger.d("key: " + key, " ;value: " + value);
        // 存入数据
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    /**
     * 获取SharePreference中的String类型值
     */
    public String getString(String key) {
        Logger.d("key: " + key);
        // 获取数据
        return mPreferences.getString(key, "");
    }

    /**
     * 获取SharePreference中的String类型值
     */
    public String getString(String key, String defValue) {
        Logger.d("key: " + key, " ;defValue: " + defValue);
        // 获取数据
        return mPreferences.getString(key, defValue);
    }

    /**
     * 将string的信息加密存入mPreferences
     */
    public boolean putByAES(String key, String value) {
        Logger.d("key: " + key, " ;value: " + value);
        // 存入数据
        SharedPreferences.Editor editor = mPreferences.edit();
        String decrypt = AESUtils.encrypt(value, key);
        editor.putString(key, decrypt);
        return editor.commit();
    }


    /**
     * 取出string通过解密的方式
     */
    public String getStringByAES(String key) {
        Logger.d("key: " + key);
        // 获取数据
        String mPreferencesString = mPreferences.getString(key, "");
        return AESUtils.decrypt(mPreferencesString, key);
    }

    /**
     * 将boolean信息存入Preferences
     */
    public boolean put(String key, boolean value) {
        Logger.d("key: " + key, " ;value: " + value);
        // 存入数据
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    /**
     * 获取SharePreference中的值
     */
    public boolean getBoolean(String key, boolean defValue) {
        Logger.d("key: " + key, " ;defValue: " + defValue);
        // 获取数据
        return mPreferences.getBoolean(key, defValue);
    }


    /**
     * 将int信息存入Preferences
     */
    public boolean put(String key, int value) {
        Logger.d("key: " + key, " ;value: " + value);
        // 存入数据
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(key, value);
        return editor.commit();
    }

    /**
     * 获取SharePreference中的int类型值
     */
    public int getInt(String key, int defValue) {
        Logger.d("key: " + key, " ;defValue: " + defValue);
        // 获取数据
        return mPreferences.getInt(key, defValue);
    }

    public boolean put(String key, long value) {
        Logger.d("key: " + key, " ;value: " + value);
        // 存入数据
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putLong(key, value);
        return editor.commit();
    }

    /**
     * 获取SharePreference中的int类型值
     */
    public Long getLong(String key, Long defValue) {
        Logger.d("key: " + key, " ;defValue: " + defValue);
        // 获取数据
        return mPreferences.getLong(key, defValue);
    }

    /**
     * 将float信息存入Preferences
     */
    public boolean put(String key, float value) {
        Logger.d("key: " + key, " ;value: " + value);
        // 存入数据
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putFloat(key, value);
        return editor.commit();
    }

    /**
     * 获取SharePreference中的值
     */
    public float getFloat(String key, float defValue) {
        Logger.d("key: " + key, " ;defValue: " + defValue);
        // 获取数据
        return mPreferences.getFloat(key, defValue);
    }

    /**
     * delete in preferences value
     */
    public boolean remove(String key) {
        Logger.d("key: " + key);
        // 存入数据
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.remove(key);
        return editor.commit();
    }

    /**
     * Mark in the editor to remove all data from the preferences.
     */
    public boolean clear() {
        Logger.d("clear all data.");
        // 存入数据
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.clear();
        return editor.commit();
    }

}
