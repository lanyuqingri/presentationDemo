package com.rokid.glass.presentationdemo.glass;

import android.os.Environment;

import com.rokid.glass.libbase.BaseLibrary;
import com.rokid.glass.libbase.logger.Logger;
import com.rokid.glass.libbase.message.dto.CarRecognizeInfoBean;
import com.rokid.glass.libbase.plate.DataConvertUtil;
import com.rokid.glass.libbase.plate.PlateManager;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CSVFileUtil {
    // 存放车牌信息的csv文件 /sdcard/car.csv
    public static String CSV_FILE_PATH = Environment.getExternalStorageDirectory() + File.separator + "car.csv";

    private volatile static CSVFileUtil mInstance = new CSVFileUtil();

    // 初始化 List
    private List<CarRecognizeInfoBean> mCarInfoList = new ArrayList<>();

    private CSVFileUtil() {
    }

    public static CSVFileUtil getInstance() {
        return mInstance;
    }

    public void initCsv(String path) {
        try {
            Logger.d("CSVFileUtil initCsv path="+path);
            if (!new File(path).exists()) {
                Logger.d("CSVFileUtil 文件不存在 path="+path);
                return;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));  // 防止出现乱码
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            for (CSVRecord csvRecord : csvRecords) {
                CarRecognizeInfoBean carInfoBean = new CarRecognizeInfoBean();
                carInfoBean.setOwner(getRecord(csvRecord,"owner"));
                carInfoBean.setIdcard(getRecord(csvRecord,"idcard"));
                carInfoBean.setAddress(getRecord(csvRecord,"address"));
                carInfoBean.setPhoneNum(getRecord(csvRecord,"phoneNum"));
                carInfoBean.setPlate(getRecord(csvRecord,"plate"));
                carInfoBean.setBrand(getRecord(csvRecord,"brand"));
                carInfoBean.setColor(getRecord(csvRecord,"color"));
                carInfoBean.setStatus(getRecord(csvRecord,"status"));
                carInfoBean.setDate(getRecord(csvRecord,"date"));
                Logger.d("load car info: " + carInfoBean.toString());
                mCarInfoList.add(carInfoBean);
            }

            PlateManager.getInstance().addPlateInfos(DataConvertUtil.convertCarBeanListToPlateList(mCarInfoList),false);
            Logger.d("CSVFileUtil car info list size : " + mCarInfoList.size());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initCsv() {
        try {
            Logger.d("CSVFileUtil initCsv");
            InputStream inputStream = BaseLibrary.getInstance().getContext().getAssets().open("car.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));  // 防止出现乱码
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            for (CSVRecord csvRecord : csvRecords) {
                CarRecognizeInfoBean carInfoBean = new CarRecognizeInfoBean();
                carInfoBean.setOwner(getRecord(csvRecord,"owner"));
                carInfoBean.setIdcard(getRecord(csvRecord,"idcard"));
                carInfoBean.setAddress(getRecord(csvRecord,"address"));
                carInfoBean.setPhoneNum(getRecord(csvRecord,"phoneNum"));
                carInfoBean.setPlate(getRecord(csvRecord,"plate"));
                carInfoBean.setBrand(getRecord(csvRecord,"brand"));
                carInfoBean.setColor(getRecord(csvRecord,"color"));
                carInfoBean.setStatus(getRecord(csvRecord,"status"));
                carInfoBean.setDate(getRecord(csvRecord,"date"));
                Logger.d("load car info: " + carInfoBean.toString());
                mCarInfoList.add(carInfoBean);
            }
            PlateManager.getInstance().addPlateInfos(DataConvertUtil.convertCarBeanListToPlateList(mCarInfoList),false);

            Logger.d("CSVFileUtil car info list size : " + mCarInfoList.size());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CarRecognizeInfoBean getCarRecogByPlate(String carNum) {
        if (mCarInfoList != null && mCarInfoList.size() > 0) {
            for (CarRecognizeInfoBean carInfo : mCarInfoList) {
                if (carInfo.getPlate().endsWith(carNum)) {
                    return carInfo;
                }
            }
        }
        return null;
    }

    private String getRecord(CSVRecord csvRecord, String key) {
        String value = "";
        try {
            value = csvRecord.get(key);
        }
        catch (Exception e) {
//            e.printStackTrace();
        }
        return value;
    }

}
