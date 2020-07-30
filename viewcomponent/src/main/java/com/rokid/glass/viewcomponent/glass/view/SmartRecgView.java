package com.rokid.glass.viewcomponent.glass.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.rokid.facelib.model.FaceDO;
import com.rokid.glass.libbase.BaseLibrary;
import com.rokid.glass.libbase.faceid.FaceIdManager;
import com.rokid.glass.libbase.faceid.database.UserInfo;
import com.rokid.glass.libbase.logger.Logger;
import com.rokid.glass.libbase.message.car.RespCarRecognizeMessage;
import com.rokid.glass.libbase.message.dto.CarRecognizeInfoBean;
import com.rokid.glass.libbase.message.face.RespOnlineSingleFaceMessage;
import com.rokid.glass.libbase.music.MusicPlayHelper;
import com.rokid.glass.libbase.plate.PlateDeployInfo;
import com.rokid.glass.libbase.plate.PlateInfo;
import com.rokid.glass.libbase.plate.PlateManager;
import com.rokid.glass.viewcomponent.R;
import com.rokid.glass.viewcomponent.glass.BitmapUtils;
import com.rokid.glass.viewcomponent.glass.OnResultShowListener;


public class SmartRecgView extends FrameLayout{
    private OnResultShowListener onResultShowListener;
    private boolean mFaceInfoShowing;
    private boolean mCarInfoShowing;
    //表示正在处理用户信息
    boolean mHandlingUserInfo;
    public RoiRect iv_roi_rect;
    public RecgCrossView iv_cross_rect;
    private ConstraintLayout cl_user_info,cl_new_friend;
    private TextView tv_bad_face;
    private TextView tv_recognition;
    private ImageView iv_head;
    private TextView tv_name,tv_place,tv_tag,tv_recog,tv_card_num;
    public TextView tv_retry;
    private ImageView iv_crop,iv_scan;

    private TextView tv_plate,tv_plate_tag;
    private ConstraintLayout cl_plate;
    private ImageView iv_plate_bg;
    private ImageView iv_pass;
    private TextView tv_num,tv_addr;
    private TextView tv_pass2;
    private boolean isNewFriendAlarm = false;
    private String newFriendDesc = "";


