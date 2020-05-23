package com.rokid.glass.libbase.music;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import static android.content.Context.BIND_AUTO_CREATE;

public class MusicPlayHelper implements ServiceConnection {
    private final static String TAG = "MusicPlayHelper";

    private static volatile MusicPlayHelper mInstance = null;

    private MusicPlayHelper() {}

    private MusicPlayService.MusicPlayBinder musicPlayBinder = null;

    private boolean isBinded = false;

    public static MusicPlayHelper getInstance() {
        if (mInstance == null) {
            synchronized (MusicPlayHelper.class) {
                if (mInstance == null) {
                    mInstance = new MusicPlayHelper();
                }
            }
        }
        return mInstance;
    }

    public void init(Context context) {
        Intent intent = new Intent(context, MusicPlayService.class);
        context.bindService(intent, this, BIND_AUTO_CREATE);
    }

    public void deinit(Context context) {
        isBinded = false;
        context.unbindService(this);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        isBinded = true;
        musicPlayBinder = (MusicPlayService.MusicPlayBinder) service;
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        isBinded = false;
        musicPlayBinder = null;
    }

    public void playMusic(String assertFileName,boolean looping) {
        try {
            if (!TextUtils.isEmpty(assertFileName) && musicPlayBinder != null && !musicPlayBinder.isPlaying()) {
                musicPlayBinder.play(assertFileName,looping);
            }
        }
        catch (Exception e) {
            Log.e(TAG, "playMusic 播放失败 assertFileName="+assertFileName+", e="+e.getMessage());
            e.printStackTrace();
        }
    }

    public void pauseMusic() {
        try {
            if (musicPlayBinder != null) {
                musicPlayBinder.pause();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopMusic() {
        try {
            if (musicPlayBinder != null) {
                musicPlayBinder.stop();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void releaseMusic() {
        try {
            if (musicPlayBinder != null) {
                musicPlayBinder.release();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isPlayingMusic() {
        if (musicPlayBinder != null) {
            return  musicPlayBinder.isPlaying();
        }
        return false;
    }
}
