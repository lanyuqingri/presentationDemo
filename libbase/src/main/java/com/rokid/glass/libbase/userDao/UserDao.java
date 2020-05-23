package com.rokid.glass.libbase.userDao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(User user);

    @Query("DELETE FROM " + Constant.USER_TABLE_NAME)
    void deleteAll();

    @Query("SELECT * FROM " + Constant.USER_TABLE_NAME + " LIMIT 1" )
    User queryUser();
}
