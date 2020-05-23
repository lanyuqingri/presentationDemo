package com.rokid.glass.libbase.utils;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @date 2019/4/15
 */

public class PropertiesUtils {
    public static Properties getProperties(Context context, final String assetsName) {
        Properties properties = new Properties();
        try {
            InputStream in = context.getAssets().open(assetsName);
            properties.load(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}

