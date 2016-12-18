package com.rookandpawn.whitenoise;

import java.util.Random;

/**
 *
 * @author kguthrie
 */
public class WhiteNoiseGenerator extends AudioGenerator {

    private final Random rand = new Random();

    public WhiteNoiseGenerator(double sampleRate) {
        super(sampleRate);
    }

    @Override
    public float getNextSample() {

        float amplitude = 0.08F;

        float result = ((2F * rand.nextFloat()) - 1F) * amplitude;

        return result;
    }
}
