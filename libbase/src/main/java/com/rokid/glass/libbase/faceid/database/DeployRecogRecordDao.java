package com.rokid.glass.libbase.faceid.database;



import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.rokid.glass.libbase.faceid.FaceConstants;

import java.util.List;

@Dao
public interface DeployRecogRecordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addDeployRecogRecord(DeployFaceRecord singleFaceRecord);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addDeployFaceRecordList(List<DeployFaceRecord> singleFaceRecordList);

    @Query("SELECT * FROM " + FaceConstants.FACE_DEPLOY_RECOG_TABLE+"  ORDER BY gmtRecg DESC LIMIT :index,:count")
    List<DeployFaceRecord> getDeployFaceRecordList(int index, int count);

    @Query("SELECT * FROM " + FaceConstants.FACE_DEPLOY_RECOG_TABLE + " ORDER BY gmtRecg DESC")
    List<DeployFaceRecord> getAllDeployFaceRecordList();

    @Query("DELETE FROM "+FaceConstants.FACE_DEPLOY_RECOG_TABLE+" WHERE id = :id")
    void removeDeployFaceRecordById(String id);

    @Query("DELETE FROM "+FaceConstants.FACE_DEPLOY_RECOG_TABLE)
    void removeAllDeployFaceRecord();

    @Query("SELECT count(*) FROM " + FaceConstants.FACE_DEPLOY_RECOG_TABLE)
    int getDeployFaceRecordCount();

    @Query("SELECT * FROM "+ FaceConstants.FACE_DEPLOY_RECOG_TABLE +" WHERE id LIKE :id LIMIT 1")
    DeployFaceRecord getDeployFaceRecordById(String id);
}
