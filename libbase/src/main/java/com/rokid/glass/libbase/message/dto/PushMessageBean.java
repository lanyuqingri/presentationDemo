package com.rokid.glass.libbase.message.dto;

import java.util.List;

/**
 * Author: Zack
 * Email:  newzzack@gmail.com
 * Date:   2019.07.24 19:27
 */
public class PushMessageBean {


    /**
     * messageId : 759D17EA43344A7CBEDFD3DD885B8ABC
     * messageTitle : e
     * messageLevel : 0
     * messageType : 0
     * messageContent : e
     * createBy : null
     * modifyBy : null
     * status : null
     * sourceId : null
     * sourceName : null
     * sourceInfoList : [{"sourceUrl":"http://172.31.9.15:9000/glass?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=LINUJFMH70X6WOT4R6A0%2F20190724%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20190724T112128Z&X-Amz-Expires=604800&X-Amz-SignedHeaders=host&X-Amz-Signature=4317d9574c2931f2a2b00b58ee466eb7b1d94471a1e0fbaf0bf491832386e00f","sourceOriginName":null}]
     */

    public enum MessageType {
        /**
         * 文本
         */
        TXT,
        /**
         * 图文
         */
        MIX,
    }

    public enum MessageLevel {
        /**
         * 普通消息
         */
        NORMAL,
        /**
         * 重要消息
         */
        IMPORTANT,
        /**
         * 命令消息
         */
        ORDER,
    }


    public enum Status {
        /**
         * 未推送
         */
        UNPUSH,
        /**
         * 未送达
         */
        UNDELIVERED,
        /**
         * 未读--已送达
         */
        UNREAD,
        /**
         * 已读
         */
        READ
    }

    private String messageId;
    private String messageTitle;
    private MessageLevel messageLevel;
    private MessageType messageType;
    private String messageContent;
    private String createBy;
    private String modifyBy;
    private Status status;
    private String sourceId;
    private String sourceName;
    private long createdTime;
    private FaceBitmapInfo faceBitmapInfo;
    private List<SourceInfoListBean> sourceInfoList;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public MessageLevel getMessageLevel() {
        return messageLevel;
    }

    public void setMessageLevel(MessageLevel messageLevel) {
        this.messageLevel = messageLevel;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public FaceBitmapInfo getFaceBitmapInfo() {
        return faceBitmapInfo;
    }

    public void setFaceBitmapInfo(FaceBitmapInfo faceBitmapInfo) {
        this.faceBitmapInfo = faceBitmapInfo;
    }

    public List<SourceInfoListBean> getSourceInfoList() {
        return sourceInfoList;
    }

    public void setSourceInfoList(List<SourceInfoListBean> sourceInfoList) {
        this.sourceInfoList = sourceInfoList;
    }

    public static class SourceInfoListBean {
        /**
         * sourceUrl : http://172.31.9.15:9000/glass?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=LINUJFMH70X6WOT4R6A0%2F20190724%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20190724T112128Z&X-Amz-Expires=604800&X-Amz-SignedHeaders=host&X-Amz-Signature=4317d9574c2931f2a2b00b58ee466eb7b1d94471a1e0fbaf0bf491832386e00f
         * sourceOriginName : null
         */

        public SourceInfoListBean() {
        }

        private String sourceUrl;
        private String sourceOriginName;

        public SourceInfoListBean(String sourceUrl, String sourceOriginName) {
            this.sourceUrl = sourceUrl;
            this.sourceOriginName = sourceOriginName;
        }

        public String getSourceUrl() {
            return sourceUrl;
        }

        public void setSourceUrl(String sourceUrl) {
            this.sourceUrl = sourceUrl;
        }

        public String getSourceOriginName() {
            return sourceOriginName;
        }

        public void setSourceOriginName(String sourceOriginName) {
            this.sourceOriginName = sourceOriginName;
        }

        @Override
        public String toString() {
            return "SourceInfoListBean{" +
                    "sourceUrl='" + sourceUrl + '\'' +
                    ", sourceOriginName='" + sourceOriginName + '\'' +
                    '}';
        }
    }

    public static class FaceBitmapInfo {
        private byte[] imageData;
        private int width;
        private int height;

        public FaceBitmapInfo() {
        }

        public FaceBitmapInfo(byte[] imageData, int width, int height) {
            this.imageData = imageData;
            this.width = width;
            this.height = height;
        }

        public byte[] getImageData() {
            return imageData;
        }

        public void setImageData(byte[] imageData) {
            this.imageData = imageData;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        @Override
        public String toString() {
            return "FaceBitmapInfo{" +
                    "imageData=" + (imageData == null ? 0 : imageData.length) +
                    ", width=" + width +
                    ", height=" + height +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "PushMessageBean{" +
                "messageId='" + messageId + '\'' +
                ", messageTitle='" + messageTitle + '\'' +
                ", messageLevel=" + messageLevel +
                ", messageType=" + messageType +
                ", messageContent='" + messageContent + '\'' +
                ", createBy='" + createBy + '\'' +
                ", modifyBy='" + modifyBy + '\'' +
                ", status=" + status +
                ", sourceId='" + sourceId + '\'' +
                ", sourceName='" + sourceName + '\'' +
                ", createdTime=" + createdTime +
                ", faceBitmapInfo=" + faceBitmapInfo +
                ", sourceInfoList=" + sourceInfoList +
                '}';
    }
}
