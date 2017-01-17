package com.rookandpawn.whitenoise.ui;

import com.google.common.collect.Sets;
import com.rookandpawn.whitenoise.HandlerRegistration;
import java.util.Set;

/**
 * Interface for an object that can send a click event
 * @author kguthrie
 */
public class HasClickHandlers {

    private final Set<ClickHandler> handlers;

    public HasClickHandlers() {
        this.handlers = Sets.newHashSet();
    }

    public HandlerRegistration addHandler(ClickHandler handler) {
        handlers.add(handler);
        return () -> handlers.remove(handler);
    }

    /**
     * Trigger the touch up inside event
     */
    public void click() {
        for (ClickHandler handler : handlers) {
            handler.onClick();
        }
    }

}
