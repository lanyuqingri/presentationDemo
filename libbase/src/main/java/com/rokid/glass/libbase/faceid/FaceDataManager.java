package com.rokid.glass.libbase.faceid;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.rokid.facelib.face.DbAddResult;
import com.rokid.glass.libbase.ErrorCode;
import com.rokid.glass.libbase.config.DeployTaskConfig;
import com.rokid.glass.libbase.faceid.database.DeployDbInfo;
import com.rokid.glass.libbase.faceid.database.FaceMapping;
import com.rokid.glass.libbase.faceid.database.UserInfo;
import com.rokid.glass.libbase.faceid.wrapper.AddFeatResultWrapper;
import com.rokid.glass.libbase.faceid.wrapper.FaceMappingWrapper;
import com.rokid.glass.libbase.logger.Logger;
import com.rokid.glass.libbase.utils.DefaultSPHelper;
import com.rokid.glass.libbase.utils.FaceUtil;
import com.rokid.glass.libbase.utils.FileUtils;
import com.rokid.glass.libbase.utils.ImageUtils;
import com.rokid.glass.pcassistant.bean.BatchPersons;
import com.rokid.glass.pcassistant.bean.DeployErrInfo;
import com.rokid.glass.pcassistant.bean.DeployInfo;
import com.rokid.glass.pcassistant.bean.DeployStatus;
import com.rokid.glass.pcassistant.bean.DeployTask;
import com.rokid.glass.pcassistant.bean.DeployTaskListInfo;
import com.rokid.glass.pcassistant.bean.ExtractFeatResult;
import com.rokid.glass.pcassistant.bean.FeatFileInfo;
import com.rokid.glass.pcassistant.bean.FeatInfo;
import com.rokid.glass.pcassistant.bean.Person;
import com.rokid.glass.pcassistant.utils.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 人脸特征相关管理类
 * Author: Zack
 * Email:  newzzack@gmail.com
 * Date:   2020.02.20 21:53
 */
public class FaceDataManager {


    /**
     * Constants.BINARY_STATUS_IDLE: 未开始生成特征包bin文件
     * Constants.BINARY_STATUS_PIC_DECOMPRESS: 布控包图片解压中
     * Constants.BINARY_STATUS_FEATURE_EXTRACTION: 布控包图片提取中
     * Constants.BINARY_STATUS_SAVING:特征包bin文件生成中
     * Constants.BINARY_STATUS_PAUSE:特征提取暂停状态
     * Constants.BINARY_STATUS_DONE: 特征包bin文件生成OK
     * Constants.BINARY_STATUS_IMPORTING: 特征包.zip, .glass导入中
     * Constants.BINARY_STATUS_IMPORT_DONE: 特征包.zip, .glass导入完成
     */
    private volatile int mStatus = Constants.BINARY_STATUS_IDLE;

    private volatile boolean isStopping = false;
    /**
     * 人员总数量
     */
    private volatile int mTotalPerson;

    /**
     * 照片总数量
     */
    private volatile int mTotalPicture;

    /**
     * 已提取的数量
     */
    private volatile int mExtracted;

    /**
     * 任务开始时间，unix时间戳
     */
    private volatile long mBatchBeginTime;

    /**
     * 累计提取时间
     */
    private volatile long mExtractTotalCost;

    /**
     * 图片zip包解压进度，取值(0~1)
     */
    private volatile float mDecompressProcess;

    /**
     * 获取布控任务状态锁
     */
    private final Object mDeployLock = new Object();


    private List<BatchPersons> mRemainPersons = new ArrayList<>();

    private List<BatchPersons> mRemainProcessPersons = new ArrayList<>();


    private Context mContext;

    private ErrorInfoManager mErrorInfoManager;
    private DeployFileManager mDeployFileManager;

    private static FaceDataManager instance;

    public static FaceDataManager getInstance(){
        if(instance == null){
            instance = new FaceDataManager();
        }
        return instance;
    }
    public FaceDataManager() {
        mErrorInfoManager = new ErrorInfoManager();
    }

    public void init(Context context){
        mContext = context;
        mDeployFileManager = new DeployFileManager(mContext);
    }

