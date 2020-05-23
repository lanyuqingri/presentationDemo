package com.rokid.glass.libbase.faceid.database;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.rokid.glass.libbase.faceid.FaceConstants;

import java.util.List;

/**
 * zhuohf1 @2019.5.9
 */

@Dao
public interface FaceMappingDao {
    @Query("SELECT * FROM " + FaceConstants.FACE_MAPPING_TABLE)
    List<FaceMapping> getAllFaceMapping();

    @Query("SELECT * FROM " + FaceConstants.FACE_MAPPING_TABLE + " WHERE isCover = 1 ORDER BY id desc")
    List<FaceMapping> getAllFaceMappingWithCover();

    @Query("SELECT * FROM " + FaceConstants.FACE_MAPPING_TABLE + " WHERE isCover = 1 ORDER BY id desc LIMIT :limit OFFSET :offset")
    List<FaceMapping> getAllFaceMappingWithCover(int offset, int limit);

    @Query("SELECT * FROM " + FaceConstants.FACE_MAPPING_TABLE + " WHERE uid = :uid ORDER BY id desc")
    List<FaceMapping> getAllFaceMappingWithUID(String uid);

    @Query("SELECT * FROM " + FaceConstants.FACE_MAPPING_TABLE + " WHERE fid = :fid LIMIT 1")
    FaceMapping getFaceMappingWithFID(String fid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addFaceMapping(FaceMapping mapping);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateFaceMapping(FaceMapping mapping);

    @Delete
    void removeFaceMapping(FaceMapping mapping);

    @Query("DELETE FROM " + FaceConstants.FACE_MAPPING_TABLE)
    void removeAllFaceMapping();

    @Query("SELECT * FROM " + FaceConstants.FACE_MAPPING_TABLE + " WHERE uid = :uid AND isCover = 1 LIMIT 1")
    FaceMapping getCoverFaceMappingWithUID(String uid);

    @Query("SELECT count(*) FROM " + FaceConstants.FACE_MAPPING_TABLE)
    int getAllFaceMappingCount();
}
