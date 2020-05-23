package com.rokid.glass.libbase.faceid.database;
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {SingleFaceRecord.class,DeployFaceRecord.class}, version = 1, exportSchema = false)
public abstract class RecogRecordDatabase extends RoomDatabase {
    private static RecogRecordDatabase fdb;

    public static RecogRecordDatabase create(final Context context, final String dbname) {
        if (null == fdb) {
            fdb = Room.databaseBuilder(context, RecogRecordDatabase.class, dbname)
                    .setJournalMode(RoomDatabase.JournalMode.TRUNCATE).allowMainThreadQueries()
                    .build();
        }

        return fdb;
    }

    public abstract SingleRecogRecordDao singleRecogRecordDao();
    public abstract DeployRecogRecordDao deployRecogRecordDao();

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
