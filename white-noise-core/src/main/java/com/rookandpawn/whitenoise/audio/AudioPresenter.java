package com.rookandpawn.whitenoise.audio;

import com.rookandpawn.whitenoise.WhiteNoiseGenerator;

/**
 * Audio controller for whitenoise
 * @author kguthrie
 */
public class AudioPresenter implements InterruptHandler {

    public static final double SAMPLE_RATE = 44100;

    private static final int fadeTimeMillis = 125;
    private static final int fadeSleepMillis = 2;

    private final AudioView audioView;

    // This is different from is playing.  This tells us whether we should be
    // playing even if the audio is not actually running.  I.E. when interrupted
    private volatile boolean isPlayingRequested;


    public AudioPresenter(AudioView audioView) {
        this.audioView = audioView;
        isPlayingRequested = false;
    }

    /**
     * Bind view to presenter (Prevents leaking this)
     */
    public void bind() {
        audioView.setGenerator(new WhiteNoiseGenerator(SAMPLE_RATE));
        audioView.getInterrupter().addHandler(this);
    }

    public AudioView getAudioView() {
        return audioView;
    }

    /**
     * Is the audio playing?
     * @return
     */
    public boolean isPlaying() {
        return isPlayingRequested;
    }

    /**
     * Play the audio
     */
    public synchronized void play() {
        isPlayingRequested = true;
        playImpl();
    }

    /**
     * Pause the audio
     */
    public synchronized void pause() {
        isPlayingRequested = false;
        pauseImpl();
    }

    /**
     * Implementation of start (decoupled from request for start)
     */
    private void playImpl() {
        audioView.getVolumeControl().setValue(0);
        audioView.play();
        fadeIn();
    }

    /**
     * Implementation of pause (decoupled from request for pause)
     */
    private void pauseImpl() {
        fadeOut();
        audioView.pause();
    }

    /**
     * fade the volume from zero to 1
     */
    private void fadeIn() {
        fade(0, 1, audioView.getVolumeControl());
    }

    /**
     * Fade the volume from 1 to zero
     */
    private void fadeOut() {
        fade(1, 0, audioView.getVolumeControl());
    }

    /**
     * Transition the given double container's value from the given start
     * value to the given end value
     * @param startValue value at the start of the fade (will be set directly)
     * @param finalValue value at the end of the fade
     * @param toFade value to transition from start
     */
    private void fade(float startValue, float finalValue, HasFloat toFade) {

        long startTime = System.currentTimeMillis();
        long endTime = startTime + fadeTimeMillis;
        long currentTime = startTime;

        float fadeScale = (finalValue - startValue);
        float timeScale = fadeTimeMillis;

        do {
            toFade.setValue(startValue
                    + ((float)(currentTime - startTime))
                    / timeScale * fadeScale);

            try {
                Thread.sleep(fadeSleepMillis);
            }
            catch(InterruptedException ex) {
                throw new RuntimeException(ex);
            }

        } while ((currentTime = System.currentTimeMillis()) < endTime);

        toFade.setValue(finalValue);
    }

    /**
     * If there is an interruption to the audio, then pause
     */
    @Override
    public void onInterruptStart() {
        if (isPlayingRequested) {
            pauseImpl();
        }
    }

    /**
     * If the interrupt finishes resume playing
     */
    @Override
    public void onInterruptFinish() {
        if (isPlayingRequested) {
            playImpl();
        }
    }
}
