package com.rokid.glass.libbase.utils;

import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.rokid.glass.libbase.logger.Logger;


/**
 * Created by wangshuwen on 2017/11/30.
 */

public class SoftKeyBoardUtil {

    public static int DEFAULT_SOFTKEYBOARD_HEIGHT = 787;

    public final static String SOFTKEYBOARD_HEIGHT = "softKeyboardHeight";

    private static int rootViewVisibleHeight;//纪录根视图的显示高度

    static int keybordHeight;

    public static void hideSoftKeyboard(Context context, View view) {
        if (null == context) {
            return;
        }
        if (null == view) {
            return;
        }
        final InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != imm && isKeyBoardShow(view.getRootView())) {
            Logger.d("hideSoftKeyboard is called ");
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    public static void showSoftKeyboard(Context context, final View view) {
        if (null == context) {
            return;
        }
        if (null == view) {
            return;
        }
        final InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != imm && !isKeyBoardShow(view.getRootView())) {
            Logger.d("showSoftKeyboard is called ");
            view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }, 500);
        }
    }


    /**
     * 软键盘是否显示
     *
     * @param rootView
     * @return
     */
    private static boolean isKeyBoardShow(View rootView) {
        final int softKeyboardHeight = 100;
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
        int heightDiff = rootView.getBottom() - r.bottom;
        return heightDiff > softKeyboardHeight * dm.density;
    }



}
