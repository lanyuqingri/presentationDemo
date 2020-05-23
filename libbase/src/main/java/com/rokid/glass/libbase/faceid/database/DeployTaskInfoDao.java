package com.rokid.glass.libbase.faceid.database;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.rokid.glass.libbase.faceid.FaceConstants;

import java.util.List;

/**
 * Author: Zack
 * Email:  newzzack@gmail.com
 * Date:   2020.02.25 20:57
 */

@Dao
public interface DeployTaskInfoDao {

    @Query("SELECT * FROM " + FaceConstants.DEPLOY_TASK_INFO_TABLE)
    List<DeployDbInfo> getAllDeployDbInfo();


    @Query("SELECT * FROM " + FaceConstants.DEPLOY_TASK_INFO_TABLE + " where keyStr = :keyStr LIMIT 1")
    DeployDbInfo getDeployDbInfoByKey(String keyStr);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addDeployDbInfo(DeployDbInfo info);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateDeployDbInfo(DeployDbInfo info);

    @Query("DELETE FROM " + FaceConstants.DEPLOY_TASK_INFO_TABLE)
    void removeAllDeployDbInfo();
}
