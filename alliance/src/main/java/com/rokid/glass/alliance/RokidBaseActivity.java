package com.rokid.glass.alliance;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.display.DisplayManager;
import android.hardware.usb.UsbDevice;
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
import com.serenegiant.usb.CameraDialog;
import com.serenegiant.usb.USBMonitor;
import com.serenegiant.usb.common.AbstractUVCCameraHandler;
import com.serenegiant.usb.widget.CameraViewInterface;

import java.util.ArrayList;
import java.util.List;

public abstract class RokidBaseActivity extends Activity implements CameraDialog.CameraDialogParent, CameraViewInterface.Callback{

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
        }

        @Override
        public void onDisConnectDev(UsbDevice device) {
            Logger.i("USBCamera onDisConnectDev -------->is called: " + device.getDeviceName());
        }
    };

    public final DisplayManager.DisplayListener mDisplayListener = new DisplayManager.DisplayListener() {
        @Override
        public void onDisplayAdded(int displayId) {
            Logger.i("onDisplayAdded -------->is called: &&& &&&& displayId =  "  + displayId);
            showPresentation();
        }

        @Override
        public void onDisplayChanged(int displayId) {
            Logger.i("onDisplayChanged -------->is called: &&& &&&& displayId =  "  + displayId);
            showPresentation();
        }

        @Override
        public void onDisplayRemoved(int displayId) {
            Logger.i("onDisplayRemoved -------->is called: &&& &&&& displayId =  "  + displayId);
            showPresentation();
        }
    };

    public void showPresentation(){
        Display[] presentationDisplays = mDisplayManager.getDisplays(DisplayManager.DISPLAY_CATEGORY_PRESENTATION);
        if(presentationDisplays != null && presentationDisplays.length > 0){
            Display defaultDisplay = presentationDisplays[0];
            if(defaultDisplay.getDisplayId() == curDisplayId ){
                if(mPresentation == null){
                    mPresentation = new SmartRecogPresentation(this,defaultDisplay);
                }
                mPresentation.show();
            } else {
                if(mPresentation != null && mPresentation.isShowing()){
                    mPresentation.dismiss();
                }
                mPresentation = new SmartRecogPresentation(this,defaultDisplay);
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
    protected void onDestroy() {
        super.onDestroy();
        if(mPresentation != null && mPresentation.isShowing()){
            mPresentation.dismiss();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout());
        initReceiver();
        mDisplayManager = (DisplayManager)getSystemService(Context.DISPLAY_SERVICE);
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
                if(mPresentation != null){
                    mPresentation.onPreviewFrame(nv21Yuv);
                }
            }
        });
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

    public abstract int layout();

    public abstract View getCameraView();
}