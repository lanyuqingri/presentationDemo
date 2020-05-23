package com.rokid.glass.libbase.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rokid.glass.libbase.R;
import com.rokid.glass.libbase.utils.ReplaceRokidUtil;
import com.rokid.glass.libbase.utils.SizeUtils;


public class MultiEditText extends TextInputLayout {

    public static final int INPUT_TYPE_TEXT = 0;
    public static final int INPUT_TYPE_NUMBER = 1;
    public static final int INPUT_TYPE_TEXT_PASSWORD = 2;
    public static final int INPUT_TYPE_NUMBER_PASSWORD = 3;
    public static final int INPUT_TYPE_NUMBER_DECIMAL = 4;

    private TextView titleTxt;
    private TextInputEditText editText;
    private TextInputLayout inputLayout;
    private IconTextView clearIcon;
    private IconTextView eyeIcon;
    private View bottomLine;

    private int style = 0;

    private int inputType;

    private boolean showEyeIcon;

    private boolean showClearIcon;

    private View.OnFocusChangeListener mOnTextFocusChangeListener;

    public MultiEditText(Context context) {
        super(context);
        initViews(null);
    }

    public MultiEditText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews(attrs);
    }

    public MultiEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(attrs);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public void textChanged(Editable editable) {
        if (showEyeIcon) {
            eyeIcon.setVisibility(View.VISIBLE);
        } else {
            eyeIcon.setVisibility(View.GONE);
        }

        if (showClearIcon && editable.length() > 0) {
            clearIcon.setVisibility(View.VISIBLE);
        } else {
            clearIcon.setVisibility(View.GONE);
        }
    }

    @NonNull
    @Override
    public TextInputEditText getEditText() {
        return editText;
    }

    public void setSelection(int index) {
        editText.setSelection(index);
    }

    public Editable getText() {
        return editText.getText();
    }

    public void setText(CharSequence string) {
        editText.setText(string);
    }

    public String getTextString() {
        return editText.getText().toString();
    }

    public void setInputString(String inputString) {
        editText.setText(inputString);
    }

    public void setInputType(int inputType) {
        this.inputType = inputType;
        switch (inputType) {
            case INPUT_TYPE_TEXT:
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
            case INPUT_TYPE_NUMBER:
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
            case INPUT_TYPE_TEXT_PASSWORD:
                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                break;

            case INPUT_TYPE_NUMBER_PASSWORD:
                editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                break;
            case INPUT_TYPE_NUMBER_DECIMAL:
                editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                break;
        }
    }

    public void setShowClearIcon(boolean showClearIcon) {
        this.showClearIcon = showClearIcon;
        if (showClearIcon && editText.getText().length() > 0) {
            clearIcon.setVisibility(View.VISIBLE);
        } else {
            clearIcon.setVisibility(View.GONE);
        }
    }

    public void setShowPasswordIcon(boolean showPasswordIcon) {
        this.showEyeIcon = showPasswordIcon;
        textChanged(editText.getEditableText());
    }

    public void showPassword() {
        editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        eyeIcon.setTextColor(getColor(R.color.rokid_main_color));
    }

    public void takeFocus() {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
    }

    private void initViews(@Nullable AttributeSet attrs) {
        TextInputLayout rootView = (TextInputLayout) View.inflate(getContext(), R.layout.common_layout_multiedittext, this);

        titleTxt = rootView.findViewById(R.id.common_editText_title);
        editText = rootView.findViewById(R.id.common_editText_edit);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textChanged(editText.getEditableText());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputLayout = rootView.findViewById(R.id.common_editText_inputLayout);
        clearIcon = rootView.findViewById(R.id.common_editText_clear_btn);
        eyeIcon = rootView.findViewById(R.id.common_editText_eye_btn);
        bottomLine = rootView.findViewById(R.id.common_editText_bottom_line);

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.MultiEditText);
        initMultiEdit(typedArray);
        initTitleView(typedArray);
        initEditView(typedArray);
        initEyeIconView(typedArray);
        initClearIconView(typedArray);
        initBottomLineView(typedArray);

        typedArray.recycle();
    }

    // 获取整体 风格
    private void initMultiEdit(TypedArray typedArray) {
        style = typedArray.getInt(R.styleable.MultiEditText_style, 0);

        int bottomBarBg = typedArray.getColor(R.styleable.MultiEditText_android_background, Color.TRANSPARENT);
        setBackgroundColor(bottomBarBg);
    }

    // 设置 title
    private void initTitleView(TypedArray typedArray) {
        String title = typedArray.getString(R.styleable.MultiEditText_title);
        titleTxt.setText(!TextUtils.isEmpty(title) ? title : "");
        titleTxt.setVisibility(!TextUtils.isEmpty(title) ? VISIBLE : GONE);
        int titleColor = typedArray.getColor(R.styleable.MultiEditText_textColor,
                getColor(R.color.common_gray_text));
        titleTxt.setTextColor(titleColor);
    }

    // 设置 editText
    private void initEditView(TypedArray typedArray) {
        editText.setHint(ReplaceRokidUtil.replaceRokid(getContext(), typedArray.getString(R.styleable.MultiEditText_android_hint)));
        int editHintColor = typedArray.getColor(R.styleable.MultiEditText_hintColor,
                getColor(R.color.common_hint_color));
        editText.setHintTextColor(editHintColor);

        editText.setTextColor(typedArray.getColor(R.styleable.MultiEditText_textColor,
                getColor(R.color.common_text_black_color)));

        int leftIconSize = typedArray.getDimensionPixelSize(R.styleable.MultiEditText_textSize, SizeUtils.sp2px(18));
        editText.setTextSize(SizeUtils.px2sp(leftIconSize));
        editText.setMaxLines(typedArray.getInteger(R.styleable.MultiEditText_maxLines, 1));

        inputType = typedArray.getInt(R.styleable.MultiEditText_inputType, 0);

        setInputType(inputType);

        editText.setOnFocusChangeListener((v, hasFocus) -> {
            bottomLine.setBackgroundColor(hasFocus ?
                    getColor(R.color.common_text_black_color) :
                    getColor(R.color.common_btn_unclickable));
            if(mOnTextFocusChangeListener != null) {
                mOnTextFocusChangeListener.onFocusChange(v, hasFocus);
            }

        });
    }

    // 设置 ClearIcon
    private void initClearIconView(TypedArray typedArray) {
        showClearIcon = typedArray.getBoolean(R.styleable.MultiEditText_showClearIcon, false);
        int clearIconColor = typedArray.getColor(R.styleable.MultiEditText_clearIconColor,
                getColor(R.color.common_gray_text));
        clearIcon.setTextColor(clearIconColor);
        clearIcon.setText(R.string.icon_clear);

        clearIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText("");
            }
        });
    }

    // 设置 EyeIcon
    private void initEyeIconView(TypedArray typedArray) {
        showEyeIcon = typedArray.getBoolean(R.styleable.MultiEditText_showPasswordIcon, false);

        final int eyeIconColor = typedArray.getColor(R.styleable.MultiEditText_passwordIconColor,
                getColor(R.color.common_gray_text));
        eyeIcon.setTextColor(eyeIconColor);
        eyeIcon.setText(R.string.icon_psw_eye_close);
        eyeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransformationMethod type = editText.getTransformationMethod();
                if (PasswordTransformationMethod.getInstance().equals(type)) {
                    eyeIcon.setText(R.string.icon_psw_eye);

                    setInputType(INPUT_TYPE_TEXT);

                    callBackEyesChangeListener(true);
                } else {
                    eyeIcon.setText(R.string.icon_psw_eye_close);

                    setInputType(INPUT_TYPE_TEXT_PASSWORD);

                    callBackEyesChangeListener(false);
                }
                Editable text = editText.getText();
                if (!TextUtils.isEmpty(text)) {
                    editText.setSelection(text.length());
                }
                editText.setSelection(editText.getText().toString().length());
            }
        });
    }

    // 设置 bottom Line
    private void initBottomLineView(TypedArray typedArray) {
        boolean isHideLine = typedArray.getBoolean(R.styleable.MultiEditText_hideLine, false);
        bottomLine.setVisibility(isHideLine ? GONE : VISIBLE);
        bottomLine.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.common_btn_unclickable));
    }

    public void setBottomLienColor(@ColorInt int color) {
        if (null != bottomLine) {
            bottomLine.setBackgroundColor(color);
        }
    }

    private int getColor(@ColorRes int id) {
        return ContextCompat.getColor(getContext(), id);
    }

    private EyesIconListener eyesIconListener;

    public interface EyesIconListener {
        void eyesChange(boolean isOpen);
    }

    public void setEyesIconListener(EyesIconListener listener) {
        this.eyesIconListener = listener;
    }

    private void callBackEyesChangeListener(boolean isOpen) {
        if (null != eyesIconListener) {
            eyesIconListener.eyesChange(isOpen);
        }
    }

    public void setClearIconColor(@ColorRes int id) {
        clearIcon.setTextColor(ContextCompat.getColor(getContext(), id));
    }

    public void addTextChangedListener(TextWatcher watcher) {
        editText.addTextChangedListener(watcher);
    }

    public void setFilters(InputFilter[] inputFilter) {
        editText.setFilters(inputFilter);
    }

    public View.OnFocusChangeListener getOnTextFocusChangeListener() {
        return mOnTextFocusChangeListener;
    }

    public void setOnTextFocusChangeListener(View.OnFocusChangeListener onTextFocusChangeListener) {
        mOnTextFocusChangeListener = onTextFocusChangeListener;
    }
}
