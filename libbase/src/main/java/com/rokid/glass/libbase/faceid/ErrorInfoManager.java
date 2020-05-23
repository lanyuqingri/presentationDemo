package com.rokid.glass.libbase.faceid;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rokid.glass.libbase.BaseLibrary;
import com.rokid.glass.libbase.utils.FileUtils;
import com.rokid.glass.pcassistant.bean.DeployErrInfo;

import java.io.File;
import java.util.ArrayList;

/**
 * Author: Zack
 * Email:  newzzack@gmail.com
 * Date:   2020.02.25 15:38
 */
public class ErrorInfoManager {

    private Gson mGson = new Gson();
    private ArrayList<DeployErrInfo> mErrorInfos;

    private static final String FILE_PATH = BaseLibrary.getInstance().getContext().getCacheDir().getAbsolutePath()
            + File.separator + "batch_error.json";

    public ErrorInfoManager (){
        init();
    }


    public void init() {
        File file = new File(FILE_PATH);
        if(file.exists()) {
            String fileContent = FileUtils.getFileContent(file.getAbsolutePath());
            if(TextUtils.isEmpty(fileContent)) {
                mErrorInfos = mGson.fromJson(fileContent, new TypeToken<ArrayList<DeployErrInfo>>(){}.getType());
            }
        }
        if(mErrorInfos == null) {
            mErrorInfos = new ArrayList<>();
        }
    }


    public void saveErrorInfo() {
        String jsonStr = mGson.toJson(mErrorInfos);
        FileUtils.writeFile(jsonStr, FILE_PATH, false);
    }


    public void addErrorInfo(DeployErrInfo errInfo) {
        mErrorInfos.add(errInfo);
    }


    public void clearErrorInfo() {
        mErrorInfos.clear();
        FileUtils.deleteFile(FILE_PATH);
    }

    public ArrayList<DeployErrInfo> getErrorInfos() {
        return mErrorInfos;
    }
}
