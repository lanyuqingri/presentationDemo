package com.rokid.glass.viewcomponent.glass.online;


import android.graphics.Bitmap;

import com.rokid.glass.libbase.faceid.SmartRecgConfig;
import com.rokid.glass.libbase.logger.Logger;
import com.rokid.glass.libbase.message.car.ReqCarRecognizeMessage;
import com.rokid.glass.libbase.message.car.ReqSyncPlateRecordMessage;
import com.rokid.glass.libbase.message.car.RespCarRecognizeMessage;
import com.rokid.glass.libbase.message.face.ReqOnlineSingleFaceMessage;
import com.rokid.glass.libbase.message.face.RespOnlineSingleFaceMessage;
import com.rokid.glass.libbase.message.recogrecord.RecordMessage;
import com.rokid.glass.libbase.plate.DataConvertUtil;
import com.rokid.glass.libbase.plate.PlateInfo;
import com.rokid.glass.libbase.utils.DefaultSPHelper;
import com.rokid.glass.viewcomponent.glass.BitmapUtils;

import java.util.ArrayList;
import java.util.List;

public class OnlineRecgHelper{
    private static OnlineRecgHelper instance;
    private String mOnlineType;
    private List<OnlineResp> onlineRespList;
    private OnlineRequest mOnlineRequest;

    private OnlineRecgHelper(){
        initOnlineRecgType();
        onlineRespList = new ArrayList<>();
    }

    public static OnlineRecgHelper getInstance(){
        if(instance == null){
            instance = new OnlineRecgHelper();
        }
        return instance;
    }

    public void init(OnlineRequest request){
        mOnlineRequest = request;
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
        mOnlineType = DefaultSPHelper.getInstance().getString(SmartRecgConfig.ONLINE_TYPE, SmartRecgConfig.ONLINE_TYPE_NETWORK);
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
            if(mOnlineRequest != null){
                mOnlineRequest.sendPlateInfo(message);
            }
        }
    }

    public void sendFaceRecgMessage(ReqOnlineSingleFaceMessage message){
        if(mOnlineType.equals(SmartRecgConfig.ONLINE_TYPE_BLUETOOTH)){
            Logger.d("sendFaceRecgMessage------------->by bluetooth");
        } else {
            if(mOnlineRequest != null){
                mOnlineRequest.sendFaceInfo(message);
            }
        }
    }

    public void sendRecordMessage(RecordMessage message){
        if(mOnlineType.equals(SmartRecgConfig.ONLINE_TYPE_BLUETOOTH)){
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
        } else {
            ///TODO 通过网络上传车辆离线识别信息
        }
    }

    public void onFaceOnlineResp(RespOnlineSingleFaceMessage faceRespMessage){
        if(onlineRespList == null){
            return;
        }

        for(OnlineResp resp:onlineRespList){
            resp.onFaceResp(faceRespMessage);
        }
    }

    public void onPlateOnlineResp(RespCarRecognizeMessage plateRespMessage){
        if(onlineRespList == null){
            return;
        }

        for(OnlineResp resp:onlineRespList){
            resp.onCarResp(plateRespMessage);
        }
    }
}
