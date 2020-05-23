package com.rokid.glass.presentationdemo.glass.view.dialog;

/**
 * Author: Zack
 * Email:  newzzack@gmail.com
 * Date:   2020.01.07 21:17
 */

import android.content.Context;
import android.view.View;

import com.rokid.glass.presentationdemo.R;


/**
 * simple content
 */
public class SimpleContentDialogBuilder extends MessageDialogBuilder<SimpleContentDialogBuilder> {

    public SimpleContentDialogBuilder(Context context) {
        super(context);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public int layoutId() {
        return R.layout.dialog_deploy_task;
    }

    @Override
    public void onAfterCreateView(View view) {
        //set content height

    }

}
