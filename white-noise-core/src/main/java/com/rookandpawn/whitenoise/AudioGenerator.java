package com.rookandpawn.whitenoise;

import com.rookandpawn.whitenoise.audio.HasBuffer;

/**
 *
 * @author kguthrie
 */
public abstract class AudioGenerator {

    private final double sampleRate;
    private final int numberOfChannels;

    public AudioGenerator(double sampleRate, int numberOfChannels) {
        this.sampleRate = sampleRate;
        this.numberOfChannels = numberOfChannels;
    }

    public int getNumberofChannels() {
        return numberOfChannels;
    }

    public final double getSampleRate() {
        return sampleRate;
    }

    public abstract void fillBuffer(int channelNumber, HasBuffer buffer);

}
