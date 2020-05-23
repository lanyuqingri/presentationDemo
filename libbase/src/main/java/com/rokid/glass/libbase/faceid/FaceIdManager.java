package com.rokid.glass.libbase.faceid;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Handler;
import android.text.TextUtils;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.rokid.facelib.ImageRokidFace;
import com.rokid.facelib.api.IImageRokidFace;
import com.rokid.facelib.conf.DFaceConf;
import com.rokid.facelib.face.DbAddResult;
import com.rokid.facelib.face.FaceDbHelper;
import com.rokid.glass.libbase.BaseLibrary;
import com.rokid.glass.libbase.faceid.database.DeployDbInfo;
import com.rokid.glass.libbase.faceid.database.FaceIdDatabase;
import com.rokid.glass.libbase.faceid.database.FaceMapping;
import com.rokid.glass.libbase.faceid.database.UserInfo;
import com.rokid.glass.libbase.logger.Logger;
import com.rokid.glass.libbase.utils.FileUtils;
import com.rokid.glass.libbase.utils.ImageUtils;
import com.rokid.glass.libbase.utils.ThreadPoolHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * zhuohf1 @2019.5.9
 */
public class FaceIdManager {
    private static final FaceIdManager Instance = new FaceIdManager();

    private IImageRokidFace mImageFace;
    private FaceDbHelper faceDbHelper;

    private FaceIdDatabase faceIdDatabase;

    private volatile boolean isSDKInited = false;
    public static FaceIdManager getInstance() {
        return Instance;
    }

    private Context mContext;

    private FaceIdManager() {}

    private Handler mReloadHandler;

    public static String PATH_ENGINE;

    private volatile String currentUid = "";

    public synchronized void init(final Context context) {
        if (!isSDKInited) {
            try {
                mContext = context;
                PATH_ENGINE = Objects.requireNonNull(context.getExternalFilesDir("facesdk")).getAbsolutePath() + File.separator;
                Logger.d("FaceIdManager: 开始初始化 isFaceDbExists isSDKInited="+isSDKInited+", PATH_ENGINE="+PATH_ENGINE);

                // Feature.db
                faceDbHelper = new FaceDbHelper(context);
                faceDbHelper.createDb();
                faceDbHelper.setEnginePath(PATH_ENGINE);
                // 判断是否是2.0升级上来的，/sdcard/facesdk下是否存在searchEngine.bin
                // 如果存在则进行重新生成
                File oldSearchBinFile = new File("/sdcard/facesdk/" + FaceConstants.FACE_SEARCH_ENGINE);
                if (oldSearchBinFile != null && oldSearchBinFile.exists()) {
                    FileUtils.deleteFile(oldSearchBinFile.getAbsolutePath());
                    Logger.e("FaceIdManager: 存在旧的searchengine.bin, 重新生成searchEngine.bin, oldSearchBinFile="+oldSearchBinFile.getAbsolutePath());
                    faceDbHelper.save(100000);
                }

                // 初始化数据库，包含2个表
                faceIdDatabase = FaceIdDatabase.create(context, FaceConstants.FACE_ID_DB);
                Logger.d("FaceIdManager: 开始初始化 done isFaceDbExists");

                // 图片人脸识别
                mImageFace = ImageRokidFace.create(context);
                mImageFace.dconfig(new DFaceConf());

                mReloadHandler = new Handler(context.getMainLooper());

                isSDKInited = true;
                Logger.d("FaceIdManager: 初始化完成");
            }
            catch (Exception e) {
                isSDKInited = false;
                Logger.e("FaceIdManager: 初始化失败");
                e.printStackTrace();
            }
        }
    }

    public synchronized boolean isInited() {
        return isSDKInited;
    }

    /**
     * 获取所有用户个数
     * @return
     */
    public int getAllUserNum() {
        int num = faceIdDatabase.userInfoDao().getAllUserCount();
        Logger.d("FaceIdManager: getAllUserNum = "+num);
        return num;
    }


