package com.jiangdg.usbcamera;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jiangdg.usbcamera.glasssdk.GlassControl;
import com.jiangdg.usbcamera.glasssdk.GlassEvent;
import com.jiangdg.usbcamera.glasssdk.OnGlassEvent;
import com.jiangdg.usbcamera.hw.GlassConfig;
import com.jiangdg.usbcamera.hw.GlassInfo;
import com.jiangdg.usbcamera.hw.GlassKeyEvent;
import com.jiangdg.usbcamera.hw.GlassSensorEvent;
import com.jiangdg.usbcamera.hw.RKKeyProcessor;
import com.jiangdg.usbcamera.hw.listener.RKKeyListener;

import java.lang.ref.WeakReference;


public class RKGlassDevice {
    private static GlassControl mGlassControl;
    private static GlassKeyEvent mKeyProcessor;
    private static GlassSensorEvent mGlassSensorEvent;
    private static RKKeyListener mRKKeyListener;
    private static GlassConfig mGlassConfig;
    private WeakReference<Context> mContextRef;
    public static final String TAG = RKGlassDevice.class.getSimpleName();

    private final OnGlassEvent mOnGlassEvent = new OnGlassEvent() {
        @Override
        public void OnKeyPress(int keycode, boolean isPress) {
            Log.i(TAG, "onKeyPress" + keycode + isPress);
            if (null != mKeyProcessor) {
                mKeyProcessor.onKeyPress(keycode, isPress);
            }
        }

        @Override
        public void OnTouchPress(int position) {
            Log.i(TAG, "onTouchPress:"+position);
            if (null != mKeyProcessor) {
                mKeyProcessor.onTouchPress(position);
            }
        }

        @Override
        public void OnTouchEvent(int event, int value) {
            Log.i(TAG, "onTouchEvent" + event + value);
            if (null != mKeyProcessor) {
                mKeyProcessor.onTouchEvent(event, value);
            }
        }

        @Override
        public void OnImuUpdate(long timestamp, float[] values) {
//            Logger.i("onImuUpdate:", String.valueOf(timestamp), String.valueOf(values[0])
//                    , String.valueOf(values[1]), String.valueOf(values[2]), String.valueOf(values[3]));
        }

        @Override
        public void OnPsensorUpdate(boolean status) {
            Log.i(TAG, "onPsensorUpdate：" + status);
            if (null != mGlassSensorEvent) {
                mGlassSensorEvent.onPSensorUpdate(status);
            }
        }

        @Override
        public void OnLsensorUpdate(int lux) {
            Log.i(TAG, "onLsensorUpdate"+lux);
            if (null != mGlassSensorEvent) {
                mGlassSensorEvent.onLSensorUpdate(lux);
            }
        }

    };

    private static RKGlassDevice mInstance = RKGlassDeviceBuilder.buildRKGlassDevice().build();


    public static RKGlassDevice getInstance() {
        return mInstance;
    }

    /**
     * 在USB 的request permission成功 后调用
     *
     * @param context ApplicationContext{@link Activity#getApplicationContext()}
     * @param device  {@link UsbDevice}
     */
    public GlassInfo init(Context context, UsbDevice device) {
        if (null == context || null == device)
            throw new RuntimeException("init failed! context or UsbDevice is null");

        UsbManager usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        if (null == usbManager) throw new RuntimeException("can not get UsbManager");

        if (!usbManager.hasPermission(device))
            throw new RuntimeException("device no permission" + device.getDeviceName());

        mGlassControl = new GlassControl(context, device);
        GlassEvent mGlassEvent = new GlassEvent(context, device);
        mGlassEvent.SetOnGlassEvent(mOnGlassEvent);

        mKeyProcessor.init(mGlassConfig, mRKKeyListener);

        return GlassInfo.GlassInfoBuilder.buildGlassInfo()
                .withOpticalId(mGlassControl.GetOpticalID())
                .withPcba(mGlassControl.GetPCBA())
                .withSn(mGlassControl.GetSerialNumber())
                .withTypeId(mGlassControl.GetTypeID())
                .withVSyncStatus(mGlassControl.GetVsyncStatus())
                .build();
    }

