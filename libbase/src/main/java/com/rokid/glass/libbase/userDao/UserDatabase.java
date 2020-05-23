package com.rokid.glass.libbase.userDao;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.rokid.glass.libbase.logger.Logger;

@Database(entities = {User.class}, version = 1, exportSchema = false)
public abstract class UserDatabase extends RoomDatabase {
    private static UserDatabase userDatabase;

    public static UserDatabase create(final Context context, final String dbname) {
        if (null == userDatabase) {
            Logger.d("PlateDatabase--------->create DataBase && dbName: " + dbname);
            userDatabase = Room.databaseBuilder(context, UserDatabase.class, dbname)
                    .setJournalMode(JournalMode.TRUNCATE)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return userDatabase;
    }

    public void closeDb() {
        if (userDatabase != null) {
            try {
                userDatabase.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                userDatabase = null;
            }
        }
    }

    public abstract  UserDao userDao();
}