    public int getAllMappingNum() {
        int num = faceIdDatabase.faceMappingDao().getAllFaceMappingCount();
        Logger.d("FaceIdManager: getAllFaceMappingCount = "+num);
        return num;
    }

    /**
     * 获取分页的人脸信息
     * @param offset
     * @param limit
     * @return
     */
    public List<FaceInfo> getUserInfo(final int offset, final int limit) {
        List<FaceInfo> faceInfo = new ArrayList<>();
        List<FaceMapping> faceMappings;
        if (offset == -1 || limit == -1) {
            faceMappings = faceIdDatabase.faceMappingDao().getAllFaceMappingWithCover();
        }
        else {
            faceMappings = faceIdDatabase.faceMappingDao().getAllFaceMappingWithCover(offset, limit);
        }

        if (faceMappings != null) {
            Logger.d("FaceIdManager: getAllFaceWithCover faceMappings.size="+faceMappings.size());
        }
        for (FaceMapping mapping : faceMappings) {
            UserInfo userInfo = faceIdDatabase.userInfoDao().getUserInfoWithUID(mapping.uid);
            if (null != userInfo) {
                faceInfo.add(new FaceInfo().setFaceMapping(mapping)
                        .setUserInfo(userInfo));
            }
        }
        return faceInfo;
    }

    /**
     * 查询某个用户下有多少张人脸
     * @param uid
     * @return
     */
    public List<FaceMapping> getFaceWithUID(final String uid) {
        List<FaceMapping> faceMappings = faceIdDatabase.faceMappingDao().getAllFaceMappingWithUID(uid);
        return faceMappings;
    }

    /**
     * 获取某个具体人脸图片
     * @param fid
     * @return
     */
    public FaceMapping getFaceWithFID(String fid) {
        FaceMapping faceMapping = faceIdDatabase.faceMappingDao().getFaceMappingWithFID(fid);
        return faceMapping;
    }

    /**
     * 添加用户
     * @param info
     */
    public void addUserInfo(UserInfo info) {
        faceIdDatabase.userInfoDao().addUserInfo(info);
    }


    public UserInfo getUserInfoById(String uid) {
        UserInfo user = faceIdDatabase.userInfoDao().getUserInfoWithUID(uid);
        return user;
    }

    /**
     * 根据fid来返回用户信息
     * @param fid
     * @return
     */
    public UserInfo getUserInfoByFid(String fid) {
        FaceMapping mapping = faceIdDatabase.faceMappingDao().getFaceMappingWithFID(fid);
        if (mapping != null && !TextUtils.isEmpty(mapping.uid)) {
            return faceIdDatabase.userInfoDao().getUserInfoWithUID(mapping.uid);
        }
        return null;
    }

    /**
     * 根据fid来返回用户图片
     * @param fid
     * @return
     */
    public Bitmap getUserImageByFid(String fid) {
        Bitmap bitmap = null;
        FaceMapping mapping = faceIdDatabase.faceMappingDao().getFaceMappingWithFID(fid);
        if (mapping != null) {
            try {
                bitmap = ImageUtils.Bytes2Bitmap(mapping.faceImg);
            }
            catch (Exception e) {
                bitmap = null;
            }
        }
        return bitmap;
    }

    /**
     * 添加mapping
     * @param mapping
     */
    public void addFaceMapping(FaceMapping mapping) {
        faceIdDatabase.faceMappingDao().addFaceMapping(mapping);
    }

