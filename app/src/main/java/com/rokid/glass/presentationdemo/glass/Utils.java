package com.rokid.glass.presentationdemo.glass;

import android.animation.Animator;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.YuvImage;


import com.rokid.facelib.model.FaceDO;
import com.rokid.facelib.utils.FaceFileUtils;
import com.rokid.glass.libbase.config.GalleryConfig;
import com.rokid.glass.libbase.faceid.FaceRecogRecordManager;
import com.rokid.glass.libbase.faceid.database.DeployFaceRecord;
import com.rokid.glass.libbase.faceid.database.SingleFaceRecord;
import com.rokid.glass.libbase.faceid.database.UserInfo;
import com.rokid.glass.libbase.message.dto.FaceInfoBean;
import com.rokid.glass.libbase.message.face.RespOnlineSingleFaceMessage;
import com.rokid.glass.libbase.message.recogrecord.FaceRecordBean;
import com.rokid.glass.libbase.message.recogrecord.RecordMessage;
import com.rokid.glass.presentationdemo.glass.view.Arrow;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.UUID;

public class Utils{
    public static void saveRespOnlineSingleFaceMessage(RespOnlineSingleFaceMessage msg, Bitmap bm, Bitmap total){
        File dir = new File(GalleryConfig.DIR_RECOG_CROP);
        if(!dir.exists()){
            dir.mkdirs();
        }
//        long minLastModified = 0;
//
//        if(dir.list().length>=300){
//            //删除最早修改的文件
//            File firstFile = null;
//            for(File file :dir.listFiles()){
//                if(file.lastModified()<minLastModified || minLastModified == 0 ){
//                    minLastModified = file.lastModified();
//                    firstFile = file;
//                }
//            }
//            firstFile.delete();
//        }

        String bm_path_crop = GalleryConfig.DIR_RECOG_CROP +msg.getFaceInfoBean().getName()+"_"+System.currentTimeMillis()+".png";
        String bm_path_total = GalleryConfig.DIR_RECOG_TOTAL+msg.getFaceInfoBean().getName()+"_"+System.currentTimeMillis()+".png";

        FaceFileUtils.saveBitmap(bm, bm_path_crop);
        FaceFileUtils.saveBitmap(total,bm_path_total);

        SingleFaceRecord singleFaceRecord = new SingleFaceRecord();
        singleFaceRecord.birthPlace = msg.getFaceInfoBean().getNativeplace();
        singleFaceRecord.name = msg.getFaceInfoBean().getName();
        singleFaceRecord.cardNo = msg.getFaceInfoBean().getCardno();
        singleFaceRecord.captureImg = bm_path_total;
        singleFaceRecord.captureFaceImg = bm_path_crop;
        singleFaceRecord.tag = msg.getFaceInfoBean().getTag();
        singleFaceRecord.id = UUID.randomUUID().toString();
        singleFaceRecord.gmtRecg = System.currentTimeMillis();
        singleFaceRecord.isOnline = true;
        FaceRecogRecordManager.getInstance().addSingleFaceRecord(singleFaceRecord);
    }


    public static void saveFaceOfflineInfo(Bitmap bm, UserInfo userInfo, FaceDO faceDO){
        File dir = new File(GalleryConfig.DIR_RECOG_CROP);
        if(!dir.exists()){
            dir.mkdirs();
        }
//        long minLastModified = 0;
//
//        if(dir.list().length>=300){
//            //删除最早修改的文件
//            File firstFile = null;
//            for(File file :dir.listFiles()){
//                if(file.lastModified()<minLastModified || minLastModified == 0 ){
//                    minLastModified = file.lastModified();
//                    firstFile = file;
//                }
//            }
//            firstFile.delete();
//        }

        String bm_path_crop = GalleryConfig.DIR_RECOG_CROP +userInfo.name+"_"+System.currentTimeMillis()+".png";

        FaceFileUtils.saveBitmap(bm,bm_path_crop);

        SingleFaceRecord singleFaceRecord = new SingleFaceRecord();
        singleFaceRecord.birthPlace = userInfo.nativeplace;
        singleFaceRecord.name = userInfo.name;
        singleFaceRecord.cardNo = userInfo.cardno;
        singleFaceRecord.captureFaceImg = bm_path_crop;
        singleFaceRecord.tag = userInfo.description;
        singleFaceRecord.id = UUID.randomUUID().toString();
        singleFaceRecord.gmtRecg = System.currentTimeMillis();
        singleFaceRecord.pose = poseFloatToString(faceDO.pose);
        singleFaceRecord.userInfoScore = faceDO.userInfoScore;
        singleFaceRecord.faceScore = faceDO.quality;
        singleFaceRecord.isOnline = false;

        FaceRecogRecordManager.getInstance().addSingleFaceRecord(singleFaceRecord);
    }

