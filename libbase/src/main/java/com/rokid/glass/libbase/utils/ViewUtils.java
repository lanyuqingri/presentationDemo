package com.rokid.glass.libbase.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

/**
 * utils
 */
public class ViewUtils {
    public static View inflate(final Context context, int layoutId) {
        return LayoutInflater.from(context).inflate(layoutId, null);
    }

    public static Pair<Integer, Integer> getScreenSize(final Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;
        return new Pair<>(width, height);
    }
}
