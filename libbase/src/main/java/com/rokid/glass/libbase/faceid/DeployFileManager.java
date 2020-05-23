package com.rokid.glass.libbase.faceid;

import android.content.Context;

import com.rokid.glass.libbase.ErrorCode;
import com.rokid.glass.libbase.config.DeployTaskConfig;
import com.rokid.glass.libbase.faceid.database.DeployDbInfo;
import com.rokid.glass.libbase.logger.Logger;
import com.rokid.glass.libbase.utils.FileUtils;
import com.rokid.glass.libbase.utils.ZipUtils;

import java.io.File;
import java.io.IOException;

/**
 * Author: Zack
 * Email:  newzzack@gmail.com
 * Date:   2020.02.28 21:33
 */
public class DeployFileManager {

    private Context mContext;

    private File mDeployImageDir;

    private File mDeployZipDir;

    private static final String DEPLOY_DIR = "/deployPackage";

    private static final String DEPLOY_IMAGE_DIR = DEPLOY_DIR + "/images";

    private static final String DEPLOY_ZIP_DIR = DEPLOY_DIR + "/zip";


    public DeployFileManager(Context context) {
        mContext = context;

        File cacheDir = mContext.getExternalCacheDir();
        if(cacheDir == null) {
            cacheDir = mContext.getFilesDir();
        }
        mDeployImageDir = new File(cacheDir.getAbsolutePath() + DEPLOY_IMAGE_DIR);
        mDeployZipDir = new File(cacheDir.getAbsolutePath() + DEPLOY_ZIP_DIR);

    }

    public ErrorCode unzipFile(String filePath, ZipUtils.DecompressCallback callback) {

        if(!mDeployImageDir.exists()) {
            mDeployImageDir.mkdirs();
        }
        //解压zip包
        try {
            ZipUtils.decompress(filePath, mDeployImageDir.getAbsolutePath(), callback);
        } catch (IOException e) {
            return ErrorCode.FACE_ADD_PERSONS_DECOMPRESS_FAILED;
        }
        return ErrorCode.OK;
    }


    public void clearFiles() {
        FileUtils.deleteDirection(mDeployImageDir);
        FileUtils.deleteDirection(mDeployZipDir);
        if(!mDeployImageDir.exists()) {
            mDeployImageDir.mkdirs();
        }
        if(!mDeployZipDir.exists()) {
            mDeployZipDir.mkdirs();
        }
    }

    public String packageDeployFile() {

        //创建布控包zip存放目录
        clearFiles();

        //布控包文件夹名字，默认使用时间戳，如果数据库能查询到布控包名字，则使用布控包名字
        DeployDbInfo dbInfo = FaceIdManager.getInstance().getDeployDbInfoByKey(DeployTaskConfig.KEYS.DB_DEPLOY_NAME);
        String deployFileName = String.valueOf(System.currentTimeMillis());
        if(dbInfo != null) {
            deployFileName = dbInfo.getValueStr();
        }
        File deployFile = new File(mDeployZipDir, deployFileName);

        if(!deployFile.exists()) {
            deployFile.mkdirs();
        }

        //拷贝数据库和bin文件到布控包目录
        File oldSearchEngine = new File(FaceIdManager.PATH_ENGINE + FaceConstants.FACE_SEARCH_ENGINE);
        if (oldSearchEngine.exists()) {
            FileUtils.copyFileToDir(oldSearchEngine.getAbsolutePath(), deployFile.getAbsolutePath());
        }
        File oldFaceidDb = mContext.getDatabasePath(FaceConstants.FACE_ID_DB);
        if (oldFaceidDb != null && oldFaceidDb.exists()) {
            FileUtils.copyFileToDir(oldFaceidDb.getAbsolutePath(), deployFile.getAbsolutePath());
        }
        File oldFeatureDb = mContext.getDatabasePath(FaceConstants.FACE_FEATURE_DB);
        if (oldFeatureDb != null && oldFeatureDb.exists()) {
            FileUtils.copyFileToDir(oldFeatureDb.getAbsolutePath(), deployFile.getAbsolutePath());
        }

        //压缩成zip
        String targetPath = deployFile.getAbsolutePath() + ".zip";
        try {
            ZipUtils.compress(deployFile.getAbsolutePath(), targetPath);
        } catch (IOException e) {
            Logger.e(e.getMessage());
        }

        return targetPath;
    }

    public String getDeployImageDir() {
        return mDeployImageDir.getAbsolutePath();
    }
}
