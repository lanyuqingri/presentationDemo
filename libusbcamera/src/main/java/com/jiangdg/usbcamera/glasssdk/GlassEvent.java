package com.jiangdg.usbcamera.glasssdk;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.util.Log;

import java.util.Arrays;

/**
 * Author: heshun
 * Date: 2020/9/2 3:55 PM
 * gmail: shunhe1991@gmail.com
 */
public class GlassEvent {

    private final String TAG = "GlassEvent";
    private OnGlassEvent mOnEvent = null;
    private UsbDeviceConnection mConnection = null;
    private UsbManager mUsbManager = null;
    private UsbEndpoint HidReportEndPoint;
    private GlassEvent.HidDataReceiver mHidDataReceiver;
    private int mPacketSize;
    private int mTouchValue;
    private int mPsensorStatus;
    private int mLsensorStatus;
    private long mTimestamp;
    private int mTouchStatus = 0;
    private TouchEvent mTouchEvent = new TouchEvent();
    private byte KeyStatus = 0;

    private int ByteStream2int32(byte[] s) {
        return (s[0] & 255) + ((s[1] & 255) << 8) + ((s[2] & 255) << 16) + ((s[3] & 255) << 24);
    }

    private long ByteStream2int64(byte[] s) {
        return (long)((s[0] & 255) + ((s[1] & 255) << 8) + ((s[2] & 255) << 16) + ((s[3] & 255) << 24) + ((s[4] & 255) << 32) + ((s[5] & 255) << 40) + ((s[6] & 255) << 48) + ((s[7] & 255) << 56));
    }

    private void HidKeyStatus(byte key) {
        if (key != this.KeyStatus) {
            this.mOnEvent.OnKeyPress(this.KeyStatus ^ key, key != 0);
            this.KeyStatus = key;
        }

    }

    private void HidTouchStatus(long timestamp, byte position, byte slide) {
        int event = this.mTouchEvent.UpdateStatus(timestamp, position, slide);
        if (event != 0) {
            this.mOnEvent.OnTouchEvent(event, this.mTouchEvent.GetSlideLen());
        }

        if (position != this.mTouchValue) {
            this.mOnEvent.OnTouchPress(position);
            this.mTouchStatus = position;
        }

    }

    private void HidImuStatus(byte[] imu) {
        float[] Q = new float[4];

        for(int i = 0; i < 4; ++i) {
            int temp = this.ByteStream2int32(Arrays.copyOfRange(imu, 4 * i, 4 * i + 4));
            Q[i] = Float.intBitsToFloat(temp & -1);
        }

        this.mOnEvent.OnImuUpdate(this.mTimestamp, Q);
    }

    private void HidPsensorStatus(byte status) {
        if (status != this.mPsensorStatus) {
            this.mOnEvent.OnPsensorUpdate(status == 0);
            this.mPsensorStatus = status;
        }

    }

    private void HidLsensorStatus(int status) {
        if (status != this.mLsensorStatus) {
            this.mOnEvent.OnLsensorUpdate(status);
            this.mLsensorStatus = status;
        }

    }

    private void HidProtocol(byte[] report) {
        if (report[0] == 2) {
            this.mTimestamp = this.ByteStream2int64(Arrays.copyOfRange(report, 1, 9));
            this.HidKeyStatus(report[47]);
            this.HidTouchStatus(this.mTimestamp, report[9], report[10]);
            this.HidPsensorStatus(report[51]);
            this.HidLsensorStatus(report[49] & 255 + (report[50] & 255) * 256);
        } else if (report[0] == 3) {
            this.HidImuStatus(Arrays.copyOfRange(report, 21, 37));
        }

    }

    public GlassEvent(Context context, UsbDevice dev) {
        this.mUsbManager = (UsbManager)context.getSystemService("usb");
        UsbInterface intf = dev.getInterface(3);
        this.HidReportEndPoint = intf.getEndpoint(0);
        if (this.HidReportEndPoint.getDirection() != 128) {
            Log.e("GlassEvent", "Error Endpoint Direction");
        }

        this.mPacketSize = this.HidReportEndPoint.getMaxPacketSize();
        this.mConnection = this.mUsbManager.openDevice(dev);
        this.mConnection.claimInterface(intf, true);
        this.mHidDataReceiver = new GlassEvent.HidDataReceiver();
        this.mHidDataReceiver.start();
    }

    public boolean SetOnGlassEvent(OnGlassEvent onEvent) {
        this.mOnEvent = onEvent;
        return true;
    }

    private class HidDataReceiver extends Thread {
        private volatile boolean isStopped = false;

        public HidDataReceiver() {
        }

        public void run() {
            try {
                if (GlassEvent.this.mConnection != null && GlassEvent.this.HidReportEndPoint != null) {
                    while(!this.isStopped) {
                        byte[] buffer = new byte[GlassEvent.this.mPacketSize];
                        int status = GlassEvent.this.mConnection.bulkTransfer(GlassEvent.this.HidReportEndPoint, buffer, GlassEvent.this.mPacketSize, 100);
                        if (status > 0 && GlassEvent.this.mOnEvent != null) {
                            GlassEvent.this.HidProtocol(buffer);
                        }
                    }
                }
            } catch (Exception var3) {
                Log.e("GlassEvent", "Error in receive thread", var3);
            }

        }

        public void stopThis() {
            this.isStopped = true;
        }
    }

}