    /**
     * 图片特征提取，用来PC端校验图片是否合格
     * @param bitmap
     * @return ExtractFeatResult
     */
    public ExtractFeatResult extractFeat(Bitmap bitmap) {
        ExtractFeatResult extractFeatResult = new ExtractFeatResult();
        DbAddResult result = FaceIdManager.getInstance().addFeature(bitmap);
        if(result == null) {
            ErrorCode error = ErrorCode.FACE_ADD_FEAT_NO_FACE_DETECT;
            extractFeatResult.setResultCode(error.getCode());
            extractFeatResult.setResultMsg(error.getMsg());
        } else {
            //目前接口只允许先入库，添加成功需要从数据库移除该feature
            FaceIdManager.getInstance().removeFeature(result.featId);
            extractFeatResult.setResultCode(ErrorCode.OK.getCode());
            extractFeatResult.setResultMsg(result.featId);
        }

        return extractFeatResult;
    }


    /**
     * 给已知人员添加新的特征素材图片(一个人可对应多张图片)
     * @param uid
     * @param featFileInfos
     * @param coverId
     * @param needSave
     * @return ErrorCode
     */
    public ErrorCode addFeats(String uid, List<FeatFileInfo> featFileInfos, String
            coverId, boolean needSave) {

        //uid不存在则返回不存在人员信息错误
        UserInfo userInfo = FaceIdManager.getInstance().getUserInfoById(uid);
        if(userInfo == null) {
            Logger.e("addFeats User not exist");
            return ErrorCode.FACE_ADD_FEAT_NO_PERSON;
        }

        if(featFileInfos == null) {
            //参数错误
            Logger.e("addFeats invalid param");
            return ErrorCode.INVALID_PARAM;
        } else {
            for(int i = 0; i < featFileInfos.size(); i ++) {
                FeatFileInfo fileInfo = featFileInfos.get(i);
                String filePath = fileInfo.getFilePath();
                Bitmap bitmap = ImageUtils.getRotateBitmap(filePath);
                Logger.i("addFeats filePath： " + filePath);
                AddFeatResultWrapper wrapper = addFeat(uid, bitmap);
                if(wrapper.getErrorCode() == ErrorCode.OK) {
                    if((!TextUtils.isEmpty(fileInfo.getFeatId()) && fileInfo.getFeatId().equals(coverId)) ||
                            (TextUtils.isEmpty(coverId) && i == 0)) {
                        // 2020-02-23 设置封面
                        FaceIdManager.getInstance().updateUser(userInfo.uid, userInfo.name,
                                userInfo.cardno, userInfo.nativeplace, userInfo.description, userInfo.isAlarm, wrapper.getFeatId());
                    }
                } else {
                    //  2020-02-25 保存错误信息
                    Logger.e("addFeats file: " + filePath + "  error: " + wrapper.getErrorCode());
                    File file = new File(filePath);
                    DeployErrInfo errInfo = new DeployErrInfo();
                    errInfo.setName(file.getName());
                    errInfo.setErrorMsg(wrapper.getErrorCode().getMsg());
                    mErrorInfoManager.addErrorInfo(errInfo);
                }

                if(bitmap != null && !bitmap.isRecycled()) {
                    bitmap.recycle();
                    bitmap = null;
                }
            }
        }

        Logger.e("addFeats needSave: " + needSave);
        if(needSave) {
            FaceIdManager.getInstance().saveFeatureDb();
            //保存错误信息
            mErrorInfoManager.saveErrorInfo();
        }
        updateDeployUpdateTime();

        // 部分成功错误码如何返回？ 暂定返回OK
        return ErrorCode.OK;
    }

    /**
     *  内部函数，封装添加特征结果
     * @param uid
     * @param featImg
     */
    private AddFeatResultWrapper addFeat(String uid, Bitmap featImg) {

        AddFeatResultWrapper addFeatResultWrapper = new AddFeatResultWrapper();
        if(featImg == null || featImg.isRecycled()) {
            addFeatResultWrapper.setErrorCode(ErrorCode.FACE_ADD_FEAT_FILE_NOT_EXIST);
        }

        long startExtractTime = System.currentTimeMillis();
        // 加入feature.db
        final DbAddResult addResult = FaceIdManager.getInstance().addFeature(featImg);

        String newFeatId = "";
        if (addResult != null && !TextUtils.isEmpty(addResult.featId)) {
            newFeatId = addResult.featId;

            Bitmap cropBitmap = FaceUtil.cropBitmap(featImg, addResult.maxFaceRect);
            try {
                FaceMapping mapping = new FaceMapping();
                mapping.uid = uid;
                mapping.fid = newFeatId;
                mapping.faceImg = ImageUtils.Bitmap2Bytes(cropBitmap);
                FaceIdManager.getInstance().addFaceMapping(mapping);
                addFeatResultWrapper.setErrorCode(ErrorCode.OK);
                addFeatResultWrapper.setFeatId(newFeatId);
            } catch (Exception e) {
                e.printStackTrace();
                FaceIdManager.getInstance().removeFeature(newFeatId); // 已经添加了feature.db，但是之后出现了异常，需要回滚数据库
                Logger.e("addFeat: 添加人脸特征出错 +");
                addFeatResultWrapper.setErrorCode(ErrorCode.FACE_ADD_FEAT_DB_ERROR);
            }
        } else {
            // feature添加失败
            Logger.e("addFeat: 插入feature.db出错");
            addFeatResultWrapper.setErrorCode(ErrorCode.FACE_ADD_FEAT_NO_FACE_DETECT);
        }

        //提取一个特征耗时
        long cost = System.currentTimeMillis() - startExtractTime;
        Logger.i("addFeat cost" + cost);

        //提取数量+1
        synchronized (mDeployLock) {
            mExtracted++;
            mExtractTotalCost += cost;
        }

        return addFeatResultWrapper;
    }


