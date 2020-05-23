package com.rokid.glass.libbase.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

import com.rokid.glass.libbase.R;
import com.rokid.glass.libbase.logger.Logger;
import com.rokid.glass.libbase.utils.ReplaceRokidUtil;
import com.rokid.glass.libbase.utils.SizeUtils;
import com.rokid.glass.libbase.utils.SoftKeyBoardUtil;


/**
 * Author: Shper
 * Version: V0.1 2017/3/30
 */
public class TitleBar extends RelativeLayout {

    private static final int NO_SET = -99999999;

    private int titleBarBg = NO_SET;
    private int leftIconColor = NO_SET;
    private int titleColor = NO_SET;
    private int rightTextEnableColor;
    private int rightTextDisableColor;

    private RelativeLayout rootView;
    private View leftView;
    private View titleView;
    private ViewGroup rightLayer;
    private View rightIconView;
    private View rightDot;
    private View bottomLine;

    public TitleBar(Context context) {
        super(context);
        initView(null);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }

    private void initView(@Nullable AttributeSet attrs) {
        rootView = (RelativeLayout) View.inflate(getContext(), R.layout.common_layout_titlebar, this);

        leftView = rootView.findViewById(R.id.common_titlebar_left_icon);
        titleView = rootView.findViewById(R.id.common_titlebar_title_tv);

        rightLayer = rootView.findViewById(R.id.common_titlebar_right_layer);
        rightIconView = rootView.findViewById(R.id.common_titlebar_right_icon);
        rightDot = rootView.findViewById(R.id.common_titlebar_right_dot);

        bottomLine = rootView.findViewById(R.id.common_titlebar_bottom_line);

        // Init View by Xml Style
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TitleBar);

        initTitleBar(typedArray);
        initLeftView(typedArray);
        initTitle(typedArray);
        initRightView(typedArray);
        initBottomLine(typedArray);


