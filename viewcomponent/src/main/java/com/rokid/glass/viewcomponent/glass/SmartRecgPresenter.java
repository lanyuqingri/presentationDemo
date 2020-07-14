package com.rokid.glass.viewcomponent.glass;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;

import com.rokid.facelib.model.FaceDO;
import com.rokid.facelib.model.FaceModel;
import com.rokid.facelib.utils.FaceFileUtils;
import com.rokid.facelib.utils.FaceRectUtils;
import com.rokid.glass.libbase.BaseLibrary;
import com.rokid.glass.libbase.config.GalleryConfig;
import com.rokid.glass.libbase.faceid.FaceConstants;
import com.rokid.glass.libbase.faceid.FaceIdManager;
import com.rokid.glass.libbase.faceid.database.UserInfo;
import com.rokid.glass.libbase.logger.Logger;
import com.rokid.glass.libbase.message.car.ReqCarRecognizeMessage;
import com.rokid.glass.libbase.message.car.RespCarRecognizeMessage;
import com.rokid.glass.libbase.message.face.ReqOnlineSingleFaceMessage;
import com.rokid.glass.libbase.message.face.RespOnlineSingleFaceMessage;
import com.rokid.glass.libbase.message.recogrecord.RecordMessage;
import com.rokid.glass.libbase.plate.DataConvertUtil;
import com.rokid.glass.libbase.plate.PlateDeployInfo;
import com.rokid.glass.libbase.plate.PlateInfo;
import com.rokid.glass.libbase.plate.PlateManager;
import com.rokid.glass.libbase.plate.PlateRecogConfig;
import com.rokid.glass.libbase.plate.PlateRecogRecord;
import com.rokid.glass.libbase.utils.DefaultSPHelper;
import com.rokid.glass.libbase.utils.RokidSystem;
import com.rokid.glass.libbase.utils.ThreadPoolHelper;
import com.rokid.glass.libbase.utils.UUIDUtils;
import com.rokid.glass.viewcomponent.glass.online.OnlineRecgHelper;
import com.rokid.glass.viewcomponent.glass.online.OnlineResp;
import com.rokid.glass.viewcomponent.glass.view.MultiFaceView;
import com.rokid.glass.viewcomponent.glass.view.SmartRecgView;

import java.io.File;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static com.rokid.glass.viewcomponent.glass.CameraParams.PREVIEW_HEIGHT;
import static com.rokid.glass.viewcomponent.glass.CameraParams.PREVIEW_WIDTH;


public class SmartRecgPresenter implements OnlineResp {
    private SmartRecgView mSmartRecgView;
    private MultiFaceView mMultiFaceView;
    private Handler mHandler;
    private int mCurTrackId;
    private long mCurFaceFirstTime = 0;
    private Bitmap mBestBitmap = null;
    private float mCurrentSharpness = 0;
    private Rect mSingleRoiRect;
    private OnlineRecgHelper mOnlineRecgHelper;
    private OnResultShowListener onResultShowListener;
    private AtomicInteger reqId;
    private AtomicBoolean isOnineRecging;
    private Bitmap mRecgCaptureBitmap;
    private Bitmap mFaceRecordBitmap;
    private static final int OUT_TIME = 3000;
    private int out_time;
    private boolean isOnlineDebug = false;
    private File debugDir = new File("/sdcard/test/");
    private Rect mRoiRect;

    public SmartRecgPresenter(){
        mOnlineRecgHelper = OnlineRecgHelper.getInstance();
        mOnlineRecgHelper.addOnlineResp(this);
        isOnineRecging = new AtomicBoolean(false);
        reqId = new AtomicInteger(0);
        isOnlineDebug = DefaultSPHelper.getInstance().getBoolean(FaceConstants.ROKID_ONLINE_FACE_DEBUG_KEY,false);
    }

