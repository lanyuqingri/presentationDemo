package com.rokid.glass.presentationdemo.glass.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

public class RoiRect extends View {
    private int status = -1;
    public static final int STATUS_NORMAL = 0;
    public static final int STATUS_DETECT = 1;
    public static final int STATUS_BAD_FACE = 2;
    private static int PADDING = 40;
    private static final int LINE_WIDTH = 32;
    private static final int RADIUS = 15;
    private Paint paint = new Paint();
    private ValueAnimator paddingAnimator, colorAnimator;
    private int color_white,color_blue,color_red;
    private int startColor,endColor;
    private int startPadding,endPadding;
    public RoiRect(Context context) {
        this(context,null);
    }

    public RoiRect(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RoiRect(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
        color_white = Color.parseColor("#7fffffff");
        color_blue = Color.parseColor("#FF5A2AFF");
        color_red =  Color.parseColor("#FFFF560B");
    }

    private void initColorAnimator() {
        colorAnimator = ValueAnimator.ofArgb(startColor,endColor);
        colorAnimator.setDuration(150);
        colorAnimator.setInterpolator(new LinearInterpolator());
        colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                paint.setColor((Integer) animation.getAnimatedValue());
                postInvalidate();
            }
        });
    }


    private void initPaint() {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(7);
        paint.setColor(color_white);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setAntiAlias(true);
    }

    private void initPaddingAnimator() {
        paddingAnimator = ValueAnimator.ofInt(startPadding,endPadding);
        paddingAnimator.setDuration(150);
        paddingAnimator.setInterpolator(new DecelerateInterpolator());
        paddingAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PADDING = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
    }

    public void setStatus(int status){
        if(this.status != status) {
            if(this.status == STATUS_NORMAL){
                startPadding = 40;
            }else{
                startPadding = 15;
            }
            switch (this.status){
                case STATUS_NORMAL:
                    startColor = color_white;
                    break;
                case STATUS_DETECT:
                    startColor = color_blue;
                    break;
                case STATUS_BAD_FACE:
                    startColor = color_red;
                    break;
            }
            switchStatus(status);
            this.status = status;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Rect rect  = new Rect();
        rect.left = PADDING;
        rect.right = getWidth() - PADDING;
        rect.top = PADDING;
        rect.bottom = getHeight() - PADDING;

        drawRountRect(canvas,rect,paint);
    }

    private void switchStatus(int status){
        switch (status){
            case STATUS_NORMAL:
                endColor = color_white;
                endPadding = 40;
                initColorAnimator();
                initPaddingAnimator();
                startColorAnimate();
                startPaddingAnmate();
                break;
            case STATUS_DETECT:
                endColor = color_blue;
                endPadding = 15;
                initColorAnimator();
                initPaddingAnimator();
                startColorAnimate();
                startPaddingAnmate();
                break;
            case STATUS_BAD_FACE:
                endColor = color_red;
                endPadding = 15;
                initColorAnimator();
                initPaddingAnimator();
                startColorAnimate();
                startPaddingAnmate();
                break;

        }
    }

    private void startColorAnimate() {
        if(!colorAnimator.isStarted()){
            colorAnimator.start();
        }
    }

    private void startPaddingAnmate(){
        if(!paddingAnimator.isStarted()){
            paddingAnimator.start();
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

    public Rect getRect(){
        Rect rect = new Rect();
        getGlobalVisibleRect(rect);
        return rect;
    }
}
