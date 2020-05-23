package com.rokid.glass.libbase.utils;

import java.lang.reflect.Method;

/**
 * @date 2019/4/15
 */

public class SystemProperties {
    public static String get(String key) {
        String value = null;
        try {
            Class<?> clazz = Class.forName("android.os.SystemProperties");
            Method get = clazz.getMethod("get", String.class);
            value = (String) (get.invoke(clazz, key));
        } catch (Exception e) {
        }
        return value;
    }

    public static String get(String key, String defaultValue) {
        String value = defaultValue;
        try {
            Class<?> clazz = Class.forName("android.os.SystemProperties");
            Method get = clazz.getMethod("get", String.class, String.class);
            value = (String) (get.invoke(clazz, key, defaultValue));
        } catch (Exception e) {
        }
        return value;
    }

    public static int getInt(String key, int defaultValue) {
        int value = defaultValue;
        try {
            Class<?> clazz = Class.forName("android.os.SystemProperties");
            Method get = clazz.getMethod("getInt", String.class, String.class);
            value = (int) (get.invoke(clazz, key, defaultValue));
        } catch (Exception e) {
        }
        return value;
    }

    public static long getLonf(String key, long defaultValue) {
        long value = defaultValue;
        try {
            Class<?> clazz = Class.forName("android.os.SystemProperties");
            Method get = clazz.getMethod("getLong", String.class, String.class);
            value = (long) (get.invoke(clazz, key, defaultValue));
        } catch (Exception e) {
        }
        return value;
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        boolean value = defaultValue;
        try {
            Class<?> clazz = Class.forName("android.os.SystemProperties");
            Method get = clazz.getMethod("getBoolean", String.class, String.class);
            value = (boolean) (get.invoke(clazz, key, defaultValue));
        } catch (Exception e) {
        }
        return value;
    }

    public static void set(String key, String value) {
        try {
            Class<?> clazz = Class.forName("android.os.SystemProperties");
            Method set = clazz.getMethod("set", String.class, String.class);
            set.invoke(clazz, key, value);
        } catch (Exception e) {
        }
    }

}

