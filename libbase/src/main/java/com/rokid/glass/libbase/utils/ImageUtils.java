package com.rokid.glass.libbase.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class ImageUtils {

    public static byte[] Bitmap2Bytes(Bitmap bm) {
        if (bm == null) return null;

        Bitmap bitmap = compressScale(bm);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);

        byte[] bytes = baos.toByteArray();
        try {
            baos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public static Bitmap Bytes2Bitmap(byte[] b) {
        if (b == null) return null;
        if (b.length != 0) {
            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            } catch (OutOfMemoryError e) {
                return null;
            }
            return bitmap;
        } else {
            return null;
        }
    }






    private static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 90;
        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset(); // 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        try {
            if (isBm != null) {
                isBm.close();
            }
            if (baos != null) {
                baos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }



    public static byte[] bitmap2Bytes(Bitmap bm) {
        if (bm == null) return null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        return baos.toByteArray();
    }



    public static String getPath(Uri uri, Context context) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().
                query(uri, filePathColumn, null, null, null);
        String path = null;
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            path = cursor.getString(columnIndex);
            cursor.close();
        }
        return path;
    }

    public static Bitmap getRotateBitmap(String imagePath) {
        Bitmap photo = BitmapFactory.decodeFile(imagePath);
        int rotation = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(imagePath);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotation = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotation = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotation = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (rotation == 0) {
            return photo;
        } else {
            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);
            return Bitmap.createBitmap(photo, 0, 0, photo.getWidth(), photo.getHeight(), matrix, true);
        }
    }

    private static final int IN_SAMPLE_SIZE = 10;
    private static final float PREFER_WIDTH = 720.f / IN_SAMPLE_SIZE;
    private static final float PREFER_HEIGHT = 1280.f / IN_SAMPLE_SIZE;

    public static Bitmap getResizeRotateBitmap(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = IN_SAMPLE_SIZE;
        int rotation = getBitmapDegree(path);

        Bitmap bitmap = BitmapFactory.decodeFile(path, options);

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        if(rotation == 90 || rotation == 270) {
            int tmp = width;
            width = height;
            height = tmp;
        }

        bitmap.recycle();

        if(width > PREFER_WIDTH || height > PREFER_HEIGHT) {
            float scale = Math.max(width / PREFER_WIDTH, height / PREFER_HEIGHT);
            options.inSampleSize  = Math.round(scale);
            bitmap = BitmapFactory.decodeFile(path, options);
            bitmap = rotateBitmap(bitmap, rotation, false);

        } else {
            bitmap = getRotateBitmap(path);
        }

        return bitmap;
    }


    public static File resizeRotateBitmapFile(File file) {
        File result = null;
        try {
            result = File.createTempFile(".tmp", ".jpeg", file.getParentFile());
            result.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
            result = file;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = IN_SAMPLE_SIZE;
        int rotation = getBitmapDegree(file.getAbsolutePath());

        Bitmap bitmap = null;

        bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);


        if(bitmap == null) {
            result.delete();
            return null;
        }

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        if(rotation == 90 || rotation == 270) {
            int tmp = width;
            width = height;
            height = tmp;
        }

        bitmap.recycle();

        if(width > PREFER_WIDTH || height > PREFER_HEIGHT) {
            float scale = Math.max(width / PREFER_WIDTH, height / PREFER_HEIGHT);
            options.inSampleSize  = Math.round(scale);
            bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
            bitmap = rotateBitmap(bitmap, rotation, false);
            saveBitmap2File(bitmap, result.getAbsolutePath());
        } else {
            bitmap = getRotateBitmap(file.getAbsolutePath());
            saveBitmap2File(bitmap, result.getAbsolutePath());
        }

        if(bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }

        return result;
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int rotation, boolean isMirror) {
        if(bitmap == null || bitmap.isRecycled()) return null;

        Matrix matrix = new Matrix();
        matrix.postRotate(rotation);
        if(isMirror)
            matrix.postScale(-1, 1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }


    private static Bitmap compressScale(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        // 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
        if (baos.toByteArray().length / 1024 > 1024) {
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 80, baos);// 这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        // float hh = 800f;// 这里设置高度为800f
        // float ww = 480f;// 这里设置宽度为480f
        float hh = 150f;
        float ww = 150f;
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w >= h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) { // 如果高度高的话根据高度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be; // 设置缩放比例
        // newOpts.inPreferredConfig = Config.RGB_565;//降低图片从ARGB888到RGB565
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
        //return bitmap;
    }

    public static void saveBitmap2File(Bitmap bitmap, String path, int quality) {
        if(bitmap == null || bitmap.isRecycled()) return;
        OutputStream os = null;
        try {
            //写大图到缓存目录
            File tmpImgFile = new File(path);
            os = new FileOutputStream(tmpImgFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, os);
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(os != null)
                    os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveBitmap2File(Bitmap bitmap, String path) {
        saveBitmap2File(bitmap, path, 100);
    }


    public static void saveData2File(byte[] data, String path) {
        OutputStream os = null;
        InputStream is = null;
        try {
            //写原始jpeg数据到目录
            os = new FileOutputStream(new File(path));
            is = new ByteArrayInputStream(data);
            int len = 0;
            byte[] buff = new byte[1024];
            while((len=is.read(buff))!=-1){
                os.write(buff, 0, len);
            }
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(os != null)
                    os.close();
                if(is != null)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Bitmap getBitmapFromFile(String path) {
        File file = new File(path);
        if(!file.exists())
            return null;
        try {
            InputStream is = new FileInputStream(file);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 读取图片的旋转的角度
     *
     * @param path
     *            图片绝对路径
     * @return 图片的旋转角度
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }



    public static Bitmap  nv21ToBitmap(byte[] nv21, int width, int height, Rect rect) {
        Bitmap bitmap = null;
        try {
            YuvImage image = new YuvImage(nv21, ImageFormat.NV21, width, height, null);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            if(rect.left<0){
                rect.left = 0;
            }
            if(rect.right>width){
                rect.right = width;
            }
            if(rect.top<0){
                rect.top = 0;
            }
            if(rect.bottom>height){
                rect.bottom = height;
            }

            image.compressToJpeg(rect, 100, stream);
            bitmap = BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.size());
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static String nv21ToFile(byte[] nv21, String path, int width, int height, Rect rect) {
        File file = null;
        try {
            YuvImage image = new YuvImage(nv21, ImageFormat.NV21, width, height, null);
            FileOutputStream stream = new FileOutputStream(new File(path));
            if(rect.left<0){
                rect.left = 0;
            }
            if(rect.right>width){
                rect.right = width;
            }
            if(rect.top<0){
                rect.top = 0;
            }
            if(rect.bottom>height){
                rect.bottom = height;
            }

            image.compressToJpeg(rect, 100, stream);

            stream.flush();
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return path;
    }


    public static String nv21ToJpgString(byte[] nv21, int width, int height, Rect rect) {
        String result;
        byte[] bytes = null;
        try {
            YuvImage image = new YuvImage(nv21, ImageFormat.NV21, width, height, null);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            if(rect.left<0){
                rect.left = 0;
            }
            if(rect.right>width){
                rect.right = width;
            }
            if(rect.top<0){
                rect.top = 0;
            }
            if(rect.bottom>height){
                rect.bottom = height;
            }

            image.compressToJpeg(rect, 100, baos);

            bytes = baos.toByteArray();

            result = byteToBase64(bytes);

            baos.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }

    public static String byteToBase64(byte[] data) {
        return Base64.encodeToString(data, Base64.NO_WRAP);
    }
    public static byte[] base64ToByte(String data) {
        return Base64.decode(data, Base64.NO_WRAP);
    }

    public static Bitmap base64ToBitmap(String base64) {
        if(TextUtils.isEmpty(base64)) return null;
        byte[] data = ImageUtils.base64ToByte(base64);
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }
}
