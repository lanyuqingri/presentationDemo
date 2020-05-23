package com.rokid.glass.libbase.message.dto;

import java.io.Serializable;

public class FileInfoBean implements Serializable {

    private static final long serialVersionUID = -7286329626959566624L;

    private long id; // _id

    private String name; // _display_name

    private String size; // _size

    private String path; // _data

    private String duration; // DURATION

    private boolean isVideo = true; // mime_type

    private String createTime;

    protected byte[] fileData;

    public FileInfoBean() {
    }

    public FileInfoBean(long id, String name, String size, String path, String duration, boolean isVideo, String createTime) {
        this.id = id;
        this.name = name;
        this.size = size;
        this.path = path;
        this.duration = duration;
        this.isVideo = isVideo;
        this.createTime = createTime;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    @Override
    public String toString() {
        return "FileInfoBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", size='" + size + '\'' +
                ", path='" + path + '\'' +
                ", duration='" + duration + '\'' +
                ", isVideo=" + isVideo +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