    public static void saveDeployFaceOfflineInfo(Bitmap bm, UserInfo userInfo, FaceDO faceDO){
        if(bm == null || bm.isRecycled()) return;
        File dir = new File(GalleryConfig.DIR_DEPLOY_CROP);
        if(!dir.exists()){
            dir.mkdirs();
        }
//        long minLastModified = 0;
//
//        if(dir.list().length>=300){
//            //删除最早修改的文件
//            File firstFile = null;
//            for(File file :dir.listFiles()){
//                if(file.lastModified()<minLastModified || minLastModified == 0 ){
//                    minLastModified = file.lastModified();
//                    firstFile = file;
//                }
//            }
//            firstFile.delete();
//        }
        String bm_path_crop = GalleryConfig.DIR_DEPLOY_CROP +userInfo.name+"_"+System.currentTimeMillis()+".png";
//        String bm_path_total = GalleryConfig.DIR_RECOG_TOTAL+userInfo.name+".png";

        FaceFileUtils.saveBitmap(bm,bm_path_crop);
//        FaceFileUtils.saveBitmap(bm_total,bm_path_total);

        DeployFaceRecord deployFaceRecord = new DeployFaceRecord();
        deployFaceRecord.birthPlace = userInfo.nativeplace;
        deployFaceRecord.name = userInfo.name;
        deployFaceRecord.cardNo = userInfo.cardno;
        deployFaceRecord.captureFaceImg = bm_path_crop;
        deployFaceRecord.tag = userInfo.description;
        deployFaceRecord.id = UUID.randomUUID().toString();
        deployFaceRecord.gmtRecg = System.currentTimeMillis();
        deployFaceRecord.pose = poseFloatToString(faceDO.pose);
        deployFaceRecord.userInfoScore = faceDO.userInfoScore;
        deployFaceRecord.faceScore = faceDO.quality;
        deployFaceRecord.isOnline = false;

        FaceRecogRecordManager.getInstance().addDeployFaceRecord(deployFaceRecord);
    }

    public static void savePlateInfo(Bitmap bm,String name){
        File dir = new File(GalleryConfig.DIR_PLATE_CROP);
        if(!dir.exists()){
            dir.mkdirs();
        }
        long minLastModified = 0;

        if(dir.list().length>=300){
            //删除最早修改的文件
            File firstFile = null;
            for(File file :dir.listFiles()){
                if(file.lastModified()<minLastModified || minLastModified == 0 ){
                    minLastModified = file.lastModified();
                    firstFile = file;
                }
            }
            firstFile.delete();
        }
        FaceFileUtils.saveBitmap(bm, GalleryConfig.DIR_PLATE_CROP +name+".png");
    }


