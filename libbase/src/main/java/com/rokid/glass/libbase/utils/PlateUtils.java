package com.rokid.glass.libbase.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlateUtils {

    public static boolean isCarNo(String carNo){
        String allPattern = "(([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z](([0-9]{5}[DF])|([DF]([A-HJ-NP-Z0-9])[0-9]{4})))|([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z][A-HJ-NP-Z0-9]{4}[A-HJ-NP-Z0-9挂学警港澳使领]))";
        Pattern p = Pattern.compile(allPattern);
        Matcher m = p.matcher(carNo);
        if (!m.matches()){
            return false;
        }
        return true;
    }
}
