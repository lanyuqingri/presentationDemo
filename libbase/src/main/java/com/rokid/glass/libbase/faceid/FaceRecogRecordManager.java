package com.rokid.glass.libbase.faceid;

import android.content.Context;
import android.util.Log;


import com.rokid.glass.libbase.faceid.database.DeployFaceRecord;
import com.rokid.glass.libbase.faceid.database.RecogRecordDatabase;
import com.rokid.glass.libbase.faceid.database.SingleFaceRecord;
import com.rokid.glass.libbase.utils.FileUtils;

import java.util.List;

public class FaceRecogRecordManager {
    private static final String TAG ="FaceRecogRecordManager";
    private static final FaceRecogRecordManager Instance = new FaceRecogRecordManager();

    private RecogRecordDatabase database;

    public static FaceRecogRecordManager getInstance() {
        return Instance;
    }
    public synchronized void init(final Context context) {
        Log.i(TAG,"db init");
        database = RecogRecordDatabase.create(context,FaceConstants.RECOG_RECORD_DB);
    }

    public List<SingleFaceRecord> getSingleFaceRecordList(int index , int count){
        List<SingleFaceRecord> singleFaceRecordList = database.singleRecogRecordDao().getSingleFaceRecordList(index,count);
        return singleFaceRecordList;
    }

    public List<DeployFaceRecord> getDeployFaceRecordList(int index , int count){
        List<DeployFaceRecord> deployFaceRecordList = database.deployRecogRecordDao().getDeployFaceRecordList(index,count);
        return deployFaceRecordList;
    }

    public List<SingleFaceRecord> getAllSingleFaceRecordList(){
        List<SingleFaceRecord> singleFaceRecordList = database.singleRecogRecordDao().getAllSingleFaceRecordList();
        return singleFaceRecordList;
    }

    public List<DeployFaceRecord> getAllDeployFaceRecordList(){
        List<DeployFaceRecord> deployFaceRecordList = database.deployRecogRecordDao().getAllDeployFaceRecordList();
        return deployFaceRecordList;
    }

    public void addSingleFaceRecord(SingleFaceRecord singleFaceRecord){
        database.singleRecogRecordDao().addSingleRecogRecord(singleFaceRecord);
    }

    public void addDeployFaceRecord(DeployFaceRecord deployFaceRecord){
        database.deployRecogRecordDao().addDeployRecogRecord(deployFaceRecord);
    }

    public int getSingleFaceRecordTotalNum() {
        return database.singleRecogRecordDao().getSingleFaceRecordCount();
    }

    public int getDeployFaceRecordTotalNum() {
        return database.deployRecogRecordDao().getDeployFaceRecordCount();
    }

    public SingleFaceRecord getSingleFaceRecord(String id){
        return database.singleRecogRecordDao().getSingleFaceRecordById(id);
    }

    public DeployFaceRecord getDeployFaceRecord(String id){
        return database.deployRecogRecordDao().getDeployFaceRecordById(id);
    }

    public void deleteAllSingleFaceRecord() throws Exception{
        List<SingleFaceRecord> singleFaceRecords = database.singleRecogRecordDao().getAllSingleFaceRecordList();
        for(SingleFaceRecord singleFaceRecord:singleFaceRecords){
            if(singleFaceRecord != null){
                FileUtils.deleteFile(singleFaceRecord.captureFaceImg);
                FileUtils.deleteFile(singleFaceRecord.captureImg);
            }
        }
        database.singleRecogRecordDao().removeAllSingleFaceRecord();
    }

    public void deleteSingleFaceRecordById(String[] strings) throws Exception{
        for(String id:strings){
            SingleFaceRecord singleFaceRecord = database.singleRecogRecordDao().getSingleFaceRecordById(id);
            if(singleFaceRecord != null){
                FileUtils.deleteFile(singleFaceRecord.captureFaceImg);
                FileUtils.deleteFile(singleFaceRecord.captureImg);
            }
            database.singleRecogRecordDao().removeSingleFaceRecordById(id);
        }
    }

    public void deleteAllDeployFaceRecord() throws Exception{
        List<DeployFaceRecord> deployFaceRecords = database.deployRecogRecordDao().getAllDeployFaceRecordList();
        for(DeployFaceRecord deployFaceRecord:deployFaceRecords){
            if(deployFaceRecord != null){
                FileUtils.deleteFile(deployFaceRecord.captureFaceImg);
                FileUtils.deleteFile(deployFaceRecord.captureImg);
            }
        }
        database.deployRecogRecordDao().removeAllDeployFaceRecord();
    }


    public void deleteDeployFaceRecordById(String[] strings) throws Exception{
        for(String id :strings){
            DeployFaceRecord deployFaceRecord = database.deployRecogRecordDao().getDeployFaceRecordById(id);
            if(deployFaceRecord != null){
                FileUtils.deleteFile(deployFaceRecord.captureFaceImg);
                FileUtils.deleteFile(deployFaceRecord.captureImg);
            }
            database.deployRecogRecordDao().removeDeployFaceRecordById(id);
        }
    }
}