    /**
     * 根据特征值id删除特征值(一个人可对应多张图片)
     * @param featId
     * @return ErrorCode
     */
    public ErrorCode deleteFeat(String featId) {
        boolean delete = FaceIdManager.getInstance().deleteFace(featId);
        Logger.i("deleteFeat result: " + delete);
        return delete ? ErrorCode.OK : ErrorCode.FACE_DELETE_FEAT_FAILED;
    }


    /**
     * 添加单个布控任务
     * @param person
     * @param featFileInfos
     * @param coverId
     * @param needSave
     * @return ErrorCode
     */
    public ErrorCode addPerson(Person person, List<FeatFileInfo> featFileInfos, String
            coverId, boolean needSave) {

        // 增加人脸数据
        boolean hasFaceDb = true;
        if(needSave) {
            // 通知主界面模块，停止识别
            FaceIdManager.getInstance().sendPreReloadBroadcast(100);
            mErrorInfoManager.clearErrorInfo();

            if(person.isOverwrite()) {

                //清空所有数据
                boolean clear = FaceIdManager.getInstance().removeAllData();
                mDeployFileManager.clearFiles();
                Logger.i("addPerson removeAllData " + clear);
                if(!clear) {
                    return ErrorCode.FACE_ADD_PERSON_CLEAR_FAILED;
                }
            }
        }

        UserInfo userInfo = new UserInfo();
        userInfo.uid = UUID.randomUUID().toString();
        userInfo.name = person.getName();
        userInfo.cardno = person.getCardNo();
        userInfo.nativeplace = person.getBirthPlace();
        userInfo.description = person.getTag();
        userInfo.isAlarm = person.isAlarm();
        Logger.i("addPerson addUserInfo " + userInfo);

        FaceIdManager.getInstance().addUserInfo(userInfo);

        ErrorCode code = addFeats(userInfo.uid, featFileInfos, coverId, needSave);

        if(needSave) {
            // 有新的人脸数据库了，发送更新UI广播
            Logger.d("addPerson isFaceDbExists 有新的人脸特征库，发送UI广播");
            FaceIdManager.getInstance().sendAfterReloadBroadcast();
        }
        return code;
    }


    /**
     * 根据人员id(uuid)删除某个布控人员
     * @param uid
     * @return ErrorCode
     */
    public ErrorCode deletePerson(String uid) {
        boolean delete = FaceIdManager.getInstance().deleteUser(uid);
        return delete ? ErrorCode.OK : ErrorCode.FACE_DELETE_PERSON_FAILED;
    }


    /**
     * 根据人员id(uuid)删除布控人员
     * @param uids
     * @return ErrorCode
     */
    public ErrorCode deletePersons(List<String> uids) {

        // 通知主界面模块，停止识别
        FaceIdManager.getInstance().sendPreReloadBroadcast(100);

        boolean oneSuccess = false;
        for (String uid : uids) {
            boolean delete = FaceIdManager.getInstance().deleteUser(uid);
            Logger.i("deletePersons delete: " + delete + " id: " + uid);
            if(delete) {
                oneSuccess = true;
            }
        }
        FaceIdManager.getInstance().sendAfterReloadBroadcast();
        return oneSuccess ? ErrorCode.OK : ErrorCode.FACE_DELETE_PERSON_FAILED;
    }


