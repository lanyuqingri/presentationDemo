package com.rokid.glass.libbase.utils;

import android.text.TextUtils;
import android.widget.EditText;

import com.rokid.glass.libbase.view.MultiEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wangshuwen on 2017/6/12.
 */
public class EditTextUtils {


    public static final int AMERICA_PHONE_LENGTH = 10;

    private static final String PHONE_NUM_REGEX = "^1[0-9]{10}$";

    private static final String CHINESE_REGEX = "^[\\u4e00-\\u9fa5]+$";


    /**
     * 验证手机号码
     *
     * @param phoneNumber 手机号码
     * @return boolean
     */
    public static boolean checkPhoneNumber(boolean isChina, String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber)) {
            return false;
        }

        if (!isChina) {
            return true;
        }

        Pattern pattern = Pattern.compile(PHONE_NUM_REGEX);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }


    public static boolean checkChinese(String text) {
        if (TextUtils.isEmpty(text)) {
            return false;
        }
        Pattern pattern = Pattern.compile(CHINESE_REGEX);
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }

    /**
     * 过滤字符串里面的空格
     *
     * @param phoneNum
     * @return
     */
    public static String filterSpace(String phoneNum) {
        if (TextUtils.isEmpty(phoneNum)) {
            return phoneNum;
        }
        String filterSpaceStr = phoneNum.replaceAll(" ", "");
        return filterSpaceStr;
    }

    /**
     * 将输入框按照文字自动空格
     *
     * @param s
     * @param start
     * @param before
     * @param editText
     */
    public static void sCodeTextChange(CharSequence s, int start, int before, EditText editText) {
        if (editText == null) {
            return;
        }
        if (TextUtils.isEmpty(s)) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (i != 1 && i != 3 && i != 5 && s.charAt(i) == ' ') {
                continue;
            } else {
                sb.append(s.charAt(i));
                if ((sb.length() == 2 || sb.length() == 4 || sb.length() == 6) && sb.charAt(sb.length() - 1) != ' ') {
                    sb.insert(sb.length() - 1, ' ');
                }
            }
        }
        if (!sb.toString().equals(s.toString())) {
            int index = start + 1;
            if (sb.charAt(start) == ' ') {
                if (before == 0) {
                    index++;
                } else {
                    index--;
                }
            } else {
                if (before == 1) {
                    index--;
                }
            }
            editText.setText(sb.toString());
            editText.setSelection(index);
        }
    }


    /**
     * 在输入框格式化手机号码
     *
     * @param s
     * @param start
     * @param before
     */
    public static void phoneNumTextChange(CharSequence s, int start, int before, MultiEditText editText) {
        if (editText == null) {
            return;
        }
        if (TextUtils.isEmpty(s)) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (i != 3 && i != 8 && s.charAt(i) == ' ') {
                continue;
            } else {
                sb.append(s.charAt(i));
                if ((sb.length() == 4 || sb.length() == 9) && sb.charAt(sb.length() - 1) != ' ') {
                    sb.insert(sb.length() - 1, ' ');
                }
            }
        }
        if (!sb.toString().equals(s.toString())) {
            int index = start + 1;

            if (start >= sb.length()) {
                return;
            }
            if (sb.charAt(start) == ' ') {
                if (before == 0) {
                    index++;
                } else {
                    index--;
                }
            } else {
                if (before == 1) {
                    index--;
                }
            }

            editText.setText(sb.toString());
            if (index > editText.getText().length()) {
                index = editText.getText().length();
            }
            editText.setSelection(index);
        }
    }

    public static String formatPhoneNum(String phoneNum) {
        if (TextUtils.isEmpty(phoneNum)) {
            return "";
        }
        StringBuilder stringBuffer = new StringBuilder(phoneNum);
        for (int i = 0; i < phoneNum.length(); i++) {
            if (i == 3 || i == 8) {
                stringBuffer.insert(i, " ");
            }
        }
        return stringBuffer.toString();
    }
}