    // 绑定智能识别view以及 handler
    public void bindView(SmartRecgView view, MultiFaceView multiFaceView,Handler handler){
        mSmartRecgView = view;
        mMultiFaceView = multiFaceView;
        mHandler = handler;
        mOnlineRecgHelper.addOnlineResp(this);
        mSingleRoiRect = mSmartRecgView.getRoiRect();
        mSmartRecgView.setOnResultShowListener(onResultShowListener);
    }

    //与智能识别view 解除绑定关系
    public void unbindView(){
        mSmartRecgView = null;
        mMultiFaceView = null;
        mHandler.removeCallbacksAndMessages(null);
        mOnlineRecgHelper.removeOnlineResp(this);
    }

    //同步人脸识别结果数据
    public void handleFaceModel(final FaceModel model, final byte[] rawData){
//        Logger.d("handleFaceModel------>is called && ThreadId: " + Thread.currentThread().getId());
        boolean isMultiFaceRecg = BaseLibrary.getInstance().isMultiFaceRecgEnable();
        if(mSmartRecgView.isSingleFaceInfoShowing() || mSmartRecgView.isCarShowing()){
            if(isMultiFaceRecg){
                mMultiFaceView.setFaceModel(model);
            }
            return;
        }
        boolean isSingleFaceRecg = BaseLibrary.getInstance().isSingleFaceRecgEnable();
        boolean isOnlineSingleRecg = BaseLibrary.getInstance().isOnlineSingleFaceRecgEnable();
        if(isSingleFaceRecg){
            if(isOnineRecging.get() || mSmartRecgView.isCarShowing()){
                //如果正在进行在线识别或者有车牌信息在展示，则本次单人人脸信息暂时不做处理
                Logger.d("handleFaceModel---------->isOnineRecging: " + isOnineRecging.get());
                return;
            }
            FaceModel rectFaceModel = getFaceInSingleRect(model);
            if(rectFaceModel == null || rectFaceModel.getFaceList() == null || rectFaceModel.getFaceList().size() <= 0){
                //无单人识别数据,如果多人识别未打开，同时车牌识别也没信息需要展示，则继续识别，此无人脸信息就不再展示了
                if(!isMultiFaceRecg && !isOnineRecging.get() && !mSmartRecgView.isCarShowing()){
                    mSmartRecgView.showNormalRect();
                }
            } else {
                FaceDO maxSingleFaceDO = rectFaceModel.getMaxFace();
                if(maxSingleFaceDO == null){
                    return;
                }
                if(isMultiFaceRecg && !isFaceLargeEnough(getFaceDoRect(maxSingleFaceDO))){
                    //开着多人，但是单人最大的框没超过roi的三十二分之一，则忽略此单人核查信息，只进行多人的展示
                    Logger.d("handleFaceModel--------> multi face recognition && single face is too small");
                } else {
                    //需要对单人进行核查
                    if(isOnlineSingleRecg){
                        //单人在线
                        isOnineRecging.set(true);
                        final FaceDO maxFaceDO = getTargetFace(rectFaceModel);
                        ThreadPoolHelper.getInstance().threadExecute(new Runnable() {
                            @Override
                            public void run() {
                                handleSingleFaceNetWork(rawData, maxFaceDO);
                            }
                        });
                    } else {
                        //单人离线
                        handleSingleFaceLocal(maxSingleFaceDO,rawData);
                    }
                }
            }
        }
        if(isMultiFaceRecg){
            mMultiFaceView.setFaceModel(model);
        }
    }

