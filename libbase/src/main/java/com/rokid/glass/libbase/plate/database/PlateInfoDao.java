package com.rokid.glass.libbase.plate.database;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.rokid.glass.libbase.faceid.FaceConstants;
import com.rokid.glass.libbase.plate.PlateInfo;

import java.util.List;

@Dao
public interface PlateInfoDao {
    @Query("SELECT * FROM " + FaceConstants.PLATE_TABLE)
    List<PlateInfo> getAllPlateDbInfo();

    @Query("SELECT COUNT(*) FROM " + FaceConstants.PLATE_TABLE)
    int getPlateCount();

    @Query("SELECT * FROM " + FaceConstants.PLATE_TABLE + " WHERE plate = :plate LIMIT 1" )
    PlateInfo queryPlateInfoByPlate(String plate);

    @Query("SELECT * FROM " + FaceConstants.PLATE_TABLE + " LIMIT :limit OFFSET :offset")
    List<PlateInfo> queryPlateInfos(int offset, int limit);

    @Query("SELECT * FROM " + FaceConstants.PLATE_TABLE + " WHERE plate LIKE '%' || :searchKey || '%' or owner LIKE '%' || :searchKey || '%' LIMIT :limit OFFSET :offset ")
    List<PlateInfo> queryPlateInfos(int offset, int limit, String searchKey);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addPlateInfo(PlateInfo info);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updatePlateInfo(PlateInfo info);

    @Query("DELETE FROM "+FaceConstants.PLATE_TABLE+" WHERE plate = :plate")
    void removePlateInfoByPlate(String plate);

    @Query("DELETE FROM " + FaceConstants.PLATE_TABLE)
    void removeAllPlateDbInfo();
}