    /**
     * 根据人员id(uuid)更新某个布控人员信息
     * @param uid
     * @param person
     * @return ErrorCode
     */
    public ErrorCode updatePerson(String uid,Person person, List<FeatFileInfo> addList) {
        Logger.i("updatePerson person: " + person + " addList: " + addList);
        // 通知主界面模块，停止识别
        FaceIdManager.getInstance().sendPreReloadBroadcast(100);

        //添加人脸

        if(addList != null && addList.size() > 0) {
            addFeats(uid, addList, person.getCoverId(), false);
        }

        List<String> deleteList = person.getDeleteFeatList();

        if(deleteList != null) {
            //删除人脸
            for (String featId : deleteList) {
                deleteFeat(featId);
            }
        }

        //更新
        boolean update = FaceIdManager.getInstance().updateUser(uid, person.getName(), person.getCardNo(),
                person.getBirthPlace(), person.getTag(), person.isAlarm(), person.getCoverId());

        save(false);

        FaceIdManager.getInstance().sendAfterReloadBroadcast();

        return update ? ErrorCode.OK : ErrorCode.FACE_UPDATE_PERSON_FAILED;
    }


    /**
     * 根据姓名或者身份证号模糊查找布控人员信息
     * @param searchString
     * @param pageNo
     * @param pageSize
     * @return DeployTaskListInfo
     */
    public DeployTaskListInfo getDeployTaskList(String searchString, int pageNo, int pageSize) {
        List<FaceInfo> list;

        DeployTaskListInfo deployTaskListInfo = new DeployTaskListInfo();
        int offset = (pageNo - 1) * pageSize;

        if(!TextUtils.isEmpty(searchString)) {
            list = FaceIdManager.getInstance().searchUserByNameOrCardNo(searchString, offset, pageSize);
            deployTaskListInfo.setTotalNum(FaceIdManager.getInstance().searchUserByNameOrCardNoCount(searchString));
        } else {
            list = FaceIdManager.getInstance().getAllUserInfo(offset, pageSize);
            deployTaskListInfo.setTotalNum(FaceIdManager.getInstance().getAllUserNum());
        }

        List<DeployTask> deployTasks = new ArrayList<>();
        for(FaceInfo faceInfo : list) {
            DeployTask deployTask = new DeployTask();
            UserInfo userInfo = faceInfo.getUserInfo();
            if(userInfo != null) {
                deployTask.setId(userInfo.uid);
                deployTask.setName(userInfo.name);
                deployTask.setCardNo(userInfo.cardno);
                deployTask.setBirthPlace(userInfo.nativeplace);
                deployTask.setTag(userInfo.description);
                deployTask.setAlarm(userInfo.isAlarm);

                FaceMappingWrapper wrapper = getPersonFeatInfo(userInfo.uid);
                if(wrapper != null) {
                    deployTask.setFeatList(wrapper.getFeatInfos());
                    deployTask.setCoverId(wrapper.getCoverId());
                }

                deployTasks.add(deployTask);
            }
        }

        deployTaskListInfo.setDeployTasks(deployTasks);

        return deployTaskListInfo;
    }


    /**
     * 内部函数封装
     * @param uid
     * @return FaceMappingWrapper
     */
    private FaceMappingWrapper getPersonFeatInfo(String uid) {
        //faceMapping 查询封面ID
        List<FaceMapping> faceMappings = FaceIdManager.getInstance().getFaceWithUID(uid);
        if(faceMappings == null || faceMappings.size() == 0) {
            return null;
        }

        String coverId = "";
        FaceMappingWrapper faceMappingWrapper = new FaceMappingWrapper();
        List<FeatInfo> featInfoList = new ArrayList<>();

        for(FaceMapping faceMapping : faceMappings) {
            FeatInfo featInfo = new FeatInfo();
            featInfo.setFeatId(faceMapping.fid);
            featInfo.setFeatImg(ImageUtils.byteToBase64(faceMapping.faceImg));
            featInfoList.add(featInfo);
            if(faceMapping.isCover) {
                coverId = faceMapping.fid;
            }
        }
        faceMappingWrapper.setFeatInfos(featInfoList);

        if(TextUtils.isEmpty(coverId)) {
            coverId = faceMappings.get(0).fid;
        }

        faceMappingWrapper.setCoverId(coverId);
        return faceMappingWrapper;
    }


