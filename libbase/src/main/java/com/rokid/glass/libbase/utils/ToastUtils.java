package com.rokid.glass.libbase.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * @date 2019/12/5
 */

public class ToastUtils {
    public static void makeText(Context context, CharSequence text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void makeText(Context context, int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }
}
