package com.jiangdg.usbcamera.glasssdk;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbManager;
import android.util.Log;

/**
 * Author: heshun
 * Date: 2020/9/2 3:55 PM
 * gmail: shunhe1991@gmail.com
 */
public class GlassControl {

    private UsbDevice mUsbDevice;
    private UsbEndpoint endpoint;
    private UsbDeviceConnection mConnection;
    private Context mContext;
    private UsbManager mUsbManager;
    private final String TAG = "GlassControl";
    private final byte LIBUSB_REQUEST_TYPE_STANDARD = 0;
    private final byte LIBUSB_REQUEST_TYPE_CLASS = 32;
    private final byte LIBUSB_REQUEST_TYPE_VENDOR = 64;
    private final byte LIBUSB_REQUEST_TYPE_RESERVED = 96;
    private final byte LIBUSB_RECIPIENT_DEVICE = 0;
    private final byte LIBUSB_RECIPIENT_INTERFACE = 1;
    private final byte LIBUSB_RECIPIENT_ENDPOINT = 2;
    private final byte LIBUSB_RECIPIENT_OTHER = 3;
    private final byte REQUEST_GET_VERSION = -128;
    private final byte REQUEST_GET_DATA = -127;
    private final byte REQUEST_SET_DATA = 1;
    private final byte REQUEST_GET_CONTROL = -126;
    private final byte REQUEST_SET_CONTROL = 2;
    private final byte REQUEST_REBOOT = 3;
    private final byte REQUEST_POWER = 4;
    private final byte SYSTEM_INDEX = 0;
    private final byte DISPLAY_INDEX = 1;
    private final byte BACKLIGHT_INDEX = 2;
    private final byte CAMERA_INDEX = 3;
    private final byte LSENSOR_INDEX = 4;
    private final byte PSENSOR_INDEX = 6;
    private final byte IMU_INDEX = 7;
    private final byte KEY_INDEX = 8;
    private final byte TOUCH_INDEX = 9;
    private final byte SPEAKER_INDEX = 10;
    private final byte LT7911D_INDEX = 11;
    private final byte I2C_INDEX = 12;
    private final byte ENV_VERSION_INDEX = 0;
    private final byte ENV_SN_INDEX = 1;
    private final byte ENV_PCBA_INDEX = 2;
    private final byte ENV_TYPE_ID_INDEX = 3;
    private final byte ENV_IMUC_INDEX = 4;
    private final byte ENV_PCAL_INDEX = 5;
    private final byte ENV_LCAL_INDEX = 6;
    private final byte ENV_OPID_INDEX = 7;
    private final byte ENV_MAX_INDEX = -128;
    private final byte RESET_NORMAL = 0;
    private final byte RESET_DFUMODE = -79;
    private final byte RESET_RECOVERY = -78;
    private final byte RESET_FACTORY = -77;

    private boolean GlassRequest(int request, int index, int value, byte[] buf, int len) {
        int bmRequest = 64;
        int ret = -1;
        if ((request & 128) == 128) {
            bmRequest |= 128;
        }

        Log.i("GlassControl", "request type:" + bmRequest + "request:" + request + " len:" + len);

        for(int retry = 0; retry < 3; ++retry) {
            ret = this.mConnection.controlTransfer(bmRequest, request, value, index, buf, len, 1000);
            if (ret == len) {
                break;
            }

            Log.e("GlassControl", "Control Transfer:" + ret);
        }

        return ret > 0;
    }

    boolean GlassReboot(int mode) {
        return this.GlassRequest(3, 0, mode, (byte[])null, 0);
    }

    boolean GlassRead(int module, int offset, byte[] buf, int len) {
        return this.GlassRequest(-127, module, offset, buf, len);
    }

    boolean GlassWrite(int module, int offset, byte[] buf, int len) {
        return this.GlassRequest(1, module, offset, buf, len);
    }

    boolean GlassSetControl(int module, int cmd) {
        byte[] buf = new byte[64];
        return this.GlassRequest(2, module, cmd, buf, 1);
    }

    int GlassGetControl(int module, int cmd) {
        byte[] temp = new byte[1];
        this.GlassRequest(-126, module, cmd, temp, 1);
        return temp[0];
    }

    public GlassControl(Context context, UsbDevice dev) {
        this.mUsbManager = (UsbManager)context.getSystemService("usb");
        this.mConnection = this.mUsbManager.openDevice(dev);
    }

    public boolean SetBrightness(int value) {
        return this.GlassSetControl(2, value);
    }

    public int GetBrightness() {
        return this.GlassGetControl(2, 0);
    }

    public String GetSerialNumber() {
        byte[] temp = new byte[64];
        this.GlassRead(0, 256, temp, 64);
        return new String(temp);
    }

    public String GetPCBA() {
        byte[] temp = new byte[64];
        this.GlassRead(0, 512, temp, 64);
        return new String(temp);
    }

    public String GetTypeID() {
        byte[] temp = new byte[64];
        this.GlassRead(0, 768, temp, 64);
        return new String(temp);
    }

    public boolean GetVsyncStatus() {
        byte[] temp = new byte[64];
        this.GlassRead(1, 0, temp, 64);
        return temp[0] == 1;
    }

    public String GetOpticalID() {
        byte[] temp = new byte[64];
        this.GlassRead(0, 1792, temp, 64);
        return new String(temp);
    }

}
