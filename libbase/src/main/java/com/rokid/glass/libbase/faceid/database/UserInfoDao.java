package com.rokid.glass.libbase.faceid.database;

import java.util.List;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.rokid.glass.libbase.faceid.FaceConstants;

/**
 * zhuohf1 @2019.5.9
 */

@Dao
public interface UserInfoDao {

    @Query("SELECT * FROM " + FaceConstants.FACE_USER_TABLE)
    List<UserInfo> getAllUserInfo();

    @Query("SELECT * FROM " + FaceConstants.FACE_USER_TABLE + " LIMIT :limit OFFSET :offset")
    List<UserInfo> getUserInfo(int offset, int limit);

    @Query("SELECT COUNT(*) FROM " + FaceConstants.FACE_USER_TABLE)
    int getAllUserCount();

    @Query("SELECT * FROM " + FaceConstants.FACE_USER_TABLE + " WHERE uid = :uid LIMIT 1")
    UserInfo getUserInfoWithUID(String uid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addUserInfo(UserInfo userInfo);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateUserInfo(UserInfo userInfo);

    @Delete
    void removeUserInfo(UserInfo userInfo);

    @Query("DELETE FROM " + FaceConstants.FACE_USER_TABLE)
    void removeAllUserInfo();

    // 开始根据用户名字开始模糊搜索
    @Query("SELECT * FROM " + FaceConstants.FACE_USER_TABLE + " WHERE name LIKE '%' || :name || '%'")
    List<UserInfo> getSearchAllUserByName(String name);

    @Query("SELECT * FROM " + FaceConstants.FACE_USER_TABLE + " WHERE name LIKE '%' || :name || '%' LIMIT :limit OFFSET :offset" )
    List<UserInfo> getSearchUserByName(String name, int offset, int limit);

    // 开始根据用户名字或者身份证开始模糊搜索
    @Query("SELECT * FROM " + FaceConstants.FACE_USER_TABLE + " WHERE name LIKE '%' || :searchString || '%' or cardno LIKE '%' || :searchString || '%' LIMIT :limit OFFSET :offset ")
    List<UserInfo> searchUserByNameOrCardNo(String searchString, int offset, int limit);

    // 开始根据用户名字或者身份证开始模糊搜索
    @Query("SELECT count(*) FROM " + FaceConstants.FACE_USER_TABLE + " WHERE name LIKE '%' || :searchString || '%' or cardno LIKE '%' || :searchString || '%'")
    int searchUserByNameOrCardNoCount(String searchString);

    @Query("SELECT COUNT(*) FROM " + FaceConstants.FACE_USER_TABLE + " WHERE name LIKE '%' || :name || '%'")
    int getSearchUserCountByName(String name);

}
