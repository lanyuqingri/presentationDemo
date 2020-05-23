package com.rokid.glass.libbase.config;

import android.os.Environment;

/**
 * @date 2019-09-03
 */

public interface GalleryConfig {
    String DIR_BASE = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
    String DIR_VIDEO = DIR_BASE + "/Camera";

    String DIR_RECOG_CROP = DIR_BASE + "/shellnon/recog/crop/"; //单人识别
    String DIR_DEPLOY_CROP = DIR_BASE + "/shellnon/deploy/crop/"; //布控
    String DIR_PLATE_CROP = DIR_BASE + "/shellnon/plate/crop/";   //车牌

    String DIR_RECOG_TOTAL = DIR_BASE + "/shellnon/recog/total/"; //单人识别
    String DIR_DEPLOY_TOTAL = DIR_BASE + "/shellnon/deploy/total/"; //布控
    String DIR_PLATE_TOTAL = DIR_BASE + "/shellnon/plate/total/";   //车牌

    String DELETE_FILE_PATH = "file_path";

    String DIR_TEST = DIR_BASE+"/shellnon/test/";

    interface KEYS {
        String VIDEO_LIST = "key_video_list";
        String PHOTO_LIST = "key_photo_list";
        String PHOTO_LIST_DELETE = "key_photo_list_delete";
    }

    interface ACTION{
        String STOP_RECORD = "stop_record";
    }

    interface TYPE{
        int RECOG = 0;
        int DEPLOY = 1;
        int PLATE = 2;
        int VIDEO_RECORD = 3;
    }
}
