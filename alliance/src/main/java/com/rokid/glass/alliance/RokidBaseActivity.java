package com.rokid.glass.alliance;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.hardware.display.DisplayManager;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.jiangdg.usbcamera.UVCCameraHelper;
import com.rokid.glass.libbase.config.DeployTaskConfig;
import com.rokid.glass.libbase.faceid.FaceConstants;
import com.rokid.glass.libbase.logger.Logger;
import com.rokid.glass.libbase.utils.DefaultSPHelper;
import com.rokid.glass.viewcomponent.glass.SmartRecogPresentation;
import com.rokid.glasssdk.GlassControl;
import com.rokid.glasssdk.GlassEvent;
import com.rokid.glasssdk.OnGlassEvent;
import com.serenegiant.usb.CameraDialog;
import com.serenegiant.usb.USBMonitor;
import com.serenegiant.usb.common.AbstractUVCCameraHandler;
import com.serenegiant.usb.widget.CameraViewInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class RokidBaseActivity extends Activity implements CameraDialog.CameraDialogParent, CameraViewInterface.Callback {

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
    private static final String ACTION_USB_PERMISSION = "com.rokid.glass.presentationdemo.MainActivity.USB_PERMISSION";
    private PendingIntent mPermissionIntent;
    public static final int KEYCODE_BACK = 16;
    public static final int KEYCODE_POWER = 4;


    private UVCCameraHelper.OnMyDevConnectListener listener = new UVCCameraHelper.OnMyDevConnectListener() {

        @Override
        public void onAttachDev(UsbDevice device) {
            // request open permission
            Logger.i("USBCamera onAttachDev -------->is called: " + device.getDeviceName());
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
            }
        }

        @Override
        public void onConnectDev(UsbDevice device, boolean isConnected) {
            Logger.i("USBCamera onConnectDev -------->is called: " + device.getDeviceName());
            if (!isConnected) {
                isPreview = false;
            } else {
                isPreview = true;
            }
            new Handler().postDelayed(()->{
                tryRequsetPermission();
            }, 500);
        }

        @Override
        public void onDisConnectDev(UsbDevice device) {
            Logger.i("USBCamera onDisConnectDev -------->is called: " + device.getDeviceName());
        }
    };

    public final DisplayManager.DisplayListener mDisplayListener = new DisplayManager.DisplayListener() {
        @Override
        public void onDisplayAdded(int displayId) {
            Logger.i("onDisplayAdded -------->is called: &&& &&&& displayId =  " + displayId);
            showPresentation();
        }

        @Override
        public void onDisplayChanged(int displayId) {
            Logger.i("onDisplayChanged -------->is called: &&& &&&& displayId =  " + displayId);
            showPresentation();
        }

        @Override
        public void onDisplayRemoved(int displayId) {
            Logger.i("onDisplayRemoved -------->is called: &&& &&&& displayId =  " + displayId);
            showPresentation();
        }
    };
    private UsbManager mUsbManager;
    private AtomicBoolean isLight = new AtomicBoolean(true);
    private final OnGlassEvent mOnGlassEvent = new OnGlassEvent() {
        @Override
        public void OnKeyPress(int keycode, boolean isPress) {

            Logger.i("onKeyPress:", keycode, isPress);
            if (keycode == KEYCODE_BACK) {

                if (!mPresentation.onKeyPress(keycode, isPress)){
                    onGlassBackPress(keycode, isPress);
                }
            } else if (keycode == KEYCODE_POWER) {
                if (null != mGlassCtrl && isPress) {
                    mGlassCtrl.SetBrightness(isLight.get() ? 0 : BRIGHTNESS);
                    isLight.set(!isLight.get());
                }
            }
        }

        @Override
        public void OnTouchPress(int position) {
            Logger.i("onTouchPress:", position);
            mPresentation.onTouchPress(position);
        }

        @Override
        public void OnTochEvent(int event, int value) {
            Logger.i("OnTochEvent:", event, value);
            mPresentation.onTouchEvent(event, value);
        }

        @Override
        public void OnImuUpdate(long timestamp, float[] values) {//IMU事件
            mPresentation.onImuUpdate(timestamp, values);
        }

        @Override
        public void OnPsensorUpdate(boolean status) { //距离传感器
            Logger.i("OnPsensorUpdate: ", status);
            onPSensorEvent(status);
        }

        @Override
        public void OnLsensorUpdate(int lux) {//前置光线传感器
            onLSensorUpdate(lux);
        }
    };

    private void onGlassBackPress(int keycode, boolean isPress) {

    }

    private void onLSensorUpdate(int lux) {//前置光纤传感器 最小值为0
        Logger.i("light sensor value:", lux);
    }

    public static int BRIGHTNESS = 70;

    public abstract int getDefaultBrightness();

    /**
     * @param status true可认为是带上眼镜 唤醒光机 false可认为已摘下眼镜 熄灭光机
     */
    public void onPSensorEvent(boolean status) {
        if (null != mGlassCtrl) {
            mGlassCtrl.SetBrightness(status ? BRIGHTNESS : 0);
            isLight.set(status);
        }
    }

    public void showPresentation() {
        Display[] presentationDisplays = mDisplayManager.getDisplays(DisplayManager.DISPLAY_CATEGORY_PRESENTATION);
        if (presentationDisplays != null && presentationDisplays.length > 0) {
            Display defaultDisplay = presentationDisplays[0];
            if (defaultDisplay.getDisplayId() == curDisplayId) {
                Point point = new Point();
                defaultDisplay.getSize(point);
                Logger.d("showPresentation----->screen width = " + point.x);
                Logger.d("showPresentation----->screen height = " + point.y);
                if (mPresentation == null) {
                    mPresentation = new SmartRecogPresentation(this, defaultDisplay);
                }
                mPresentation.show();
            } else {
                Point point = new Point();
                defaultDisplay.getSize(point);
                Logger.d("showPresentation----->screen width = " + point.x);
                Logger.d("showPresentation----->screen height = " + point.y);
                if (mPresentation != null && mPresentation.isShowing()) {
                    mPresentation.dismiss();
                }
                mPresentation = new SmartRecogPresentation(this, defaultDisplay);
                mPresentation.show();
            }
            curDisplayId = defaultDisplay.getDisplayId();
        } else {
            if (mPresentation != null) {
                mPresentation.dismiss();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresentation != null && mPresentation.isShowing()) {
            mPresentation.dismiss();
        }
        unregisterReceiver(usbReceiver);
    }

    private GlassControl mGlassCtrl;
    private GlassEvent mGlassEvent;
    private final BroadcastReceiver usbReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Logger.i("receive usb permission ", action);
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

                    Logger.e("ReceiveGlassDevice::");
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (device != null && device.getProductId() == getResources().getInteger(R.integer.usb_device_product_id)
                                && device.getVendorId() == getResources().getInteger(R.integer.usb_device_vendor_id)) {
                            mGlassCtrl = new GlassControl(context, device);
                            mGlassEvent = new GlassEvent(context, device);
                            mGlassEvent.SetOnGlassEvent(mOnGlassEvent);
                        }
                    } else {
                        Logger.i("permission denied for device ", device);
                        Toast.makeText(RokidBaseActivity.this, R.string.required_permission_usb_glass_device, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            } else if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
                tryRequsetPermission();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout());
        initReceiver();
        mDisplayManager = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);
        mDisplayManager.registerDisplayListener(mDisplayListener, new Handler());
        mTextureView = getCameraView();
        mUVCCameraView = (CameraViewInterface) mTextureView;
        mUVCCameraView.setCallback(this);
        mCameraHelper = UVCCameraHelper.getInstance();
        mCameraHelper.setDefaultFrameFormat(UVCCameraHelper.FRAME_FORMAT_MJPEG);
        mCameraHelper.initUSBMonitor(this, mUVCCameraView, listener);
        mCameraHelper.registerUSB();
        mCameraHelper.setOnPreviewFrameListener(new AbstractUVCCameraHandler.OnPreViewResultListener() {
            @Override
            public void onPreviewResult(byte[] nv21Yuv) {
                if (mPresentation != null) {
                    mPresentation.onPreviewFrame(nv21Yuv);
                }
            }
        });

        int defaultBrightness = getDefaultBrightness();
        if (0 != defaultBrightness) BRIGHTNESS = defaultBrightness;

        showPresentation();

        if (isVersionM() && !checkAndRequestPermissions()) {
            return;
        }

        requestGlassDevice();
    }

    public void requestGlassDevice(){
        Logger.e("requestGlassDevice::");
        mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        registerReceiver(usbReceiver, filter);
        mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
        tryRequsetPermission();
    }

    private void tryRequsetPermission() {
        HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();
        if (null == deviceList || deviceList.size() == 0) return;

        Iterator<UsbDevice> iterator = deviceList.values().iterator();
        while (iterator.hasNext()) {
            UsbDevice usbDevice = iterator.next();
            if (usbDevice != null && usbDevice.getProductId() == getResources().getInteger(R.integer.usb_device_product_id)
                    && usbDevice.getVendorId() == getResources().getInteger(R.integer.usb_device_vendor_id)){
                Logger.i("find glass device");
                if (!mUsbManager.hasPermission(usbDevice)) {
                    mUsbManager.requestPermission(usbDevice, mPermissionIntent);
                } else {
                    Logger.i("glass already has permission");

                }
            }
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
            requestGlassDevice();
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
            } else if (intent.getAction().equals(FaceConstants.ACTION_FACE_ID_DB_CHANGE)) {
            } else if (intent.getAction().equals(FaceConstants.ACTION_FACE_DEPLOY_INFO_CHANGED)) {
                boolean newFriendSwitch = DefaultSPHelper.getInstance().getBoolean(DeployTaskConfig.KEYS.DB_DEPLOY_NEW_FRIEND_SWITCH, false);
                boolean newFriendAlarm = DefaultSPHelper.getInstance().getBoolean(DeployTaskConfig.KEYS.DB_DEPLOY_NEW_FRIEND_ALARM, false);
                String newFriendDesc = DefaultSPHelper.getInstance().getString(DeployTaskConfig.KEYS.DB_DEPLOY_NEW_FRIEND_DESC, "");
                if (newFriendSwitch) {
                    mPresentation.setNewFriendConfig(newFriendAlarm, newFriendDesc);
                } else {
                    mPresentation.setNewFriendConfig(false, "");
                }
            }
        }
    };

    public abstract int layout();

    public abstract View getCameraView();
}
