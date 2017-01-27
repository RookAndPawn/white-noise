package com.rookandpawn.whitenoise;

import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Handler;

import com.rookandpawn.whitenoise.audio.AudioPresenter;
import com.rookandpawn.whitenoise.audio.HasBuffer;
import com.rookandpawn.whitenoise.audio.HasFloat;
import com.rookandpawn.whitenoise.audio.HasInterruptHandlers;

/**
 * Created by kguthrie on 1/18/17.
 */

public class AudioView implements com.rookandpawn.whitenoise.audio.AudioView, HasFloat, HasBuffer {

    private static final int bufferSize = 2048;

    private final AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC
            , (int) AudioPresenter.SAMPLE_RATE
            , AudioFormat.CHANNEL_OUT_MONO
            , AudioFormat.ENCODING_PCM_FLOAT
            , bufferSize * 8
            , AudioTrack.MODE_STREAM);
    private final Handler handler = new Handler();

    private final float[] buffer = new float[bufferSize];

    private AudioGenerator audioGenerator;
    private HasInterruptHandlers interrupter = new HasInterruptHandlers();

    private volatile boolean stillPlaying;

    private final Runnable bufferScheduler;

    private Runnable postBufferScheduler;

    public AudioView() {

        bufferScheduler = new Runnable() {
            @Override
            public void run() {
                audioGenerator.fillBuffer(0, AudioView.this);
                audioTrack.write(buffer, 0, bufferSize, AudioTrack.WRITE_NON_BLOCKING);
                postBufferScheduler.run();
            }
        };

        postBufferScheduler = new Runnable() {
            @Override
            public void run() {
                handler.post(bufferScheduler);
            }
        };
    }


    private Thread playerThread;

    private float volume;

    @Override
    public void setGenerator(AudioGenerator audioGenerator) {
        this.audioGenerator = audioGenerator;
    }

    @Override
    public void play() {
        stillPlaying = true;
        playerThread = new Thread(postBufferScheduler);
        playerThread.run();
        audioTrack.play();
    }

    @Override
    public void pause() {
        stillPlaying = false;
        audioTrack.pause();
        playerThread.interrupt();
        audioTrack.flush();
    }

    @Override
    public boolean isPlaying() {
        return audioTrack.getPlayState() == AudioTrack.PLAYSTATE_PLAYING;
    }

    @Override
    public HasFloat getVolumeControl() {
        return this;
    }

    @Override
    public HasInterruptHandlers getInterrupter() {
        return interrupter;
    }

    @Override
    public float getValue() {
        return volume;
    }

    @Override
    public void setValue(float v) {
        this.volume = v;
        audioTrack.setVolume(v);
    }

    @Override
    public float[] getBuffer() {
        return buffer;
    }

    public int getSessionId() {
        return audioTrack.getAudioSessionId();
    }
}
