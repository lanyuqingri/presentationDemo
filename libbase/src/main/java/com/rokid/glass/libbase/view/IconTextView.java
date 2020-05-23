package com.rokid.glass.libbase.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.rokid.glass.libbase.R;
import com.rokid.glass.libbase.logger.Logger;
import com.rokid.glass.libbase.utils.SpanUtil;
import com.rokid.glass.libbase.utils.TypefaceHelper;


/**
 * Author: Shper
 * Version: V0.1 2017/3/30
 */
public class IconTextView extends AppCompatTextView {

    public IconTextView(Context context) {
        super(context);
        initViews(null);
    }

    public IconTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(attrs);
    }

    public IconTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(attrs);
    }

    private void initViews(AttributeSet attrs) {
        Logger.d("Init the iconTextView.");
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.IconTextView);

        if (typedArray.hasValue(R.styleable.IconTextView_iconColor)) {
            int iconColor = typedArray.getColor(R.styleable.IconTextView_iconColor,
                    ContextCompat.getColor(getContext(), R.color.common_gray_text));
            Logger.d("This hav iconColor, so do set it, Color: " + iconColor);
            setTextColor(iconColor);
        }

        if (!typedArray.hasValue(R.styleable.IconTextView_android_textStyle)) {
            setTypeface();
        } else {
            int textStyle = typedArray.getInt(R.styleable.IconTextView_android_textStyle, Typeface.NORMAL);
            setTypeface(textStyle);
        }

        typedArray.recycle();
    }

    private void setTypeface() {
        if (!isInEditMode()) {
            setTypeface(TypefaceHelper.getInstance().getIconFontTypeface());
        }
    }

    public void setTypeface(int style) {
        if (!isInEditMode()) {
            super.setTypeface(TypefaceHelper.getInstance().getIconFontTypeface(), style);
        }
    }

    @Override
    public void setText(CharSequence text, TextView.BufferType type) {
        if (!TextUtils.isEmpty(text) && text.toString().contains("x") && !text.toString().contains("sandBox")) {
            String[] strings = text.toString().split("x");
            int index = 0;
            for (String childStr : strings) {
                text = SpanUtil.genStringWithNormalFont(text, index + childStr.length(), 1);
                index = index + childStr.length();
            }
        }
        super.setText(text, type);
    }

    public void setIcon(CharSequence text) {
        setText(text);
    }

    public void setIcon(@StringRes int resId) {
        setText(resId);
    }

    public void setIconColor(@ColorInt int color) {
        setTextColor(color);
    }

    public void setIconSize(float size) {
        setTextSize(size);
    }

    public void setIconSize(int unit, float size) {
        setTextSize(unit, size);
    }

}
