package com.rokid.glass.libbase.plate;


import android.content.Context;
import android.text.TextUtils;

import com.rokid.glass.libbase.ErrorCode;
import com.rokid.glass.libbase.config.DeployTaskConfig;
import com.rokid.glass.libbase.faceid.FaceConstants;
import com.rokid.glass.libbase.logger.Logger;
import com.rokid.glass.libbase.plate.database.PlateDatabase;
import com.rokid.glass.libbase.utils.DefaultSPHelper;
import com.rokid.glass.libbase.utils.FileUtils;
import com.rokid.glass.libbase.utils.JSONHelper;
import com.rokid.glass.libbase.utils.PlateUtils;

import java.util.ArrayList;
import java.util.List;


public class PlateManager{
    private static final PlateManager Instance = new PlateManager();
    private PlateDatabase mPlateDatabase;
    private Context mContext;

    public static PlateManager getInstance() {
        return Instance;
    }

    public void init(Context context){
        mContext = context;
        mPlateDatabase = PlateDatabase.create(context, FaceConstants.PLATE_DB);
        Logger.d("platemanager init && setBluetoothCallback for this");
    }

    public int addPlateInfo(PlateInfo info){
        if(mPlateDatabase != null && info != null && !TextUtils.isEmpty(info.getPlate())){
            mPlateDatabase.plateInfoDao().addPlateInfo(info);
            return ErrorCode.OK.getCode();
        }
        return ErrorCode.INVALID_PARAM.getCode();
    }

    public int addPlateInfos(List<PlateInfo> infos, boolean isOverWrite){
        if(infos == null || infos.size() <= 0){
            return ErrorCode.INVALID_PARAM.getCode();
        }
        if(isOverWrite){
            deletePlates(true, null);
        }
        if(infos.size() == 1){
            PlateInfo plateInfo = infos.get(0);
            if(plateInfo == null || TextUtils.isEmpty(plateInfo.getPlate()) || !PlateUtils.isCarNo(plateInfo.getPlate())){
                return  ErrorCode.INVALID_PARAM.getCode();
            }
            PlateInfo checkInfo = queryPlateInfo(plateInfo.getPlate());
            if(checkInfo != null){
                return ErrorCode.PLATE_HAS_EXIST.getCode();   //已经存在
            }
            addPlateInfo(plateInfo);
        } else {
            for(PlateInfo info : infos){
                addPlateInfo(info);
            }
        }
        return ErrorCode.OK.getCode();
    }

    public int updatePlateInfo(PlateInfo info){
        if(mPlateDatabase != null && info != null && !TextUtils.isEmpty(info.getPlate()) || !PlateUtils.isCarNo(info.getPlate())){
            mPlateDatabase.plateInfoDao().updatePlateInfo(info);
            return ErrorCode.OK.getCode();
        }
        return ErrorCode.INVALID_PARAM.getCode();
    }

    public PlateInfo queryPlateInfo(String plate){
        if(TextUtils.isEmpty(plate)){
            return null;
        }
        if(mPlateDatabase != null){
            return mPlateDatabase.plateInfoDao().queryPlateInfoByPlate(plate);
        }
        return null;
    }

    public List<PlateInfo> queryPlateInfos(int offset, int pageSize, String searchKey){
        if(offset < 0 || pageSize < 1){
            return null;
        }
        if(mPlateDatabase != null){
            if(TextUtils.isEmpty(searchKey)){
                return mPlateDatabase.plateInfoDao().queryPlateInfos(offset,pageSize);
            } else {
                return mPlateDatabase.plateInfoDao().queryPlateInfos(offset,pageSize, searchKey);
            }
        }
        return null;
    }

    public int getPlateCount(){
        if(mPlateDatabase != null){
            return mPlateDatabase.plateInfoDao().getPlateCount();
        }
        return 0;
    }

    public int deletePlates(boolean isAll, List<String> plates){
        if(mPlateDatabase != null){
            if(isAll){
                mPlateDatabase.plateInfoDao().removeAllPlateDbInfo();
                return ErrorCode.OK.getCode();
            } else {
                if(plates == null || plates.size() <= 0){
                    return ErrorCode.INVALID_PARAM.getCode();
                }
                for(String plate:plates){
                    if(!TextUtils.isEmpty(plate)){
                        mPlateDatabase.plateInfoDao().removePlateInfoByPlate(plate);
                    }
                }
                return ErrorCode.OK.getCode();
            }
        }
        return ErrorCode.PLATE_DATABASE_NOT_INIT.getCode();
    }

