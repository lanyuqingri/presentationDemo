package com.rokid.glass.libbase.plate.database;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.rokid.glass.libbase.faceid.FaceConstants;
import com.rokid.glass.libbase.plate.PlateRecogRecord;

import java.util.List;

@Dao
public interface PlateRecogRecordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addPlateRecogRecord(PlateRecogRecord plateRecord);

    @Query("SELECT COUNT(*) FROM " + FaceConstants.PLATE_DEPLOY_RECOG_TABLE)
    int getPlateRecordCount();

    @Query("SELECT * FROM " + FaceConstants.PLATE_DEPLOY_RECOG_TABLE + " ORDER BY gmtTime DESC LIMIT :limit OFFSET :offset")
    List<PlateRecogRecord> queryPlateRecordList(int offset, int limit);

    @Query("SELECT * FROM " + FaceConstants.PLATE_DEPLOY_RECOG_TABLE + " WHERE recordId = :recordId LIMIT 1" )
    PlateRecogRecord queryPlateRecordInfoByRecordId(String recordId);

    @Query("SELECT * FROM " + FaceConstants.PLATE_DEPLOY_RECOG_TABLE + " ORDER BY gmtTime DESC")
    List<PlateRecogRecord> getAllPlateRecordDbInfo();

    @Query("DELETE FROM "+FaceConstants.PLATE_DEPLOY_RECOG_TABLE+" WHERE recordId = :recordId")
    void removePlateRecordInfoByRecordId(String recordId);

    @Query("DELETE FROM " + FaceConstants.PLATE_DEPLOY_RECOG_TABLE)
    void removeAllPlateRecordDbInfo();
}
