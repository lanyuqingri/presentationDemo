package com.rokid.glass.viewcomponent.glass;

import com.rokid.glass.libbase.logger.Logger;
import com.rokid.glass.libbase.utils.DefaultSPHelper;

public class CameraParams {

    public final static String KEY_SP_CAMERA_PREVIEW_WIDTH = "sp.camera.preview.width";
    public final static String KEY_SP_CAMERA_PREVIEW_HEIGHT = "sp.camera.preview.height";

    public final static int PREVIEW_WIDTH;
    public final static int PREVIEW_HEIGHT;

    // 默认设置的相机预览尺寸
    public final static int DEFAULT_PREVIEW_WIDTH = 1280;
    public final static int DEFAULT_PREVIEW_HEIGHT = 720;

    // 录像分辨率，这个是固定的
    public final static int VIDEO_WIDTH = 1280;
    public final static int VIDEO_HEIGHT = 720;

    public final static int VIDEO_CROP_LEFT;
    public final static int VIDEO_CROP_TOP;
    public final static int VIDEO_DATA_WIDTH;
    public final static int VIDEO_DATA_HEIGHT;

    static {
        PREVIEW_WIDTH = DefaultSPHelper.getInstance().getInt(KEY_SP_CAMERA_PREVIEW_WIDTH, DEFAULT_PREVIEW_WIDTH);
        PREVIEW_HEIGHT = DefaultSPHelper.getInstance().getInt(KEY_SP_CAMERA_PREVIEW_HEIGHT, DEFAULT_PREVIEW_HEIGHT);
        Logger.d("CameraParams: PREVIEW_WIDTH="+PREVIEW_WIDTH+", PREVIEW_HEIGHT="+PREVIEW_HEIGHT);

        // 确定如何裁剪YUV，这里确保preview的尺寸大于预览尺寸
        float previewRatio = (float)PREVIEW_WIDTH / PREVIEW_HEIGHT; // 1.333
        float videoRatio = (float)VIDEO_WIDTH / VIDEO_HEIGHT; // 1.777

        if (videoRatio > previewRatio) {
            Logger.d("CameraParams: 截取高度 videoRatio="+videoRatio+", previewRatio="+previewRatio);
            VIDEO_CROP_LEFT = 0;
            VIDEO_CROP_TOP = (PREVIEW_HEIGHT - PREVIEW_WIDTH * VIDEO_HEIGHT / VIDEO_WIDTH) / 2;
            VIDEO_DATA_WIDTH = PREVIEW_WIDTH;
            VIDEO_DATA_HEIGHT = PREVIEW_HEIGHT - VIDEO_CROP_TOP * 2;
        }
        else if (videoRatio < previewRatio) {
            Logger.d("CameraParams: 截取宽度 videoRatio="+videoRatio+", previewRatio="+previewRatio);
            VIDEO_CROP_LEFT = (PREVIEW_WIDTH - PREVIEW_HEIGHT * VIDEO_WIDTH / VIDEO_HEIGHT) / 2;
            VIDEO_CROP_TOP = 0;
            VIDEO_DATA_WIDTH = PREVIEW_WIDTH - VIDEO_CROP_LEFT * 2;
            VIDEO_DATA_HEIGHT = PREVIEW_HEIGHT;
        }
        else {
            VIDEO_CROP_LEFT = 0;
            VIDEO_CROP_TOP = 0;
            VIDEO_DATA_WIDTH = PREVIEW_WIDTH;
            VIDEO_DATA_HEIGHT = PREVIEW_HEIGHT;
            Logger.d("CameraParams: 录像和预览尺寸一样");
        }
    }

}
