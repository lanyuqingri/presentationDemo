package com.rokid.glass.presentationdemo.glass.view.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rokid.glass.presentationdemo.R;
import com.rokid.glass.ui.dialog.GlassDialog;
import com.rokid.glass.ui.dialog.GlassDialogBuilder;
import com.rokid.glass.ui.util.Utils;

/**
 * Author: Zack
 * Email:  newzzack@gmail.com
 * Date:   2020.01.07 21:16
 */

public class LoadingDialogBuilder extends GlassDialogBuilder {

    public LoadingDialogBuilder(Context context) {
        super(context);
    }

    @Override
    protected void init() {
    }

    @Override
    protected void onCreateContent(Context context, ViewGroup parent, GlassDialog dialog) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_deploy_checking, parent, false);
        parent.addView(view);
    }

    @Override
    protected void onAfter(Context context, ViewGroup parent, GlassDialog dialog) {
        super.onAfter(context, parent, dialog);
        ViewGroup.LayoutParams params = parent.getLayoutParams();
        params.height = Utils.getScreenHeight(context);
        parent.setLayoutParams(params);
    }
}