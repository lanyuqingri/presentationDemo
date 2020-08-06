package com.rokid.glass.alliance;

import android.app.Application;
import android.text.TextUtils;
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
import com.rokid.glass.libbase.utils.FileUtils;
import com.rokid.glass.libbase.utils.PropertiesUtils;
import com.rokid.glass.libbase.utils.ThreadPoolHelper;
import com.rokid.glass.lpr_sdk.RokidLPR;

import java.io.File;
import java.util.Properties;

public class RokidSDK {

    private static RokidSDK instance;
    public static RokidSDK getInstance(){
        if(instance == null){
            instance = new RokidSDK();
        }
        return instance;
    }

    public void initApp(Application application){
        initBaseLibrary(application);
        initLPRSDK(application);
        initFaceSDK(application);
        initDir();
        initFaceManager(application);
        loadModules(application);
        initDb(application);
        initEventBus(application);
    }



    private void initLPRSDK(Application application) {
        RokidLPR.Init(application.getApplicationContext(), false);
    }

    private void initFaceSDK(Application application) {
        RokidFace.Init(application.getApplicationContext(), false);
    }

    private void initDir() {
        ThreadPoolHelper.getInstance().threadExecute(() -> {
            FileUtils.createFolders(GalleryConfig.DIR_RECOG_CROP);
            FileUtils.createFolders(GalleryConfig.DIR_DEPLOY_CROP);
            FileUtils.createFolders(GalleryConfig.DIR_PLATE_CROP);
        });
    }

    private void initBaseLibrary(Application application) {
        BaseLibrary.initialize(application);
    }


    private void initDb(Application application) {
        FaceDataManager.getInstance().init(application);
        FaceRecogRecordManager.getInstance().init(application);
        PlateManager.getInstance().init(application);
    }

    private void loadModules(Application application) {
        Properties properties = PropertiesUtils.getProperties(application.getApplicationContext(), "modules.properties");
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
                    moduleInit.moduleInit(application);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void initFaceManager(Application application) {
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
                    FaceIdManager.getInstance().init(application.getApplicationContext());
                    // 初始化文件管理器
                } catch (Exception e) {
                    Toast.makeText(application.getApplicationContext(), "初始化Face sdk ....失败", Toast.LENGTH_SHORT).show();
                    Logger.e("##### 初始化Face sdk ....失败, e=" + e.getMessage());
                }
                Logger.d("##### 初始化Face sdk ....Done");
            }
        });
    }


    private void initEventBus(Application application) {
        LiveEventBus
                .config()
                .supportBroadcast(application.getApplicationContext())
                .lifecycleObserverAlwaysActive(true)
                .autoClear(false);
    }
}
