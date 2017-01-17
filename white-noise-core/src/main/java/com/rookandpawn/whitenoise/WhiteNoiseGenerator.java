package com.rookandpawn.whitenoise;

import com.rookandpawn.whitenoise.audio.HasBuffer;
import java.util.Random;

/**
 *
 * @author kguthrie
 */
public class WhiteNoiseGenerator extends AudioGenerator {

    private static final float amplitude = 0.08F;

    private final Random rand = new Random();

    public WhiteNoiseGenerator(double sampleRate) {
        super(sampleRate, 1);
    }

    @Override
    public void fillBuffer(int channelNumber, HasBuffer buffer) {
        if (channelNumber != 0) {
            return;
        }

        for (int i = 0; i < buffer.getBuffer().length; i++) {
            buffer.getBuffer()[i] = ((2F * rand.nextFloat()) - 1F) * amplitude;
        }
    }

}
