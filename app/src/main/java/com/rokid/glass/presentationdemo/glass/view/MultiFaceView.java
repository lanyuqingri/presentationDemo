package com.rokid.glass.presentationdemo.glass.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;

import androidx.annotation.Nullable;

import com.rokid.facelib.model.FaceDO;
import com.rokid.facelib.model.FaceModel;
import com.rokid.glass.libbase.BaseLibrary;
import com.rokid.glass.libbase.faceid.FaceConstants;
import com.rokid.glass.libbase.faceid.FaceIdManager;
import com.rokid.glass.libbase.faceid.database.UserInfo;
import com.rokid.glass.libbase.logger.Logger;
import com.rokid.glass.libbase.message.face.RespOnlineSingleFaceMessage;
import com.rokid.glass.libbase.music.MusicPlayHelper;
import com.rokid.glass.libbase.utils.LimitQueue;
import com.rokid.glass.libbase.utils.RokidSystem;
import com.rokid.glass.presentationdemo.R;
import com.rokid.glass.presentationdemo.glass.CameraParams;
import com.rokid.glass.presentationdemo.glass.Utils;
import com.rokid.glass.presentationdemo.glass.animation.BreatheAnimate;
import com.rokid.glass.presentationdemo.glass.bean.FaceBean;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * @author cc & wenbing
 */
public class MultiFaceView extends View /*implements IBluetoothCallback */{


    private Context context;
    private FaceModel faceModel;
    private Paint paint;

    private int whiteColor, greenColor, blueColor, redColor;
    private static final int PREVIEW_WIDTH = CameraParams.PREVIEW_WIDTH;
    private static final int PREVIEW_HEIGHT = CameraParams.PREVIEW_HEIGHT;
    private static final int GAP = 100;
    private static final int RECT_GAP_WIDTH = CameraParams.PREVIEW_WIDTH;
    private static final int RECT_GAP_HEIGHT = CameraParams.PREVIEW_HEIGHT;
    private static final int NUMBER_OF_PERSON = 5;
    private static final int LINE_WIDTH = 32;
    private static final int RADIUS = 15;
    private Rect baseRect = new Rect(10,10,1270,710);
    private Rect baseArrowRect = new Rect(50,50,1230,670);
    private WeakReference<byte[]> data ;
    private ArrayList<Integer> currentIds;
    private static final String TAG = "MultiFaceView";
    private BreatheAnimate breatheAnimate;
    private SparseArray curFaceBean;

    private LimitQueue<RespOnlineSingleFaceMessage> respFaceList;

    public MultiFaceView(Context context) {
        this(context, null);
    }

    public MultiFaceView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiFaceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);

