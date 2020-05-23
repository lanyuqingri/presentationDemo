package com.rokid.glass.presentationdemo.glass.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.rokid.facelib.model.FaceDO;
import com.rokid.facelib.model.FaceModel;


public class FaceModelView extends View {
    private FaceModel faceModel;
    private Paint paint;
    private Paint textPaint;
    private int startColor,endColor;
    private float loadingF1,loadingF2,loadingF3,loadingF4;

    public FaceModelView(Context context) {
        this(context,null);
    }

    public FaceModelView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FaceModelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        textPaint = new Paint();
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(20);
        textPaint.setColor(Color.parseColor("#EF5350"));

        startColor = Color.parseColor("#FFCA28");
        endColor = Color.parseColor("#EF5350");
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
//        paint.setStrokeWidth(30);
//        paint.setColor(Color.parseColor("#EF5350"));
        paint.setColor(Color.rgb(239,83,80));
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setAntiAlias(true);

        ValueAnimator paintAnim = ValueAnimator.ofFloat(0f, 1f,0f);
        paintAnim.setDuration(1000);
        paintAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float f = (float) animation.getAnimatedValue();
                float width = paint.getStrokeWidth()+5*f;
                paint.setStrokeWidth(width);
            }
        });
        paintAnim.setRepeatCount(ValueAnimator.INFINITE);
        paintAnim.start();

//        startLoadingAnim();

    }

    private void startLoadingAnim() {
        ValueAnimator loadingAnim1 = ValueAnimator.ofFloat(0f, 1f, 0f);
        loadingAnim1.setDuration(2000);
        loadingAnim1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                loadingF1 = (float) animation.getAnimatedValue();
            }
        });
        loadingAnim1.setRepeatCount(ValueAnimator.INFINITE);
        loadingAnim1.start();

        ValueAnimator loadingAnim2 = ValueAnimator.ofFloat(0f, 1f, 0f);
        loadingAnim2.setDuration(2000);
        loadingAnim2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                loadingF2 = (float) animation.getAnimatedValue();
            }
        });
        loadingAnim2.setRepeatCount(ValueAnimator.INFINITE);
        loadingAnim2.setStartDelay(500);
        loadingAnim2.start();

        ValueAnimator loadingAnim3 = ValueAnimator.ofFloat(0f, 1f, 0f);
        loadingAnim3.setDuration(2000);
        loadingAnim3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                loadingF3 = (float) animation.getAnimatedValue();
            }
        });
        loadingAnim3.setRepeatCount(ValueAnimator.INFINITE);
        loadingAnim3.setStartDelay(1000);
        loadingAnim3.start();

        ValueAnimator loadingAnim4 = ValueAnimator.ofFloat(0f, 1f, 0f);
        loadingAnim4.setDuration(2000);
        loadingAnim4.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                loadingF4 = (float) animation.getAnimatedValue();
            }
        });
        loadingAnim4.setRepeatCount(ValueAnimator.INFINITE);
        loadingAnim4.setStartDelay(1200);
        loadingAnim4.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.save();
        try {
            if(faceModel!=null&&faceModel.getFaceList()!=null&&faceModel.getFaceList().size()>0) {
                for (FaceDO faceDO : faceModel.getFaceList()) {
                    Rect rect = faceDO.toRect(getWidth(),getHeight());
                    initPaint(faceDO,rect);
//                Log.i("FaceModelView","faceDO.quality:"+faceDO.quality);
//                canvas.drawRect(rect, paint);
                    //画矩形框
                    drawRountRect(canvas,rect);
//                    canvas.drawText(faceDO.userInfoScore+"", rect.left + 50, rect.top + textPaint.getTextSize()*3 + 5, textPaint);
//                    canvas.drawText(faceDO.goodPose&&faceDO.goodSharpness&&faceDO.goodQuality?"good face":"bad face", rect.left + 50, rect.top + textPaint.getTextSize()*5 + 5, textPaint);
                    canvas.drawText(faceDO.goodPose?"good pose":"bad pose",rect.left + 50, rect.top + textPaint.getTextSize()*5,textPaint);
                    if(faceDO.featid==null) {
                        //画loading动画
//                        drawLoading(canvas, rect);
                    }else{
                        //识别成功
                        drawResult(canvas,rect,faceDO);
                    }
                }
            }else{
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

//        canvas.restore();
    }





    private Paint paintResult;
    private void drawResult(Canvas canvas, Rect rect, FaceDO face) {
        canvas.save();
        canvas.translate((rect.left+rect.right)/2f,(rect.top+rect.bottom)/2f);
        if(paintResult == null) {
            paintResult = new Paint();
            paintResult.setColor(Color.rgb(243, 170, 60));
        }
        paintResult.setTextSize(rect.width()/4);
        String result= face.featid;
        if(result.length()!=0){
            canvas.drawText(result.split("\\.")[0],-rect.width()/4,0,paintResult);
        }
        canvas.restore();
    }

    Paint paintLoading;
    private void drawLoading(Canvas canvas, Rect rect) {
        canvas.save();
        canvas.translate((rect.left+rect.right)/2f,(rect.top+rect.bottom)/2f);
        if(paintLoading == null) {
            paintLoading = new Paint();
            paintLoading.setColor(Color.rgb(243, 170, 60));
        }
        paintLoading.setTextSize(rect.width()/4);
        canvas.drawText("LOADING...",-rect.width()/2,0,paintLoading);
        canvas.restore();
    }

    public void setFaceModel(FaceModel faceModel){
        this.faceModel = faceModel;
        postInvalidate();
    }

    private void initPaint(FaceDO faceDO, Rect rect){
        paint.setStrokeWidth(rect.width()/30);
    }

    private void drawRountRect(Canvas canvas, Rect rect) {

        Log.i("drawRountRect","width:"+rect.width());
        canvas.save();

        canvas.translate((rect.left+rect.right)/2f, (rect.top+rect.bottom) / 2f);

        drawRect(canvas, 0,rect.width(),rect.height());
        drawRect(canvas, 90,rect.width(),rect.height());
        drawRect(canvas, 180,rect.width(),rect.height());
        drawRect(canvas, 270,rect.width(),rect.height());

        canvas.restore();
    }

    private void drawRect(Canvas canvas, int angle,int width,int height) {
        int line_width = width/10;
        int radius = width/20;

        canvas.save();
        canvas.rotate(angle);

        Path path = new Path();
        path.moveTo(-width / 2, height / 2 - line_width);
        path.lineTo(-width / 2, height / 2 - radius);

        RectF rectF = new RectF(-width / 2f, height / 2f - 2 * radius,
                -width / 2f + 2 * radius, height / 2f);

        path.addArc(rectF, 90, 90f);

        path.moveTo(-width / 2 + radius, height / 2);
        path.lineTo(-width / 2 + line_width, height / 2);
        canvas.drawPath(path, paint);

        canvas.restore();
    }


}
