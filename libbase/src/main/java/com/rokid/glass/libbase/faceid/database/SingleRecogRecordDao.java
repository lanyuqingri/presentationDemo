package com.rokid.glass.libbase.faceid.database;



import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.rokid.glass.libbase.faceid.FaceConstants;

import java.util.List;

@Dao
public interface SingleRecogRecordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addSingleRecogRecord(SingleFaceRecord singleFaceRecord);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addSingleFaceRecordList(List<SingleFaceRecord> singleFaceRecordList);

    @Query("SELECT * FROM " + FaceConstants.FACE_SINGLE_RECOG_TABLE+" ORDER BY gmtRecg DESC LIMIT :index,:count")
    List<SingleFaceRecord> getSingleFaceRecordList(int index, int count);

    @Query("SELECT * FROM " + FaceConstants.FACE_SINGLE_RECOG_TABLE + " ORDER BY gmtRecg DESC")
    List<SingleFaceRecord> getAllSingleFaceRecordList();

    @Query("DELETE FROM "+FaceConstants.FACE_SINGLE_RECOG_TABLE+" WHERE id = :id")
    void removeSingleFaceRecordById(String id);

    @Query("DELETE FROM "+FaceConstants.FACE_SINGLE_RECOG_TABLE)
    void removeAllSingleFaceRecord();


    @Query("SELECT count(*) FROM " + FaceConstants.FACE_SINGLE_RECOG_TABLE)
    int getSingleFaceRecordCount();

    @Query("SELECT * FROM "+ FaceConstants.FACE_SINGLE_RECOG_TABLE +" WHERE id LIKE :id LIMIT 1")
    SingleFaceRecord getSingleFaceRecordById(String id);
}