//        RokidBluetoothManager.getInstance().addBluetoothCallback(this);
        this.context = context;

        whiteColor = FaceConstants.WHITE_COLOR;
        blueColor = FaceConstants.BLUE_COLOR;
        greenColor = FaceConstants.GREEN_COLOR;
        redColor = FaceConstants.RED_COLOR;

        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        paint.setColor(whiteColor);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setAntiAlias(true);

        breatheAnimate = new BreatheAnimate(1000,0.8f,1.2f);
        curFaceBean = new SparseArray();
        respFaceList = new LimitQueue<>(10);
    }

    private volatile boolean drawing = false;

    private boolean isTrackIdExit(int trackId){
        if(faceModel == null || faceModel.getFaceList() == null || faceModel.getFaceList().size() <= 0){
            return false;
        }
        synchronized (faceModel){
            for (int i = 0; i < faceModel.getFaceList().size(); i++) {
                if(faceModel.getFaceList().get(i).trackId == trackId){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean addRespFaceResult(RespOnlineSingleFaceMessage faceMessage){
        Logger.d("addRespFaceResult-------->faceMessage.TrackId = " + faceMessage.getTrackId());
        int trackId = faceMessage.getTrackId();
        if(!isTrackIdExit(trackId)){
            return false;
        }
        synchronized (respFaceList){
            respFaceList.offer(faceMessage);
            postInvalidate();
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawing = true;
        paint.setColor(whiteColor);
        drawRountRect(canvas, baseRect, paint);
        // Add try-catch 避免faceModel被异步修改导致crash
        try {
            if (faceModel != null && faceModel.getFaceList() != null && faceModel.getFaceList().size() > 0) {
//                Logger.i("MultiFaceView-------> onDraw && face size:" + faceModel.getFaceList().size());
                for (int i = 0; i < faceModel.getFaceList().size(); i++) {
                    FaceDO mFaceDO = faceModel.getFaceList().get(i);
                    if (mFaceDO != null && mFaceDO.faceRectF != null) {
                        drawNormalFace(canvas, mFaceDO);
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            Logger.e("Error: onDraw "+e.getMessage());
        }
        drawing = false;
    }

    public void setRawData(byte[] rawData){
        data = new WeakReference<>(rawData);
    }


    private void drawNormalFace(Canvas canvas, FaceDO faceNumberDo) {
//        Logger.i("drawNormalFace","faceNumberDo:"+faceNumberDo.faceRectF);
        Rect finalRect;
        if (BaseLibrary.getInstance().getEnableMultiFace()) {
            // Preview预览显示
            finalRect = faceNumberDo.toRect(
                    BaseLibrary.getInstance().getScreenSize().first,
                    BaseLibrary.getInstance().getScreenSize().second);
        }
        else {
            // AR显示，需要经过alignment
            Rect rect = faceNumberDo.toRect(PREVIEW_WIDTH,PREVIEW_HEIGHT);
            finalRect = RokidSystem.getAlignmentRect(PREVIEW_WIDTH, PREVIEW_HEIGHT, rect);
        }

        //Logger.i("drawNormalFace","rect:"+rect);
//        Rect finalRect = faceNumberDo.toRect(PREVIEW_WIDTH,PREVIEW_HEIGHT);

//        Rect finalRect = new Rect(rect.left-100-150,rect.top-500-100,rect.right+100-150,rect.bottom-500+100);
        if (finalRect == null) {
            return;
        }
        Logger.i("drawNormalFace"," && faceNumberDo---------——>finalRect:" + finalRect.toString());
        if (faceNumberDo.featid != null) {   //离线识别featid才可能不为空，在线识别featid均为空
            paint.setColor(redColor);
            drawRountRect(canvas, finalRect, paint);
            FaceBean faceBean = (FaceBean) curFaceBean.get(faceNumberDo.trackId);
            if (faceBean == null) {
                // 如果没有保存过识别用户
                UserInfo userInfo = FaceIdManager.getInstance().getUserInfoByFid(faceNumberDo.featid);
                if(userInfo != null) {
                    faceBean = new FaceBean();
                    faceBean.name = userInfo.name;
                    faceBean.isAlarm = userInfo.isAlarm;
                    // 保存用户
                    curFaceBean.put(faceNumberDo.trackId, faceBean);

                    // 保存识别记录
                    Utils.saveDeployFaceOfflineInfo(faceNumberDo.recogBitmap,userInfo,faceNumberDo);
                } else {
                    Logger.e("Error: 识别到人脸，但是没有匹配的用户信息！faceNumberDo.featid="+faceNumberDo.featid);
                }
            }

            if(faceBean != null){
//                Logger.i(TAG,"get faceid already exist "+faceNumberDo.trackId);
                // 用户信息不为空，则开始绘制
                drawResult(canvas, finalRect, faceBean.name, redColor);
                if(faceBean.isAlarm){
//                    MusicPlayHelper.getInstance().playMusic("alarm.wav",false);
                }
            }
        } else {
            RespOnlineSingleFaceMessage faceMessage = findFaceMsgByTrackId(faceNumberDo.trackId);
            if(faceMessage != null && faceMessage.getFaceInfoBean() != null && faceMessage.getFaceInfoBean().isAlarm()){
                paint.setColor(redColor);
                drawRountRect(canvas, finalRect, paint);
                drawResult(canvas, finalRect, faceMessage.getFaceInfoBean().getName(), redColor);
                MusicPlayHelper.getInstance().playMusic("alarm.wav",false);
            } else {
                paint.setColor(whiteColor);
                drawRountRect(canvas, finalRect, paint);
            }
        }
    }

    private RespOnlineSingleFaceMessage findFaceMsgByTrackId(int trackId){
        if(respFaceList == null){
            Logger.d("findFaceMsgByTrackId-------->TrackId = " + trackId);
            return null;
        }
        synchronized (respFaceList){
            if(respFaceList.size() > 0){
                for(int i = 0; i < respFaceList.size(); i++){
                    RespOnlineSingleFaceMessage faceMessage = respFaceList.get(i);
                    if(faceMessage.getTrackId() == trackId){
                        Logger.d("findFaceMsgByTrackId-------->trackId: " + trackId + "  has find" );
                        return faceMessage;
                    }
                }
            } else {
                Logger.d("findFaceMsgByTrackId-------->respFaceList is null " );
            }
        }
        return null;
    }
    public void clearCache(){
        if(curFaceBean!=null){
            curFaceBean.clear();
        }
        if (faceModel != null) {
            faceModel = null;
        }
    }


    private void drawResult(Canvas canvas, Rect rect, String name, int color) {

        canvas.save();

        int gapCharNameTop = 20;
        int gapCharNameBottom = 40;

        if (rect.left < GAP) {
            canvas.translate((rect.left + rect.right) / 2f + rect.width(), (rect.top + rect.bottom) / 2f);
        } else if (RECT_GAP_WIDTH - rect.right < GAP) {
            canvas.translate(rect.left, (rect.top + rect.bottom) / 2f);
        } else if (RECT_GAP_HEIGHT - rect.bottom < GAP) {
            canvas.translate((rect.left + rect.right) / 2f, rect.top - gapCharNameTop);
        } else {
            canvas.translate((rect.left + rect.right) / 2f, rect.bottom + gapCharNameBottom);
        }


        Paint paint = new Paint();
        int textSize = (rect.width() / NUMBER_OF_PERSON) > 36?(rect.width() / NUMBER_OF_PERSON) : 36;
        paint.setTextSize(textSize);
        paint.setColor(color);
        String showName = "";
        if(name != null){
            showName = name.length() > NUMBER_OF_PERSON ? (name.substring(0, NUMBER_OF_PERSON) + "...") : name;
        }
        canvas.drawText(showName, -rect.width() / 2, 0, paint);
        canvas.restore();
    }



    public void setFaceModel(FaceModel faceModel) {
//        Logger.d("handleFaceModel  && MultiFaceView-------> setFaceModel is called && drawing = " + drawing);
        if (drawing) {
            return;
        }
        this.faceModel = faceModel;
        if(currentIds ==null){
            currentIds = new ArrayList<>();
        }

        currentIds.clear();

        // Add try-catch 避免faceModel被异步修改导致crash
        try {
            for(int i=0;i<faceModel.size();i++){
                FaceDO Face = faceModel.getFace(i);
                if (null != Face) {
                    currentIds.add(Face.trackId);
                }
            }
            postInvalidate();
        } catch (Exception e) {
            e.printStackTrace();
            Logger.e("Error: setFaceModel "+e.getMessage());
        }
    }

    /**
     *
     * @param canvas 画布
     * @param rect 画质的矩形
     * @param paint 画笔
     */
    private void drawRountRect(Canvas canvas, Rect rect, Paint paint) {
        canvas.save();
        canvas.translate((rect.left + rect.right) / 2f, (rect.top + rect.bottom) / 2f);
        drawRectLeft(canvas, 0, rect.width(), rect.height(), paint);
        drawRectRight(canvas, 0, rect.width(), rect.height(), paint);
        drawRectLeft(canvas, 180, rect.width(), rect.height(), paint);
        drawRectRight(canvas, 180, rect.width(), rect.height(), paint);
        canvas.restore();
        if(!baseRect.contains(rect.left,rect.top)&&!baseRect.contains(rect.right,rect.bottom)
                &&!baseRect.contains(rect.left,rect.bottom)&&!baseRect.contains(rect.right,rect.top)){
            Arrow arrow = Utils.getArrow(baseArrowRect,rect);
            drawArrow(canvas,arrow,paint);
        }
    }

    private void drawArrow(Canvas canvas,Arrow arrow,Paint paint) {
        if (BaseLibrary.getInstance().getEnableMultiFace()) {
            return;
        }

        Bitmap bm_arrow = BitmapFactory.decodeResource(context.getResources(), R.drawable.arrow);
        Matrix matrix = new Matrix();
        int offsetX = bm_arrow.getWidth() / 2;
        int offsetY = bm_arrow.getHeight() / 2;
        matrix.postTranslate(-offsetX, -offsetY);
        breatheAnimate.startAnimate();
        float value = breatheAnimate.getValue();
        matrix.postScale(value,value);

        matrix.postRotate(-arrow.angle);
        matrix.postTranslate(arrow.point.x, arrow.point.y);

        canvas.drawBitmap(bm_arrow,matrix,paint);
    }

    /**
     *
     * @param canvas 画布
     * @param angle 画的角度
     * @param width 横线宽度
     * @param height 横线高度
     * @param paint 画笔，决定画笔的颜色
     */
    private void drawRectLeft(Canvas canvas, int angle, int width, int height, Paint paint) {

        canvas.save();
        canvas.rotate(angle);

        Path path = new Path();
        path.moveTo(-width / 2, height / 2 - LINE_WIDTH);
        path.lineTo(-width / 2, height / 2 - RADIUS);

        RectF rectF = new RectF(-width / 2f, height / 2f - 2 * RADIUS,
                -width / 2f + 2 * RADIUS, height / 2f);
        path.addArc(rectF, 90, 90f);

        path.moveTo(-width / 2 + RADIUS, height / 2);
        path.lineTo(-width / 2 + LINE_WIDTH, height / 2);
        canvas.drawPath(path, paint);

        canvas.restore();

    }


    /**
     *
     * @param canvas 画布
     * @param angle 画的角度
     * @param width 横线宽度
     * @param height 横线高度
     * @param paint 画笔，决定画笔的颜色
     */
    private void drawRectRight(Canvas canvas, int angle, int width, int height, Paint paint) {

        canvas.save();
        canvas.rotate(angle);

        Path path = new Path();
        path.moveTo(width / 2, height / 2 - LINE_WIDTH);
        path.lineTo(width / 2, height / 2 - RADIUS);
        RectF rectF = new RectF(width / 2f - 2 * RADIUS, height / 2f - 2 * RADIUS,
                width / 2f, height / 2f);
        path.addArc(rectF, 90, -90f);

        path.moveTo(width / 2 - RADIUS, height / 2);
        path.lineTo(width / 2 - LINE_WIDTH, height / 2);
        canvas.drawPath(path, paint);
        canvas.restore();

    }

    private Rect normalRect(int width, int height, RectF rectNoraml) {
        Rect rect = new Rect();
        rect.left = Math.round(width * rectNoraml.left);
        rect.top = Math.round(height * rectNoraml.top);
        rect.right = Math.round(width * rectNoraml.right);
        rect.bottom = Math.round(height * rectNoraml.bottom);
        return rect;
    }

//    @Override
//    public void onBluetoothStatusChange(RokidBluetoothManager.BlueSocketStatus status) {
//
//    }
//
//    @Override
//    public void onBluetoothMessageReceiver(IMessage message) {
//
//    }
}
