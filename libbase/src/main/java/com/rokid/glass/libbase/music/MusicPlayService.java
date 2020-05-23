package com.rokid.glass.libbase.music;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaDataSource;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.util.Formatter;
import java.util.Locale;

public class MusicPlayService extends Service {

    private MusicPlayBinder musicPlayBinder;
    private MediaPlayer mediaPlayer;
    private String duration;
    private MusicPlayCallback musicPlayCallback;

    private boolean isMusicStart = false;

    public static String stringForTime(int timeMs) {
        StringBuilder mFormatBuilder = new StringBuilder();
        Formatter mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours   = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    private MusicPlayListener musicPlayListener = new MusicPlayListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            isMusicStart = false;
            return false;
        }

        @Override
        public void onCompletion(MediaPlayer mp) {
            isMusicStart = false;
        }

        @Override
        public void onPrepared(MediaPlayer mp) {
            duration = stringForTime(mediaPlayer.getDuration());
            if(musicPlayCallback!=null){
                musicPlayCallback.onGetDuration(duration);
            }
            if (isMusicStart) {
                musicPlayBinder.resume();
            } else {
                musicPlayBinder.stop();
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        init();
    }

    private void init() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(musicPlayListener);
        mediaPlayer.setOnCompletionListener(musicPlayListener);
        mediaPlayer.setOnErrorListener(musicPlayListener);
    }

    public boolean musicStart() {
        return isMusicStart;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (musicPlayBinder == null) {
            musicPlayBinder = new MusicPlayBinder();
        }
        return musicPlayBinder;
    }

    public class MusicPlayBinder extends Binder implements MusicPlayInterface {

        @Override
        public boolean isStart() {
            return isMusicStart;
        }

        @Override
        public boolean play(String assertFileName,boolean looping) {
            try {
                AssetFileDescriptor afd = getAssets().openFd(assertFileName);
                mediaPlayer.reset();
                mediaPlayer.setDataSource(afd.getFileDescriptor(),
                        afd.getStartOffset(), afd.getLength());
                mediaPlayer.prepareAsync();
                mediaPlayer.setLooping(looping);
                isMusicStart = true;
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;

//            try {
//                mediaPlayer.setDataSource(url);
//                mediaPlayer.prepareAsync();
//                isMusicStart = true;
//                return true;
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return false;
        }
        @Override
        public boolean play(MediaDataSource source,boolean looping) {
            init();
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mediaPlayer.setDataSource(source);
                    mediaPlayer.prepareAsync();
                    mediaPlayer.setLooping(looping);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            isMusicStart = true;
            return true;
        }

        @Override
        public void resume() {
            mediaPlayer.start();
        }

        @Override
        public void pause() {
            mediaPlayer.pause();
        }

        @Override
        public void stop() {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            isMusicStart = false;
        }

        @Override
        public void release() {
            mediaPlayer.release();
            isMusicStart = false;
        }

        @Override
        public boolean isPlaying() {
            return mediaPlayer.isPlaying();
        }

        @Override
        public void setMusicPlayCallback(MusicPlayCallback musicPlayCallback) {
            MusicPlayService.this.musicPlayCallback = musicPlayCallback;
        }

    }

    public interface MusicPlayInterface {

        boolean isStart();

        boolean play(String assertFileName, boolean looping);

        boolean play(MediaDataSource source, boolean looping);

        void resume();

        void pause();

        void stop();

        void release();

        boolean isPlaying();

        void setMusicPlayCallback(MusicPlayCallback musicPlayCallback);
    }

    public interface MusicPlayListener extends MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener {
        @Override
        boolean onError(MediaPlayer mp, int what, int extra);

        @Override
        void onCompletion(MediaPlayer mp);

        @Override
        void onPrepared(MediaPlayer mp);
    }

    public interface MusicPlayCallback{
        void onGetDuration(String duration);
    }
}