    /**
     * 根据人员ID获取人员详情
     * @param uid
     * @return DeployTask
     */
    public DeployTask getDeployTask(String uid) {
        UserInfo userInfo = FaceIdManager.getInstance().getUserInfoById(uid);
        if(userInfo == null) {
            return null;
        }
        FaceMappingWrapper wrapper = getPersonFeatInfo(uid);
        DeployTask deployTask = new DeployTask();
        deployTask.setId(userInfo.uid);
        deployTask.setName(userInfo.name);
        deployTask.setCardNo(userInfo.cardno);
        deployTask.setBirthPlace(userInfo.nativeplace);
        deployTask.setTag(userInfo.description);
        deployTask.setAlarm(userInfo.isAlarm);

        if(wrapper != null) {
            deployTask.setFeatList(wrapper.getFeatInfos());
            deployTask.setCoverId(wrapper.getCoverId());
        }
        return deployTask;
    }
    /**
     * 批量添加布控人员信息
     * @param deployInfo
     * @param batchPersons
     * @param isOverwrite
     * @param filePath
     * @return ErrorCode
     */
    public ErrorCode addPersons(DeployInfo deployInfo, String filePath, List<BatchPersons>
            batchPersons, boolean isOverwrite) {

        isStopping = false;

        //开始时间
        mBatchBeginTime = System.currentTimeMillis();

        // 通知主界面模块，停止识别
        FaceIdManager.getInstance().sendPreReloadBroadcast(100);

        synchronized (mDeployLock) {
            mTotalPerson = batchPersons.size();
            for (BatchPersons batchPerson : batchPersons) {
                List<String> paths = batchPerson.getPictureList();
                mTotalPicture += paths.size();
            }
            Logger.i("addPersons mTotalPerson " + mTotalPerson + " mTotalPicture: " + mTotalPicture);
        }

        //清理错误信息文件
        mErrorInfoManager.clearErrorInfo();

        if(isOverwrite) {
            //清空所有数据
            boolean clear = FaceIdManager.getInstance().removeAllData();
            mDeployFileManager.clearFiles();
            Logger.i("addPersons removeAllData " + clear);
            if(!clear) {
                return ErrorCode.FACE_ADD_PERSONS_CLEAR_FAILED;
            }
        }

        mStatus = Constants.BINARY_STATUS_PIC_DECOMPRESS;

        ErrorCode unzipCode = mDeployFileManager.unzipFile(filePath, percent -> mDecompressProcess = percent);
        Logger.d("unzipCode: " + unzipCode);
        if(unzipCode != ErrorCode.OK) {
            return unzipCode;
        }

        mStatus = Constants.BINARY_STATUS_FEATURE_EXTRACTION;
        //更新布控包名字、过期时间
        ErrorCode updateResult = updateDeploy(deployInfo);
        Logger.i("addPersons updateDeploy " + updateResult);


        Logger.d("pauseResume addPersons batchPersons: " + batchPersons.size());
        for(BatchPersons batchPerson : batchPersons) {

            if(mStatus == Constants.BINARY_STATUS_PAUSE) {
                mRemainPersons.add(batchPerson);
                Logger.d("pauseResume pause restore: " + mRemainPersons.size());
            } else {
                List<String> paths = batchPerson.getPictureList();
                Person person = new Person();
                person.setName(batchPerson.getName());
                person.setCardNo(batchPerson.getCardNo());
                person.setBirthPlace(batchPerson.getBirthPlace());
                person.setTag(batchPerson.getTag());
                person.setAlarm(batchPerson.isAlarm());

                List<FeatFileInfo> featFileInfos = new ArrayList<>();
                for (String path : paths) {
                    FeatFileInfo featFileInfo = new FeatFileInfo();
                    String parentPath = mDeployFileManager.getDeployImageDir();
                    featFileInfo.setFilePath(parentPath + File.separator + path);
                    featFileInfos.add(featFileInfo);
                }

                ErrorCode code = addPerson(person, featFileInfos, null, false);
            }

            // 停止批处理
            if(isStopping) {
                // 2020-02-27  清空数据
                mDeployFileManager.clearFiles();
                mStatus = Constants.BINARY_STATUS_DONE;
                isStopping = false;
                break;
            }
        }

        //保存错误信息
        mErrorInfoManager.saveErrorInfo();

        //批处理最后一步做保存
        save(mStatus == Constants.BINARY_STATUS_PAUSE);

        FaceIdManager.getInstance().sendAfterReloadBroadcast();

        return ErrorCode.OK;
    }
    /**
     * 清除状态
     */
    private void clear() {
        mStatus = Constants.BINARY_STATUS_IDLE;
        mTotalPerson = 0;
        mTotalPicture = 0;
        mExtracted = 0;
        mExtractTotalCost = 0;
    }


    /**
     * 保存特征，生成bin文件
     */
    private void save(boolean isPause) {
        if(isPause) {
            //保存
            FaceIdManager.getInstance().saveFeatureDb();
            mStatus = Constants.BINARY_STATUS_PAUSE;
        } else {
            //开始保存
            mStatus = Constants.BINARY_STATUS_SAVING;
            //保存
            FaceIdManager.getInstance().saveFeatureDb();
            //保存完成
            mStatus = Constants.BINARY_STATUS_DONE;
            //重置批处理状态
            clear();
        }
    }


