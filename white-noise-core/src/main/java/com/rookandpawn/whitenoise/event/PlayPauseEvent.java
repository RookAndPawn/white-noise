package com.rookandpawn.whitenoise.event;

/**
 *
 * @author kguthrie
 */
public class PlayPauseEvent {

    public static enum Type {
        play
        , pause
        , toggle
    }

    private final Type type;

    public PlayPauseEvent(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }
    
}
