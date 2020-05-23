package com.rokid.glass.libbase.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.os.PowerManager;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SysUtils {

    private static final String TAG = SysUtils.class.getSimpleName();

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;



    /**
     * @param type
     * @return
     */
    public static File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), TAG);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "failed to create directory");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
//            outputMediaFileType = "image/*";
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_" + timeStamp + ".mp4");
//            outputMediaFileType = "video/*";
        } else {
            return null;
        }
//        outputMediaFileUri = Uri.fromFile(mediaFile);
        return mediaFile;
    }

    /**
     * 唤醒手机屏幕并解锁
     */
    public static void wakeUpAndUnlock() {
        // 获取电源管理器对象
        PowerManager pm = (PowerManager)ContextUtil.getApplicationContext()
                .getSystemService(Context.POWER_SERVICE);
        boolean screenOn = pm.isScreenOn();
        if (!screenOn) {
            // 获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
            @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl = pm.newWakeLock(
                    PowerManager.ACQUIRE_CAUSES_WAKEUP |
                            PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");
            wl.acquire(10000); // 点亮屏幕
            wl.release(); // 释放
        }
//        // 屏幕解锁
//        KeyguardManager keyguardManager = (KeyguardManager) BaseLibrary.getInstance().getContext()
//                .getSystemService(KEYGUARD_SERVICE);
//        KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("unLock");
//        // 屏幕锁定
//        keyguardLock.reenableKeyguard();
//        keyguardLock.disableKeyguard(); // 解锁
    }
}
