package com.rokid.glass.viewcomponent.glass;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.rokid.facelib.face.FaceDbHelper;

import java.io.File;


/**
 * @author cc
 */
public class InitDateBaseModule implements IInitDateBase {

    private static final String TAG = InitDateBaseModule.class.getSimpleName();
    private static final String DATABASE_FILENAME = "facemapping.db";
    private final Context context;
    private static final String USER_MAP = "usermap";
    private static final String IMG_FACE = "faceImg";
    private static final String IMG_UUID = "uuid";
    private static Object mObject = new Object();

    private static final String ROKID_APP_DATA_PATH = "/data/data/com.rokid.camera.cameradeploy/databases/";

    private static SQLiteDatabase mSqliteDataBase;

    public InitDateBaseModule(Context context) {
        this.context = context;
    }

    @Override
    public void doInitBase() {


        initDataBase();
    }

    @Override
    public void onDestory() {


        if (mSqliteDataBase != null) {
            mSqliteDataBase.close();
            mSqliteDataBase.releaseReference();
            mSqliteDataBase = null;
        }
    }

    private static void initDataBase() {
        try {
            if(mSqliteDataBase == null) {
                String path = FaceDbHelper.PATH_OUTPUT + DATABASE_FILENAME;
                if (!new File(path).exists()) {
                    return;
                }
                mSqliteDataBase = SQLiteDatabase.openDatabase(FaceDbHelper.PATH_OUTPUT + DATABASE_FILENAME, null, SQLiteDatabase.OPEN_READONLY);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param uuid 人脸的UUID
     * @return 图片的字节流
     * @deprecated
     */
    public static byte[] getImageNumber(byte[] uuid) {

        byte[] image = null;
        final String sql = "SELECT * FROM face_mapping WHERE uuid LIKE ? LIMIT 1";
        Cursor cursor = mSqliteDataBase.rawQuery(sql, new String[]{new String(uuid)});
        if (cursor.moveToFirst()) {
            do {
                image = cursor.getBlob(cursor.getColumnIndex(IMG_FACE));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return image;
    }

    /**
     *
     * @param uuid 人脸的UUID
     * @return 图片的字节流
     */
    public static byte[] getStringImageNumber(String uuid) {

        byte[] image = null;
        final String sql = "SELECT * FROM face_mapping WHERE uuid LIKE ? LIMIT 1";

        synchronized (mObject) {
            if (mSqliteDataBase != null) {
                Cursor cursor = mSqliteDataBase.rawQuery(sql, new String[]{uuid});
                if (cursor.moveToFirst()) {
                    do {
                        image = cursor.getBlob(cursor.getColumnIndex(IMG_FACE));
                    } while (cursor.moveToNext());
                }
                cursor.close();
            }
        }
        return image;
    }
}
