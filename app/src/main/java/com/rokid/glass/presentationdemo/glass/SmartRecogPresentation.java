package com.rokid.glass.presentationdemo.glass;

import android.app.Presentation;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.rokid.facelib.VideoRokidFace;
import com.rokid.facelib.api.IVideoRokidFace;
import com.rokid.facelib.api.RokidFaceCallback;
import com.rokid.facelib.conf.DetectFaceConf;
import com.rokid.facelib.conf.RecogFaceConf;
import com.rokid.facelib.conf.VideoDetectFaceConf;
import com.rokid.facelib.input.VideoInput;
import com.rokid.facelib.model.FaceModel;
import com.rokid.glass.libbase.BaseLibrary;
import com.rokid.glass.libbase.config.DeployTaskConfig;
import com.rokid.glass.libbase.faceid.FaceConstants;
import com.rokid.glass.libbase.faceid.FaceIdManager;
import com.rokid.glass.libbase.faceid.database.DeployDbInfo;
import com.rokid.glass.libbase.logger.Logger;
import com.rokid.glass.libbase.utils.DefaultSPHelper;
import com.rokid.glass.libbase.utils.FaceUtil;
import com.rokid.glass.libbase.utils.PlateUtils;
import com.rokid.glass.lpr_sdk.LPRConfig;
import com.rokid.glass.lpr_sdk.RokidLPR;
import com.rokid.glass.lpr_sdk.RokidLPRCallback;
import com.rokid.glass.lpr_sdk.bean.LPRDO;
import com.rokid.glass.lpr_sdk.bean.LPRModel;
import com.rokid.glass.presentationdemo.R;
import com.rokid.glass.presentationdemo.glass.view.MultiFaceView;
import com.rokid.glass.presentationdemo.glass.view.SmartRecgView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

public class SmartRecogPresentation extends Presentation {
    private SmartRecgView mSmartRecgView;
    private MultiFaceView mMultiFaceView;
    private SmartRecgPresenter mSmartRecgPresenter;
    private IVideoRokidFace videoFace;
    private static final int PREVIEW_WIDTH = CameraParams.PREVIEW_WIDTH;
    private static final int PREVIEW_HEIGHT = CameraParams.PREVIEW_HEIGHT;
    private DetectFaceConf dFaceConf;
    private RecogFaceConf sFaceConf;
    private RokidLPR rokidLPR;
    private Rect roiRect;
    private int qualityValue;
    private Handler mHandler = new Handler();
    private OnResultShowListener onResultShowListener;
    private AtomicBoolean isCameraStop = new AtomicBoolean(false);

    public SmartRecogPresentation(Context outerContext, Display display) {
        this(outerContext, display, R.style.LauncherTheme);
    }

