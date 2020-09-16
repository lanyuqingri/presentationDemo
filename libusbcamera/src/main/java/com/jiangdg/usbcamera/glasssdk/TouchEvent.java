package com.jiangdg.usbcamera.glasssdk;

/**
 * Author: heshun
 * Date: 2020/9/2 3:54 PM
 * gmail: shunhe1991@gmail.com
 */
public class TouchEvent {

    private int mPressCount = 0;
    private long mLastTimeStamp = 0L;
    private byte mLastPostion = 0;
    private long mStartTimeStamp = 0L;
    private byte mStartPosition = 0;
    private byte mTouchStatus = 0;
    private byte mSlideStatus = 0;
    private final int IDLE = 0;
    private final int PRESS = 1;
    public static final int SHORT_PRESS = 2;
    public static final int LONG_PRESS = 3;
    public static final int FORWARD_SLIDE = 4;
    public static final int BACKWARD_SLIDE = 5;
    private final int REPORTED = 6;
    public final int LONG_PRESS_THREADHOLD_MS = 1000;

    public TouchEvent() {
    }

    private int IsSlide(byte value) {
        this.mSlideStatus = value;
        if ((this.mSlideStatus & 1) == 1) {
            return 5;
        } else {
            return (this.mSlideStatus & 2) == 2 ? 4 : 0;
        }
    }

    private long InfervalAfterStart(long timestamp) {
        return timestamp - this.mStartTimeStamp;
    }

    public int GetSlideLen() {
        return (this.mSlideStatus & 255) >> 2;
    }

    public int UpdateStatus(long timeStamp, byte position, byte slide) {
        switch(this.mTouchStatus) {
            case 0:
                if (position != 0) {
                    this.mStartTimeStamp = timeStamp;
                    this.mStartPosition = position;
                    this.mTouchStatus = 1;
                }
                break;
            case 1:
                if (slide != 0) {
                    if (this.IsSlide(slide) == 4) {
                        this.mTouchStatus = 4;
                    } else if (this.IsSlide(slide) == 5) {
                        this.mTouchStatus = 5;
                    }
                } else if (position != 0) {
                    if (this.InfervalAfterStart(timeStamp) > 1000L) {
                        this.mTouchStatus = 3;
                    }
                } else if (this.InfervalAfterStart(timeStamp) < 1000L) {
                    this.mTouchStatus = 2;
                } else {
                    this.mTouchStatus = 3;
                }
                break;
            case 2:
                this.mTouchStatus = 0;
                return 2;
            case 3:
                this.mTouchStatus = 6;
                return 3;
            case 4:
                if (position == 0) {
                    this.mTouchStatus = 0;
                    return 4;
                }
                break;
            case 5:
                if (position == 0) {
                    this.mTouchStatus = 0;
                    return 5;
                }
                break;
            case 6:
                if (position == 0) {
                    this.mTouchStatus = 0;
                }
        }

        this.mLastPostion = position;
        this.mLastTimeStamp = timeStamp;
        return 0;
    }

}
