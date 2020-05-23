package com.rokid.glass.libbase.plate.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.rokid.glass.libbase.logger.Logger;
import com.rokid.glass.libbase.plate.PlateInfo;
import com.rokid.glass.libbase.plate.PlateRecogRecord;

@Database(entities = {PlateInfo.class, PlateRecogRecord.class}, version = 1, exportSchema = false)
public abstract class PlateDatabase extends RoomDatabase {
    private static PlateDatabase plateDatabase;

    public static PlateDatabase create(final Context context, final String dbname) {
        if (null == plateDatabase) {
            Logger.d("PlateDatabase--------->create DataBase && dbName: " + dbname);
            plateDatabase = Room.databaseBuilder(context, PlateDatabase.class, dbname)
                    .setJournalMode(JournalMode.TRUNCATE)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return plateDatabase;
    }

    public void closeDb() {
        if (plateDatabase != null) {
            try {
                plateDatabase.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                plateDatabase = null;
            }
        }
    }

    public abstract PlateInfoDao plateInfoDao();
    public abstract PlateRecogRecordDao plateRecogRecordDao();
}
