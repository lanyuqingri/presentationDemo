package com.rokid.glass.presentationdemo;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import androidx.multidex.MultiDex;

import com.rokid.glass.alliance.RokidSDK;
import com.rokid.glass.libbase.logger.Logger;


public class PresentationApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RokidSDK.getInstance().initApp(this);
        fixDisplay();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void fixDisplay() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                realFix(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

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
    }

    private void realFix(Activity activity) {
        final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        final float targetDensity = displayMetrics.widthPixels / 360;
        final int targetDensityDpi = (int) (160 * targetDensity);
        Logger.d("realFix------>targetDensity = " + targetDensity);
        Logger.d("realFix------>targetDensityDpi = " + targetDensityDpi);
        displayMetrics.density = displayMetrics.scaledDensity = targetDensity;
        displayMetrics.densityDpi = targetDensityDpi;

        final DisplayMetrics activityDisplayMetrics = activity.getResources().getDisplayMetrics();
        activityDisplayMetrics.density = activityDisplayMetrics.scaledDensity = targetDensity;
        activityDisplayMetrics.densityDpi = targetDensityDpi;


    }
}