        typedArray.recycle();
    }

    // 设置 整体风格
    private void initTitleBar(TypedArray typedArray) {

        if (typedArray.hasValue(R.styleable.TitleBar_android_background)) {
            titleBarBg = typedArray.getColor(R.styleable.TitleBar_android_background,
                    ContextCompat.getColor(getContext(), R.color.common_white));
        }

        Logger.d("This titleBarBg: " + titleBarBg);
        setTitleBarBg();
    }

    private void initLeftView(TypedArray typedArray) {
        final String leftIcon = typedArray.getString(R.styleable.TitleBar_leftIcon);
        setLeftIcon(null != leftIcon ? leftIcon : getContext().getString(R.string.icon_back));

        if (typedArray.hasValue(R.styleable.TitleBar_leftIconColor)) {
            leftIconColor = typedArray.getColor(R.styleable.TitleBar_leftIconColor,
                    ContextCompat.getColor(getContext(), R.color.common_text_black_color));
        }
        setLeftIconColor();

        int leftIconSize = typedArray.getDimensionPixelSize(R.styleable.TitleBar_leftIconSize, 28);
        setLeftIconSize(leftIconSize);

        setLeftOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == getContext() || !(getContext() instanceof Activity)) {
                    return;
                }

                ((Activity) getContext()).finish();
                SoftKeyBoardUtil.hideSoftKeyboard(getContext(), leftView);
            }
        });
    }

    private void initTitle(TypedArray typedArray) {
        int titleStyle = typedArray.getInt(R.styleable.TitleBar_titleStyle, Typeface.BOLD);
        setTitleTypeface(titleStyle);

        if (typedArray.hasValue(R.styleable.TitleBar_titleTextColor)) {
            titleColor = typedArray.getColor(R.styleable.TitleBar_titleTextColor,
                    ContextCompat.getColor(getContext(), R.color.common_text_black_color));
        }
        setTitleColor();

        int titleSize = typedArray.getDimensionPixelSize(R.styleable.TitleBar_titleTextSize, SizeUtils.sp2px(16));
        setTitleSize(SizeUtils.px2sp(titleSize));

        String titleText = typedArray.getString(R.styleable.TitleBar_titleText);
        setTitle(null != titleText ? ReplaceRokidUtil.replaceRokid(getContext(), titleText) : "");
    }

    private void initRightView(TypedArray typedArray) {
        String rightText = typedArray.getString(R.styleable.TitleBar_rightText);
        if (!TextUtils.isEmpty(rightText)) {
            setRightText(rightText);
        }

        rightTextEnableColor = typedArray.getColor(R.styleable.TitleBar_rightTextColor,
                ContextCompat.getColor(getContext(), R.color.rokid_main_color));
        setRightTextColorInt(rightTextEnableColor);

        rightTextDisableColor = typedArray.getColor(R.styleable.TitleBar_rightTextDisableColor,
                ContextCompat.getColor(getContext(), R.color.common_gray_text));

        int rightSize = typedArray.getDimensionPixelSize(R.styleable.TitleBar_rightTextSize, SizeUtils.sp2px(15));
        setRightTextSize(SizeUtils.px2sp(rightSize));

        int rightTextStyle = typedArray.getInt(R.styleable.TitleBar_rightTextStyle, Typeface.NORMAL);
        setRightTextTypeface(rightTextStyle);
    }

    private void initBottomLine(TypedArray typedArray) {
        boolean isHideLine = typedArray.getBoolean(R.styleable.TitleBar_hideLine, false);
        bottomLine.setVisibility(isHideLine ? GONE : VISIBLE);

        bottomLine.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.common_btn_unclickable));
    }

    public void setStyle() {
        setTitleBarBg();
        setLeftIconColor();
        setTitleColor();
        setRightViewColor();
        setBottomLineBg();
    }


    private void setTitleBarBg() {
        setBackgroundColor(titleBarBg != NO_SET ? titleBarBg : ContextCompat.getColor(getContext(), R.color.common_white));
    }

    private void setLeftIconColor() {
        setLeftIconColorInt(leftIconColor != NO_SET ? leftIconColor : ContextCompat.getColor(getContext(),
                R.color.common_text_black_color));
    }

    private void setTitleColor() {
        setTitleColorInt(titleColor != NO_SET ? titleColor : ContextCompat.getColor(getContext(),
                R.color.common_text_black_color));
    }

    private void setRightViewColor() {
        if (rightIconView.isEnabled()) {
            setRightTextColorInt(ContextCompat.getColor(getContext(),
                    R.color.rokid_main_color));
        } else {
            setRightTextColorInt(ContextCompat.getColor(getContext(),
                    R.color.common_gray_text));
        }
    }

    private void setBottomLineBg() {
        bottomLine.setBackgroundColor(ContextCompat.getColor(getContext(),
                R.color.common_btn_unclickable));
    }

    public void setLeftIcon(@StringRes int resId) {
        if (null == leftView) {
            Logger.w("LeftView is empty!!!");
            return;
        }

        if (leftView instanceof IconTextView) {
//            Logger.d("LeftView as a IconFontTextView.");
            ((IconTextView) leftView).setText(resId);
        }
    }

    public void setLeftIconVisibility(boolean visible) {
        if (null == leftView) {
            Logger.w("LeftView is empty!!!");
            return;
        }
        leftView.setVisibility(visible ? VISIBLE : GONE);
    }

    public void setLeftIcon(CharSequence text) {
        if (null == leftView) {
            Logger.w("LeftView is empty!!!");
            return;
        }

        if (leftView instanceof IconTextView) {
//            Logger.d("LeftView as a IconFontTextView.");
            ((IconTextView) leftView).setText(text);
        }
    }

    public void setLeftIconTypeface(@IntRange(from = 0) int style) {
        if (null == leftView) {
            Logger.w("TitleView is empty!!!");
            return;
        }

        if (leftView instanceof IconTextView) {
//            Logger.d("TitleView as a IconFontTextView.");
            ((IconTextView) leftView).setTypeface(style);
        }
    }

    public void setLeftIconColor(@ColorRes int resId) {
        if (null == leftView) {
            Logger.w("LeftView is empty!!!");
            return;
        }

        if (leftView instanceof IconTextView) {
//            Logger.d("LeftView as a IconFontTextView.");
            ((IconTextView) leftView).setTextColor(ContextCompat.getColor(getContext(), resId));
        }
    }

    public void setLeftIconColor(@NonNull String colorStr) {
        if (!colorStr.startsWith("#") || colorStr.length() != 7 || colorStr.length() != 9) {
            Logger.w("The Color String is invalid.");
            return;
        }

        setLeftIconColorInt(Color.parseColor(colorStr));
    }

    public void setLeftIconColorInt(@ColorInt int colorInt) {
        if (null == leftView) {
            Logger.w("LeftView is empty!!!");
            return;
        }

        if (leftView instanceof IconTextView) {
//            Logger.d("LeftView as a IconFontTextView.");
            ((IconTextView) leftView).setTextColor(colorInt);
        }
    }

    public void setLeftIconSize(@IntRange(from = 1) int size) {
        if (null == leftView) {
            Logger.w("LeftView is empty!!!");
            return;
        }

        if (leftView instanceof IconTextView) {
//            Logger.d("LeftView as a IconFontTextView.");
            ((IconTextView) leftView).setTextSize(size);
        }
    }

    public void setLeftView(@NonNull View view) {
        setLeftView(view, null);
    }

    public void setLeftView(@NonNull View view, LayoutParams layoutParams) {
        if (null != leftView) {
            ViewGroup parent = (ViewGroup) leftView.getParent();
            if (null != parent) {
                parent.removeView(leftView);
            }
        }
        if (null == layoutParams) {
            layoutParams = (LayoutParams) leftView.getLayoutParams();
        }

        leftView = view;
        rootView.addView(leftView, getLayoutParams(leftView, layoutParams, RelativeLayout.ALIGN_PARENT_LEFT));
    }

    public void setLeftOnClickListener(@Nullable OnClickListener listener) {
        if (null == leftView) {
            Logger.w("LeftView is empty!!!");
            return;
        }

        leftView.setOnClickListener(listener);
    }

    public void setTitle(CharSequence text) {
        if (null == titleView) {
            Logger.w("TitleView is empty!!!");
            return;
        }

        if (titleView instanceof IconTextView) {
//            Logger.d("TitleView as a IconFontTextView.");
            ((IconTextView) titleView).setText(ReplaceRokidUtil.replaceRokid(getContext(), text));
        }
    }

    public void setTitleAlpha(float alpha) {
        titleView.setAlpha(alpha);
    }

    public void setTitle(@StringRes int resId) {
        setTitle(getContext().getString(resId));
    }

    public CharSequence getTitle() {
        if (null == titleView) {
            Logger.w("TitleView is empty!!!");
            return "";
        }

        if (titleView instanceof IconTextView) {
//            Logger.d("TitleView as a IconFontTextView.");
            return ((IconTextView) titleView).getText();
        }

        return "";
    }

    public int getLeftIconColor() {
        if (null == leftView) {
            Logger.w("TitleView is empty!!!");
            return NO_SET;
        }

        if (leftView instanceof IconTextView) {
            return ((IconTextView) leftView).getCurrentTextColor();
        }

        return NO_SET;
    }

    public void setTitleSize(float size) {
        if (null == titleView) {
            Logger.w("TitleView is empty!!!");
            return;
        }

        if (titleView instanceof IconTextView) {
//            Logger.d("TitleView as a IconFontTextView.");
            ((IconTextView) titleView).setTextSize(size);
        }
    }

    public void setTitleColor(@ColorRes int resId) {
        if (null == titleView) {
            Logger.w("TitleView is empty!!!");
            return;
        }

        if (titleView instanceof IconTextView) {
//            Logger.d("TitleView as a IconFontTextView.");
            ((IconTextView) titleView).setTextColor(ContextCompat.getColor(getContext(), resId));
        }
    }

    public void setTitleColorInt(@ColorInt int colorInt) {
        if (null == titleView) {
            Logger.w("TitleView is empty!!!");
            return;
        }

        if (titleView instanceof IconTextView) {
//            Logger.d("TitleView as a IconFontTextView.");
            ((IconTextView) titleView).setTextColor(colorInt);
        }
    }

    public void setTitleTypeface(@IntRange(from = 0) int style) {
        if (null == titleView) {
            Logger.w("TitleView is empty!!!");
            return;
        }

        if (titleView instanceof IconTextView) {
//            Logger.d("TitleView as a IconFontTextView.");
            ((IconTextView) titleView).setTypeface(style);
        }
    }

    public void setTitleView(@NonNull View view) {
        setTitleView(view, null);
    }

    public void setTitleView(@NonNull View view, LayoutParams layoutParams) {
        if (null != titleView) {
            rootView.removeView(titleView);
        }

        titleView = view;
        rootView.addView(titleView, getLayoutParams(titleView, layoutParams, RelativeLayout.CENTER_IN_PARENT));
    }

    public void setTitleOnClickListener(@Nullable OnClickListener listener) {
        if (null == titleView) {
            Logger.w("TitleView is empty!!!");
            return;
        }

        titleView.setOnClickListener(listener);
    }

    public void setRightText(@StringRes int resId) {
        if (null == rightIconView) {
            Logger.w("RightView is empty!!!");
            return;
        }

        if (rightIconView instanceof IconTextView) {
//            Logger.d("RightView as a IconFontTextView.");
            ((IconTextView) rightIconView).setText(resId);
        }
    }

    public void setRightText(CharSequence text) {
        if (null == rightIconView) {
            Logger.w("RightView is empty!!!");
            return;
        }

        if (rightIconView instanceof IconTextView) {
//            Logger.d("RightView as a IconFontTextView.");
            ((IconTextView) rightIconView).setText(text);
        }
    }

    public void setRightTextColor(@ColorRes int resId) {
        if (null == rightIconView) {
            Logger.w("RightView is empty!!!");
            return;
        }

        if (rightIconView instanceof IconTextView) {
//            Logger.d("RightView as a IconFontTextView.");
            ((IconTextView) rightIconView).setTextColor(ContextCompat.getColor(getContext(), resId));
        }
    }

    public void setRightTextColorInt(@ColorInt int colorInt) {
        Logger.d("colorInt: " + colorInt);

        if (null == rightIconView) {
            Logger.w("RightView is empty!!!");
            return;
        }

        if (rightIconView instanceof IconTextView) {
//            Logger.d("RightView as a IconFontTextView.");
            ((IconTextView) rightIconView).setTextColor(colorInt);
        }
    }

    public void setRightTextSize(@IntRange(from = 1) int size) {
        if (null == rightIconView) {
            Logger.w("RightView is empty!!!");
            return;
        }

        if (rightIconView instanceof IconTextView) {
//            Logger.d("RightView as a IconFontTextView.");
            ((IconTextView) rightIconView).setTextSize(size);
        }
    }

    public void setRightTextTypeface(@IntRange(from = 0) int style) {
        if (null == rightIconView) {
            Logger.w("RightView is empty!!!");
            return;
        }

        if (rightIconView instanceof IconTextView) {
//            Logger.d("TitleView as a IconFontTextView.");
            ((IconTextView) rightIconView).setTypeface(style);
        }
    }

    public void setRightEnable(boolean enable) {
        if (null == rightIconView) {
            Logger.w("RightView is empty!!!");
            return;
        }

        rightLayer.setEnabled(enable);
        rightIconView.setEnabled(enable);
        setRightTextColorInt(enable ? rightTextEnableColor : rightTextDisableColor);
    }

    public boolean isRightEnable() {
        if (null == rightIconView) {
            Logger.w("RightView is empty!!!");
            return false;
        }

        return rightIconView.isEnabled();
    }

    public void setRightDotVisibility(boolean isVisibility) {
        rightDot.setVisibility(isVisibility ? VISIBLE : GONE);
    }

    public void removeRightView() {
        if (null != rightLayer) {
            rightLayer.removeAllViews();
        }
    }

    public void setRightView(@NonNull View view) {
        setRightView(view, null);
    }

    public void setRightView(@NonNull View view, int width, int height) {
        rightLayer.removeAllViews();

        rightLayer.addView(view, width, height);
    }

    public void setRightView(@NonNull View view, LayoutParams layoutParams) {
        rightLayer.removeAllViews();

        if (null != layoutParams) {
            rightLayer.addView(view, layoutParams);
        } else {
            rightLayer.addView(view);
        }

    }

    public void setRightOnClickListener(@Nullable OnClickListener listener) {
        if (null == rightLayer) {
            Logger.w("RightView is empty!!!");
            return;
        }

        rightLayer.setOnClickListener(listener);
    }

    public void setLineVisibility(boolean isVisibility) {
        bottomLine.setVisibility(isVisibility ? VISIBLE : GONE);
    }

    public boolean isLineVisibile() {
        return bottomLine.getVisibility() == VISIBLE;
    }

    public void setRightVisibility(boolean isVisibility) {
        rightLayer.setVisibility(isVisibility ? VISIBLE : GONE);
    }


    private LayoutParams getLayoutParams(@NonNull View view, LayoutParams layoutParams, int verb) {
        if (null == layoutParams) {
            layoutParams = (LayoutParams) view.getLayoutParams();
        }
        if (null == layoutParams) {
            layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        layoutParams.addRule(verb);

        return layoutParams;
    }
}
