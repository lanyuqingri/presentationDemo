package com.rokid.glass.libbase.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Author: Zack
 * Email:  newzzack@gmail.com
 * Date:   2019.07.31 10:41
 */
public class GzipUtil {

    public static byte[] compress(byte[] originData) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
            gzipOutputStream.write(originData);
            gzipOutputStream.flush();
            gzipOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArrayOutputStream.toByteArray();
    }

    public static byte[] uncompress(byte[] compressData) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(compressData);

        try {
            GZIPInputStream unzip = new GZIPInputStream(byteArrayInputStream);
            byte[] buffer = new byte[256];
            int n;
            while((n = unzip.read(buffer)) > 0) {
                byteArrayOutputStream.write(buffer, 0, n);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArrayOutputStream.toByteArray();
    }

}
