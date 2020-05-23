package com.rokid.glass.libbase.utils;

import android.content.Context;
import android.text.TextUtils;

/**
 * Created by tt on 2018/10/20.
 */

public class ReplaceRokidUtil {

    public static String replaceRokid(Context context, String text) {
        if (TextUtils.isEmpty(text)) {
            return "";
        }

//        if (text.contains("若琪")) {
//            String vendorName = AppCenter.Companion.getInfo().getVendorName();
//            text = text.replace("若琪", vendorName);
//        }
        return text;
    }

    public static CharSequence replaceRokid(Context context, CharSequence text) {
        if (TextUtils.isEmpty(text)) {
            return "";
        }

//        if (text.toString().contains("若琪")) {
//            String vendorName = AppCenter.Companion.getInfo().getVendorName();
//            text = text.toString().replace("若琪", vendorName);
//        }
        return text;
    }
}