    public SmartRecgView(Context context) {
        this(context,null);
    }
    public SmartRecgView(Context context, @Nullable AttributeSet attrs){
        this(context,attrs,0);
    }
    public SmartRecgView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context,attrs,defStyleAttr);
        inflate(getContext(), R .layout.view_smart_recg,this);

        Animation animation= AnimationUtils.loadAnimation(getContext(), R.anim.side_in_right);
        LayoutAnimationController controller = new LayoutAnimationController(animation);
        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
        controller.setDelay(0.3f);

        iv_roi_rect = findViewById(R.id.iv_roi_rect);
        iv_cross_rect = findViewById(R.id.iv_cross_rect);
        cl_user_info = findViewById(R.id.cl_user_info);
        cl_user_info.setLayoutAnimation(controller);

        tv_bad_face = findViewById(R.id.tv_bad_face);
        tv_recognition = findViewById(R.id.tv_recognition);
        iv_head = findViewById(R.id.iv_head);
        tv_name = findViewById(R.id.tv_name);
        tv_place = findViewById(R.id.tv_place);
        tv_tag = findViewById(R.id.tv_tag);
        iv_crop = findViewById(R.id.iv_crop);
        iv_scan = findViewById(R.id.iv_scan);
        tv_card_num = findViewById(R.id.tv_card_num);

        cl_new_friend = findViewById(R.id.cl_new_friend);
        tv_recog = findViewById(R.id.tv_recog);
        tv_retry = findViewById(R.id.tv_retry);

        tv_plate = findViewById(R.id.tv_plate);
        tv_plate_tag = findViewById(R.id.tv_plate_tag);
        cl_plate = findViewById(R.id.cl_plate);
        cl_plate.setLayoutAnimation(controller);
        tv_tag = findViewById(R.id.tv_tag);
        tv_num = findViewById(R.id.tv_num);
        tv_addr = findViewById(R.id.tv_addr);
        iv_plate_bg = findViewById(R.id.iv_plate_bg);
        iv_pass = findViewById(R.id.iv_pass);
        tv_pass2 = findViewById(R.id.tv_pass2);
        showNormalRect();
        boolean isSingleRecg = BaseLibrary.getInstance().isSingleFaceRecgEnable();
        boolean isPlateRecg = BaseLibrary.getInstance().isPlateRecgEnable();
        if(!isSingleRecg && !isPlateRecg){
            iv_roi_rect.setVisibility(GONE);
            iv_cross_rect.setVisibility(GONE);
            tv_recog.setVisibility(GONE);
        } else if(isSingleRecg && isPlateRecg){
            tv_recog.setText(R.string.deploy_single_face_and_plate);
        } else if(isSingleRecg){
            tv_recog.setText(R.string.deploy_single_face);
        } else {
            tv_recog.setText(R.string.deploy_plate);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public Rect getRoiRect(){
        if(iv_roi_rect != null && iv_roi_rect.getVisibility() == VISIBLE){
            return iv_roi_rect.getRect();
        } else if(iv_cross_rect != null && iv_cross_rect.getVisibility() == VISIBLE){
            return iv_cross_rect.getRect();
        }
        return null;
    }

    public boolean isCarShowing(){
        return mCarInfoShowing;
    }
    public boolean isSingleFaceInfoShowing(){
        return mFaceInfoShowing;
    }


    public void reset(){
        mFaceInfoShowing = false;
        mCarInfoShowing = false;
        boolean isSingleRecg = BaseLibrary.getInstance().isSingleFaceRecgEnable();
        boolean isPlateRecg = BaseLibrary.getInstance().isPlateRecgEnable();
        if(isSingleRecg && isPlateRecg){
            tv_recog.setText(R.string.deploy_single_face_and_plate);
        } else if(isSingleRecg){
            tv_recog.setText(R.string.deploy_single_face);
        } else {
            tv_recog.setText(R.string.deploy_plate);
        }
        stopScan();
        showNormalRect();
    }

    public void showCropBitmap(Bitmap bm){
        mFaceInfoShowing = true;
        if(bm!=null){
            iv_crop.setImageBitmap(bm);
        }
        iv_crop.setVisibility(VISIBLE);
        cl_user_info.setVisibility(GONE);
        tv_retry.setVisibility(GONE);
        tv_bad_face.setVisibility(GONE);
        iv_roi_rect.setVisibility(GONE);
        iv_cross_rect.setVisibility(GONE);
        tv_recog.setVisibility(GONE);
        tv_recognition.setVisibility(GONE);
        iv_scan.setVisibility(VISIBLE);
        tv_retry.setVisibility(GONE);
        hidePlateView();
        startScan();
    }
    public void showFaceDetailInfo(RespOnlineSingleFaceMessage message){
        Logger.i("showFaceDetailInfo--------->is called && mHandlingUserInfo: " + mHandlingUserInfo);
        stopScan();
        hidePlateView();
        Bitmap bm = null;
        mFaceInfoShowing = true;
        cl_user_info.setVisibility(VISIBLE);
        tv_retry.setVisibility(VISIBLE);
        iv_head.setVisibility(VISIBLE);
        tv_name.setVisibility(VISIBLE);
        tv_place.setVisibility(VISIBLE);
        tv_tag.setVisibility(VISIBLE);
        tv_card_num.setVisibility(VISIBLE);
        tv_name.setText(message.getFaceInfoBean().getName());
        tv_card_num.setText(message.getFaceInfoBean().getCardno());
        byte[] imgeBytes = message.getFaceInfoBean().getFaceImage();
        if(imgeBytes!=null){
            bm = BitmapFactory.decodeByteArray(imgeBytes,0,imgeBytes.length);
        }
        if(bm!=null) {
            iv_head.setImageBitmap(bm);
        }
        tv_tag.setText(message.getFaceInfoBean().getTag());
        if (message.getFaceInfoBean().isAlarm()) {
            MusicPlayHelper.getInstance().playMusic("alarm.wav",false);
        }
        tv_place.setText(message.getFaceInfoBean().getNativeplace());
        iv_crop.setVisibility(GONE);
        iv_scan.setVisibility(GONE);
        tv_bad_face.setVisibility(GONE);
        iv_roi_rect.setVisibility(GONE);
        iv_cross_rect.setVisibility(GONE);
        tv_recog.setVisibility(GONE);
        tv_recognition.setVisibility(GONE);
    }

    public void showFaceInfo(FaceDO faceDO){
        Logger.i("showFaceInfo--------->is called && mHandlingUserInfo: " + mHandlingUserInfo);
        if(!mHandlingUserInfo) {
            mHandlingUserInfo = true;
            mFaceInfoShowing = true;
            hidePlateView();
            cl_user_info.setVisibility(VISIBLE);
            iv_head.setVisibility(VISIBLE);
            tv_name.setVisibility(VISIBLE);
            tv_place.setVisibility(VISIBLE);
            Bitmap bm = null;
            UserInfo info = FaceIdManager.getInstance().getUserInfoByFid(faceDO.featid);
            if(info != null) {
                tv_name.setText(info.name);
                // 播放报警音
                if(!TextUtils.isEmpty(info.description)){
                    tv_tag.setText(info.description);
                    tv_tag.setVisibility(VISIBLE);
                }
                if (info.isAlarm) {
//                    MusicPlayHelper.getInstance().playMusic("alarm.wav",false);
                }
                tv_card_num.setText(info.cardno);
                tv_place.setText(info.nativeplace);
                bm = FaceIdManager.getInstance().getUserImageByFid(faceDO.featid);
                bm = BitmapUtils.createCircleBitmap(bm);

                if (bm != null) {
                    iv_head.setImageBitmap(bm);
                }
            }

            tv_bad_face.setVisibility(GONE);
            tv_bad_face.setVisibility(GONE);
            iv_roi_rect.setVisibility(GONE);
            iv_cross_rect.setVisibility(GONE);
            tv_recog.setVisibility(GONE);
            tv_recognition.setVisibility(GONE);
            mHandlingUserInfo = false;
            if(onResultShowListener!=null){
                onResultShowListener.onResultShow();
            }
        }
        cl_user_info.startLayoutAnimation();
    }

    public void showDetect(){
        boolean isMultiFaceEnable = BaseLibrary.getInstance().isMultiFaceRecgEnable();
        hidePlateView();
        cl_user_info.setVisibility(GONE);
        iv_head.setVisibility(GONE);
        tv_name.setVisibility(GONE);
        tv_place.setVisibility(GONE);
        tv_tag.setVisibility(GONE);
        tv_bad_face.setVisibility(GONE);
        if(isMultiFaceEnable){
            iv_cross_rect.setVisibility(VISIBLE);
            iv_cross_rect.setStatus(RoiRect.STATUS_DETECT);
            tv_recog.setVisibility(GONE);
            iv_roi_rect.setVisibility(GONE);
        } else {
            tv_recog.setVisibility(VISIBLE);
            iv_roi_rect.setVisibility(VISIBLE);
            iv_roi_rect.setStatus(RoiRect.STATUS_DETECT);
            iv_cross_rect.setVisibility(GONE);
        }
        tv_recognition.setVisibility(VISIBLE);
        tv_retry.setVisibility(GONE);
    }

    public void showBadPose(){
        boolean isMultiFaceEnable = BaseLibrary.getInstance().isMultiFaceRecgEnable();
        hidePlateView();
        cl_new_friend.setVisibility(GONE);
        cl_user_info.setVisibility(GONE);
        iv_head.setVisibility(GONE);
        tv_name.setVisibility(GONE);
        tv_place.setVisibility(GONE);
        tv_tag.setVisibility(GONE);
        tv_bad_face.setVisibility(VISIBLE);
        tv_recog.setVisibility(GONE);
        tv_recognition.setVisibility(GONE);
        tv_retry.setVisibility(GONE);
        if(isMultiFaceEnable){
            iv_cross_rect.setVisibility(VISIBLE);
            iv_cross_rect.setStatus(RoiRect.STATUS_BAD_FACE);
            tv_recog.setVisibility(GONE);
            iv_roi_rect.setVisibility(GONE);
        } else {
            tv_recog.setVisibility(VISIBLE);
            iv_roi_rect.setVisibility(VISIBLE);
            iv_roi_rect.setStatus(RoiRect.STATUS_BAD_FACE);
            iv_cross_rect.setVisibility(GONE);
        }
    }

    public void showNewFriend(){
        mFaceInfoShowing = true;
        cl_new_friend.setVisibility(VISIBLE);
        tv_bad_face.setVisibility(GONE);
        iv_roi_rect.setVisibility(GONE);
        iv_cross_rect.setVisibility(GONE);
        tv_recog.setVisibility(GONE);
        tv_recognition.setVisibility(GONE);
        tv_retry.setVisibility(GONE);
        iv_crop.setVisibility(GONE);
        hidePlateView();
        if(isNewFriendAlarm){
            iv_pass.setImageResource(R.mipmap.ic_refused);
            if(!TextUtils.isEmpty(newFriendDesc)){
                tv_pass2.setText(newFriendDesc);
            }
//            MusicPlayHelper.getInstance().playMusic("alarm.wav",false);
        } else {
            iv_pass.setImageResource(R.mipmap.icon_pass);
            if(!TextUtils.isEmpty(newFriendDesc)){
                tv_pass2.setText(newFriendDesc);
            } else {
                tv_pass2.setText(R.string.s_new_friend);
            }
        }
        if(onResultShowListener!=null){
            onResultShowListener.onResultShow();
        }
    }

    public void showNormalRect() {
        hidePlateView();
        cl_new_friend.setVisibility(GONE);
        cl_user_info.setVisibility(GONE);
        iv_crop.setVisibility(GONE);
        iv_head.setVisibility(GONE);
        tv_name.setVisibility(GONE);
        tv_place.setVisibility(GONE);
        tv_tag.setVisibility(GONE);
        tv_bad_face.setVisibility(GONE);
        tv_recognition.setVisibility(GONE);
        tv_retry.setVisibility(GONE);
        if(BaseLibrary.getInstance().isMultiFaceRecgEnable()){
            iv_cross_rect.setVisibility(VISIBLE);
            iv_cross_rect.setStatus(RoiRect.STATUS_NORMAL);
            tv_recog.setVisibility(GONE);
            iv_roi_rect.setVisibility(GONE);
        } else {
            tv_recog.setVisibility(VISIBLE);
            iv_roi_rect.setVisibility(VISIBLE);
            iv_roi_rect.setStatus(RoiRect.STATUS_NORMAL);
            iv_cross_rect.setVisibility(GONE);
        }
    }

    public void showCarDetailInfo(PlateInfo plateInfo){
        if(plateInfo == null){
            return;
        }
        Logger.i("showCarDetailInfo----->is called && RespCarRecognizeMessage: " + plateInfo.toString());
        mCarInfoShowing = true;
        iv_roi_rect.setVisibility(GONE);
        cl_new_friend.setVisibility(GONE);
        iv_cross_rect.setVisibility(GONE);
        tv_recog.setVisibility(GONE);
        cl_plate.setVisibility(VISIBLE);
        tv_plate.setVisibility(VISIBLE);
        tv_plate.setText(plateInfo.getPlate());
        tv_plate_tag.setVisibility(VISIBLE);
        tv_plate_tag.setText(plateInfo.getTag());
        tv_num.setVisibility(VISIBLE);
        tv_num.setText(plateInfo.getBrand()+" "+ plateInfo.getColor());
        tv_addr.setVisibility(VISIBLE);
        tv_addr.setText(plateInfo.getOwner());
        iv_plate_bg.setVisibility(VISIBLE);
        cl_plate.startLayoutAnimation();
    }

    public void showCarDetailInfo(RespCarRecognizeMessage message){
        if(message == null || message.getCarRecognizeInfoBean() == null){
            return;
        }
        Logger.i("showCarDetailInfo----->is called && RespCarRecognizeMessage: " + message.toString());
        mCarInfoShowing = true;
        cl_new_friend.setVisibility(GONE);
        iv_roi_rect.setVisibility(GONE);
        iv_cross_rect.setVisibility(GONE);
        tv_recog.setVisibility(GONE);
        cl_plate.setVisibility(VISIBLE);
        tv_plate.setVisibility(VISIBLE);
        tv_plate.setText(message.getCarRecognizeInfoBean().getPlate());
        tv_plate_tag.setVisibility(VISIBLE);
        tv_plate_tag.setText(message.getCarRecognizeInfoBean().getTag());
        tv_num.setVisibility(VISIBLE);
        tv_num.setText(message.getCarRecognizeInfoBean().getBrand()+" "+ message.getCarRecognizeInfoBean().getColor());
        tv_addr.setVisibility(VISIBLE);
        tv_addr.setText(message.getCarRecognizeInfoBean().getOwner());
        iv_plate_bg.setVisibility(VISIBLE);
        cl_plate.startLayoutAnimation();
    }

    public void showCarDetailInfo(CarRecognizeInfoBean message){
        if(message == null){
            Logger.d("showCarDetailInfo ========CarRecognizeInfoBean is null");
            return;
        }
        Logger.i("showCarDetailInfo----->is called && CarRecognizeInfoBean: " + message.toString());
        mCarInfoShowing = true;
        hideSingleFaceView();
        cl_new_friend.setVisibility(GONE);
        iv_roi_rect.setVisibility(GONE);
        iv_cross_rect.setVisibility(GONE);
        tv_recog.setVisibility(GONE);
        cl_plate.setVisibility(VISIBLE);
        tv_plate.setVisibility(VISIBLE);
        tv_plate.setText(message.getPlate());
        tv_plate_tag.setVisibility(VISIBLE);
        tv_plate_tag.setText(message.getTag());
        tv_num.setVisibility(VISIBLE);
        tv_num.setText(message.getBrand()+" "+ message.getColor());
        tv_addr.setVisibility(VISIBLE);
        tv_addr.setText(message.getOwner());
        iv_plate_bg.setVisibility(VISIBLE);
        cl_plate.startLayoutAnimation();
    }

    public void showNoCarInfo(){
        mCarInfoShowing = true;
        cl_new_friend.setVisibility(VISIBLE);
        PlateDeployInfo info = PlateManager.getInstance().getPlateDeployInfo();
        if(info != null && info.isNewPlateSwitch() && !TextUtils.isEmpty(info.getNewPlateDesc())){
            tv_pass2.setText(info.getNewPlateDesc());
        } else {
            tv_pass2.setText(R.string.s_new_plate);
        }
        tv_bad_face.setVisibility(GONE);
        iv_roi_rect.setVisibility(GONE);
        iv_cross_rect.setVisibility(GONE);
        tv_recog.setVisibility(GONE);
        tv_recognition.setVisibility(GONE);
        tv_retry.setVisibility(GONE);
        hideSingleFaceView();
        if(onResultShowListener!=null){
            onResultShowListener.onResultShow();
        }
    }

    private void startScan() {
        post(new Runnable() {
            @Override
            public void run() {
                if(iv_scan.isShown()) {
                    iv_scan.clearAnimation();
                }
                iv_scan.setVisibility(View.VISIBLE);
                ViewAnimator.animate(iv_scan).translationY(-iv_scan.getMeasuredHeight(),iv_crop.getMeasuredHeight()-iv_scan.getMeasuredHeight()).duration(2000).repeatCount(100).repeatMode(ValueAnimator.INFINITE).start();
            }
        });
    }

    private void stopScan() {
        post(new Runnable() {
            @Override
            public void run() {
                if(iv_scan.isShown()) {
                    iv_scan.setVisibility(View.INVISIBLE);
                    iv_scan.clearAnimation();
                }
            }
        });
    }

    private void hidePlateView(){
        mCarInfoShowing = false;
        cl_plate.setVisibility(View.GONE);
    }

    private void hideSingleFaceView(){
        mFaceInfoShowing = false;
        cl_user_info.setVisibility(View.GONE);
    }
    public void setOnResultShowListener(OnResultShowListener onResultShowListener) {
        if(this.onResultShowListener == null){
            this.onResultShowListener = onResultShowListener;
        }
    }

    public void setNewFriendConfig(boolean isNewFriendAlarm, String newFriendDesc){
        this.isNewFriendAlarm = isNewFriendAlarm;
        this.newFriendDesc = newFriendDesc;
    }
}
