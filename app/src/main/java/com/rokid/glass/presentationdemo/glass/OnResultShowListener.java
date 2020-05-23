package com.rokid.glass.presentationdemo.glass;

import android.graphics.Bitmap;

public interface OnResultShowListener {
    void onResultShow();
    void onResultHide();
    void onFaceOnlineRecog(Bitmap bm);
}
