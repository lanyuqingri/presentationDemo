package com.rokid.glass.libbase.utils;

import android.Manifest;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.PowerManager;
import android.os.SystemClock;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import androidx.core.app.ActivityCompat;

import com.rokid.glass.libbase.logger.Logger;

import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;

/**
 * @date 2019-07-12
 */

public class AndroidUtils {
    public static void openActivity(final Context context, final String intentStr) {
        try {
            Intent intent = Intent.parseUri(intentStr, 0);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static void screenOff(final Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (pm.isInteractive()) {
            try {
                pm.getClass().getMethod("goToSleep", new Class[]{long.class})
                        .invoke(pm, SystemClock.uptimeMillis());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取手机IMEI号
     */
    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            Logger.d("getIMEI------->READ_PHONE_STATE permission is not granted");
            return "null";
        }
        String imei = "";
        imei = telephonyManager.getDeviceId();
        Logger.d("getIMEI------->result = " + imei);
        return imei;
    }

    public static void sendKey(final int keycode) {
        Instrumentation instrumentation = new Instrumentation();
        instrumentation.sendKeyDownUpSync(keycode);
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
