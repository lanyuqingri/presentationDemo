package com.rokid.glass.libbase.utils;

import android.graphics.Bitmap;

import java.nio.ByteBuffer;

/**
 * @date 2019-07-29
 */

public class BitmapUtils {
    public static Bitmap loadBitmap(final byte[] bytes, final int width, final int height) {
        if (null == bytes) {
            return null;
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.copyPixelsFromBuffer(ByteBuffer.wrap(bytes));
        return bitmap;
    }
}
