package com.rokid.glass.libbase.faceid;

import android.graphics.Color;
import android.os.Environment;

import java.io.File;

/**
 * zhuohf1 @2019.5.9
 */
public class FaceConstants {

    public static final int WHITE_COLOR = Color.parseColor("#7fffffff");
    public static final int GRAY_WHITE_COLOR = Color.parseColor("#80ffffff");
    public static final int GREEN_COLOR = Color.parseColor("#FF5A2AFF");
    public static final int BLUE_COLOR = Color.parseColor("#b47fd125");
    public static final int RED_COLOR = Color.parseColor("#FFFF5821");

    public final static String FACE_DB_COPY_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator +"facedb/";

    public final static String FACE_FEATURE_DB = "feature.db";
    public final static String FACE_SEARCH_ENGINE = "SearchEngine.bin";
    public final static String FACE_ID_DB = "faceid.db";
    public final static String RECOG_RECORD_DB = "record.db";
    public final static String PLATE_DB = "plate.db";

    public final static String FACE_USER_TABLE = "tbl_user";
    public final static String FACE_MAPPING_TABLE = "tbl_mapping";
    public final static String FACE_TAG_TABLE = "tbl_tag";

    public final static String ROKID_FACE_QUALITY = "rokid_face_quality";
    public final static int DEFAULT_FACE_QUALITY_VALUE = 80;

    /**增加布控信息表*/
    public static final String DEPLOY_TASK_INFO_TABLE = "tbl_deploy_task_info";

    public final static String FACE_SINGLE_RECOG_TABLE = "tbl_recog_face_single";
    public final static String FACE_DEPLOY_RECOG_TABLE = "tbl_recog_face_deploy";
    public final static String PLATE_DEPLOY_RECOG_TABLE = "tbl_recog_plate_deploy";
    public final static String PLATE_TABLE = "tbl_plate";
    public final static String KEY_PLATE_IS_ONLINE = "plateOnline";

    public final static String ROKID_ONLINE_FACE_DEBUG_KEY = "rokid_online_face_debug_key";


    public static final String ACTION_FACE_ID_UPDATE_START = "com.rokid.facedatebase_update_start";
    public static final String ACTION_FACE_ID_UPDATE_DONE = "com.rokid.facedatebase_update_done";

    // 特征库包拷贝成功
    public static final String ACTION_FACE_ID_COPY_DB_DONE = "com.rokid.facedatebase_copy_db_done";

    // 人脸布控包信息变化
    public static final String ACTION_FACE_DEPLOY_INFO_CHANGED = "com.rokid.face_deploy_info_changed";

    // 数据库变化
    public static final String ACTION_FACE_ID_DB_CHANGE = "com.rokid.facedatebase_db_change";

    public static final String KEY_NEW_FACE_RECORD = "key_new_face_record";
    public static final String KEY_NEW_PLATE_RECORD = "key_new_plate_record";
}
