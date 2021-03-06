package com.rokid.glass.libbase.utils;


import com.rokid.glass.libbase.logger.Logger;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * Author: Zack
 * Email:  newzzack@gmail.com
 * Date:   2020.02.26 15:23
 */
public class ZipUtils {

    private static final int BUFFER = 8192;

    public static void compress(String srcPath , String dstPath) throws IOException {
        File srcFile = new File(srcPath);
        File dstFile = new File(dstPath);
        if (!srcFile.exists()) {
            throw new FileNotFoundException(srcPath + "不存在！");
        }

        FileOutputStream out = null;
        ZipOutputStream zipOut = null;
        try {
            out = new FileOutputStream(dstFile);
            CheckedOutputStream cos = new CheckedOutputStream(out,new CRC32());
            zipOut = new ZipOutputStream(cos);
            String baseDir = "";
            compress(srcFile, zipOut, baseDir);
        }
        finally {
            if(null != zipOut){
                zipOut.close();
                out = null;
            }

            if(null != out){
                out.close();
            }
        }
    }

    private static void compress(File file, ZipOutputStream zipOut, String baseDir) throws IOException{
        if (file.isDirectory()) {
            compressDirectory(file, zipOut, baseDir);
        } else {
            compressFile(file, zipOut, baseDir);
        }
    }

    /** 压缩一个目录 */
    private static void compressDirectory(File dir, ZipOutputStream zipOut, String baseDir) throws IOException{
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            compress(files[i], zipOut, baseDir + dir.getName() + "/");
        }
    }

    /** 压缩一个文件 */
    private static void compressFile(File file, ZipOutputStream zipOut, String baseDir)  throws IOException{
        if (!file.exists()){
            return;
        }

        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(file));
            ZipEntry entry = new ZipEntry(baseDir + file.getName());
            zipOut.putNextEntry(entry);
            int count;
            byte data[] = new byte[BUFFER];
            while ((count = bis.read(data, 0, BUFFER)) != -1) {
                zipOut.write(data, 0, count);
            }

        }finally {
            if(null != bis){
                bis.close();
            }
        }
    }

    public static void decompress(String zipFile , String dstPath, DecompressCallback decompressCallback) throws IOException {
        File pathFile = new File(dstPath);
        if(!pathFile.exists()){
            pathFile.mkdirs();
        }
        ZipFile zip = new ZipFile(zipFile);

        int total = zip.size();
        int process = 0;

        for(Enumeration entries = zip.entries(); entries.hasMoreElements();){
            ZipEntry entry = (ZipEntry)entries.nextElement();
            String zipEntryName = entry.getName();
            InputStream in = null;
            OutputStream out = null;
            try{
                in =  zip.getInputStream(entry);
                String outPath = (dstPath+"/"+zipEntryName).replaceAll("\\*", "/");;
                //判断路径是否存在,不存在则创建文件路径
                File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
                if(!file.exists()){
                    file.mkdirs();
                }
                process++;
                //判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
                if(new File(outPath).isDirectory()){
                    continue;
                }

                out = new FileOutputStream(outPath);
                byte[] buf1 = new byte[1024];
                int len;
                while((len=in.read(buf1))>0){
                    out.write(buf1,0,len);
                }

                if(decompressCallback != null) {
                    Logger.e("unzip file: " + zipEntryName);
                    double percent = Math.round((process * 100 / total)) / 100.0;
                    decompressCallback.onDecompressPercent((float)percent);
                }
            }
            finally {
                if(null != in){
                    in.close();
                }

                if(null != out){
                    out.close();
                }
            }
        }
        zip.close();
    }

    public interface DecompressCallback {
        /**
         * @param percent 取值范围为0~1
         */
        void onDecompressPercent(float percent);
    }


    public static void main(String[] args)throws Exception{
        String targetFolderPath = "/a/b/c/zipFolder";
        String rawZipFilePath = "/a/b/c/raw.zip";
        String newZipFilePath = "/a/b/c/new.zip";

        //将Zip文件解压缩到目标目录
//        ZipUtils.decompress(rawZipFilePath , targetFolderPath);

        //将目标目录的文件压缩成Zip文件
        ZipUtils.compress(targetFolderPath , newZipFilePath);

    }
}
