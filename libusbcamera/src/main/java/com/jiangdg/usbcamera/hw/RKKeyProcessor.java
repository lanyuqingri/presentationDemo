package com.jiangdg.usbcamera.hw;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.KeyEvent;

import androidx.annotation.NonNull;


import com.jiangdg.usbcamera.hw.listener.KeyEventType;
import com.jiangdg.usbcamera.hw.listener.RKKeyListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: heshun
 * Date: 2020/8/10 10:53 PM
 * gmail: shunhe1991@gmail.com
 */
public class RKKeyProcessor implements GlassKeyEvent {

    private long clickDelayTime;
    private long longPressTime;
    private Handler mHandler;
    private final Map<Integer, KeyEvent> mCacheKeyEvents = new HashMap<>();
    private RKKeyListener mListener;

    private static final RKKeyProcessor instance = new RKKeyProcessor();

    private RKKeyProcessor() {
        HandlerThread handlerThread = new HandlerThread("KeyProcessor");
        handlerThread.start();
        mHandler = new Handler(handlerThread.getLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                synchronized (mCacheKeyEvents) {
                    mCacheKeyEvents.remove(msg.what == MSG_POWER_SINGLE_CLICK ?
                            RKGlassKeycode.GLASS_KEYCODE_POWER : RKGlassKeycode.GLASS_KEYCODE_BACK);
                }
                onClick(msg.what == MSG_POWER_SINGLE_CLICK ?
                        RKGlassKeycode.GLASS_KEYCODE_POWER : RKGlassKeycode.GLASS_KEYCODE_BACK);
                return true;
            }
        });
    }

    public void setListener(RKKeyListener listener) {
        this.mListener = listener;
    }


    public static RKKeyProcessor getInstance() {
        return instance;
    }

    @Override
    public void init(GlassConfig config, RKKeyListener listener) {
        clickDelayTime = config.getClickDelayTime();
        longPressTime = config.getLongClickTime();
        if (null == listener) return;
        mListener = listener;
    }

    @Override
    public void setGlassConfig(GlassConfig config) {
        clickDelayTime = config.getClickDelayTime();
        longPressTime = config.getLongClickTime();
    }

    @Override
    public void setRKKeyListener(RKKeyListener listener) {
        mListener = listener;
    }

    public static final int MSG_POWER_SINGLE_CLICK = 100;
    public static final int MSG_BACK_SINGLE_CLICK = 110;

    @Override
    public void onKeyPress(int keycode, boolean isPress) {
        long currentTime = System.currentTimeMillis();
        KeyEvent cacheKey = mCacheKeyEvents.get(keycode);
        if (isPress) {
            if (null != cacheKey) {
                mHandler.removeMessages(keycode == RKGlassKeycode.GLASS_KEYCODE_POWER ? MSG_POWER_SINGLE_CLICK : MSG_BACK_SINGLE_CLICK);
                cacheKey = new KeyEvent(currentTime, -1, KeyEvent.ACTION_DOWN, keycode, 3);
                synchronized (mCacheKeyEvents) {

                    mCacheKeyEvents.put(keycode, cacheKey);
                }
            } else {
                KeyEvent keyEvent = new KeyEvent(currentTime, -1, KeyEvent.ACTION_DOWN, keycode, 1);
                synchronized (mCacheKeyEvents) {

                    mCacheKeyEvents.put(keycode, keyEvent);
                }
            }

        } else {
            if (null == cacheKey) {
                return;
            }

            long downTime = cacheKey.getDownTime();
            if (currentTime - downTime >= longPressTime) {//长按事件
                mHandler.post(() -> {
                    synchronized (mCacheKeyEvents) {
                        mCacheKeyEvents.remove(keycode);
                    }
                    if (null != mListener) {
                        onLongClick(keycode);
                    }
                });

            } else if (cacheKey.getRepeatCount() == 3) {//double click
//                if (currentTime - cacheKey.getDownTime() < clickDelayTime) {//暂时不判断
                mHandler.removeMessages(keycode == RKGlassKeycode.GLASS_KEYCODE_POWER ? MSG_POWER_SINGLE_CLICK : MSG_BACK_SINGLE_CLICK);
                synchronized (mCacheKeyEvents) {
                    mCacheKeyEvents.remove(keycode);
                }
                mHandler.post(() -> {
                    onDoubleClick(keycode);
                });
//                }
            } else if (cacheKey.getRepeatCount() == 1) {//singleClick
                cacheKey = new KeyEvent(currentTime, -1, KeyEvent.ACTION_UP, keycode, 2);
                synchronized (mCacheKeyEvents) {

                    mCacheKeyEvents.put(keycode, cacheKey);
                }
                KeyEvent finalCacheKey = cacheKey;
                Message message = mHandler.obtainMessage();
                message.obj = finalCacheKey;
                if (keycode == RKGlassKeycode.GLASS_KEYCODE_POWER) {
                    message.what = MSG_POWER_SINGLE_CLICK;
                } else if (keycode == RKGlassKeycode.GLASS_KEYCODE_BACK) {
                    message.what = MSG_BACK_SINGLE_CLICK;
                }
                mHandler.sendMessageDelayed(message, clickDelayTime);
            }
        }
    }

    private void processSingleClick(KeyEvent cacheKey) {
        synchronized (mCacheKeyEvents) {
            mCacheKeyEvents.remove(cacheKey.getKeyCode());
        }
    }

    private void onLongClick(int keycode) {
        if (null == mListener) return;

        if (keycode == RKGlassKeycode.GLASS_KEYCODE_BACK) {
            mListener.onBackKeyEvent(KeyEventType.LONG_CLICK);
        } else if (keycode == RKGlassKeycode.GLASS_KEYCODE_POWER) {
            mListener.onPowerKeyEvent(KeyEventType.LONG_CLICK);
        }
    }

    private void onDoubleClick(int keycode) {
        if (null == mListener) return;

        if (keycode == RKGlassKeycode.GLASS_KEYCODE_BACK) {
            mListener.onBackKeyEvent(KeyEventType.DOUBLE_CLICK);
        } else if (keycode == RKGlassKeycode.GLASS_KEYCODE_POWER) {
            mListener.onPowerKeyEvent(KeyEventType.DOUBLE_CLICK);
        }
    }

    private void onClick(int keycode) {
        if (null == mListener) return;

        if (keycode == RKGlassKeycode.GLASS_KEYCODE_BACK) {
            mListener.onBackKeyEvent(KeyEventType.SINGLE_CLICK);
        } else if (keycode == RKGlassKeycode.GLASS_KEYCODE_POWER) {
            mListener.onPowerKeyEvent(KeyEventType.SINGLE_CLICK);
        }
    }

    @Override
    public void onTouchPress(int position) {

    }

    @Override
    public void onTouchEvent(int event, int value) {
        if (null == mListener) return;

        if (event == RKGlassTouchEvent.SHORT_PRESS) {
            mListener.onTouchKeyEvent(KeyEventType.SINGLE_CLICK);
        } else if (event == RKGlassTouchEvent.LONG_PRESS) {
            mListener.onTouchKeyEvent(KeyEventType.LONG_CLICK);
        } else if (event == RKGlassTouchEvent.FORWARD_SLIDE) {
            mListener.onTouchSlideForward();
        } else if (event == RKGlassTouchEvent.BACKWARD_SLIDE) {
            mListener.onTouchSlideBack();
        }
    }
}