    /**
     * 删除某张人脸，mapping表，feature表都要删除
     * 如果是只有一张人脸，user表也要删除
     * @param fid
     */
    public boolean deleteFace(final String fid) {
        boolean result;
        try {
            // 1. 先删除feature表
            result = faceDbHelper.remove(fid);
            // 2. 获取mapping中的个数
            if (result) {
                final FaceMapping mapping = faceIdDatabase.faceMappingDao().getFaceMappingWithFID(fid);
                if (mapping != null && !TextUtils.isEmpty(mapping.uid)) {
                    final String uid = mapping.uid;
                    List<FaceMapping> userMappings = faceIdDatabase.faceMappingDao().getAllFaceMappingWithUID(uid);
                    if (userMappings != null && userMappings.size() == 1) {
                        // 如果这个face对应的User只有一张人脸，则删除user表
                        UserInfo user = faceIdDatabase.userInfoDao().getUserInfoWithUID(uid);
                        faceIdDatabase.userInfoDao().removeUserInfo(user);
                    }

                    faceIdDatabase.faceMappingDao().removeFaceMapping(mapping);
                }
                else {
                    Logger.e("FaceIdManager: deleteFace 删除人脸失败 no mapping fid="+fid);
                }
            }
            else {
                Logger.e("FaceIdManager: deleteFace 调用remove删除人脸失败 fid="+fid);
            }
        }
        catch (Exception e) {
            result = false;
            e.printStackTrace();
            Logger.e("FaceIdManager: deleteFace 删除人脸失败 fid="+fid + ", e="+e.getMessage());
        }
        return result;
    }

    /**
     * 删除某个用户，包括user表，mapping表，feature表都要删除
     * @param uid
     */
    public boolean deleteUser(final String uid) {
        boolean result = true;
        try {
            // 获取所有mapping
            List<FaceMapping> userMappings = faceIdDatabase.faceMappingDao().getAllFaceMappingWithUID(uid);
            if (userMappings != null && userMappings.size() > 0) {
                for (FaceMapping mapping : userMappings) {
                    // 删除feature表
                    if (faceDbHelper.remove(mapping.fid)){
                        // 删除mapping表
                        faceIdDatabase.faceMappingDao().removeFaceMapping(mapping);
                    }
                    else {
                        result = false; // 只要有一个删除失败，就不要删除用户
                        Logger.e("FaceIdManager: deleteUser 删除用户失败 fid="+mapping.fid);
                    }
                }
            }

            if (result) {
                // 先删除user表
                UserInfo user = faceIdDatabase.userInfoDao().getUserInfoWithUID(uid);
                faceIdDatabase.userInfoDao().removeUserInfo(user);
            }
        }
        catch (Exception e) {
            result = false;
            e.printStackTrace();
            Logger.e("FaceIdManager: deleteFace 删除用户失败 uid="+uid);
        }
        return result;
    }


    public boolean removeAllData() {
        boolean result = true;
        try {
            faceIdDatabase.faceMappingDao().removeAllFaceMapping();
            faceIdDatabase.userInfoDao().removeAllUserInfo();
            faceDbHelper.close();
            faceDbHelper.clearDb();
            faceDbHelper.createDb();
        }catch (Exception e) {
            result = false;
            Logger.e(e.getMessage());
        }
        return result;
    }

    public boolean updateUser(final String uid, final String name, final String cardno,
                              final String nativeplace, final String tagDesc, final boolean tagAlarm,
                              final String cover_fid) {
        Logger.d("FaceIdManager: updateUser name="+name+", cardno="+cardno+", nativeplace="+nativeplace+
                ", tagDesc="+tagDesc+", uid="+uid+ ", cover_fid="+cover_fid);
        if (TextUtils.isEmpty(uid) || TextUtils.isEmpty(name)) {
            return false;
        }

        // 更新user表信息
        if (!TextUtils.isEmpty(name)) {
            UserInfo userInfo = faceIdDatabase.userInfoDao().getUserInfoWithUID(uid);
            if (null != userInfo) {
                userInfo.name = name;
                userInfo.cardno = cardno;
                userInfo.nativeplace = nativeplace;
                userInfo.description = tagDesc;
                userInfo.isAlarm = tagAlarm;
                faceIdDatabase.userInfoDao().updateUserInfo(userInfo);
            }
        }
        // 更新mapping表
        List<FaceMapping> faceMappings = faceIdDatabase.faceMappingDao().getAllFaceMappingWithUID(uid);
        if (faceMappings != null && faceMappings.size() > 0) {
            if (faceMappings.size() == 1) {
                // 如果某个用户下mapping只有一个，则必须是封面
                FaceMapping mapping = faceMappings.get(0);
                if (mapping != null && !mapping.isCover) {
                    mapping.isCover = true;
                    faceIdDatabase.faceMappingDao().updateFaceMapping(mapping);
                }
            }
            else {
                if (!TextUtils.isEmpty(cover_fid)) {
                    for (FaceMapping mapping : faceMappings) {
                        // 如果cover_uuid不为空，说明需要更新cover
                        if (mapping.fid.equalsIgnoreCase(cover_fid)) {
                            mapping.isCover = true;
                        }
                        else {
                            mapping.isCover = false;
                        }
                        faceIdDatabase.faceMappingDao().updateFaceMapping(mapping);
                    }
                }
            }
        }
        return true;
    }