    /**
     * 暂定批处理
     * @return
     */
    public ErrorCode pauseDeployProcess() {
        if(mStatus ==  Constants.BINARY_STATUS_FEATURE_EXTRACTION) {
            mRemainPersons.clear();
            mStatus = Constants.BINARY_STATUS_PAUSE;
            return ErrorCode.OK;
        }
        return ErrorCode.FACE_ADD_PERSONS_PAUSE_FAILED;
    }


    /**
     * 继续批处理
     * @return
     */
    public ErrorCode resumeDeployProcess() {
        if(mStatus ==  Constants.BINARY_STATUS_PAUSE) {
            mStatus = Constants.BINARY_STATUS_FEATURE_EXTRACTION;
            mRemainProcessPersons.clear();
            mRemainProcessPersons.addAll(mRemainPersons);
            mRemainPersons.clear();
            Logger.d("pauseResume mRemainProcessPersons: " + mRemainProcessPersons.size());
            Logger.d("pauseResume mRemainPersons: " + mRemainPersons.size());
            // 通知主界面模块，停止识别
            FaceIdManager.getInstance().sendPreReloadBroadcast(100);
            boolean hasFaceDb = FaceIdManager.getInstance().isFaceDbExists();

            //更新布控包名字、过期时间

            for(BatchPersons batchPerson : mRemainProcessPersons) {

                if(mStatus == Constants.BINARY_STATUS_PAUSE) {
                    mRemainPersons.add(batchPerson);
                    Logger.d("pauseResume pause restore: " + mRemainPersons.size());
                } else {

                    List<String> paths = batchPerson.getPictureList();

                    Person person = new Person();
                    person.setName(batchPerson.getName());
                    person.setCardNo(batchPerson.getCardNo());
                    person.setBirthPlace(batchPerson.getBirthPlace());
                    person.setTag(batchPerson.getTag());

                    List<FeatFileInfo> featFileInfos = new ArrayList<>();
                    for (String path : paths) {
                        FeatFileInfo featFileInfo = new FeatFileInfo();
                        String parentPath = mDeployFileManager.getDeployImageDir();
                        featFileInfo.setFilePath(parentPath + File.separator + path);
                        featFileInfos.add(featFileInfo);
                    }

                    ErrorCode code = addPerson(person, featFileInfos, null, false);
                }

                // 停止批处理
                if(isStopping) {
                    // 2020-02-27  清空数据
                    mDeployFileManager.clearFiles();
                    mStatus = Constants.BINARY_STATUS_DONE;
                    isStopping = false;
                    break;
                }
            }

            //保存错误信息
            mErrorInfoManager.saveErrorInfo();

            //批处理最后一步做保存
            save(mStatus == Constants.BINARY_STATUS_PAUSE);

            if (!hasFaceDb && FaceIdManager.getInstance().isFaceDbExists()) {
                // 有新的人脸数据库了，发送更新UI广播
                Logger.i("##### addPersons isFaceDbExists 有新的人脸特征库，发送UI广播");
                FaceIdManager.getInstance().sendAfterReloadBroadcast();
            }
            return ErrorCode.OK;
        }
        return ErrorCode.FACE_ADD_PERSONS_RESUME_FAILED;
    }


    /**
     * 停止提取特征值
     * @return ErrorCode
     */
    public synchronized ErrorCode stopDeployProcess() {
        isStopping = true;
        return ErrorCode.OK;
    }


    /**
     * 获取布控任务状态接口
     * @return DeployStatus
     */
    public DeployStatus getDeployStatus() {
        DeployStatus deployStatus = new DeployStatus();
        synchronized (mDeployLock) {
            deployStatus.setTotalPerson(mTotalPerson);
            deployStatus.setTotalPicture(mTotalPicture);
            deployStatus.setExtracted(mExtracted);
            deployStatus.setBinaryStatus(mStatus);
            if(mStatus == Constants.BINARY_STATUS_IDLE || mStatus == Constants.BINARY_STATUS_DONE || mStatus == Constants.BINARY_STATUS_IMPORT_DONE) {
                deployStatus.setStatus(Constants.BINARY_STATUS_IDLE);
            } else {
                deployStatus.setStatus(1);
            }

            long remain = 0L;
            if(mExtracted > 0) {
                remain =  (mTotalPicture / mExtracted  - 1 ) * mExtractTotalCost / 1000;
            }
            deployStatus.setRemainingTime(remain);
            deployStatus.setBeginTime(mBatchBeginTime);
            deployStatus.setDecompressProcess(mDecompressProcess);
        }
        return deployStatus;
    }


