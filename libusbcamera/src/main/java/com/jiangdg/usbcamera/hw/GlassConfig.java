package com.jiangdg.usbcamera.hw;

/**
 * Author: heshun
 * Date: 2020/8/10 11:03 PM
 * gmail: shunhe1991@gmail.com
 */
public class GlassConfig {

    private long clickDelayTime;
    private long longClickTime;

    public long getClickDelayTime() {
        return clickDelayTime;
    }

    public void setClickDelayTime(long clickDelayTime) {
        this.clickDelayTime = clickDelayTime;
    }

    public long getLongClickTime() {
        return longClickTime;
    }

    public void setLongClickTime(long longClickTime) {
        this.longClickTime = longClickTime;
    }


    public static final class GlassConfigBuilder {
        private long clickDelayTime;
        private long longClickTime;

        private GlassConfigBuilder() {
        }

        public static GlassConfigBuilder buildGlassConfig() {
            return new GlassConfigBuilder();
        }

        public GlassConfigBuilder withClickDelayTime(long clickDelayTime) {
            this.clickDelayTime = clickDelayTime;
            return this;
        }

        public GlassConfigBuilder withLongClickTime(long longClickTime) {
            this.longClickTime = longClickTime;
            return this;
        }

        public GlassConfig build() {
            GlassConfig glassConfig = new GlassConfig();
            glassConfig.setClickDelayTime(clickDelayTime);
            glassConfig.setLongClickTime(longClickTime);
            return glassConfig;
        }
    }
}
