package com.rokid.glass.presentationdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.display.DisplayManager;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jiangdg.usbcamera.RKGlassDevice;
import com.jiangdg.usbcamera.UVCCameraHelper;
import com.jiangdg.usbcamera.hw.GlassInfo;
import com.jiangdg.usbcamera.hw.GlassSensorEvent;
import com.jiangdg.usbcamera.hw.RKGlassTouchEvent;
import com.jiangdg.usbcamera.hw.listener.KeyEventType;
import com.jiangdg.usbcamera.hw.listener.RKKeyListener;
import com.jiangdg.usbcamera.utils.FileUtils;
import com.rokid.glass.libbase.config.DeployTaskConfig;
import com.rokid.glass.libbase.faceid.FaceConstants;
import com.rokid.glass.libbase.logger.Logger;
import com.rokid.glass.libbase.utils.DefaultSPHelper;
import com.rokid.glass.libbase.utils.RokidSystem;
import com.rokid.glass.presentationdemo.glass.OnResultShowListener;
import com.rokid.glass.presentationdemo.glass.SmartRecogPresentation;
import com.serenegiant.usb.CameraDialog;
import com.serenegiant.usb.USBMonitor;
import com.serenegiant.usb.common.AbstractUVCCameraHandler;
import com.serenegiant.usb.encoder.RecordParams;
import com.serenegiant.usb.widget.CameraViewInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CameraDialog.CameraDialogParent, CameraViewInterface.Callback, OnResultShowListener {
    public static final String TAG = MainActivity.class.getSimpleName();

    private static final String[] REQUIRED_PERMISSION_LIST = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
    };
    private static final int REQUEST_CODE = 1;
    private List<String> mMissPermissions = new ArrayList<>();
    private DisplayManager mDisplayManager;
    private SmartRecogPresentation mPresentation;
    private UVCCameraHelper mCameraHelper;
    private CameraViewInterface mUVCCameraView;
    private boolean isPreview;
    private boolean isRequest;
    public View mTextureView;
    private int curDisplayId = -1;

    private ImageView mFaceImageView;
    private TextView mTvUsbSN;
    private PendingIntent mPermissionIntent;
    private static final String ACTION_USB_PERMISSION = "com.rokid.glass.MainActivity.USB_PERMISSION";
    private final BroadcastReceiver usbReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (device != null) {
                            getUSBHWInfo();
                        }
                    }
                }
            }
        }
    };

    private void getUSBHWInfo() {
        UsbManager mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
        while (deviceIterator.hasNext()) {
            UsbDevice device = deviceIterator.next();
            Logger.d("getUSBHWInfo-------->device.getVendorId = " + device.getVendorId());
            Logger.d("getUSBHWInfo-------->device.getProductId = " + device.getProductId());
            if (device.getVendorId() == Constant.ROKID_USB_VENDOR_ID && device.getProductId() == Constant.ROKID_USB_PRODUCT_ID) {
                if (mUsbManager.hasPermission(device)) {
                    String opticalId = RKGlassDevice.getInstance().getHWOpticalId();
                    registerGlassEvent(device);
                    if (TextUtils.isEmpty(opticalId)) {
                        Logger.d("Glass Optical type is empty should be A");
                    } else {
                        Logger.d("Glass Optical type is " + opticalId);
                    }
                    if (TextUtils.isEmpty(opticalId) || opticalId.startsWith("A")) {
                        RokidSystem.setHW_OPTICAL_TYPE(RokidSystem.HW_OPTICAL_A);
                    } else if (opticalId.startsWith("B")) {
                        RokidSystem.setHW_OPTICAL_TYPE(RokidSystem.HW_OPTICAL_B);
                    } else if (opticalId.startsWith("C")) {
                        RokidSystem.setHW_OPTICAL_TYPE(RokidSystem.HW_OPTICAL_C);
                    } else {
                        RokidSystem.setHW_OPTICAL_TYPE(RokidSystem.HW_OPTICAL_A);
                    }

                } else {
                    mUsbManager.requestPermission(device, mPermissionIntent);
                }
                return;
            }
        }
    }

    private void showCurrUsbDeviceSN(UsbDevice device){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTvUsbSN.setText(String.format(getString(R.string.s_current_usb_device), device.getSerialNumber()));
            }
        });
    }
    private UVCCameraHelper.OnMyDevConnectListener listener = new UVCCameraHelper.OnMyDevConnectListener() {

        @Override
        public void onAttachDev(UsbDevice device) {
            // request open permission
            Logger.i("USBCamera onAttachDev -------->is called: " + device.getDeviceName());
            if(device.getVendorId() == Constant.ROKID_USB_VENDOR_ID && device.getProductId() == Constant.ROKID_USB_PRODUCT_ID){
                showCurrUsbDeviceSN(device);
            }
            if (!isRequest) {
                isRequest = true;
                if (mCameraHelper != null) {
                    mCameraHelper.requestPermission(0);
                }
            }
        }

        @Override
        public void onDettachDev(UsbDevice device) {
            Logger.i("USBCamera onDettachDev -------->is called: " + device.getDeviceName());
            // close camera
            if (isRequest) {
                isRequest = false;
                mCameraHelper.closeCamera();
                showShortMsg(device.getDeviceName() + " is out");
            }
        }

        @Override
        public void onConnectDev(UsbDevice device, boolean isConnected) {
            Logger.i("USBCamera onConnectDev -------->is called: " + device.getDeviceName());
            if (!isConnected) {
                showShortMsg("fail to connect,please check resolution params");
                isPreview = false;
            } else {
                getUSBHWInfo();

                isPreview = true;
                showShortMsg("connecting");



            }
            startMediaRecord(UVCCameraHelper.ROOT_PATH +"/videos/" + System.currentTimeMillis());
        }

        @Override
        public void onDisConnectDev(UsbDevice device) {
            Logger.i("USBCamera onDisConnectDev -------->is called: " + device.getDeviceName());
            showShortMsg("disconnecting");
            stopMediaRecord();
        }
    };

    private void registerGlassEvent(UsbDevice device){

        GlassInfo glassInfo = RKGlassDevice.getInstance().init(this, device);
        RKGlassDevice.getInstance().setGlassSensorEvent(new GlassSensorEvent() {

            /**
             * 距离传感器你
             * @param status  true可认为是带上眼镜 唤醒光机 false可认为已摘下眼镜 熄灭光机
             */
            @Override
            public void onPSensorUpdate(final boolean status) {
                Log.i(TAG, "onPSensorUpdate:" + status);
//                                RKGlassDevice.getInstance().setBrightness(status ? 100 : 0);//可以根据距离传感器来判断眼镜是否处于佩戴状态，来调整光机的亮度节能

//                                mBinding.tvPsensor.post(() -> mBinding.tvPsensor.setText("距离传感器状态: " + status));
            }


            /**
             * 前置光线传感器
             * @param lux 最小值为0
             */
            @Override
            public void onLSensorUpdate(final int lux) {
//                                mBinding.tvLsensor.post(() -> mBinding.tvLsensor.setText("光线传感器: " + lux));
                Log.i(TAG, "onLSensorUpdate" + lux);
            }
        });
        RKGlassDevice.getInstance().setRkKeyListener(new RKKeyListener() {

            /**
             * 电源键键事件
             * @param eventType {@link KeyEventType}
             */
            @Override
            public void onPowerKeyEvent(final int eventType) {
                Log.i(TAG, "onPowerKeyEvent: "+ eventToString(eventType));
//                                mBinding.tvPower.post(() -> mBinding.tvPower.setText("电源键事件: " + eventToString(eventType)));
            }

            /**
             * 回退键事件
             * @param eventType {@link KeyEventType}
             */
            @Override
            public void onBackKeyEvent(final int eventType) {

                Log.i(TAG, "onBackKeyEvent: "+ eventToString(eventType));
                //mBinding.tvBack.post(() -> mBinding.tvBack.setText("回退键事件: " + eventToString(eventType)));
            }

            /**
             * 触控板事件
             * @param eventType {@link RKGlassTouchEvent}
             */
            @Override
            public void onTouchKeyEvent(final int eventType) {
                Log.i(TAG, "onTouchKeyEvent: "+ eventToString(eventType));

                //mBinding.tvTouch.post(() -> mBinding.tvTouch.setText("TouchBar事件: " + eventToString(eventType)));
            }

            /**
             * 触控板向后滑动
             */
            @Override
            public void onTouchSlideBack() {
                Log.i(TAG, "onTouchSlideBack");

                //mBinding.tvSlide.post(() -> mBinding.tvSlide.setText("向后滑动"));
            }

            /**
             * 触控板向前滑动
             */
            @Override
            public void onTouchSlideForward() {
                Log.i(TAG, "onTouchSlideForward");

                //mBinding.tvSlide.post(() -> mBinding.tvSlide.setText("向前滑动"));
            }
        });
    }

    public String eventToString(int eventType) {
        if (eventType == KeyEventType.SINGLE_CLICK) {
            return "单击";
        } else if (eventType == KeyEventType.DOUBLE_CLICK) {
            return "双击";
        } else {
            return "长按";
        }
    }

    private final DisplayManager.DisplayListener mDisplayListener = new DisplayManager.DisplayListener() {
        @Override
        public void onDisplayAdded(int displayId) {
            showPresentation();
        }

        @Override
        public void onDisplayChanged(int displayId) {
            showPresentation();
        }

        @Override
        public void onDisplayRemoved(int displayId) {
            showPresentation();
        }
    };

    private void showShortMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void showPresentation(){
        Display[] presentationDisplays = mDisplayManager.getDisplays(DisplayManager.DISPLAY_CATEGORY_PRESENTATION);
        if(presentationDisplays != null && presentationDisplays.length > 0){
            Display defaultDisplay = presentationDisplays[0];
            if(defaultDisplay.getDisplayId() == curDisplayId ){
                if(mPresentation == null){
                    mPresentation = new SmartRecogPresentation(this,defaultDisplay);
                }
                mPresentation.setOnResultShowListener(this);
                mPresentation.show();
            } else {
                if(mPresentation != null && mPresentation.isShowing()){
                    mPresentation.dismiss();
                }
                mPresentation = new SmartRecogPresentation(this,defaultDisplay);
                mPresentation.setOnResultShowListener(this);
                mPresentation.show();
            }
            curDisplayId = defaultDisplay.getDisplayId();
        } else {
            if(mPresentation != null){
                mPresentation.dismiss();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initReceiver();
        mDisplayManager = (DisplayManager)getSystemService(Context.DISPLAY_SERVICE);
        mDisplayManager.registerDisplayListener(mDisplayListener, new Handler());
        mTextureView = findViewById(R.id.camera_view);
        mFaceImageView = findViewById(R.id.iv_crop_face);
        mTvUsbSN = findViewById(R.id.tv_usb_sn);
        mTvUsbSN.setText(String.format(getString(R.string.s_current_usb_device), getString(R.string.s_usb_device_offline)));
        mUVCCameraView = (CameraViewInterface) mTextureView;
        mUVCCameraView.setCallback(this);
        mCameraHelper = UVCCameraHelper.getInstance();
        mCameraHelper.setDefaultFrameFormat(UVCCameraHelper.FRAME_FORMAT_MJPEG);
        mCameraHelper.initUSBMonitor(this, mUVCCameraView, listener);
        mCameraHelper.registerUSB();
        mCameraHelper.setOnPreviewFrameListener(new AbstractUVCCameraHandler.OnPreViewResultListener() {
            @Override
            public void onPreviewResult(byte[] nv21Yuv) {
//                Logger.d( "onPreviewResult: "+nv21Yuv.length);
                if(mPresentation != null){
                    mPresentation.onPreviewFrame(nv21Yuv);
                }
            }
        });
        IntentFilter intentFilter = new IntentFilter(ACTION_USB_PERMISSION);
        registerReceiver(usbReceiver, intentFilter);
        mPermissionIntent = PendingIntent.getBroadcast(this,
                0, new Intent(ACTION_USB_PERMISSION), 0);
        if (isVersionM() && !checkAndRequestPermissions()) {
            return;
        }
    }

    private boolean isVersionM() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    private boolean checkAndRequestPermissions() {
        mMissPermissions.clear();
        for (String permission : REQUIRED_PERMISSION_LIST) {
            int result = ContextCompat.checkSelfPermission(this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                mMissPermissions.add(permission);
            }
        }
        // check permissions has granted
        if (mMissPermissions.isEmpty()) {
            return true;
        } else {
            ActivityCompat.requestPermissions(this,
                    mMissPermissions.toArray(new String[mMissPermissions.size()]),
                    REQUEST_CODE);
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Logger.d("onRequestPermissionsResult-------->requestCode = " + requestCode);
        if (requestCode == REQUEST_CODE) {
            for (int i = grantResults.length - 1; i >= 0; i--) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    mMissPermissions.remove(permissions[i]);
                }
            }
        }
        // Get permissions success or not
        if (mMissPermissions.isEmpty()) {
         } else {
            Toast.makeText(this, "get permissions failed,exiting...", Toast.LENGTH_SHORT).show();
            this.finish();
        }
    }

    @Override
    public USBMonitor getUSBMonitor() {
        Logger.d("getUSBMonitor---->is called");
        return mCameraHelper.getUSBMonitor();
    }

    @Override
    public void onDialogResult(boolean canceled) {
        if (canceled) {
            showShortMsg("取消操作");
        }
    }

    public boolean isCameraOpened() {
        Logger.d("isCameraOpened---->is called");
        return mCameraHelper.isCameraOpened();
    }

    @Override
    public void onSurfaceCreated(CameraViewInterface view, Surface surface) {
        Logger.d("onSurfaceCreated---->is called");
        if (!isPreview && mCameraHelper.isCameraOpened()) {
            mCameraHelper.startPreview(mUVCCameraView);
            isPreview = true;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mCameraHelper.unregisterUSB();
        mCameraHelper.release();
        mCameraHelper = null;
        finish();
    }

    @Override
    public void onSurfaceChanged(CameraViewInterface view, Surface surface, int width, int height) {
        Logger.d("onSurfaceChanged---->is called : width = " + width + " && height = " + height);
    }

    @Override
    public void onSurfaceDestroy(CameraViewInterface view, Surface surface) {
        Logger.d("onSurfaceDestroy---->is called");
        if (isPreview && mCameraHelper.isCameraOpened()) {
            mCameraHelper.stopPreview();
            isPreview = false;
        }
    }

    private void initReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(FaceConstants.ACTION_FACE_ID_UPDATE_START);
        filter.addAction(FaceConstants.ACTION_FACE_ID_UPDATE_DONE);
        filter.addAction(FaceConstants.ACTION_FACE_ID_COPY_DB_DONE);
        filter.addAction(FaceConstants.ACTION_FACE_ID_DB_CHANGE);
        filter.addAction(FaceConstants.ACTION_FACE_DEPLOY_INFO_CHANGED);
        LocalBroadcastManager.getInstance(this).registerReceiver(mFaceReceiver, filter);

    }
    private final BroadcastReceiver mFaceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (null == mPresentation) {
                return;
            }

            if (intent.getAction().equals(FaceConstants.ACTION_FACE_ID_UPDATE_START)) {
            } else if (intent.getAction().equals(FaceConstants.ACTION_FACE_ID_UPDATE_DONE)) {
                mPresentation.reloadSDK();
            } else if (intent.getAction().equals(FaceConstants.ACTION_FACE_ID_COPY_DB_DONE)) {
            }
            else if (intent.getAction().equals(FaceConstants.ACTION_FACE_ID_DB_CHANGE)) {
            } else if(intent.getAction().equals(FaceConstants.ACTION_FACE_DEPLOY_INFO_CHANGED)){
                boolean newFriendSwitch = DefaultSPHelper.getInstance().getBoolean(DeployTaskConfig.KEYS.DB_DEPLOY_NEW_FRIEND_SWITCH,false);
                boolean newFriendAlarm = DefaultSPHelper.getInstance().getBoolean(DeployTaskConfig.KEYS.DB_DEPLOY_NEW_FRIEND_ALARM, false);
                String newFriendDesc = DefaultSPHelper.getInstance().getString(DeployTaskConfig.KEYS.DB_DEPLOY_NEW_FRIEND_DESC,"");
                if(newFriendSwitch){
                    mPresentation.setNewFriendConfig(newFriendAlarm, newFriendDesc);
                } else {
                    mPresentation.setNewFriendConfig(false, "");
                }
            }
        }
    };

    @Override
    public void onResultShow() {

    }

    @Override
    public void onResultHide() {

    }

    @Override
    public void onFaceOnlineRecog(Bitmap bm) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mFaceImageView.setImageBitmap(bm);
            }
        });
    }

    private void stopMediaRecord(){
        FileUtils.releaseFile();
        mCameraHelper.stopPusher();
    }

    private void startMediaRecord(String path){
//        String videoPath = UVCCameraHelper.ROOT_PATH +"/videos/" + System.currentTimeMillis()
//                + UVCCameraHelper.SUFFIX_MP4;

        String videoPath = path;
        RecordParams params = new RecordParams();
        params.setRecordPath(videoPath);
        params.setRecordDuration(0);                        // auto divide saved,default 0 means not divided
        params.setVoiceClose(false);    // is close voice

        params.setSupportOverlay(true); // overlay only support armeabi-v7a & arm64-v8a
        mCameraHelper.startPusher(params, new AbstractUVCCameraHandler.OnEncodeResultListener() {
            @Override
            public void onEncodeResult(byte[] data, int offset, int length, long timestamp, int type) {
                // type = 1,h264 video stream
                if (type == 1) {
                    FileUtils.putFileStream(data, offset, length);
                }
                // type = 0,aac audio stream
                if(type == 0) {

                }
            }

            @Override
            public void onRecordResult(String videoPath) {
                if(TextUtils.isEmpty(videoPath)) {
                    return;
                }
            }
        });
    }
}
