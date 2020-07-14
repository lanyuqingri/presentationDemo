package com.rokid.glass.viewcomponent.glass.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

import com.rokid.glass.libbase.faceid.FaceConstants;


public class RecgCrossView extends View {
    private int status = -1;
    public static final int STATUS_NORMAL = 0;
    public static final int STATUS_DETECT = 1;
    public static final int STATUS_BAD_FACE = 2;
    private Paint mPaint = new Paint();
    private Paint mDashPaint = new Paint();
    private static final int CROSS_WIDTH = 32;
    private static final int LINE_WIDTH = 32;
    private static final int RADIUS = 15;
    private static int PADDING = 80;
    private ValueAnimator  colorAnimator;
    private int startColor,endColor;

    public RecgCrossView(Context context) {
        this(context,null);
    }

    public RecgCrossView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RecgCrossView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public RecgCrossView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
        drawDashRoundRect(canvas,rect);
    }

    private void initPaint() {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(3);
        mPaint.setColor(FaceConstants.WHITE_COLOR);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setAntiAlias(true);

        mDashPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mDashPaint.setColor(FaceConstants.GRAY_WHITE_COLOR);
        mDashPaint.setStrokeWidth(2);
        mDashPaint.setStyle(Paint.Style.STROKE);
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
                mDashPaint.setColor((Integer) animation.getAnimatedValue());
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
        canvas.save();
        canvas.drawLine((rect.right - CROSS_WIDTH + PADDING)/2,(rect.bottom + PADDING)/2 ,(rect.right + CROSS_WIDTH + PADDING)/2 ,(rect.bottom + PADDING)/2,mPaint);
        canvas.drawLine((rect.right + PADDING)/2,(rect.bottom - CROSS_WIDTH + PADDING)/2 ,(rect.right + PADDING)/2,(rect.bottom + CROSS_WIDTH + PADDING)/2,mPaint);
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

    private void drawDashRoundRect(Canvas canvas, Rect rect){
        canvas.save();
        Path path = new Path();
        path.moveTo(rect.left, rect.top + RADIUS*2);
        path.lineTo(rect.left, rect.bottom - RADIUS*2);

        path.moveTo(rect.left + RADIUS*2,rect.bottom);
        path.lineTo(rect.right - RADIUS*2, rect.bottom);

        path.moveTo(rect.right,rect.bottom - RADIUS*2);
        path.lineTo(rect.right, rect.top + RADIUS*2);

        path.moveTo(rect.right - RADIUS*2,rect.top);
        path.lineTo(rect.left + RADIUS*2, rect.top);

        PathEffect effects = new DashPathEffect(new float[] { 15, 5 }, 1);
        mDashPaint.setPathEffect(effects);
        canvas.drawPath(path, mDashPaint);
        canvas.restore();
        drawRountRect(canvas, rect, mDashPaint);
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
}