    public static Bitmap nv21ToBitmap(byte[] nv21, int width, int height,Rect rect) {
        Bitmap bitmap = null;
        try {
            YuvImage image = new YuvImage(nv21, ImageFormat.NV21, width, height, null);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            if(rect.left<0){
                rect.left = 0;
            }
            if(rect.right>width){
                rect.right = width;
            }
            if(rect.top<0){
                rect.top = 0;
            }
            if(rect.bottom>height){
                rect.bottom = height;
            }

            image.compressToJpeg(rect, 80, stream);
            bitmap = BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.size());
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 两点之间距离
     * @return
     */
    public static double getDistance(Point p1,Point p2){
        double r = Math.sqrt((p1.x-p2.x)*(p1.x-p2.x)+(p1.y-p2.y)*(p1.y-p2.y));
        return r;
    }


    /**
     * UserInfo 转换 RecordMessage
     * @return
     */
    public static RecordMessage ConvertUserInfo2RecordMessage(UserInfo userInfo, Bitmap rawBm, Bitmap dbBm, boolean singleFace){
        RecordMessage recordMessage = new RecordMessage();
        recordMessage.setFaceRecord(true);
        FaceRecordBean faceRecordBean = new FaceRecordBean();
        faceRecordBean.setRawImage(BitmapUtils.bitmap2Bytes(rawBm));
        faceRecordBean.setIsSingleFace(singleFace);
        FaceInfoBean faceInfoBean = new FaceInfoBean();
        faceInfoBean.setCardno(userInfo.cardno);
        faceInfoBean.setFaceImage(BitmapUtils.bitmap2Bytes(dbBm));
        faceInfoBean.setName(userInfo.name);
        faceInfoBean.setNativeplace(userInfo.nativeplace);
        faceInfoBean.setUid(userInfo.uid);
        faceInfoBean.setTag(userInfo.description);
        faceInfoBean.setAlarm(userInfo.isAlarm);
        faceRecordBean.setFaceInfoBean(faceInfoBean);
        recordMessage.setFaceInfo(faceRecordBean);
        return recordMessage;
    }

    /**
     * 通过baseRect和targetRect 来确定箭头的位置和方向
     * 实现思路：
     * 在以baseRect中心点为原点的坐标系下
     * 设箭头的位置为x1,y1 , 目标Rect的位置为x2,y2;
     * 则x2 = targetCenter.x - baseRect.width()/2
     *   y2 = baseRect.height()/2 - targetCenter.y
     * 1.将目标框所在位置，分成2种情况分类实现
     *      1.1.|y2/x2|<|base.height/base.width|
     *          x1 = x1
     *          y1 = x1*y2/x2
     *
     *      1.2.|y2/x2|>|base.height/base.width|
     *          x1 = x2*y1/y2
     *          y1 = y1
     * @param baseRect
     * @param targetRect
     * @return
     */
    public static Arrow getArrow(Rect baseRect, Rect targetRect){
        Arrow arrow = new Arrow();
        PointF pointF = new PointF();
        Point baseCenter = new Point(baseRect.left/2+baseRect.right/2,baseRect.top/2+baseRect.bottom/2);
        Point targetCenter = new Point(targetRect.left/2+targetRect.right/2,targetRect.top/2+targetRect.bottom/2);
        double s = Utils.getDistance(baseCenter,targetCenter);
        double x2 = targetCenter.x - baseRect.width()/2+baseRect.left;
        double y2 =  - targetCenter.y + baseRect.height()/2 - baseRect.top ;
        double x1;
        double y1;
        if(Math.abs(y2/x2)<Math.abs((double)baseRect.height()/(double)baseRect.width())){
            if(x2>baseRect.width()/2){
                x1 = baseRect.width()/2;
                y1 = x1 * y2 / x2;
                pointF.x = (float) baseRect.right;
                pointF.y = (float) (baseRect.height()/2 - y1 - baseRect.top);
            }else if(x2<-baseRect.width()/2){
                x1 = -baseRect.width()/2;
                y1 = x1 * y2 / x2;
                pointF.x = (float) baseRect.left;
                pointF.y = (float) (baseRect.height()/2 - y1- baseRect.top);
            }
        }else{
            if(y2<-baseRect.height()/2){
                y1 = -baseRect.height()/2;
                x1 = x2 * y1 / y2;
                pointF.x = (float) (x1+baseRect.width()/2+baseRect.left);
                pointF.y = (float) baseRect.bottom;
            }else if(y2>baseRect.height()/2){
                y1 = baseRect.height()/2;
                x1 = x2 * y1 / y2;
                pointF.x = (float) (x1+baseRect.width()/2+baseRect.left);
                pointF.y = (float) baseRect.top;
            }
        }
        arrow.point = pointF;
        int baseAngle = (int) (Math.atan(Math.abs(y2/x2)) * 180.0 / Math.PI);
        if(x2 > 0 && y2 >0){
            //第一象限
            arrow.angle = baseAngle;
        }else if(x2 < 0 && y2 >0){
            //第二象限
            arrow.angle = 180-baseAngle;
        }else if(x2 < 0 && y2 < 0){
            //第三象限
            arrow.angle =  baseAngle+180;
        }else if(x2 > 0 && y2 < 0){
            //第四象限
            arrow.angle = -baseAngle;
        }
        return arrow;
    }



    public static LayoutTransition getLayoutTransition(Context context){
        LayoutTransition mTransition = new LayoutTransition();
        mTransition.setDuration(LayoutTransition.CHANGE_APPEARING,200);
        mTransition.setDuration(LayoutTransition.CHANGE_DISAPPEARING,200);
        mTransition.setDuration(LayoutTransition.APPEARING,200);
        mTransition.setDuration(LayoutTransition.DISAPPEARING,200);
        //-----------------------设置动画--------------------
        mTransition.setAnimator(LayoutTransition.APPEARING,getInAnim(context));
        mTransition.setAnimator(LayoutTransition.DISAPPEARING,getOutAnim(context));
        //---------------------------------------------------
        mTransition.setStartDelay(LayoutTransition.CHANGE_APPEARING,0);
        mTransition.setStartDelay(LayoutTransition.APPEARING,0);
        mTransition.setStartDelay(LayoutTransition.DISAPPEARING,0);
        mTransition.setStartDelay(LayoutTransition.CHANGE_DISAPPEARING,50);
        //----viewgroup绑定----
        return mTransition;
    }

    @SuppressLint("ObjectAnimatorBinding")
    private static Animator getInAnim(Context context) {
         PropertyValuesHolder trX = PropertyValuesHolder.ofFloat("translationX",100f, 0f);
        PropertyValuesHolder trY = PropertyValuesHolder.ofFloat("translationY",0f, 0f);
        PropertyValuesHolder trAlpha = PropertyValuesHolder.ofFloat("alpha", 0f,1f);
        return ObjectAnimator.ofPropertyValuesHolder(context,trY,trAlpha,trX);
    }

    @SuppressLint("ObjectAnimatorBinding")
    private static Animator getOutAnim(Context context) {
        PropertyValuesHolder trY2 = PropertyValuesHolder.ofFloat("translationY",0f, -100f);
        PropertyValuesHolder trX = PropertyValuesHolder.ofFloat("translationX",0f, 0f);
        PropertyValuesHolder trAlpha2 = PropertyValuesHolder.ofFloat("alpha",1f, 0f);
        return ObjectAnimator.ofPropertyValuesHolder(context,trY2,trAlpha2,trX);
    }

    public static String poseFloatToString(float[] pose){
        String poseStr = null;
        if(pose!=null && pose.length>3){
            poseStr = pose[0]+","+pose[1]+","+pose[2]+","+pose[3];
        }
        return poseStr;
    }

    public static float[] poseStringToFloat(String pose){
        float[] fPose = null;
        if(pose!=null){
            String[] poseArray = pose.split(",");
            if(poseArray.length>3){
                fPose = new float[4];
                fPose[0] = Float.parseFloat(poseArray[0]);
                fPose[1] = Float.parseFloat(poseArray[1]);
                fPose[2] = Float.parseFloat(poseArray[2]);
                fPose[3] = Float.parseFloat(poseArray[3]);
            }
        }
        return fPose;
    }
}