    private void handleSingleFaceLocal(final FaceDO targetFace, final byte[] rawData){
        Logger.d("handleSingleFaceLocal-------->  is called");
        boolean isMultiRecg = BaseLibrary.getInstance().isMultiFaceRecgEnable();
        boolean needShowNoFace = (!isMultiRecg);
        if(targetFace == null){
            Logger.d("handleSingleFaceLocal--------> targetFace: is null");
            return;
        }
        if(!TextUtils.isEmpty(targetFace.featid)){
            Logger.d("handleSingleFaceLocal-------------->targetFace.featid: " + targetFace.featid);
            final UserInfo info = FaceIdManager.getInstance().getUserInfoByFid(targetFace.featid);
            if(info != null){
                mSmartRecgView.showFaceInfo(targetFace);
                showTimer();
                ThreadPoolHelper.getInstance().threadExecute(new Runnable() {
                    @Override
                    public void run() {
                        Rect faceRect = targetFace.toRect(PREVIEW_WIDTH,PREVIEW_HEIGHT);
                        Rect finalRect = FaceRectUtils.scaleRect(faceRect,PREVIEW_WIDTH,PREVIEW_HEIGHT,1.6f);
                        Bitmap bm = BitmapUtils.nv21ToBitmap(rawData,PREVIEW_WIDTH,PREVIEW_HEIGHT,finalRect);
                        Utils.saveFaceOfflineInfo(bm, info, targetFace);
                        RecordMessage recordMessage = Utils.ConvertUserInfo2RecordMessage(info,bm,bm,true);
                        OnlineRecgHelper.getInstance().sendRecordMessage(recordMessage);
                    }
                });
            } else if(needShowNoFace){
                mSmartRecgView.showDetect();
            }
        } else if(!targetFace.goodPose){
            Logger.d("handleSingleFaceLocal--------> targetFace.goodPose: is false && needShowNoFace: " + needShowNoFace);
            if(needShowNoFace){
                mSmartRecgView.showBadPose();
            }
        } else if(targetFace.recogOutTime){
            Logger.d("handleSingleFaceLocal--------> targetFace.recogOutTime: needShowNoFace: " + needShowNoFace);
            if(needShowNoFace){
                mSmartRecgView.showNewFriend();
                showTimer();
            }
        } else {
            Logger.d("handleSingleFaceLocal--------> targetFace.featId is empty && needShowNoFace: " + needShowNoFace);
            if(needShowNoFace){
                mSmartRecgView.showDetect();
            }
        }
    }
    /**
     * 处理单人识别的网络请求
     */
    private void handleSingleFaceNetWork(byte[] rawdata, FaceDO targetFace) {
        if(mSmartRecgView.isSingleFaceInfoShowing()){
            isOnineRecging.set(false);
            return;
        }

        if(targetFace == null){
            isOnineRecging.set(false);
            return;
        }

        targetFace.trackInterval = SystemClock.elapsedRealtime() - mCurFaceFirstTime;
        Logger.i("handleSingleNetWork------>targetFace.trackInterval: " + targetFace.trackInterval);
        Logger.i("handleSingleNetWork------>targetFace.faceScore:"+targetFace.faceScore);
        if(targetFace.goodQuality && targetFace.faceScore>0.75) {
            Logger.i("-----goodQuality-------");
            Rect faceRect = targetFace.toRect(PREVIEW_WIDTH,PREVIEW_HEIGHT);
            Rect finalRect = FaceRectUtils.scaleRect(faceRect,PREVIEW_WIDTH,PREVIEW_HEIGHT,1.6f);
            Logger.i("-----rect finalRect-----"+finalRect);
            Bitmap bm = BitmapUtils.nv21ToBitmap(rawdata,PREVIEW_WIDTH,PREVIEW_HEIGHT,finalRect);
            mRecgCaptureBitmap = BitmapUtils.nv21ToBitmap(rawdata,PREVIEW_WIDTH,PREVIEW_HEIGHT);
            doNetWork(bm);
        }else{
            if(targetFace.sharpness > mCurrentSharpness && targetFace.faceScore > 0.75){
                Rect faceRect = targetFace.toRect(PREVIEW_WIDTH,PREVIEW_HEIGHT);
                Rect finalRect = FaceRectUtils.scaleRect(faceRect,PREVIEW_WIDTH,PREVIEW_HEIGHT,1.6f);
                Logger.i("finalRect:"+finalRect);
                mBestBitmap = BitmapUtils.nv21ToBitmap(rawdata,PREVIEW_WIDTH,PREVIEW_HEIGHT,finalRect);
                mCurrentSharpness = targetFace.sharpness;
            }
            Logger.i("targetFace.trackInterval:" + targetFace.trackInterval);
            if(targetFace.trackInterval > 2000) {
                Logger.i("-----timeOut-------");
                mRecgCaptureBitmap = BitmapUtils.nv21ToBitmap(rawdata,PREVIEW_WIDTH,PREVIEW_HEIGHT);
                doNetWork(mBestBitmap);
            } else {
                isOnineRecging.set(false);
            }
        }
    }