    /**
     * 保存到feature.db中
     */
    public DbAddResult addFeature(Bitmap srcBitmap) {
        try {
            return faceDbHelper.addReturnDetail(srcBitmap);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除feature.db中条目
     */
    public boolean removeFeature(String fid) {
        try {
            return faceDbHelper.remove(fid);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public IImageRokidFace getImageFace() {
        return mImageFace;
    }

    /**
     * 保存db数据库
     */
    public void saveFeatureDb() {
        if (faceDbHelper != null) {
            faceDbHelper.save(100000);
        }
    }

    /**
     * 根据条件，搜索用户信息
     * @param searchName
     * @param offset
     * @param limit
     * @return
     */
    public List<FaceInfo> getSearchUserInfo(String searchName, int offset, int limit) {
        List<UserInfo> userInfoList = faceIdDatabase.userInfoDao().getSearchUserByName(searchName, offset, limit);
        List<FaceInfo> faceInfo = new ArrayList<>();
        try {
            int size = userInfoList.size();
            Logger.d("FaceIdManager:  getSearchUserInfo userInfoList.size="+size);
            if (userInfoList != null && size > 0) {
                for (UserInfo userInfo : userInfoList) {
                    FaceMapping mapping = faceIdDatabase.faceMappingDao().getCoverFaceMappingWithUID(userInfo.uid);
                    faceInfo.add(new FaceInfo().setFaceMapping(mapping)
                            .setUserInfo(userInfo));
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return faceInfo;
    }


    /**
     * 根据条件，搜索用户信息
     * @param searchString
     * @param pageNo
     * @param limit
     * @return
     */
    public List<FaceInfo> searchUserByNameOrCardNo(String searchString, int pageNo, int limit) {
        List<UserInfo> userInfoList = faceIdDatabase.userInfoDao().searchUserByNameOrCardNo(searchString, pageNo, limit);
        List<FaceInfo> faceInfo = new ArrayList<>();

        int offset = pageNo * limit;
        try {
            int size = userInfoList.size();
            Logger.d("FaceIdManager:  searchUserByNameOrCardNo userInfoList.size="+size);
            userInfoList = userInfoList.subList(
                    offset > size ? 0 : offset,
                    (limit + offset) > size ? size : (limit + offset));
            for (UserInfo userInfo : userInfoList) {
                FaceMapping mapping = faceIdDatabase.faceMappingDao().getCoverFaceMappingWithUID(userInfo.uid);
                faceInfo.add(new FaceInfo().setFaceMapping(mapping)
                        .setUserInfo(userInfo));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return faceInfo;
    }


    public int searchUserByNameOrCardNoCount(String searchString) {
        return faceIdDatabase.userInfoDao().searchUserByNameOrCardNoCount(searchString);
    }


    public List<FaceInfo> getAllUserInfo(int offset, int limit) {
        List<UserInfo> userInfoList = faceIdDatabase.userInfoDao().getUserInfo(offset, limit);
        List<FaceInfo> faceInfo = new ArrayList<>();
        for (UserInfo userInfo : userInfoList) {
            FaceMapping mapping = faceIdDatabase.faceMappingDao().getCoverFaceMappingWithUID(userInfo.uid);
            faceInfo.add(new FaceInfo().setFaceMapping(mapping)
                    .setUserInfo(userInfo));
        }
        return faceInfo;
    }

    public int getSearchUserCount(String searchName) {
        return faceIdDatabase.userInfoDao().getSearchUserCountByName(searchName);
    }
    /**
     * 拷贝数据库到/sdcard/facesdk目录下
     */
    private void saveDb() {
        saveFeatureDb();
    }

    private void copyFaceDbAssetsFile(String fileName,File destFile){
        try {
            InputStream inputStream = mContext.getAssets().open(fileName);
            FileOutputStream outputStream = new FileOutputStream(destFile);
            byte[] buffer = new byte[1024];
            int byteCount=0;
            while ((byteCount = inputStream.read(buffer)) != -1){
                outputStream.write(buffer,0,byteCount);
            }
            outputStream.flush();
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void checkAssetsOfflineFaceDb(){
        faceIdDatabase.closeDb();
        copyFaceDbAssetsFile("faceid.db", mContext.getDatabasePath(FaceConstants.FACE_ID_DB));
        copyFaceDbAssetsFile("feature.db",mContext.getDatabasePath(FaceConstants.FACE_FEATURE_DB));
        copyFaceDbAssetsFile("SearchEngine.bin",new File(PATH_ENGINE + FaceConstants.FACE_SEARCH_ENGINE));
        // 重新初始化
        init(mContext);
        sendAfterReloadBroadcast();
    }

    // ===================================================
    /**
     * 检测当前是否有新的特征库包，如果有就更新并应用
     */
    public void checkOfflineFaceIdDB(OnFaceDbReplacedCallback callback) {
        // 判断是否有离线包
        File copyDir = new File(FaceConstants.FACE_DB_COPY_PATH);
        if (copyDir.exists() && copyDir.isDirectory()) {
            File[] filelist = copyDir.listFiles();
            for (File tempFile : filelist) {
                String extName = FileUtils.getExtensionName(tempFile.getName());
                if (!TextUtils.isEmpty(extName) && (extName.equalsIgnoreCase("zip") || extName.equalsIgnoreCase("glass"))) {
                    Logger.d("FaceIdManager: checkOfflineFaceIdDB, 有新的离线特征库包: "+tempFile.getName());
                    applyFaceIdDb(tempFile, callback);
                    break;
                }
            }
        }
    }

    /**
     * 解压拷贝离线包，并初始化数据库
     * @param offlineDb
     * @param callback
     */
    private void applyFaceIdDb(final File offlineDb, final OnFaceDbReplacedCallback callback) {
        Logger.d("FaceIdManager: 特征库拷贝, 开始");
        // 启动一个线程开始解压和拷贝
        Future<Boolean> future = ThreadPoolHelper.getInstance()
                .threadSubmit(new Callable<Boolean>() {
                                  @Override
                                  public Boolean call() throws Exception {
                                      Thread.sleep(10);
                                      // 解压离线包
                                      if (unzipOfflineDb(offlineDb)) {
                                          // 发送停止失败广播
                                          sendPreReloadBroadcast(100);
                                          // 拷贝离线包
                                          isSDKInited = false;
                                          if (mReloadHandler != null) {
                                              mReloadHandler.removeCallbacksAndMessages(null);
                                              mReloadHandler = null;
                                          }

                                          // 关闭数据库
                                          faceIdDatabase.closeDb();
                                          // 覆盖拷贝数据库和searchEngine.bin
                                          copyOfflineDb();
                                          // 重新初始化
                                          init(mContext);
                                          sendAfterReloadBroadcast();

                                          Logger.d("FaceIdManager: 离线特征库包 "+offlineDb.getName()+" 拷贝完成退出, 发送广播");
                                          if(callback != null) {
                                              callback.onReplaced(FileUtils.getFileNameNoEx(offlineDb.getName()), System.currentTimeMillis());
                                          }
                                          LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(FaceConstants.ACTION_FACE_ID_COPY_DB_DONE));
                                          return true;
                                      }
                                      else {
                                          Logger.e("FaceIdManager: 离线特征库包 "+offlineDb.getName()+" 解压失败");
                                          return false;
                                      }
                                  }
                              }
                );
    }

    private boolean unzipOfflineDb(File offlineDb) {
        if (offlineDb == null || !offlineDb.exists()) {
            Logger.e("FaceIdManager: 离线特征库不存在，退出");
            return false;
        }

        // 删除之前解压的文件，如果有的话
        try {
            File oldSearchEngine = new File(FaceConstants.FACE_DB_COPY_PATH + FaceConstants.FACE_SEARCH_ENGINE);
            if (oldSearchEngine != null && oldSearchEngine.exists()) {
                oldSearchEngine.delete();
            }
            File oldFaceidDb = new File(FaceConstants.FACE_DB_COPY_PATH + FaceConstants.FACE_ID_DB);
            if (oldFaceidDb != null && oldFaceidDb.exists()) {
                oldFaceidDb.delete();
            }
            File oldFeatureDb = new File(FaceConstants.FACE_DB_COPY_PATH + FaceConstants.FACE_FEATURE_DB);
            if (oldFeatureDb != null && oldFeatureDb.exists()) {
                oldFeatureDb.delete();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // 开始解压
        ZipInputStream zin = null;
        try {
            zin = new ZipInputStream(new FileInputStream(offlineDb.getAbsoluteFile()));
        } catch (Exception e) {
            e.printStackTrace();
            Logger.e("FaceIdManager: ZipInputStream 错误 "+e);
            return false;
        }
        if (zin != null) {
            ZipEntry zipEntry;
            try {
                while ((zipEntry = zin.getNextEntry()) != null) {
                    String tempName = zipEntry.getName();
                    if (TextUtils.isEmpty(tempName)) {
                        continue;
                    }
                    String name="";
                    if (tempName.endsWith(FaceConstants.FACE_SEARCH_ENGINE)) {
                        name = FaceConstants.FACE_SEARCH_ENGINE;
                    }
                    else if (tempName.endsWith(FaceConstants.FACE_ID_DB)) {
                        name = FaceConstants.FACE_ID_DB;
                    }
                    else if (tempName.endsWith(FaceConstants.FACE_FEATURE_DB)) {
                        name = FaceConstants.FACE_FEATURE_DB;
                    }

                    if (TextUtils.isEmpty(name)) {
                        continue;
                    }

                    Logger.d("FaceIdManager: unzip file name="+name+", tempName="+tempName);
                    File newFile = new File(FaceConstants.FACE_DB_COPY_PATH + name);
                    if (newFile.exists()) {
                        newFile.delete();
                        newFile.createNewFile();
                    }
                    FileOutputStream fout = new FileOutputStream(newFile);

                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = zin.read(buffer)) > 0) {
                        fout.write(buffer, 0, len);
                    }
                    fout.flush();
                    fout.close();
                    zin.closeEntry();
                }
                zin.close();
            } catch (IOException e) {
                Logger.e("FaceIdManager: 解压错误 "+e);
                e.printStackTrace();
                return false;
            }
        }
        Logger.d("FaceIdManager: 解压完成！");
        return true;
    }


    private boolean copyOfflineDb() {
        // 1. 检查要拷贝的文件是否存在
        File faceidDb = new File(FaceConstants.FACE_DB_COPY_PATH + FaceConstants.FACE_ID_DB);
        File featureDb = new File(FaceConstants.FACE_DB_COPY_PATH + FaceConstants.FACE_FEATURE_DB);
        File searchBin = new File(FaceConstants.FACE_DB_COPY_PATH + FaceConstants.FACE_SEARCH_ENGINE);
        // 确保解压出来同时存在faceid.db和search.bin文件
        if(!faceidDb.exists() || !searchBin.exists()){
            Logger.e("FaceIdManager: applyOfflineDb 特征库不完整，退出拷贝");
            return false;
        }

        // 2. 删除本地data/data下的数据库和sdcard上的search.bin
        try {
            File oldSearchEngine = new File(FaceIdManager.PATH_ENGINE + FaceConstants.FACE_SEARCH_ENGINE);
            if (oldSearchEngine != null && oldSearchEngine.exists()) {
                oldSearchEngine.delete();
            }
            File oldFaceidDb = mContext.getDatabasePath(FaceConstants.FACE_ID_DB);
            if (oldFaceidDb != null && oldFaceidDb.exists()) {
                oldFaceidDb.delete();
            }
            File oldFeatureDb = mContext.getDatabasePath(FaceConstants.FACE_FEATURE_DB);
            if (oldFeatureDb != null && oldFeatureDb.exists()) {
                oldFeatureDb.delete();
            }
            File oldFaceidDbJournal = mContext.getDatabasePath(FaceConstants.FACE_ID_DB+"-journal");
            if (oldFaceidDbJournal != null && oldFaceidDbJournal.exists()) {
                oldFaceidDbJournal.delete();
            }
            File oldFeatureDbJournal = mContext.getDatabasePath(FaceConstants.FACE_FEATURE_DB+"-journal");
            if (oldFeatureDbJournal != null && oldFeatureDbJournal.exists()) {
                oldFeatureDbJournal.delete();
            }
        } catch (Exception e) {
            Logger.e("FaceIdManager: applyOfflineDb 删除旧数据出现异常");
            e.printStackTrace();
        }

        // 3. 开始拷贝新数据
        FileUtils.copyFile(searchBin, new File(PATH_ENGINE + FaceConstants.FACE_SEARCH_ENGINE));
        FileUtils.copyFile(faceidDb, mContext.getDatabasePath(FaceConstants.FACE_ID_DB));
        FileUtils.copyFile(featureDb, mContext.getDatabasePath(FaceConstants.FACE_FEATURE_DB));

        // 4. 删除copy目录下的文件，之后再创建一个空的
        FileUtils.deleteDirection(new File(FaceConstants.FACE_DB_COPY_PATH));
        File copyFolder = new File(FaceConstants.FACE_DB_COPY_PATH);
        if (!copyFolder.exists()) copyFolder.mkdirs();
        return true;
    }


    public void sendPreReloadBroadcast(long time) {
        Logger.d("FaceIdManager: sendPreReloadBroadcast time="+time);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(FaceConstants.ACTION_FACE_ID_UPDATE_START));
        // 先暂停100ms
        try {
            Thread.sleep(time);
        }
        catch (Exception e){
        }
    }

    public void sendAfterReloadBroadcast() {
        Logger.d("FaceIdManager: sendAfterReloadBroadcast");
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(FaceConstants.ACTION_FACE_ID_UPDATE_DONE));
    }

    /**
     * 判断当前人脸数据库是否存在
     * @return
     */
    public boolean isFaceDbExists() {
        try {
            File faceidDb = BaseLibrary.getInstance().getContext().getDatabasePath(FaceConstants.FACE_ID_DB);
            if (faceidDb != null && faceidDb.exists()) {
                if (faceIdDatabase != null && faceIdDatabase.userInfoDao().getAllUserCount() > 0) {
                    Logger.d("FaceIdManager isFaceDbExists=======>>>True");
                    return true;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        Logger.d("FaceIdManager isFaceDbExists=======>>>False");
        return false;
    }

    public interface OnFaceDbReplacedCallback {
        void onReplaced(String fileName, long timestamp);
    }


    /**布控任务信息表相关*/
    public List<DeployDbInfo> getAllDeployDbInfo() {
        return faceIdDatabase.deployTaskInfoDao().getAllDeployDbInfo();

    }

    public DeployDbInfo getDeployDbInfoByKey(String keyStr) {
        DeployDbInfo info = faceIdDatabase.deployTaskInfoDao().getDeployDbInfoByKey(keyStr);
        return info;
    }

    public void updateDeployDbInfo(DeployDbInfo info) {
        faceIdDatabase.deployTaskInfoDao().updateDeployDbInfo(info);
    }

    public void addDeployDbInfo(DeployDbInfo info) {
        faceIdDatabase.deployTaskInfoDao().addDeployDbInfo(info);
    }

    public void removeAllDeployDbInfo() {
        faceIdDatabase.deployTaskInfoDao().removeAllDeployDbInfo();
    }
}


