package com.rokid.glass.libbase.utils;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.TypefaceSpan;

import androidx.annotation.StringRes;

import com.rokid.glass.libbase.BaseLibrary;


/**
 * Created by wangshuwen on 2017/7/4.
 */

public class SpanUtil {

//    /**
//     * 生成带中文上下引号的字符串
//     * getString(R.string.icon_closequote)
//     *
//     * @param content
//     */
//    public static SpannableStringBuilder genStringWithQuote(String content, @ColorInt int color) {
//        SpannableStringBuilder spannable = new SpannableStringBuilder(getString(R.string.icon_backquote)
//                + content
//                + getString(R.string.icon_closequote));
//        spannable.setSpan(new ForegroundColorSpan(color), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        spannable.setSpan(new ForegroundColorSpan(color), spannable.length() - 1, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        return spannable;
//    }
//
//    /**
//     * 生成带选择的 spannable
//     */
//    public static SpannableStringBuilder genStringWithSwitch(String content, @ColorInt int color) {
//        SpannableStringBuilder spannable = new SpannableStringBuilder(getString(R.string.icon_switch) + "    " + content);
//        spannable.setSpan(new ForegroundColorSpan(color), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        return spannable;
//    }
//
//    /**
//     * action sheet 带后面红点的string
//     *
//     * @param text
//     * @param color
//     * @return
//     */
//    public static SpannableStringBuilder genStringWithBehindDot(String text, @ColorInt int color) {
//        SpannableStringBuilder spannable = new SpannableStringBuilder("　　" + text + "　" + getString(R.string.icon_dot_big));
//        spannable.setSpan(new ForegroundColorSpan(color), spannable.length() - 1, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        return spannable;
//    }
//
//
//    /**
//     * action sheet 带前面蓝点的string
//     *
//     * @param text
//     * @param color
//     * @return
//     */
//    public static SpannableStringBuilder genStringWithFrontDot(String text, @ColorInt int color) {
//        SpannableStringBuilder spannable = new SpannableStringBuilder(getString(R.string.icon_dot_left) + text);
//        spannable.setSpan(new ForegroundColorSpan(color), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        return spannable;
//    }
//
//    public static SpannableStringBuilder genStringWithNextIcon(String text, int spSize) {
//        SpannableStringBuilder spannable = new SpannableStringBuilder(text + getString(R.string.icon_next));
//        spannable.setSpan(new AbsoluteSizeSpan(SizeUtils.sp2px(spSize)), spannable.length() - 1, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        return spannable;
//    }
//
//    public static SpannableStringBuilder genStringWithLink(String text, int spSize) {
//        SpannableStringBuilder spannable = new SpannableStringBuilder(text + getString(R.string.icon_link));
//        spannable.setSpan(new AbsoluteSizeSpan(SizeUtils.sp2px(spSize)), spannable.length() - 1, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        return spannable;
//    }

    private static String getString(@StringRes int id) {
        return BaseLibrary.getInstance().getContext().getResources().getString(id);
    }

    public static SpannableStringBuilder genStringWithNormalFont(CharSequence content, int start, int length) {
        SpannableStringBuilder spannable = new SpannableStringBuilder(content);
        spannable.setSpan(
                new TypefaceSpan("sans-serif"),
                start,
                start + length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }
}