    private void doNetWork(final Bitmap bm){
        Logger.i("-----doNetWork------- && isOnineRecging： " + isOnineRecging.get());
        if(bm == null){
            return;
        }
        mFaceRecordBitmap = bm;
        mSmartRecgView.post(new Runnable() {
            @Override
            public void run() {
                if(onResultShowListener!=null){
                    onResultShowListener.onFaceOnlineRecog(bm);
                }
                mSmartRecgView.showCropBitmap(bm);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        reset();
                    }
                },2000);
            }
        });

        if(isOnlineDebug) {
            if (!debugDir.exists()) {
                debugDir.mkdirs();
            }
            FaceFileUtils.saveBitmap(bm, "/sdcard/test/" + (mCurTrackId) + ".png");
        }

        byte[] bytes = BitmapUtils.bitmap2bytes(bm);
        ReqOnlineSingleFaceMessage onlineSingleFaceMessage = new ReqOnlineSingleFaceMessage(mCurTrackId, bytes);
        onlineSingleFaceMessage.setWidth(bm.getWidth());
        onlineSingleFaceMessage.setHeight(bm.getHeight());
        OnlineRecgHelper.getInstance().sendFaceRecgMessage(onlineSingleFaceMessage);
        Logger.i("-----sendMessage-------");
    }

    // 同步车牌识别结果数据，看是否要在线识别还是离线识别
    public void handlePlate(final String plateName, final Bitmap bm) {
        if(TextUtils.isEmpty(plateName)){
            return;
        }
        if(mSmartRecgView.isCarShowing()){
            return;
        }
        boolean isOnlinePlateRecg = BaseLibrary.getInstance().isOnlinePlateRecg();

        if(isOnlinePlateRecg){
            //在线车牌识别
            if(isOnineRecging.get()){
                //如果正在在线识别中，则不再进行车牌的识别
                return;
            }
            isOnineRecging.set(true);
            ThreadPoolHelper.getInstance().threadExecute(new Runnable() {
                @Override
                public void run() {
                    ReqCarRecognizeMessage message = transReqCarRecgMessage(plateName,bm);
                    Logger.i("handlePlate && online requestId: " + message.getRequestId() + ";  && playeNum: " + plateName);
                    OnlineRecgHelper.getInstance().sendCarRecgMessage(message);
                }
            });
        } else {
            //本地车牌识别
            Logger.i("handlePlate && local ;  && playeNum: " + plateName + ";     && ThreadId: " + Thread.currentThread().getId());
            PlateDeployInfo deployInfo = PlateManager.getInstance().getPlateDeployInfo();
            if(deployInfo != null && deployInfo.getExpireTime() > System.currentTimeMillis()){
                //车牌布控包已经失效，则不再进行匹配，全部是未识别
                mSmartRecgView.showNoCarInfo();
                showTimer();
                return;
            }
            PlateInfo plateInfo = DataConvertUtil.convertCarBeanToPlateInfo(CSVFileUtil.getInstance().getCarRecogByPlate(plateName));
            if(plateInfo == null){
                mSmartRecgView.showNoCarInfo();
                showTimer();
            } else {
                mSmartRecgView.showCarDetailInfo(plateInfo);
                showTimer();
                String fileName = plateInfo.getOwner()+"_"+ SystemClock.elapsedRealtime();
                String filePath = GalleryConfig.DIR_PLATE_CROP + fileName + ".png";
                Utils.savePlateInfo(bm ,fileName);
                savePlateRecordToDb(plateInfo, filePath, null,false);
                OnlineRecgHelper.getInstance().syncPlateOfflineRecordToServer(plateInfo, bm);
            }
        }
    }

    private void savePlateRecordToDb(PlateInfo plateInfo, String recordFilePath, String location, boolean isOnline){
        PlateRecogRecord plateRecogRecord = new PlateRecogRecord();
        plateRecogRecord.setRecordId(UUIDUtils.generateUUID());
        plateRecogRecord.setTag(plateInfo.getTag());
        plateRecogRecord.setStatus(plateInfo.getStatus());
        plateRecogRecord.setPlate(plateInfo.getPlate());
        plateRecogRecord.setRecogFilePath(recordFilePath);
        plateRecogRecord.setPhoneNum(plateInfo.getPhoneNum());
        plateRecogRecord.setOwner(plateInfo.getOwner());
        plateRecogRecord.setLocation(location);
        plateRecogRecord.setGmtTime(System.currentTimeMillis());
        plateRecogRecord.setIdcard(plateInfo.getIdcard());
        plateRecogRecord.setDate(plateInfo.getDate());
        plateRecogRecord.setColor(plateInfo.getColor());
        plateRecogRecord.setBrand(plateInfo.getBrand());
        plateRecogRecord.setAddress(plateInfo.getAddress());
        plateRecogRecord.setOnline(isOnline);
        plateRecogRecord.setApiFrom(PlateRecogConfig.DEFAULT_API_FROM);
        PlateManager.getInstance().addPlateRecord(plateRecogRecord);
    }

    private ReqCarRecognizeMessage transReqCarRecgMessage(String plateNum, Bitmap bm){
        ReqCarRecognizeMessage reqMessage = new ReqCarRecognizeMessage();
        reqMessage.setRequestId(reqId.get());
        if(bm!=null){
            byte[] bytes = BitmapUtils.bitmap2Bytes(bm);
            reqMessage.setPlateImage(bytes);
            reqMessage.setWidth(bm.getWidth());
            reqMessage.setHeight(bm.getHeight());
        }
        reqMessage.setPlate(plateNum);
        return reqMessage;
    }

    private FaceModel getFaceInSingleRect(FaceModel model){
        if(model == null){
            return null;
        }
        List<FaceDO> faceDOList = model.getFaceList();
        if(faceDOList == null || faceDOList.size() <= 0){
            return null;
        }
        boolean isMultiEnable = BaseLibrary.getInstance().isMultiFaceRecgEnable();
        Logger.d("getFaceInSingleRect------->faceDOList.size(): " + faceDOList.size());
        if(faceDOList.size() == 1 || !isMultiEnable){
            //仅有一张人脸，或者没有同时开启多人识别，则不再检测是否在rect内
            return model;
        }
        FaceModel faceModel = new FaceModel();
        for(FaceDO faceDO: faceDOList){
            if(isFaceInSingleRect(faceDO)){
                faceModel.addFace(faceDO);
            }
        }
        return faceModel;
    }

    public boolean isFaceInSingleRect(FaceDO faceDO){
        Rect rect = getFaceDoRect(faceDO);
        Logger.d("isFaceInSingleRect------->faceRect: " + rect.toString());
        Logger.d("isFaceInSingleRect------->singleRoiRect: " + mSingleRoiRect.toString());
        if(rect.left >= mSingleRoiRect.left && rect.right <= mSingleRoiRect.right &&
                rect.top >= mSingleRoiRect.top && rect.bottom <= mSingleRoiRect.bottom){
            Logger.d("isFaceInSingleRect----------> return true");
            return true;
        }
        return false;
    }

    private boolean isFaceLargeEnough(Rect rect){
        int rectWidth = (rect.right - rect.left);
        int rectHeight = (rect.bottom - rect.top);
        int targetRectWidth = (mSingleRoiRect.right - mSingleRoiRect.left);
        int targetRectHeight = (mSingleRoiRect.bottom - mSingleRoiRect.top);
        boolean isMultiFaceEnable = BaseLibrary.getInstance().isMultiFaceRecgEnable();
        if(!isMultiFaceEnable){
            //如果没开多人识别，则识别到的人脸都认为足够大，不再做阈值判断
            return true;
        }
        if((rectHeight * rectWidth) >= (targetRectHeight * targetRectWidth)/64){
            Logger.d("isFaceLargeEnough-------------------------------->return true");
            //目前多人/单人同时识别时单人的人脸需要大于六十四分之一框，才认为足够大
            return true;
        }
        return false;
    }

    private Rect getFaceDoRect(FaceDO faceDO){
        Rect finalRect;
        if (BaseLibrary.getInstance().getEnableMultiFace()) {
            // Preview预览显示
            finalRect = faceDO.toRect(
                    BaseLibrary.getInstance().getScreenSize().first,
                    BaseLibrary.getInstance().getScreenSize().second);
        }
        else {
            // AR显示，需要经过alignment
            Rect rect = faceDO.toRect(PREVIEW_WIDTH, PREVIEW_HEIGHT);
            finalRect = RokidSystem.getAlignmentRect(PREVIEW_WIDTH, PREVIEW_HEIGHT, rect);
        }
        return finalRect;
    }
    /**
     * @fun 单人识别时获取目标人脸
     * @param model
     * @return
     */
    public FaceDO getTargetFace(FaceModel model) {
        FaceDO targetFace = model.getMaxFace();
        if(targetFace!=null){
            if(!targetFace.goodPose){
                return null;
            }
            if(targetFace.trackId!= mCurTrackId){
                Logger.i("getTargetFace targetFace.trackId:"+targetFace.trackId + " curTrackid:" + mCurTrackId);
                mBestBitmap = null;
                mCurFaceFirstTime = SystemClock.elapsedRealtime();
                mCurrentSharpness = 0;
            }
            mCurTrackId = targetFace.trackId;
        }else{
            mCurTrackId = -1;
            mBestBitmap = null;
            mCurFaceFirstTime = SystemClock.elapsedRealtime();
            mCurrentSharpness = 0;
        }
        Logger.i("getTargetFace curTrackid:" + mCurTrackId);
        return targetFace;
    }

    public FaceDO getFaceById(FaceModel model, int trackid){
        for(FaceDO faceDO:model.getFaceList()){
            if(faceDO.trackId == trackid){
                return faceDO;
            }
        }
        return null;
    }

    public void showTimer() {
        out_time = OUT_TIME;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                showContinueRecg();
            }
        });
    }

    private void showContinueRecg(){
        if(out_time <= 0){
            mSmartRecgView.post(new Runnable() {
                @Override
                public void run() {
                    reset();
                }
            });
        } else {
            mSmartRecgView.post(new Runnable() {
                @Override
                public void run() {
                    mSmartRecgView.tv_retry.setVisibility(View.VISIBLE);
                    mSmartRecgView.tv_retry.setText("继续识别(" + ((out_time / 1000)+1) + "s)");
                }
            });
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showContinueRecg();
                }
            },1000);
            out_time -= 1000;
        }
    }

    public void reset(){
        mHandler.removeCallbacksAndMessages(null);
        isOnineRecging.set(false);
        mSmartRecgView.reset();
        mMultiFaceView.clearCache();
        mCurrentSharpness = 0;
        mBestBitmap = null;
        mCurTrackId = -1;
        mCurFaceFirstTime = 0;
        if(onResultShowListener!=null){
            onResultShowListener.onResultHide();
        }
    }

    public void setOnResultShowListener(OnResultShowListener onResultShowListener) {
        if(this.onResultShowListener == null){
            this.onResultShowListener = onResultShowListener;
        }
        if(mSmartRecgView != null){
            mSmartRecgView.setOnResultShowListener(onResultShowListener);
        }
    }

    /*
    * 在线车牌识别结果返回，目前先暂时使用之前蓝牙传输定义的数据协议
     */
    @Override
    public void onCarResp(RespCarRecognizeMessage respCarRecognizeMessage) {
        switch (respCarRecognizeMessage.getErrorCode()){
            case 0:
                Logger.i("onCarResp && requestId: " + respCarRecognizeMessage.getRequestId());
                mHandler.removeCallbacksAndMessages(null);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mSmartRecgView.showCarDetailInfo(respCarRecognizeMessage);
                    }
                });

                showTimer();