    /**
     * 删除布控包
     * @return ErrorCode
     */
    public ErrorCode deleteDeploy() {
        //清除所有人脸数据
        boolean delete = FaceIdManager.getInstance().removeAllData();

        if(!delete) {
            return ErrorCode.DEPLOY_DELETE_DATA_FAILED;
        }

        //清除布控包信息数据
        FaceIdManager.getInstance().removeAllDeployDbInfo();

        return ErrorCode.OK;
    }


    /**
     * 获取布控包信息----用于导出布控包给PC
     * 步骤：
     *  1.先清理布控包目录
     *  2.打包所有文件，保存至指定目录
     *  3.返回文件路径
     * @return String
     */
    public String getDeployPackagePath() {
        return  mDeployFileManager.packageDeployFile();
    }


    /**
     * 更新离线布控包----用于PC导入离线布控包给眼镜
     * @param filePath
     * @return ErrorCode
     */
    public ErrorCode replaceDeployPackage(String filePath) {
        FileUtils.copyFileToDir(filePath, FaceConstants.FACE_DB_COPY_PATH);
        FaceIdManager.getInstance().checkOfflineFaceIdDB((fileName, timestamp) -> {
            Logger.i("onReplaced " + fileName, timestamp);
            //  2020-02-26 更新布控包更新时间
            updateDeployUpdateTime();
            mStatus = Constants.BINARY_STATUS_IMPORT_DONE;
        });
        // 2020-02-26 该操作是异步的，需要确认立刻返回结果是否正确
        mStatus = Constants.BINARY_STATUS_IMPORTING;
        return ErrorCode.OK;
    }

    /**
     * 更新布控包"更新时间"
     */
    private void updateDeployUpdateTime() {
        long now = System.currentTimeMillis();
        DeployDbInfo updateTimeInfo = FaceIdManager.getInstance().getDeployDbInfoByKey(DeployTaskConfig.KEYS.DB_DEPLOY_UPDATE_TIME);
        if(updateTimeInfo != null) {
            updateTimeInfo.setValueStr(String.valueOf(now));
            FaceIdManager.getInstance().updateDeployDbInfo(updateTimeInfo);
        } else {
            //如果数据库不存在字段，则插入一条新数据
            updateTimeInfo = new DeployDbInfo();
            updateTimeInfo.setKeyStr(DeployTaskConfig.KEYS.DB_DEPLOY_UPDATE_TIME);
            updateTimeInfo.setValueStr(String.valueOf(now));
            FaceIdManager.getInstance().addDeployDbInfo(updateTimeInfo);
        }
    }

    /**
     * 更新布控包"陌生人识别开关"状态
     */
    private void updateDeployNewFriendSwitch(boolean newFriendSwitch) {
        //布控任务信息没必要放到数据库中，放在SharedPreferences 中更加快捷
        DefaultSPHelper.getInstance().put(DeployTaskConfig.KEYS.DB_DEPLOY_NEW_FRIEND_SWITCH, newFriendSwitch);
    }

    /**
     * 更新布控包"陌生人识别开关"状态
     */
    private void updateDeployNewFriendDesc(String newFriendDesc) {
        //布控任务信息没必要放到数据库中，放在SharedPreferences 中更加快捷
        if(!TextUtils.isEmpty(newFriendDesc)){
            DefaultSPHelper.getInstance().put(DeployTaskConfig.KEYS.DB_DEPLOY_NEW_FRIEND_DESC, newFriendDesc);
        }
    }

