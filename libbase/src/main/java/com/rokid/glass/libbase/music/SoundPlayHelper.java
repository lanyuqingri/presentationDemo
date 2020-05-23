package com.rokid.glass.libbase.music;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;

import com.rokid.glass.libbase.R;
import com.rokid.glass.libbase.logger.Logger;


public class SoundPlayHelper {

    private static volatile SoundPlayHelper mInstance = null;

    private Context mContext;

    // sound related
    private SoundPool mSoundPool;
    // Maximum sound stream.
    private static final int MAX_STREAMS = 5;
    // Stream type.
    private static final int streamType = AudioManager.STREAM_MUSIC;
    private boolean mLoaded;
    private float mVolume;
    // 播放和停止录像音
    private int mSoundIdVideoStart;
    private int mSoundIdVideoStop;

    private SoundPlayHelper() {}

    public static SoundPlayHelper getInstance() {
        if (mInstance == null) {
            synchronized (SoundPlayHelper.class) {
                if (mInstance == null) {
                    mInstance = new SoundPlayHelper();
                }
            }
        }
        return mInstance;
    }

    public void init(Context context) {
        Logger.d("SoundPlayHelper---init="+mLoaded);
        this.mContext = context;

        // AudioManager audio settings for adjusting the mVolume
        AudioManager mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);

        // Current volumn Index of particular stream type.
        float currentVolumeIndex = 0;
        if (mAudioManager != null) {
            currentVolumeIndex = (float) mAudioManager.getStreamVolume(streamType);
        }

        // Get the maximum mVolume index for a particular stream type.
        float maxVolumeIndex  = 0;
        if (mAudioManager != null) {
            maxVolumeIndex = (float) mAudioManager.getStreamMaxVolume(streamType);
        }

        // Volumn (0 --> 1)
        this.mVolume = currentVolumeIndex / maxVolumeIndex;

        // Suggests an audio stream whose mVolume should be changed by
        // the hardware mVolume controls.
        AudioAttributes audioAttrib = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        SoundPool.Builder builder= new SoundPool.Builder();
        builder.setAudioAttributes(audioAttrib).setMaxStreams(MAX_STREAMS);

        this.mSoundPool = builder.build();

        // When Sound Pool load complete.
        this.mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                Logger.d("SoundPlayHelper---mLoaded="+mLoaded);
                mLoaded = true;
            }
        });

        // Load sound file (video_start.wav) into SoundPool.
        this.mSoundIdVideoStart = this.mSoundPool.load(mContext, R.raw.video_start,1);
        // Load sound file (video_stop.wav) into SoundPool.
        this.mSoundIdVideoStop = this.mSoundPool.load(mContext, R.raw.video_stop,1);
    }

    /**
     * 注意，因为目前MediaRecorderManager是在整个应用的生命周期都存在，所以即便没有调用这里的release也没有问题
     */
    public void release() {
        if (mSoundPool != null) {
            mSoundPool.unload(R.raw.video_start);
            mSoundPool.unload(R.raw.video_stop);
            mSoundPool.setOnLoadCompleteListener(null);
            mSoundPool.release();
            mSoundPool = null;
        }
    }

    /**
     * Play a sound when video recording starts.
     */
    public void playVideoStartSound()  {
        Logger.d("SoundPlayHelper playSoundVideoStart+++mLoaded="+mLoaded);
        if(mLoaded)  {
            float leftVolumn = mVolume;
            float rightVolumn = mVolume;
            this.mSoundPool.play(this.mSoundIdVideoStart,leftVolumn, rightVolumn, 1, 0, 1f);
        }
    }

    /**
     * Play a sound when video recording stops.
     */
    public void playVideoStopSound()  {
        if(mLoaded)  {
            Logger.d("SoundPlayHelper playSoundVideoStop---mLoaded="+mLoaded);
            float leftVolumn = mVolume;
            float rightVolumn = mVolume;
            this.mSoundPool.play(this.mSoundIdVideoStop,leftVolumn, rightVolumn, 1, 0, 1f);
        }
    }



}