//                byte[] imgeBytes = respCarRecognizeMessage.getPlateImage();
//                Bitmap bm = null;
//                if(imgeBytes!=null){
//                    bm = BitmapFactory.decodeByteArray(imgeBytes,0,imgeBytes.length);
//                }
//                if(bm!= null) {
//                    Utils.savePlateInfo(bm ,respCarRecognizeMessage.getCarRecognizeInfoBean().getOwner()+"_"+SystemClock.elapsedRealtime());
//                }
//                PlateInfo plateInfo = DataConvertUtil.convertCarBeanToPlateInfo(respCarRecognizeMessage.getCarRecognizeInfoBean());
//                String fileName = plateInfo.getOwner()+"_"+ SystemClock.elapsedRealtime();
//                String filePath = GalleryConfig.DIR_PLATE_CROP + fileName + ".png";
//                savePlateRecordToDb(plateInfo,filePath,null,true);
                break;
            case 1:
                boolean isMultiFaceRecg = BaseLibrary.getInstance().isMultiFaceRecgEnable();
                boolean isSingleFaceRecg = BaseLibrary.getInstance().isSingleFaceRecgEnable();
                if(!isMultiFaceRecg && !isSingleFaceRecg){
                    mHandler.removeCallbacksAndMessages(null);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mSmartRecgView.showNoCarInfo();
                        }
                    });
                    showTimer();
                } else {   //同时开启多人/单人识别时，继续进行人脸相关的多人/单人识别，无车牌信息就不再展示
                }
                break;
        }
        reqId.addAndGet(1);
        isOnineRecging.set(false);
    }

    /*
     * 在线单人核查识别结果返回，目前先暂时使用之前蓝牙传输定义的数据协议
     */
    @Override
    public void onFaceResp(RespOnlineSingleFaceMessage respOnlineSingleFaceMessage) {
        RespOnlineSingleFaceMessage resp = respOnlineSingleFaceMessage;
        Logger.d("onFaceResp------------------------>is called && serverCode: " + resp.getServerCode());
        switch (resp.getServerCode()){
            case OK:
                if(resp.getTrackId() == mCurTrackId){
                    mHandler.removeCallbacksAndMessages(null);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mSmartRecgView.showFaceDetailInfo(respOnlineSingleFaceMessage);
                            if(onResultShowListener!=null) {
                                onResultShowListener.onResultShow();
                            }
                        }
                    });
                    showTimer();
//                    Utils.saveRespOnlineSingleFaceMessage(resp,mFaceRecordBitmap,mRecgCaptureBitmap);
                }
                break;
            default:
                boolean isMultiRecg = BaseLibrary.getInstance().isMultiFaceRecgEnable();
                if(!isMultiRecg){
                    mHandler.removeCallbacksAndMessages(null);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mSmartRecgView.showNewFriend();
                        }
                    });

                    showTimer();
                } else {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mSmartRecgView.reset();
                        }
                    });
                }
                break;
        }
        isOnineRecging.set(false);
    }
}