    /**
     * 更新布控包"陌生人识别开关"状态
     */
    private void updateDeployNewFriendAlarm(boolean newFriendAlarm) {
        //布控任务信息没必要放到数据库中，放在SharedPreferences 中更加快捷，旧的信息仍然在数据库中
        DefaultSPHelper.getInstance().put(DeployTaskConfig.KEYS.DB_DEPLOY_NEW_FRIEND_ALARM, newFriendAlarm);
    }
    /**
     * 更新布控包信息，名字和过期时间
     * @param deployInfo
     * @return ErrorCode
     */
    public ErrorCode updateDeploy(DeployInfo deployInfo) {
        if(deployInfo == null){
            return ErrorCode.INVALID_PARAM;
        }
        if(!TextUtils.isEmpty(deployInfo.getName())) {
            DeployDbInfo nameInfo = FaceIdManager.getInstance().getDeployDbInfoByKey(DeployTaskConfig.KEYS.DB_DEPLOY_NAME);
            if (nameInfo != null) {
                nameInfo.setValueStr(deployInfo.getName());
                FaceIdManager.getInstance().updateDeployDbInfo(nameInfo);
            } else {
                //如果数据库不存在字段，则插入一条新数据
                nameInfo = new DeployDbInfo();
                nameInfo.setKeyStr(DeployTaskConfig.KEYS.DB_DEPLOY_NAME);
                nameInfo.setValueStr(deployInfo.getName());
                FaceIdManager.getInstance().addDeployDbInfo(nameInfo);
            }
        }

        //不传过期时间，默认传过来的值未-2，不做更新
        if(deployInfo.getExpireTime() != -2) {
            DeployDbInfo expireTimeInfo = FaceIdManager.getInstance().getDeployDbInfoByKey(DeployTaskConfig.KEYS.DB_DEPLOY_EXPIRE_TIME);
            if (expireTimeInfo != null) {
                expireTimeInfo.setValueStr(String.valueOf(deployInfo.getExpireTime()));
                FaceIdManager.getInstance().updateDeployDbInfo(expireTimeInfo);
            } else {
                //如果数据库不存在字段，则插入一条新数据
                expireTimeInfo = new DeployDbInfo();
                expireTimeInfo.setKeyStr(DeployTaskConfig.KEYS.DB_DEPLOY_EXPIRE_TIME);
                expireTimeInfo.setValueStr(String.valueOf(deployInfo.getExpireTime()));
                FaceIdManager.getInstance().addDeployDbInfo(expireTimeInfo);
            }
        }
        updateDeployUpdateTime();
        updateDeployNewFriendSwitch(deployInfo.isNewFriendSwitch());
        if(!TextUtils.isEmpty(deployInfo.getNewFriendDesc())){
            updateDeployNewFriendDesc(deployInfo.getNewFriendDesc());
        }
        updateDeployNewFriendAlarm(deployInfo.isNewFriendAlarm());
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(FaceConstants.ACTION_FACE_DEPLOY_INFO_CHANGED));
        return ErrorCode.OK;
    }


    /**
     * 获取布控任务信息
     * @return DeployInfo
     */
    public DeployInfo getDeployInfo() {
        DeployInfo info = new DeployInfo();

        List<DeployDbInfo> infos =FaceIdManager.getInstance().getAllDeployDbInfo();
        if(infos == null) {
            return info;
        }

        //过期时间默认-1
        info.setExpireTime(-1);
        info.setPersonNum(FaceIdManager.getInstance().getAllUserNum());
        info.setFaceImgNum(FaceIdManager.getInstance().getAllMappingNum());

        for(DeployDbInfo dbInfo : infos) {
            if(DeployTaskConfig.KEYS.DB_DEPLOY_NAME.equals(dbInfo.getKeyStr())) {
                info.setName(dbInfo.getValueStr());
            }
            if(DeployTaskConfig.KEYS.DB_DEPLOY_EXPIRE_TIME.equals(dbInfo.getKeyStr())) {
                try {
                    long expireTime = Long.parseLong(dbInfo.getValueStr());
                    if(expireTime == 0) {
                        expireTime = -1;
                    }
                    info.setExpireTime(expireTime);
                } catch (Exception e) {
                    Logger.e(DeployTaskConfig.KEYS.DB_DEPLOY_EXPIRE_TIME + " parse error");
                }
            }
            if(DeployTaskConfig.KEYS.DB_DEPLOY_UPDATE_TIME.equals(dbInfo.getKeyStr())) {
                try {
                    long updateTime = Long.parseLong(dbInfo.getValueStr());
                    info.setUpdateTime(updateTime);
                } catch (Exception e) {
                    Logger.e(DeployTaskConfig.KEYS.DB_DEPLOY_UPDATE_TIME + " parse error");
                }
            }
        }
        info.setNewFriendSwitch(DefaultSPHelper.getInstance().getBoolean(DeployTaskConfig.KEYS.DB_DEPLOY_NEW_FRIEND_SWITCH, false));
        info.setNewFriendDesc(DefaultSPHelper.getInstance().getString(DeployTaskConfig.KEYS.DB_DEPLOY_NEW_FRIEND_DESC, null));
        info.setNewFriendAlarm(DefaultSPHelper.getInstance().getBoolean(DeployTaskConfig.KEYS.DB_DEPLOY_NEW_FRIEND_ALARM, false));
        return info;
    }


    /**
     * 获取本次批量布控任务的整体错误信息
     * @return List<DeployErrInfo>
     */
    public List<DeployErrInfo> getDeployProcessErrorInfo() {
        return mErrorInfoManager.getErrorInfos();
    }

}
