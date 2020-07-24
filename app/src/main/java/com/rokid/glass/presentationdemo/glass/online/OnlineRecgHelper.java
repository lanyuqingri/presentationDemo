package com.rokid.glass.presentationdemo.glass.online;


import android.graphics.Bitmap;
import android.os.Handler;
import android.os.HandlerThread;

import com.rokid.glass.libbase.faceid.SmartRecgConfig;
import com.rokid.glass.libbase.logger.Logger;
import com.rokid.glass.libbase.message.car.ReqCarRecognizeMessage;
import com.rokid.glass.libbase.message.car.ReqSyncPlateRecordMessage;
import com.rokid.glass.libbase.message.dto.FaceInfoBean;
import com.rokid.glass.libbase.message.face.ReqOnlineSingleFaceMessage;
import com.rokid.glass.libbase.message.face.RespOnlineSingleFaceMessage;
import com.rokid.glass.libbase.message.recogrecord.RecordMessage;
import com.rokid.glass.libbase.plate.DataConvertUtil;
import com.rokid.glass.libbase.plate.PlateInfo;
import com.rokid.glass.libbase.utils.DefaultSPHelper;
import com.rokid.glass.presentationdemo.glass.BitmapUtils;

import java.util.ArrayList;
import java.util.List;

public class OnlineRecgHelper{
    private static OnlineRecgHelper instance;
    private String mOnlineType;
    private List<OnlineResp> onlineRespList;
    private HandlerThread mHandlerThread;
    private Handler mHandler;

    private OnlineRecgHelper(){
        initOnlineRecgType();
        onlineRespList = new ArrayList<>();
        mHandlerThread = new HandlerThread(OnlineRecgHelper.class.getName());
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());
    }
    public static OnlineRecgHelper getInstance(){
        if(instance == null){
            instance = new OnlineRecgHelper();
        }
        return instance;
    }

    public void addOnlineResp(OnlineResp onlineResp){
        if(onlineRespList == null){
            onlineRespList = new ArrayList<>();
        }
        if(onlineRespList.contains(onlineResp)){
            return;
        }
        onlineRespList.add(onlineResp);
    }

    public void removeOnlineResp(OnlineResp resp){
        if(onlineRespList != null && onlineRespList.contains(resp)){
            onlineRespList.remove(resp);
        }
    }

    public void initOnlineRecgType(){
        mOnlineType = DefaultSPHelper.getInstance().getString(SmartRecgConfig.ONLINE_TYPE, SmartRecgConfig.ONLINE_TYPE_BLUETOOTH);
    }

    public String getOnlineRecgType(){
        return mOnlineType;
    }

    public void setOnlineRecgType(String type){
        //设置在线识别的类型，当前需要支持的是蓝牙/网络来在线传输识别
        DefaultSPHelper.getInstance().put(SmartRecgConfig.ONLINE_TYPE,type);
        mOnlineType = type;
    }

    public void sendCarRecgMessage(ReqCarRecognizeMessage message){
        if(mOnlineType.equals(SmartRecgConfig.ONLINE_TYPE_BLUETOOTH)){
//            RokidBluetoothManager.getInstance().sendMessage(BTPackage.obtainMessage(MessageType.CAR,message));
        } else {
            ///TODO 通过网络上传车辆在线识别信息
        }
    }

    public void sendFaceRecgMessage(ReqOnlineSingleFaceMessage message){
        if(mOnlineType.equals(SmartRecgConfig.ONLINE_TYPE_BLUETOOTH)){
            Logger.d("sendFaceRecgMessage------------->by bluetooth");
//            RokidBluetoothManager.getInstance().sendMessage(BTPackage.obtainMessage(MessageType.FACE,message));
        } else {
            ///TODO 通过网络上传人脸在线识别信息
        }
        Logger.d("sendFaceRecgMessage-------->trackId = " + message.getTrackId());
        //MOCK在线数据返回
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(onlineRespList == null || onlineRespList.size() <= 0){
                    return;
                }
                RespOnlineSingleFaceMessage faceMessage = new RespOnlineSingleFaceMessage();
                faceMessage.setServerCode(RespOnlineSingleFaceMessage.ServerErrorCode.OK);
                faceMessage.setTrackId(message.getTrackId());
                FaceInfoBean faceInfoBean = new FaceInfoBean();
                faceInfoBean.setAlarm(true);
                faceInfoBean.setName("xxxx");
                faceMessage.setFaceInfoBean(faceInfoBean);
                for(OnlineResp resp : onlineRespList){
                    resp.onFaceResp(faceMessage);
                }
            }
        },1000);
    }

    public void sendRecordMessage(RecordMessage message){
        if(mOnlineType.equals(SmartRecgConfig.ONLINE_TYPE_BLUETOOTH)){
//            RokidBluetoothManager.getInstance().sendMessage(BTPackage.obtainMessage(MessageType.RECORD,message));
        } else {
            ///TODO 通过网络上传人脸在线识别信息
        }
    }

    public void syncPlateOfflineRecordToServer(PlateInfo plateInfo, Bitmap bm){
        if(mOnlineType.equals(SmartRecgConfig.ONLINE_TYPE_BLUETOOTH)){
            ReqSyncPlateRecordMessage reqSyncPlateRecordMessage = new ReqSyncPlateRecordMessage();
            reqSyncPlateRecordMessage.setCarBean(DataConvertUtil.convertPlateInfoToCarBean(plateInfo));
            if(bm!=null){
                byte[] bytes = BitmapUtils.bitmap2Bytes(bm);
                reqSyncPlateRecordMessage.setPlateImg(bytes);
                reqSyncPlateRecordMessage.setImageWidth(bm.getWidth());
                reqSyncPlateRecordMessage.setImageHeight(bm.getHeight());
            }
//            RokidBluetoothManager.getInstance().sendMessage(BTPackage.obtainMessage(MessageType.PLATE_INFO,reqSyncPlateRecordMessage));
        } else {
            ///TODO 通过网络上传车辆离线识别信息
        }
    }
//
//    @Override
//    public void onBluetoothStatusChange(RokidBluetoothManager.BlueSocketStatus status) {
//
//    }
//
//    @Override
//    public void onBluetoothMessageReceiver(IMessage message) {
//        Logger.d("onBluetoothMessageReceiver----------------------->is called");
//        if(message instanceof RespCarRecognizeMessage){
//            for(OnlineResp resp:onlineRespList){
//                resp.onCarResp((RespCarRecognizeMessage)message);
//            }
//        } else if(message instanceof RespOnlineSingleFaceMessage){
//            for(OnlineResp resp:onlineRespList){
//                resp.onFaceResp((RespOnlineSingleFaceMessage)message);
//            }
//        }
//    }
}
