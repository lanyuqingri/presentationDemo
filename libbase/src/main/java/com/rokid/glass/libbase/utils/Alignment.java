package com.rokid.glass.libbase.utils;


import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @date 2019-08-08
 */

public class Alignment {
    public static final int align720p = 0;
    public static final int alignHD = 1;
    public static final int align2K = 2;

    @IntDef({align2K, alignHD, align720p})
    @Retention(RetentionPolicy.SOURCE)
    @interface type {

    }
}
