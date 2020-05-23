package com.rokid.glass.libbase.utils;

import android.os.Build;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class DeviceInfoUtils {

    public final static String DEVICE_TYPE_ID = "ro.boot.devicetypeid";
    public final static String DEVICE_ID = "ro.serialno";
    public final static String DEVICE_SEED = "ro.boot.rokidseed";
    public final static String DEVICE_OTA_VERSION = "ro.build.version.incremental";
    public final static String DEVICE_MODEL = "ro.product.model";

    /**
     * The default return value of any method in this class when an
     * error occurs or when processing fails (Currently set to -1). Use this to check if
     * the information about the device in question was successfully obtained.
     */
    private static final int DEVICEINFO_UNKNOWN = -1;

    public static String getSystemProperty(String name) {
        String serial = "";
        try {
            Class<?> c =Class.forName("android.os.SystemProperties");
            Method get =c.getMethod("get", String.class);
            serial = (String)get.invoke(c, name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serial;
    }


    /**
     * Reads the number of CPU cores from {@code /sys/devices/system/cpu/}.
     *
     * @return Number of CPU cores in the phone, or DEVICEINFO_UKNOWN = -1 in the event of an error.
     */
    public static int getNumberOfCPUCores() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
            // Gingerbread doesn't support giving a single application access to both cores, but a
            // handful of devices (Atrix 4G and Droid X2 for example) were released with a dual-core
            // chipset and Gingerbread; that can let an app in the background run without impacting
            // the foreground application. But for our purposes, it makes them single core.
            return 1;
        }

        int cores;
        try {
            cores = new File("/sys/devices/system/cpu/").listFiles(CPU_FILTER).length;
        } catch (SecurityException e) {
            cores = DEVICEINFO_UNKNOWN;
        } catch (NullPointerException e) {
            cores = DEVICEINFO_UNKNOWN;
        }

        return cores;
    }

    private static final FileFilter CPU_FILTER = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
            String path = pathname.getName();
            //regex is slow, so checking char by char.
            if (path.startsWith("cpu")) {
                for (int i = 3; i < path.length(); i++) {
                    if (path.charAt(i) < '0' || path.charAt(i) > '9') {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }
    };


    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:",b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "02:00:00:00:00:00";
    }

    /**
     * 判断是否是一代眼镜
     * @return
     */
    public static boolean isGenerationOneGlass() {
        if ("Rokid Glass".equalsIgnoreCase(SystemProperties.get(DEVICE_MODEL))) {
            return true;
        }
        return false;
    }

    /**
     * 是否开启人脸识别功能
     * @return
     */
    public static boolean isEnableFaceRecgnize() {
        return true;
    }
}