    public SmartRecogPresentation(Context outerContext, Display display, int theme) {
        super(outerContext, display, theme);
        if(CameraParams.PREVIEW_WIDTH == 1280) {
            roiRect = new Rect(200, 160, 850, 650);
        } else if (CameraParams.PREVIEW_WIDTH == 1920) {
            roiRect = new Rect(525, 515, 1125, 975);
        }
//        roiRect = new Rect(525, 515, 1125, 975);
        qualityValue = DefaultSPHelper.getInstance().getInt(FaceConstants.ROKID_FACE_QUALITY, FaceConstants.DEFAULT_FACE_QUALITY_VALUE);
        if (mSmartRecgPresenter == null) {
            mSmartRecgPresenter = new SmartRecgPresenter();
        }
        initFaceSDK();
        initPlateSDK();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_smart_recg);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mSmartRecgView = (SmartRecgView)findViewById(R.id.smart_recg_view);
        mMultiFaceView = (MultiFaceView)findViewById(R.id.multiFaceView);
        mSmartRecgPresenter.bindView(mSmartRecgView, mMultiFaceView, mHandler);
        mSmartRecgPresenter.setOnResultShowListener(onResultShowListener);
        initAllView();
    }

    private void initAllView() {
        boolean isMultiFaceEnable = BaseLibrary.getInstance().isMultiFaceRecgEnable();
        boolean isSingleFaceEnable = BaseLibrary.getInstance().isSingleFaceRecgEnable();
        boolean isPlateEnbale = BaseLibrary.getInstance().isPlateRecgEnable();
        if(mMultiFaceView != null){
            mMultiFaceView.setVisibility(isMultiFaceEnable ? View.VISIBLE : View.GONE);
        }
        if(mSmartRecgView != null){
            mSmartRecgView.setVisibility((!isSingleFaceEnable && !isPlateEnbale) ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * 初始化人脸识别sdk
     */
    private void initFaceSDK() {
        boolean isMultiFaceRecg = BaseLibrary.getInstance().isMultiFaceRecgEnable();
        boolean isSingleFaceRecg = BaseLibrary.getInstance().isSingleFaceRecgEnable();
        if (!isMultiFaceRecg && !isSingleFaceRecg) {
            return;
        }
        dFaceConf = new VideoDetectFaceConf().setSize(PREVIEW_WIDTH, PREVIEW_HEIGHT).setPoolNum(10).setDetectMaxFace(10);
        if (!isMultiFaceRecg) {
            dFaceConf.setRoi(roiRect);
            dFaceConf.setSingleRecogModel(true);
        } else {
            dFaceConf.setSingleRecogModel(false);
            dFaceConf.setSize(PREVIEW_WIDTH, PREVIEW_HEIGHT);
        }
        videoFace = VideoRokidFace.create(getContext(), dFaceConf);
        qualityValue = DefaultSPHelper.getInstance().getInt(FaceConstants.ROKID_FACE_QUALITY, FaceConstants.DEFAULT_FACE_QUALITY_VALUE);
        DeployDbInfo deployDbInfo = FaceIdManager.getInstance().getDeployDbInfoByKey(DeployTaskConfig.KEYS.DB_DEPLOY_EXPIRE_TIME);
        boolean recognize = true;
        long now = System.currentTimeMillis();
        Logger.d("deploy package expire info: " + deployDbInfo);
        if (deployDbInfo != null) {
            String expireTimeStr = deployDbInfo.getValueStr();
            long expireTime = Long.parseLong(expireTimeStr);
            if (expireTime > 0 && expireTime < now) {
                recognize = false;
                Logger.d("deploy package expired!");
            }
        }
        sFaceConf = new RecogFaceConf().setRecog(recognize, FaceIdManager.PATH_ENGINE).setOutTime(1500).setRecogInterval(5000).setTargetScore(qualityValue);
        videoFace.sconfig(sFaceConf);
        videoFace.startTrack(new RokidFaceCallback() {
            @Override
            public void onFaceCallback(final FaceModel model) {
                // 检测到有人脸数据则展示人脸数据
                FaceModel faceModel = FaceUtil.copyFaceModel(model);
                mSmartRecgPresenter.handleFaceModel(model, videoFace.getBytes());
//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//
//                    }
//                });
            }
        });
    }

    /**
     * 初始化车牌识别sdk
     */
    private void initPlateSDK() {
        LPRConfig lprConfig = new LPRConfig();
        lprConfig.width = PREVIEW_WIDTH;
        lprConfig.height = PREVIEW_HEIGHT;
        rokidLPR = new RokidLPR();
        rokidLPR.initEngine(lprConfig);
        rokidLPR.startLPR(new RokidLPRCallback() {
            @Override
            public void onLPRCallback(LPRModel model) {
                byte[] data = rokidLPR.getBytes();
                if (model.lps == null || model.lps.size() == 0) {
                    return;
                }
                for (LPRDO lprdo : model.lps) {
                    Logger.d("onLPRCallback------> lprdo.score: " + lprdo.score);
                    Logger.d("onLPRCallback------> lprdo.licensePlate: " + lprdo.licensePlate);
                    if (lprdo.score > 0.97 && PlateUtils.isCarNo(lprdo.licensePlate)) {
                        String name = lprdo.licensePlate;
                        Bitmap bm = BitmapUtils.nv21ToBitmap(data, PREVIEW_WIDTH, PREVIEW_HEIGHT, lprdo.toRect(PREVIEW_WIDTH, PREVIEW_HEIGHT));
                        Logger.i("PlateSDK ------>onLPRCallback  && rect:" + lprdo.toRect(PREVIEW_WIDTH, PREVIEW_HEIGHT));
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mSmartRecgPresenter.handlePlate(name, bm);
                            }
                        });
                        break;
                    }
                }
            }
        });
    }

    public void setOnResultShowListener(OnResultShowListener onResultShowListener) {
        this.onResultShowListener = onResultShowListener;
        if (mSmartRecgPresenter != null) {
            mSmartRecgPresenter.setOnResultShowListener(onResultShowListener);
        }
    }

    public void setNewFriendConfig(boolean isNewFriendAlarm, String newFriendDesc) {
        mSmartRecgView.setNewFriendConfig(isNewFriendAlarm, newFriendDesc);
    }

    public void reloadSDK(){
        if(videoFace!=null){
            videoFace.destroy();
        }
        stopCameraPreview();
        reset();
        initAllView();
        initPlateSDK();
        restartCameraPreview();
    }

    public void reset(){
        if(mSmartRecgPresenter != null){
            mSmartRecgPresenter.reset();
        }
        if(onResultShowListener != null){
            onResultShowListener.onResultHide();
        }
    }

    public void stopCameraPreview(){
        isCameraStop.set(true);
    }

    public void restartCameraPreview(){
        isCameraStop.set(false);
    }

    public void onPreviewFrame(byte[] data) {
//            Logger.d("onPreviewFrame---------->data.length: " + data.length + ";    isCameraStop = " + isCameraStop.get());
        if(isCameraStop.get()){
            return;
        }
        // 现在车牌/人脸识别SDK的回调都是同步在同一个线程回调的，因此车牌/人脸识别目前串行执行
        if (mSmartRecgView.isCarShowing() || mSmartRecgView.isSingleFaceInfoShowing()) {
            //如果正在展示单人或者车牌信息，则暂不进行继续识别，等待显示结束后再进行识别
        }
        if(false){
            debugPreviewData(data);
        }
        boolean isPlateOpen = BaseLibrary.getInstance().isPlateRecgEnable();
        boolean isSingleFaceRecg = BaseLibrary.getInstance().isSingleFaceRecgEnable();
        boolean isMultiFaceRecg = BaseLibrary.getInstance().isMultiFaceRecgEnable();
        if (isPlateOpen) {
            rokidLPR.setData(data);
        }
        if (isSingleFaceRecg || isMultiFaceRecg) {
            videoFace.setData(new VideoInput(data));
        }
    }

    private void debugPreviewData(byte[] data){
        Bitmap bm = BitmapUtils.nv21ToBitmap(data,PREVIEW_WIDTH,PREVIEW_HEIGHT);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm:ss:SSS");
        Date d1 = new Date(System.currentTimeMillis());
        Utils.savePlateInfo(bm, simpleDateFormat.format(d1));
    }
}
