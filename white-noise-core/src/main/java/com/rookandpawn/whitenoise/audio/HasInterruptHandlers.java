package com.rookandpawn.whitenoise.audio;

import com.google.common.collect.Sets;
import com.rookandpawn.whitenoise.HandlerRegistration;
import java.util.Set;

/**
 *
 * @author kguthrie
 */
public class HasInterruptHandlers {

    private final Set<InterruptHandler> handlers
            = Sets.newHashSet();

    public HandlerRegistration addHandler(InterruptHandler handler) {
        handlers.add(handler);

        return () -> handlers.remove(handler);
    }

    /**
     * This method should be called when an interruption starts
     */
    public void interruptStart() {
        for (InterruptHandler handler : handlers) {
            handler.onInterruptStart();
        }
    }

    /**
     * This method should be called when the interruption stops
     */
    public void interruptFinish() {
        for (InterruptHandler handler : handlers) {
            handler.onInterruptFinish();
        }
    }

}
