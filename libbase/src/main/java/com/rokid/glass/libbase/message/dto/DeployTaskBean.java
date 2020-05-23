package com.rokid.glass.libbase.message.dto;

/**
 * Author: Zack
 * Email:  newzzack@gmail.com
 * Date:   2020.01.03 11:18
 */
public class DeployTaskBean {

    /**
     * 布控任务状态
     */
    public enum Status {
        /**
         * 可下载 未读
         */
        UNREAD,
        /**
         * 已读
         */
        READ,
        /**
         * 已更新
         */
        UPDATED
    }

    /**
     * 任务id
     */
    private String id;
    /**
     * 布控包名称
     */
    private String taskName;

    /**
     * 创建时间
     */
    private String gmtCreate;

    /**
     * 文件大小
     */
    private long fileSize;

    /**
     * 文件url
     */
    private String downloadFileUrl;

    /**
     * 文件md5
     */
    private String fileMd5;

    /**
     * 特征数量
     */
    private int featCount;

    /**
     * 人员数量
     */
    private int personCount;

    /**
     * 任务状态: 0, 未读,  1, 已读未更新, 2: 已更新
     */
    private Status status;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getDownloadFileUrl() {
        return downloadFileUrl;
    }

    public void setDownloadFileUrl(String downloadFileUrl) {
        this.downloadFileUrl = downloadFileUrl;
    }

    public String getFileMd5() {
        return fileMd5;
    }

    public void setFileMd5(String fileMd5) {
        this.fileMd5 = fileMd5;
    }

    public int getFeatCount() {
        return featCount;
    }

    public void setFeatCount(int featCount) {
        this.featCount = featCount;
    }

    public int getPersonCount() {
        return personCount;
    }

    public void setPersonCount(int personCount) {
        this.personCount = personCount;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "DeployTaskBean{" +
                "id='" + id + '\'' +
                ", taskName='" + taskName + '\'' +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", fileSize=" + fileSize +
                ", downloadFileUrl='" + downloadFileUrl + '\'' +
                ", fileMd5='" + fileMd5 + '\'' +
                ", featCount=" + featCount +
                ", personCount=" + personCount +
                ", status=" + status +
                '}';
    }
}
