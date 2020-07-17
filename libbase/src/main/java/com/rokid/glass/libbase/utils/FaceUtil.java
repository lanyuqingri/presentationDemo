package com.rokid.glass.libbase.utils;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.RectF;

import com.rokid.facelib.model.FaceDO;
import com.rokid.facelib.model.FaceModel;
import com.rokid.facelib.utils.FaceRectUtils;
import com.rokid.glass.libbase.logger.Logger;

/**
 * 人脸图像数据处理工具类
 * Author: Zack
 * Email:  newzzack@gmail.com
 * Date:   2019.12.18 19:02
 */
public class FaceUtil {

    /**
     * 从NV21数据中抠图转化成Bitmap
     * @param data
     * @param faceDO
     * @param previewWidth
     * @param previewHeight
     * @return
     */
    public static Bitmap cropBitmapFromNV21(byte[] data, FaceDO faceDO, int previewWidth, int previewHeight) {
        Rect faceRect = computeCropRect(faceDO.faceRectF, previewWidth, previewHeight);

        if (data == null) {
            Logger.e("[CDFR] cropBitmapFromNV21 data == null");
            return null;
        }
        return ImageUtils.nv21ToBitmap(data, previewWidth, previewHeight, faceRect);
    }


    public static Bitmap cropBitmap(Bitmap srcBitmap, Rect faceRect) {
        Rect srcRect = faceRect;
        Rect dstRect = FaceRectUtils.toRect(
                srcRect, 1, srcBitmap.getWidth(), srcBitmap.getHeight());
        Logger.d("cropBitmap faceDO srcRect="+srcRect+", dstRect="+dstRect);

        int left = dstRect.left;
        int top = dstRect.top;
        int right = dstRect.right;
        int bottom = dstRect.bottom;

        int width = right - left, height = bottom - top;
        //人脸框会偏下，这里往上偏移1/8
        int centerX = left + width / 2, centerY = top + height / 8 * 3;
        int diameter = width > height ? width : height;

        //人脸框放大系数，尽可能保证抠出来的小图宽高相等
        float scaleFactor = 2.0f;
        float side = diameter * scaleFactor;
        int radius = (int)(side / 2);

        left = centerX - radius;
        if (left < 0) left = 0;
        right = centerX + radius;
        if (right > srcBitmap.getWidth()) right = srcBitmap.getWidth();
        top = centerY - radius;
        if (top < 0) top = 0;
        bottom = centerY + radius;
        if (bottom > srcBitmap.getHeight()) bottom = srcBitmap.getHeight();

        Bitmap cropBitmap = Bitmap.createBitmap(srcBitmap, left, top, right - left, bottom - top);
        return cropBitmap;
    }

    /**
     * 计算抠图区域
     * @param faceRectF
     * @param previewWidth
     * @param previewHeight
     * @return
     */
    private static Rect computeCropRect(RectF faceRectF, int previewWidth, int previewHeight) {
        int left = (int) (faceRectF.left * previewWidth);
        int top = (int) (faceRectF.top * previewHeight);
        int right = (int) (faceRectF.right * previewWidth);
        int bottom = (int) (faceRectF.bottom * previewHeight);

        int width = right - left, height = bottom - top;
        //人脸框会偏下，这里往上偏移1/8
        int centerX = left + width / 2, centerY = top + height / 8 * 3;
        int diameter = width > height ? width : height;

        //人脸框放到系数，尽可能保证抠出来的小图宽高相等
        float CROP_SCALE_FACTOR = 2.f;
        float side = diameter * CROP_SCALE_FACTOR;
        int radius = (int) (side / 2);

        left = centerX - radius;
        if (left < 0) left = 0;
        right = centerX + radius;
        if (right > previewWidth) right = previewWidth;
        top = centerY - radius;
        if (top < 0) top = 0;
        bottom = centerY + radius;
        if (bottom > previewHeight) bottom = previewHeight;

        return new Rect(left,top,right,bottom);
    }


    /**
     * 从NV21数据中抠图转换成Base64 String
     * @param data
     * @param faceDO
     * @param previewWidth
     * @param previewHeight
     * @return
     */
    public static String cropToBase64FromNV21(byte[] data, FaceDO faceDO, int previewWidth, int previewHeight) {

        Rect faceRect = computeCropRect(faceDO.faceRectF, previewWidth, previewHeight);

        if (data == null) {
            Logger.i("[CDFR] cropToBase64FromNV21 data == null");
            return null;
        }

        String dataStr = ImageUtils.nv21ToJpgString(data, previewWidth, previewHeight, faceRect);
        return dataStr;
    }


    public static boolean isGoodFace(FaceDO faceDO) {
        Logger.d("isGoodFace goodQuality: " + faceDO.goodQuality + "   faceScore: " + faceDO.faceScore);
        return faceDO.goodQuality && faceDO.faceScore > 0.75;
    }

    /**
     * 深拷贝FaceModel对象
     * @param faceModel
     * @return
     */
    public static FaceModel copyFaceModel(final FaceModel faceModel) {
        FaceModel copy = new FaceModel();
        copy.width = faceModel.width;
        copy.height = faceModel.height;

        if(faceModel.getFaceList() != null) {
            for(FaceDO faceDO : faceModel.getFaceList()) {
                FaceDO copyFace = new FaceDO();
                copyFace.faceRectF = faceDO.faceRectF;
                copyFace.trackId = faceDO.trackId;
                copyFace.goodQuality = faceDO.goodQuality;
                copyFace.goodPose = faceDO.goodPose;
                copyFace.qualityGoodEnough = faceDO.qualityGoodEnough;
                if(faceDO.pose != null)
                    copyFace.pose = faceDO.pose.clone();
                if(faceDO.pts != null)
                    copyFace.pts = faceDO.pts.clone();
                copyFace.userInfoScore = faceDO.userInfoScore;
                copyFace.quality = faceDO.quality;
                copyFace.recogOutTime = faceDO.recogOutTime;
                copyFace.trackInterval = faceDO.trackInterval;
                copyFace.faceScore = faceDO.faceScore;
                copyFace.featid = faceDO.featid;
                // 2019-12-18 识别图暂时不需要，没有用到。算法支持RGB图之后需要做拷贝操作
//                copyFace.recogBitmap = faceDO.recogBitmap;
                copy.addFace(copyFace);
            }
        }
        return copy;
    }
}
