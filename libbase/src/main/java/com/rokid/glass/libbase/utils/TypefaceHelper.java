package com.rokid.glass.libbase.utils;

import android.graphics.Typeface;


import com.rokid.glass.libbase.BaseLibrary;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Author: Shper
 * Version: V0.1 2017/8/30
 */
public class TypefaceHelper {

    private static volatile TypefaceHelper mInstance;
    private final AtomicReference<Typeface> typefaceAtom = new AtomicReference<>();

    public static TypefaceHelper getInstance() {
        if (null == mInstance) {
            synchronized (TypefaceHelper.class) {
                if (null == mInstance) {
                    mInstance = new TypefaceHelper();
                }
            }
        }

        return mInstance;
    }

    public Typeface getIconFontTypeface() {
        if (null == typefaceAtom.get()) {
            typefaceAtom.getAndSet(Typeface.createFromAsset(BaseLibrary.getInstance().getContext().getAssets(),
                    "iconfont/iconfont.ttf"));
        }
        return typefaceAtom.get();
    }

}
