package com.rokid.glass.presentationdemo;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.rokid.facelib.RokidFace;
import com.rokid.glass.libbase.BaseLibrary;
import com.rokid.glass.libbase.IModuleInit;
import com.rokid.glass.libbase.config.GalleryConfig;
import com.rokid.glass.libbase.faceid.FaceConstants;
import com.rokid.glass.libbase.faceid.FaceDataManager;
import com.rokid.glass.libbase.faceid.FaceIdManager;
import com.rokid.glass.libbase.faceid.FaceRecogRecordManager;
import com.rokid.glass.libbase.logger.Logger;
import com.rokid.glass.libbase.plate.PlateManager;
import com.rokid.glass.libbase.router.Router;
import com.rokid.glass.libbase.utils.FileUtils;
import com.rokid.glass.libbase.utils.PropertiesUtils;
import com.rokid.glass.libbase.utils.ThreadPoolHelper;
import com.rokid.glass.lpr_sdk.RokidLPR;
import com.rokid.glass.presentationdemo.glass.CSVFileUtil;

import java.io.File;
import java.util.Properties;

public class PresentationApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initBaseLibrary();
        initLPRSDK();
        initFaceSDK();
        initDir();
        initFaceManager();
        loadModules();
        initRouter();
        initDb();
        preloadSomeData();
        initEventBus();

        fixDisplay();
    }

    private void fixDisplay() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                realFix(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    private void realFix(Activity activity) {
        final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        final float targetDensity = displayMetrics.widthPixels / 360;
        final int targetDensityDpi = (int) (160 * targetDensity);

        displayMetrics.density = displayMetrics.scaledDensity = targetDensity;
        displayMetrics.densityDpi = targetDensityDpi;

        final DisplayMetrics activityDisplayMetrics = activity.getResources().getDisplayMetrics();
        activityDisplayMetrics.density = activityDisplayMetrics.scaledDensity = targetDensity;
        activityDisplayMetrics.densityDpi = targetDensityDpi;


    }


    private void initLPRSDK() {
        RokidLPR.Init(this, false);
    }

    private void initFaceSDK() {
        RokidFace.Init(this, false);
    }

    private void initDir() {
        ThreadPoolHelper.getInstance().threadExecute(() -> {
            FileUtils.createFolders(GalleryConfig.DIR_RECOG_CROP);
            FileUtils.createFolders(GalleryConfig.DIR_DEPLOY_CROP);
            FileUtils.createFolders(GalleryConfig.DIR_PLATE_CROP);
        });
    }

    private void initBaseLibrary() {
        BaseLibrary.initialize(this);
    }

    private void initRouter() {
        Router.getInstance().init(this);
    }

    private void initDb() {
        FaceDataManager.getInstance().init(this);
        FaceRecogRecordManager.getInstance().init(this);
        PlateManager.getInstance().init(this);
    }

    private void loadModules() {
        Properties properties = PropertiesUtils.getProperties(this, "modules.properties");
        if (null == properties) {
            return;
        }
        String modulesStr = properties.getProperty("modules");
        if (!TextUtils.isEmpty(modulesStr)) {
            String[] modules = modulesStr.split(",");
            for (String module : modules) {
                try {
                    IModuleInit moduleInit = (IModuleInit) Class.forName(module).newInstance();
                    if (null == moduleInit) {
                        continue;
                    }
                    moduleInit.moduleInit(this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void initFaceManager() {
        // 初始化人脸和文件管理模块
        ThreadPoolHelper.getInstance().threadSubmit(new Runnable() {
            @Override
            public void run() {
                // 初始FACE SDK，因为比较耗时，放在线程中执行
                Logger.d("##### 开始初始化Face sdk, FaceConstants.FACE_DB_COPY_PATH=" + FaceConstants.FACE_DB_COPY_PATH);
                // 人脸离线特征包拷贝的地址/sdcard/facedb
                File copyFolder = new File(FaceConstants.FACE_DB_COPY_PATH);
                if (!copyFolder.exists()) copyFolder.mkdirs();
                try {
                    // 初始化人脸管理器
                    FaceIdManager.getInstance().init(getApplicationContext());
                    // 初始化文件管理器
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "初始化Face sdk ....失败", Toast.LENGTH_SHORT).show();
                    Logger.e("##### 初始化Face sdk ....失败, e=" + e.getMessage());
                }
                Logger.d("##### 初始化Face sdk ....Done");
            }
        });
    }

    private void preloadSomeData() {
        ThreadPoolHelper.getInstance().threadSubmit(new Runnable() {
            @Override
            public void run() {
                try {
                    // 读取车牌/人脸数据
                    CSVFileUtil.getInstance().initCsv();
                    FaceIdManager.getInstance().checkAssetsOfflineFaceDb();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initEventBus() {
        LiveEventBus
                .config()
                .supportBroadcast(this)
                .lifecycleObserverAlwaysActive(true)
                .autoClear(false);
    }

}
