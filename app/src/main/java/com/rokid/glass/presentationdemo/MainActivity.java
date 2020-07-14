package com.rokid.glass.presentationdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

//import com.rokid.glass.alliance.RokidBaseActivity;
//import com.rokid.glass.libbase.message.car.ReqCarRecognizeMessage;
//import com.rokid.glass.libbase.message.car.RespCarRecognizeMessage;
//import com.rokid.glass.libbase.message.dto.CarRecognizeInfoBean;
//import com.rokid.glass.libbase.message.dto.FaceInfoBean;
//import com.rokid.glass.libbase.message.face.ReqOnlineSingleFaceMessage;
//import com.rokid.glass.libbase.message.face.RespOnlineSingleFaceMessage;
//import com.rokid.glass.viewcomponent.glass.online.OnlineRecgHelper;
//import com.rokid.glass.viewcomponent.glass.online.OnlineRequest;


public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        init();
    }

//    @Override
//    public int layout() {
//        return R.layout.activity_main;
//    }
//
//    @Override
//    public View getCameraView() {
//        return findViewById(R.id.camera_view);
//    }

//    private void init(){
//        OnlineRecgHelper.getInstance().init(new OnlineRequest() {
//            @Override
//            public void sendFaceInfo(ReqOnlineSingleFaceMessage reqOnlineSingleFaceMessage) {
//                //TODO: 将人脸信息上传云端做比对
//                RespOnlineSingleFaceMessage respOnlineSingleFaceMessage = new RespOnlineSingleFaceMessage();
//                FaceInfoBean faceInfoBean = new FaceInfoBean();
//                faceInfoBean.setName("xxx");  //在线识别 人员名字
//                faceInfoBean.setNativeplace("浙江.杭州");  // 在线识别 人员籍贯，比如"浙江.杭州"
//                faceInfoBean.setCardno("xxxxxxxxxxxxxxxx");  //在线识别 人员身份证信息
//                faceInfoBean.setTag("上访人员");    //在线识别 人员标签信息，比如"逃犯"/"可疑人员"/"上访人员"
//                faceInfoBean.setAlarm(true);  //是否开启警报音
//                faceInfoBean.setFaceImage(reqOnlineSingleFaceMessage.getFaceImage());   //在线识别后需要眼镜端展示的人员头像图片数据，此处只是mock了从眼镜端截取的图片数据
//                respOnlineSingleFaceMessage.setTrackId(reqOnlineSingleFaceMessage.getTrackId());
//                respOnlineSingleFaceMessage.setFaceInfoBean(faceInfoBean);
//                OnlineRecgHelper.getInstance().onFaceOnlineResp(respOnlineSingleFaceMessage);   //调用此接口将在线识别结果返回给眼镜
//            }
//
//            @Override
//            public void sendPlateInfo(ReqCarRecognizeMessage reqCarRecognizeMessage) {
//                //TODO: 将车牌信息上传云端做比对
//                RespCarRecognizeMessage respCarRecognizeMessage = new RespCarRecognizeMessage();
//                CarRecognizeInfoBean carRecognizeInfoBean = new CarRecognizeInfoBean();
//                carRecognizeInfoBean.setPlate("浙ADA0178");  //车牌号
//                carRecognizeInfoBean.setOwner("xxxx");  // 车主姓名
//                carRecognizeInfoBean.setBrand("BYD");  //品牌
//                carRecognizeInfoBean.setColor("红");  //车身颜色
//                carRecognizeInfoBean.setTag("xxxxxxx");  //标签信息，比如"违章3次"/"失踪车牌" 等
//                respCarRecognizeMessage.setCarRecognizeInfoBean(carRecognizeInfoBean);
//                OnlineRecgHelper.getInstance().onPlateOnlineResp(respCarRecognizeMessage);  //调用此接口将在线识别结果返回给眼镜
//            }
//        });
//    }
}
