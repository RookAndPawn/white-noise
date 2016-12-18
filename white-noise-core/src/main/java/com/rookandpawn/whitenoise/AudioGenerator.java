package com.rookandpawn.whitenoise;

/**
 *
 * @author kguthrie
 */
public abstract class AudioGenerator {

    private final double sampleRate;

    public AudioGenerator(double sampleRate) {
        this.sampleRate = sampleRate;
    }

    public final double getSampleRate() {
        return sampleRate;
    }

    public abstract float getNextSample();

}
