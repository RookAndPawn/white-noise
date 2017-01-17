package com.rookandpawn.whitenoise.audio;

import com.rookandpawn.whitenoise.AudioGenerator;

/**
 * method for interfacing into a system's audio system indirectly
 * @author kguthrie
 */
public interface AudioView {

    void setGenerator(AudioGenerator generator);

    void play();

    void pause();

    boolean isPlaying();

    HasFloat getVolumeControl();

    HasInterruptHandlers getInterrupter();

}
