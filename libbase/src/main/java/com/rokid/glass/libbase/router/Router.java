package com.rokid.glass.libbase.router;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.alibaba.android.arouter.launcher.ARouter;
import com.rokid.glass.libbase.BaseLibrary;
import com.rokid.glass.libbase.R;

/**
 * @date 2019-07-12
 */

public class Router {

    private Router() {
    }

    private static class RouterHolder {
        private static Router instance = new Router();
    }

    public static Router getInstance() {
        return RouterHolder.instance;
    }

    public void debug() {
        ARouter.openLog();
        ARouter.openDebug();
    }

    public void init(final Application application) {
        ARouter.init(application);
    }

    public void inject(final Object thiz) {
        ARouter.getInstance().inject(thiz);
    }

    public void open(final String path) {
        open(path, null);
    }

    public void open(final String path, final Bundle params) {
        if (null == params) {
            ARouter.getInstance()
                    .build(path)
                    .withTransition(R.anim.animate_shrink_enter, R.anim.animate_shrink_exit)
                    .navigation(BaseLibrary.getInstance().getTopActivity());
        } else {
            ARouter.getInstance()
                    .build(path)
                    .with(params)
                    .withTransition(R.anim.animate_shrink_enter, R.anim.animate_shrink_exit)
                    .withFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .navigation(BaseLibrary.getInstance().getTopActivity());
        }
    }
}