    public boolean isVSyncStatus() {
        if (null != mGlassControl) {
            return mGlassControl.GetVsyncStatus();
        }
        return false;
    }



    public void setGlassSensorEvent(GlassSensorEvent glassSensorEvent) {
        mGlassSensorEvent = glassSensorEvent;
    }

    public void setRkKeyListener(RKKeyListener rkKeyListener) {
        mRKKeyListener = rkKeyListener;
        mKeyProcessor.setRKKeyListener(rkKeyListener);
    }

    public void setGlassConfig(GlassConfig glassConfig) {
        mGlassConfig = glassConfig;
        mKeyProcessor.setGlassConfig(glassConfig);
    }

    public String getHWOpticalId() {
        if (mGlassControl != null) {
            return mGlassControl.GetOpticalID();
        }
        return null;
    }

    public @IntRange(from = 0, to = 100)
    int getBrightness() {
        if (null != mGlassControl) {
            return mGlassControl.GetBrightness();
        }
        return 0;
    }

    public boolean setBrightness(@IntRange(from = 0, to = 100) int brightness) {
        if (null != mGlassControl) {
            return mGlassControl.SetBrightness(brightness);
        }
        return false;
    }

    public String getGlassSerialNumber() {
        if (null != mGlassControl) {
            return mGlassControl.GetSerialNumber();
        }
        return null;
    }

    public String getPCBA() {
        if (null != mGlassControl) {
            return mGlassControl.GetPCBA();
        }
        return null;
    }

    public String getTypeId() {
        if (null != mGlassControl) {
            return mGlassControl.GetTypeID();
        }
        return null;
    }

    public void setRKKeyProcessor(GlassKeyEvent glassKeyEvent) {
        mKeyProcessor = glassKeyEvent;
    }


    public void deInit() {
        mKeyProcessor.setRKKeyListener(null);
        mRKKeyListener = null;
        mGlassSensorEvent = null;
    }


    public static final class RKGlassDeviceBuilder {
        private GlassKeyEvent keyProcessor;
        private GlassSensorEvent glassSensorEvent;
        private RKKeyListener rkKeyListener;
        private GlassConfig glassConfig;

        private RKGlassDeviceBuilder() {
        }

        public static RKGlassDeviceBuilder buildRKGlassDevice() {
            return new RKGlassDeviceBuilder();
        }

        public RKGlassDeviceBuilder withKeyProcessor(GlassKeyEvent keyProcessor) {
            this.keyProcessor = keyProcessor;
            return this;
        }

        public RKGlassDeviceBuilder withGlassSensorEvent(GlassSensorEvent glassSensorEvent) {
            this.glassSensorEvent = glassSensorEvent;
            return this;
        }

        public RKGlassDeviceBuilder withRKKeyListener(RKKeyListener rkKeyListener) {
            this.rkKeyListener = rkKeyListener;
            return this;
        }

        public RKGlassDeviceBuilder withGlassConfig(GlassConfig glassConfig) {
            this.glassConfig = glassConfig;
            return this;
        }

        public RKGlassDevice build() {
            if (null == mInstance) {
                mInstance = new RKGlassDevice();
            }
            RKGlassDevice rkGlassDevice =  mInstance;
            if (null == keyProcessor) {
                keyProcessor = RKKeyProcessor.getInstance();
            }

            if (null == glassConfig) {
                glassConfig = GlassConfig.GlassConfigBuilder.buildGlassConfig()
                        .withClickDelayTime(300)
                        .withLongClickTime(500)
                        .build();
            }
            rkGlassDevice.setRKKeyProcessor(keyProcessor);
            rkGlassDevice.setRkKeyListener(rkKeyListener);
            rkGlassDevice.setGlassSensorEvent(glassSensorEvent);
            rkGlassDevice.setGlassConfig(glassConfig);
            this.keyProcessor = null;
            this.glassConfig = null;
            this.glassSensorEvent = null;
            this.rkKeyListener = null;
            return rkGlassDevice;
        }
    }
}
