package com.rokid.glass.viewcomponent.glass.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

import com.rokid.glass.libbase.faceid.FaceConstants;


public class CrossView extends View {
    private int status = -1;
    public static final int STATUS_NORMAL = 0;
    public static final int STATUS_DETECT = 1;
    public static final int STATUS_BAD_FACE = 2;
    private Paint mPaint = new Paint();
    private static final int CROSS_WIDTH = 64;
    private static int PADDING = 40;
    private ValueAnimator  colorAnimator;
    private int startColor,endColor;

    public CrossView(Context context) {
        this(context,null);
    }

    public CrossView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CrossView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public CrossView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initPaint();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Rect rect  = new Rect();
        rect.left = PADDING;
        rect.right = getWidth() - PADDING;
        rect.top = PADDING;
        rect.bottom = getHeight() - PADDING;
        drawCross(canvas,rect);
    }

    private void initPaint() {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(7);
        mPaint.setColor(FaceConstants.WHITE_COLOR);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setAntiAlias(true);
    }

    private void initColorAnimator() {
        colorAnimator = ValueAnimator.ofArgb(startColor,endColor);
        colorAnimator.setDuration(150);
        colorAnimator.setInterpolator(new LinearInterpolator());
        colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mPaint.setColor((Integer) animation.getAnimatedValue());
                postInvalidate();
            }
        });
    }
    private void switchStatus(int status){
        switch (status){
            case STATUS_NORMAL:
                endColor = FaceConstants.WHITE_COLOR;
                initColorAnimator();
                startColorAnimate();
                break;
            case STATUS_DETECT:
                endColor = FaceConstants.BLUE_COLOR;
                initColorAnimator();
                startColorAnimate();
                break;
            case STATUS_BAD_FACE:
                endColor = FaceConstants.RED_COLOR;
                initColorAnimator();
                startColorAnimate();
                break;

        }
    }

    private void startColorAnimate() {
        if(!colorAnimator.isStarted()){
            colorAnimator.start();
        }
    }

    private void drawCross(Canvas canvas, Rect rect){
        int rectWidth = rect.width();
        int rectHeight = rect.height();
        canvas.save();
        canvas.drawLine(rectWidth/2 - CROSS_WIDTH/2 + PADDING,rectHeight/2 ,rectWidth/2 + CROSS_WIDTH/2 + PADDING,rectHeight/2,mPaint);
        canvas.drawLine(rectWidth/2 + PADDING,rectHeight/2 - CROSS_WIDTH/2 ,rectWidth/2 + PADDING,rectHeight/2 + CROSS_WIDTH/2,mPaint);
        canvas.restore();
    }

    public void setStatus(int status){
        if(this.status != status) {
            switch (this.status){
                case STATUS_NORMAL:
                    startColor = FaceConstants.WHITE_COLOR;
                    break;
                case STATUS_DETECT:
                    startColor = FaceConstants.BLUE_COLOR;
                    break;
                case STATUS_BAD_FACE:
                    startColor = FaceConstants.RED_COLOR;
                    break;
            }
            switchStatus(status);
            this.status = status;
        }
    }

    public Rect getRect(){
        Rect rect = new Rect();
        getGlobalVisibleRect(rect);
        return rect;
    }
}
