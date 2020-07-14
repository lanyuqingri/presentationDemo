package com.rokid.glass.libbase;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Pair;


import androidx.core.app.ActivityOptionsCompat;

import com.rokid.glass.libbase.config.AppConfig;
import com.rokid.glass.libbase.faceid.SmartRecgConfig;
import com.rokid.glass.libbase.music.MusicPlayHelper;
import com.rokid.glass.libbase.utils.DefaultSPHelper;
import com.rokid.glass.libbase.utils.DeviceInfoUtils;
import com.rokid.glass.libbase.utils.ViewUtils;

import java.util.concurrent.atomic.AtomicBoolean;

public class BaseLibrary {

    private static volatile BaseLibrary mInstance;

    private volatile Application application;
    private volatile Activity mTopActivity;
    private volatile Pair<Integer,Integer> mScreenSize;
    private AtomicBoolean isMultiFaceRecg = new AtomicBoolean(false);
    private AtomicBoolean isSingleFaceRecg = new AtomicBoolean(true);
    private AtomicBoolean isPlateRecg = new AtomicBoolean(true);
    private AtomicBoolean isSingleFaceOnlineRecg = new AtomicBoolean(false);
    private AtomicBoolean isPlateOnlineRecg = new AtomicBoolean(false);

    private ActivityOptionsCompat mTransitionActivityOptions;

    public static BaseLibrary getInstance() {
        if (null == mInstance) {
            synchronized (BaseLibrary.class) {
                if (null == mInstance) {
                    mInstance = new BaseLibrary();
                }
            }
        }
        return mInstance;
    }

    public Application getContext() {
        return this.application;
    }

    public static void initialize(Application application) {
        getInstance().application = application;
        MusicPlayHelper.getInstance().init(application);
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                getInstance().setTopActivity(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {
                getInstance().setTopActivity(activity);
            }

            @Override
            public void onActivityResumed(Activity activity) {
                getInstance().setTopActivity(activity);
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
        getInstance().mScreenSize = ViewUtils.getScreenSize(application.getApplicationContext());
    }

    public void release() {
    }


    public int getVoiceWakeup() {
        // 如果是一代眼镜，就把语音功能都关闭
        if (!getEnableVoice()) {
            return AppConfig.Settings.VOICE_OFF;
        }
        int wakeup = Settings.Global.getInt(
                application.getContentResolver(),
                AppConfig.Settings.DB_VOICE_WAKEUP_KEY, AppConfig.Settings.VOICE_ON);
        return wakeup;
    }

    public Activity getTopActivity() {
        return mTopActivity;
    }

    public ActivityOptionsCompat getTransitionActivityOptions() {
        return mTransitionActivityOptions;
    }

    public BaseLibrary setTransitionActivityOptions(ActivityOptionsCompat transitionActivityOptions) {
        this.mTransitionActivityOptions = transitionActivityOptions;
        return this;
    }

    public BaseLibrary setTopActivity(Activity topActivity) {
        this.mTopActivity = topActivity;
        return this;
    }



    public Pair<Integer, Integer> getScreenSize() {
        return mScreenSize;
    }


    private volatile boolean mEnableMultiFace;
    public void setEnableMultiFace(boolean enable) {
        this.mEnableMultiFace = enable;
    }

    public boolean getEnableMultiFace() {
        return this.mEnableMultiFace;
    }

    public void initSmartRecgConfig(){
        isMultiFaceRecg.set(DefaultSPHelper.getInstance().getBoolean(SmartRecgConfig.KEY_MULTI_FACE_RECG_OPEN,false));
        isSingleFaceRecg.set(DefaultSPHelper.getInstance().getBoolean(SmartRecgConfig.KEY_SINGLE_FACE_RECG_OPEN,true));
        isSingleFaceOnlineRecg.set(DefaultSPHelper.getInstance().getBoolean(SmartRecgConfig.KEY_SINGLE_RECG_ONLINE,true));
        isPlateRecg.set(DefaultSPHelper.getInstance().getBoolean(SmartRecgConfig.KEY_PLATE_RECG_OPEN,true));
        isPlateOnlineRecg.set(DefaultSPHelper.getInstance().getBoolean(SmartRecgConfig.KEY_PLATE_RECG_ONLINE,true));
    }

    public void setMultiFaceRecgEnable(boolean isEnable){
        isMultiFaceRecg.set(isEnable);
        DefaultSPHelper.getInstance().put(SmartRecgConfig.KEY_MULTI_FACE_RECG_OPEN,isEnable);
    }

    public void setSingleFaceRecg(boolean isEnable){
        isSingleFaceRecg.set(isEnable);
        DefaultSPHelper.getInstance().put(SmartRecgConfig.KEY_SINGLE_FACE_RECG_OPEN,isEnable);
    }

    public void setSingleFaceOnlineRecg(boolean isEnable){
        isSingleFaceOnlineRecg.set(isEnable);
        DefaultSPHelper.getInstance().put(SmartRecgConfig.KEY_SINGLE_RECG_ONLINE,isEnable);
    }

    public void setPlateRecg(boolean isEnable){
        isPlateRecg.set(isEnable);
        DefaultSPHelper.getInstance().put(SmartRecgConfig.KEY_PLATE_RECG_OPEN,isEnable);
    }

    public void setPlateOnlineRecg(boolean isEnable){
        isPlateOnlineRecg.set(isEnable);
        DefaultSPHelper.getInstance().put(SmartRecgConfig.KEY_PLATE_RECG_ONLINE,isEnable);
    }

    public boolean isMultiFaceRecgEnable(){
        return isMultiFaceRecg.get();
    }

    public boolean isSingleFaceRecgEnable(){
        return isSingleFaceRecg.get();
    }

    public boolean isOnlineSingleFaceRecgEnable(){
        return isSingleFaceOnlineRecg.get();
    }

    public boolean isPlateRecgEnable(){
        return isPlateRecg.get();
    }

    public boolean isOnlinePlateRecg(){
        return isPlateOnlineRecg.get();
    }
    /**
     * 是否开启语音
     * @return
     */
    public boolean getEnableVoice() {
        if (DeviceInfoUtils.isGenerationOneGlass()) {
            return false;
        } else {
            return true;
        }
    }
}
