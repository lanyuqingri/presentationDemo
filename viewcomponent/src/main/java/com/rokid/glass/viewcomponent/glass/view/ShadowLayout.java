package com.rokid.glass.viewcomponent.glass.view;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ShadowLayout extends FrameLayout {

    private static final String TAG = ShadowLayout.class.getSimpleName();
    private boolean isSelect = false;
    private boolean isEnable = true;
    private int padding = 40;

    public ShadowLayout(@NonNull Context context) {
        this(context, null);
    }

    public ShadowLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShadowLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setPadding(int p) {
        padding = p;
    }

    public void setSelect(boolean press) {
        if (isEnable) {
            isSelect = press;
            postInvalidate();
        }
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
        postInvalidate();
    }

    private void init() {
        setBackgroundColor(0x01ffffff);
        // BlurMaskFilter 不支持硬件加速
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Log.e(TAG, "onDraw");

        if (isEnable) {
            if (isSelect) {
                drawShaowBitmap(canvas, getMeasuredWidth(), getMeasuredHeight(), padding);
            } else {
                drawRountRectBitmap(canvas, getMeasuredWidth(), getMeasuredHeight(), padding);
            }
        }

    }


    public void drawRountRectBitmap(Canvas canvas, int width, int height, int padding) {
        float strokeWidth = 1.5f * getContext().getResources().getDisplayMetrics().density;

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(0xff909090);
        paint.setStyle(Paint.Style.STROKE);

        RectF rectF = new RectF(padding, padding, width - padding, height - padding);


        rectF.left += (strokeWidth / 2);
        rectF.right -= (strokeWidth / 2);
        rectF.top += (strokeWidth / 2);
        rectF.bottom -= (strokeWidth / 2);
        paint.setStrokeWidth(strokeWidth);
        canvas.drawRoundRect(rectF, rectF.height() / 2, rectF.height() / 2, paint);
    }

    public void drawShaowBitmap(Canvas canvas, int width, int height, int padding) {

        float strokeWidth = 1.5f * getContext().getResources().getDisplayMetrics().density;

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);

        RectF rectF = new RectF(padding, padding, width - padding, height - padding);

        LinearGradient linearGradient = null;

        paint.setMaskFilter(new BlurMaskFilter(padding, BlurMaskFilter.Blur.OUTER));
        paint.setStyle(Paint.Style.FILL);

        linearGradient = new LinearGradient(rectF.left, rectF.top, rectF.right, rectF.bottom,
                0x66ADDFFF, 0xBF7784FF, Shader.TileMode.CLAMP);
        paint.setShader(linearGradient);

        canvas.drawRoundRect(rectF, rectF.height() / 2, rectF.height() / 2, paint);


        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);

        rectF.left += (strokeWidth / 2);
        rectF.right -= (strokeWidth / 2);
        rectF.top += (strokeWidth / 2);
        rectF.bottom -= (strokeWidth / 2);

        linearGradient = new LinearGradient((rectF.left + rectF.right) / 2, rectF.top, (rectF.left + rectF.right) / 2, rectF.bottom,
                0xffDFDFE7, 0xff353469, Shader.TileMode.CLAMP);
        paint.setStrokeWidth(strokeWidth);
        paint.setShader(linearGradient);
        canvas.drawRoundRect(rectF, rectF.height() / 2, rectF.height() / 2, paint);

    }
}
