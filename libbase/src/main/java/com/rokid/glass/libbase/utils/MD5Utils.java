package com.rokid.glass.libbase.utils;

import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Author: Zack
 * Email:  newzzack@gmail.com
 * Date:   2020.01.08 11:54
 */
public class MD5Utils {


    public static String getFileMD5(File file) {
        if(file == null || !file.exists() ) return null;
        MessageDigest md = null;
        FileInputStream fis = null;
        byte[] buffer = new byte[1024];
        int len;

        try {
            md = MessageDigest.getInstance("MD5");
            fis = new FileInputStream(file);

            while((len = fis.read(buffer, 0, 1024)) != -1) {
                md.update(buffer, 0, len);
            }
            fis.close();
        }  catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
            return null;
        }

        BigInteger bigInteger = new BigInteger(1, md.digest());
        return bigInteger.toString(16);
    }

    public static String getFileMD5(String filePath) {
        if(TextUtils.isEmpty(filePath)) return null;

        File file = new File(filePath);

        return getFileMD5(file);
    }

    public static String getMD5String(String str) {
        byte[] bytes = encryptMD5(str);
        return byte2hex(bytes);
    }

    private static byte[] encryptMD5(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return md.digest(data.getBytes("UTF-8"));

        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }

        return new byte[0];
    }

    private static String byte2hex(byte[] bytes) {
        StringBuilder sign = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(b & 0xFF);
            if (hex.length() == 1) {
                sign.append("0");
            }
            sign.append(hex.toUpperCase());
        }
        return sign.toString();
    }

}
