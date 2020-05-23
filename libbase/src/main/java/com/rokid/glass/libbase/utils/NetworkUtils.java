package com.rokid.glass.libbase.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;


import com.rokid.glass.libbase.logger.Logger;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Author: Shper
 * Version: V0.1 2017/8/15
 */
public class NetworkUtils {

    public static boolean isConnect() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                ContextUtil.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
//                BaseLibrary.getInstance().getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null == connectivityManager) {
            Logger.e("The connect manager is null.");
            return false;
        }

        // 华为一些机型 在 获取 ActiveNetworkInfo 时，异常
        NetworkInfo activeNetworkInfo = null;
        try {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (null == activeNetworkInfo || !activeNetworkInfo.isAvailable() || !activeNetworkInfo.isConnected()) {
            Logger.e("The network is not connected.");
            return false;
        }

        if (ConnectivityManager.TYPE_WIFI == activeNetworkInfo.getType() ||
                ConnectivityManager.TYPE_MOBILE == activeNetworkInfo.getType()) {
            Logger.d("The network is connected, The network type : " + activeNetworkInfo.getType());
            return true;
        }

        Logger.d("The network is not connected, The network type : " + activeNetworkInfo.getType());
        return false;
    }

    public static String getIPAddress() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                ContextUtil.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null == connectivityManager) {
            Logger.e("The connect manager is null.");
            return "";
        }

        // 华为一些机型 在 获取 ActiveNetworkInfo 时，异常
        NetworkInfo activeNetworkInfo = null;
        try {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (null == activeNetworkInfo || !activeNetworkInfo.isConnected()) {
            Logger.d("The network is not connected.");
            return "";
        }

        if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) { // 当前使用2G/3G/4G网络
            try {
                for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                    NetworkInterface intf = en.nextElement();
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                            return inetAddress.getHostAddress();
                        }
                    }
                }
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }

        if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
            WifiManager wifiManager = (WifiManager) ContextUtil.
                    getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (null == wifiManager) {
                Logger.d("The wifi manager is null.");
                return "";
            }

            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (null == wifiInfo) {
                Logger.d("The wifi info is null.");
                return "";
            }

            return intIP2StringIP(wifiInfo.getIpAddress()); // 得到IPV4地址
        }

        return "";
    }

    /**
     * 将得到的int类型的IP转换为String类型
     */
    private static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }

}
