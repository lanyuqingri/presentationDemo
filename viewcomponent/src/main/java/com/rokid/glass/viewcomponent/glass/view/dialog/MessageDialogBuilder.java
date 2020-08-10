package com.rokid.glass.viewcomponent.glass.view.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rokid.glass.ui.button.GlassButton;
import com.rokid.glass.ui.dialog.GlassDialog;
import com.rokid.glass.ui.dialog.GlassDialogBuilder;
import com.rokid.glass.ui.dialog.GlassDialogListener;
import com.rokid.glass.ui.util.Utils;
import com.rokid.glass.viewcomponent.R;

/**
 * Author: Zack
 * Email:  newzzack@gmail.com
 * Date:   2020.01.07 21:16
 */

public abstract class MessageDialogBuilder<T extends GlassDialogBuilder> extends GlassDialogBuilder<T> {
    protected GlassButton mConfirmBtn;
    protected GlassButton mCancelBtn;
    protected String mConfirmText;
    protected String mCancelText;
    protected LinearLayout mContentLayout;
    protected TextView mContentTextView;
    protected String mContentStr;
    protected ImageView mContentIcon;
    protected int mContentIconResId;

    protected GlassDialogListener mConfirmListener;
    protected GlassDialogListener mCancelListener;

    public MessageDialogBuilder(Context context) {
        super(context);
    }

    @Override
    protected void init() {

    }

    @Override
    protected void onCreateContent(Context context, ViewGroup parent, GlassDialog dialog) {
        View view = LayoutInflater.from(context).inflate(layoutId(), parent, false);
//        mTitleTv = view.findViewById(R.id.dialog_title);
        mConfirmBtn = view.findViewById(R.id.dialog_confirm);
        mCancelBtn = view.findViewById(R.id.dialog_cancel);
        mContentLayout = view.findViewById(R.id.dialog_tip);
        mContentTextView = view.findViewById(R.id.dialog_tip_content);
        mContentIcon = view.findViewById(R.id.dialog_tip_img);

//        mTitleTv.setText(mTitle);

        if (!TextUtils.isEmpty(mConfirmText)) {
            mConfirmBtn.setText(mConfirmText);
        }

        if (!TextUtils.isEmpty(mCancelText)) {
            mCancelBtn.setText(mCancelText);
        } else {
            mCancelBtn.setVisibility(View.GONE);
        }

        mConfirmBtn.setOnClickListener(v -> {
            if (null != mConfirmListener) {
                mConfirmListener.onClick(v);
            }
        });

        mCancelBtn.setOnClickListener(v -> {
            dismiss();
            if (null != mCancelListener) {
                mCancelListener.onClick(v);
            }
        });

        if(!TextUtils.isEmpty(mContentStr)) {
            mContentTextView.setText(mContentStr);
        } else {
            mContentLayout.setVisibility(View.GONE);
        }

        if(mContentIconResId == 0) {
            mContentIcon.setVisibility(View.GONE);
        } else {
            mContentIcon.setImageResource(mContentIconResId);
        }

        onAfterCreateView(view);
        parent.addView(view);
    }

    @Override
    protected void onAfter(Context context, ViewGroup parent, GlassDialog dialog) {
        super.onAfter(context, parent, dialog);
        ViewGroup.LayoutParams params = parent.getLayoutParams();
        params.height = Utils.getScreenHeight(context);
        parent.setLayoutParams(params);
    }

    public T setConfirmText(String confirmText) {
        this.mConfirmText = confirmText;
        return (T) this;
    }

    public T setCancelText(String cancelText) {
        this.mCancelText = cancelText;
        return (T) this;
    }


    public T setConfirmListener(GlassDialogListener confirmListener) {
        this.mConfirmListener = confirmListener;
        return (T) this;
    }

    public T setCancelListener(GlassDialogListener cancelListener) {
        this.mCancelListener = cancelListener;
        return (T) this;
    }

    //dynamic content
    public void dynamicCancelText(final String cancelText) {
        if (mGlassDialog.isShowing()) {
            mCancelBtn.setText(cancelText);
        }
    }

    public T setContent(String contentStr) {
        mContentStr = contentStr;
        return (T) this;
    }

    public T setContentIconResId(int contentIconResId) {
        mContentIconResId = contentIconResId;
        return (T) this;
    }

    public abstract int layoutId();

    public abstract void onAfterCreateView(final View view);
}