package com.rokid.glass.libbase.faceid.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * zhuohf1 @2019.5.9
 */

@Database(entities = {FaceMapping.class, UserInfo.class, DeployDbInfo.class}, version = 2, exportSchema = false)
public abstract class FaceIdDatabase extends RoomDatabase {
    private static FaceIdDatabase fdb;

    public static FaceIdDatabase create(final Context context, final String dbname) {
        if (null == fdb) {
            fdb = Room.databaseBuilder(context, FaceIdDatabase.class, dbname)
                    .setJournalMode(JournalMode.TRUNCATE)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return fdb;
    }

    public abstract FaceMappingDao faceMappingDao();
    public abstract UserInfoDao userInfoDao();
    public abstract DeployTaskInfoDao deployTaskInfoDao();

//    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
//        @Override
//        public void migrate(SupportSQLiteDatabase database) {
//            // Since we didn't alter the table, there's nothing else to do here.
//            //  v1->v2 添加新的DeployDbInfo表
//            Logger.e("FaceIdManager:  开始数据库升级 v1->v2");
//            database.execSQL("CREATE TABLE IF NOT EXISTS tbl_deploy_task_info (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, keyStr TEXT, valueStr TEXT)");
//            database.execSQL("CREATE INDEX IF NOT EXISTS index_tbl_deploy_task_info_id on tbl_deploy_task_info(id)");
//
//            // 删除tagId列
//            database.execSQL("CREATE TABLE tbl_user_backup AS SELECT uid, name, cardno, nativeplace FROM tbl_user");
//            database.execSQL("DROP TABLE tbl_user");
//            database.execSQL("ALTER TABLE tbl_user_backup RENAME TO tbl_user");
//            // 增加description和isAlarm列
//            database.execSQL("ALTER TABLE tbl_user ADD COLUMN description TEXT");
//            database.execSQL("ALTER TABLE tbl_user ADD COLUMN isAlarm INTEGER");
//
//            // 删除feature.db
////            FaceIdManager.getInstance().removeAllData();
//        }
//    };

    public void closeDb() {
        if (fdb != null) {
            try {
                fdb.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                fdb = null;
            }
        }
    }
}
