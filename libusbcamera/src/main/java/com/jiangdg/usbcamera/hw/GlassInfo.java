package com.jiangdg.usbcamera.hw;

/**
 * Author: heshun
 * Date: 2020/8/19 9:36 PM
 * gmail: shunhe1991@gmail.com
 */
public class GlassInfo {

    private String sn;
    private String typeId;
    private String pcba;
    private boolean vSyncStatus;
    private String opticalId;

    public String getSn() {
        return sn;
    }

    public String getTypeId() {
        return typeId;
    }

    public String getPcba() {
        return pcba;
    }

    public boolean isvSyncStatus() {
        return vSyncStatus;
    }

    public String getOpticalId() {
        return opticalId;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public void setPcba(String pcba) {
        this.pcba = pcba;
    }

    public void setVSyncStatus(boolean vSyncStatus) {
        this.vSyncStatus = vSyncStatus;
    }

    public void setOpticalId(String opticalId) {
        this.opticalId = opticalId;
    }


    public static final class GlassInfoBuilder {
        private String sn;
        private String typeId;
        private String pcba;
        private boolean vSyncStatus;
        private String opticalId;

        private GlassInfoBuilder() {
        }

        public static GlassInfoBuilder buildGlassInfo() {
            return new GlassInfoBuilder();
        }

        public GlassInfoBuilder withSn(String sn) {
            this.sn = sn;
            return this;
        }

        public GlassInfoBuilder withTypeId(String typeId) {
            this.typeId = typeId;
            return this;
        }

        public GlassInfoBuilder withPcba(String pcba) {
            this.pcba = pcba;
            return this;
        }

        public GlassInfoBuilder withVSyncStatus(boolean vSyncStatus) {
            this.vSyncStatus = vSyncStatus;
            return this;
        }

        public GlassInfoBuilder withOpticalId(String opticalId) {
            this.opticalId = opticalId;
            return this;
        }

        public GlassInfo build() {
            GlassInfo glassInfo = new GlassInfo();
            glassInfo.setSn(sn);
            glassInfo.setTypeId(typeId);
            glassInfo.setPcba(pcba);
            glassInfo.setVSyncStatus(vSyncStatus);
            glassInfo.setOpticalId(opticalId);
            return glassInfo;
        }
    }
}