    private int deletePlate(String plate){
        if(TextUtils.isEmpty(plate)){
            return ErrorCode.INVALID_PARAM.getCode();
        }
        if(mPlateDatabase != null){
            mPlateDatabase.plateInfoDao().removePlateInfoByPlate(plate);
            return ErrorCode.OK.getCode();
        }
        return ErrorCode.PLATE_DATABASE_NOT_INIT.getCode();
    }
    public List<PlateInfo> exportPlates(boolean isAll, List<String> plates){
        if(mPlateDatabase != null){
            if(isAll){
                return mPlateDatabase.plateInfoDao().getAllPlateDbInfo();
            } else {
                if(plates == null || plates.size() <= 0){
                    return null;
                }
                List<PlateInfo> infos = new ArrayList<>();
                for(String plate:plates){
                    PlateInfo info = mPlateDatabase.plateInfoDao().queryPlateInfoByPlate(plate);
                    if(info != null){
                        infos.add(info);
                    }
                }
                return infos;
            }
        }
        return null;
    }

    public int updatePlateDeployInfo(PlateDeployInfo plateDeployInfo){
        if(plateDeployInfo == null){
            return -1;
        }
        plateDeployInfo.setUpdateTime(System.currentTimeMillis());
        DefaultSPHelper.getInstance().put(DeployTaskConfig.KEYS.SP_PLATE_DEPLOY_NAME, JSONHelper.toJson(plateDeployInfo));
        return 0;
    }

    public PlateDeployInfo getPlateDeployInfo(){
        String plateDeployStr = DefaultSPHelper.getInstance().getString(DeployTaskConfig.KEYS.SP_PLATE_DEPLOY_NAME,"{}");
        PlateDeployInfo plateDeployInfo = JSONHelper.fromJson(plateDeployStr, PlateDeployInfo.class);
        plateDeployInfo.setPlateNum(getPlateCount());
        return plateDeployInfo;
    }

    public int deletePlateDeploy(){
        DefaultSPHelper.getInstance().put(DeployTaskConfig.KEYS.SP_PLATE_DEPLOY_NAME, "{}");
        deletePlates(true, null);
        return 0;
    }

    public int getPlateRecgRecordNumber(){
        if(mPlateDatabase != null){
            return mPlateDatabase.plateRecogRecordDao().getPlateRecordCount();
        }
        return 0;
    }

    public List<PlateRecogRecord> queryPlateRecogRecordList(int offset, int pageSize){
        if(offset < 0 || pageSize < 1){
            return null;
        }
        if(mPlateDatabase != null){
            return mPlateDatabase.plateRecogRecordDao().queryPlateRecordList(offset, pageSize);
        }
        return null;
    }

    public List<PlateRecogRecord> queryPlateRecordList(boolean isAll, List<String> recordIds){
        if(mPlateDatabase != null){
            if(isAll){
                return mPlateDatabase.plateRecogRecordDao().getAllPlateRecordDbInfo();
            } else {
                if(recordIds == null || recordIds.size() <= 0){
                    return null;
                }
                List<PlateRecogRecord> infos = new ArrayList<>();
                for(String recordId:recordIds){
                    PlateRecogRecord info = mPlateDatabase.plateRecogRecordDao().queryPlateRecordInfoByRecordId(recordId);
                    if(info != null){
                        infos.add(info);
                    }
                }
                return infos;
            }
        }
        return null;
    }

    public int deletePlateRecogs(boolean isAll, List<String> recordIds){
        if(mPlateDatabase != null){
            if(isAll){
                List<PlateRecogRecord> plateRecogRecords = mPlateDatabase.plateRecogRecordDao().getAllPlateRecordDbInfo();
                for(PlateRecogRecord plateRecogRecord:plateRecogRecords){
                    if(plateRecogRecord != null){
                        FileUtils.deleteFile(plateRecogRecord.getRecogFilePath());
                    }
                }
                mPlateDatabase.plateRecogRecordDao().removeAllPlateRecordDbInfo();
                return 0;
            } else {
                if(recordIds == null || recordIds.size() <= 0){
                    return -1;
                }
                for(String recordId:recordIds){
                    if(!TextUtils.isEmpty(recordId)){
                        PlateRecogRecord plateRecogRecord = mPlateDatabase.plateRecogRecordDao().queryPlateRecordInfoByRecordId(recordId);
                        if(plateRecogRecord != null){
                            FileUtils.deleteFile(plateRecogRecord.getRecogFilePath());
                        }
                        mPlateDatabase.plateRecogRecordDao().removePlateRecordInfoByRecordId(recordId);
                    }
                }
                return 0;
            }
        }
        return -1;
    }

    public int addPlateRecord(PlateRecogRecord recogRecord){
        if(mPlateDatabase != null){
            mPlateDatabase.plateRecogRecordDao().addPlateRecogRecord(recogRecord);
        }
        return -1;
    }
}
