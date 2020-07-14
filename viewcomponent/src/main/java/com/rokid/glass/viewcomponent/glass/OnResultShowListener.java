package com.rokid.glass.viewcomponent.glass;

import android.graphics.Bitmap;

public interface OnResultShowListener {
    void onResultShow();
    void onResultHide();
    void onFaceOnlineRecog(Bitmap bm);
}
