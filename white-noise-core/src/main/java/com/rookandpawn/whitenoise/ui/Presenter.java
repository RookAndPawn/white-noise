package com.rookandpawn.whitenoise.ui;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.rookandpawn.whitenoise.audio.AudioPresenter;
import com.rookandpawn.whitenoise.event.PlayPauseEvent;

/**
 * Presenter for the user interface for the white noise app (such as it is)
 * @author kguthrie
 */
public class Presenter {

    private static final PlayPauseEvent togglePlayPauseEvent
            = new PlayPauseEvent(PlayPauseEvent.Type.toggle);

    private final View view;
    private final AudioPresenter audioPresenter;
    private final EventBus eventBus;

    public Presenter(View view
            , AudioPresenter audioPresenter
            , EventBus eventBus) {
        this.view = view;
        this.audioPresenter = audioPresenter;
        this.eventBus = eventBus;
    }

    /**
     * Bind the view to the presenter; this presents leaking this
     */
    public void bind() {
        view.getPlayButton().addHandler(() -> {
            eventBus.post(new PlayPauseEvent(PlayPauseEvent.Type.play));
        });

        view.getPauseButton().addHandler(() -> {
            eventBus.post(new PlayPauseEvent(PlayPauseEvent.Type.pause));
        });

        view.getPlayPauseButton().addHandler(() -> {
            eventBus.post(togglePlayPauseEvent);
        });

        eventBus.register(this);
        view.getPlayPauseButtonText().setText("Play");
    }

    /**
     * Handle the toggle play-pause event
     */
    @Subscribe
    synchronized void onPlayPauseEvent(PlayPauseEvent event) {

        switch(event.getType()) {
            case toggle: {
                if (audioPresenter.isPlaying()) {
                    pause();
                }
                else {
                    play();
                }
                break;
            }
            case play: {
                play();
                break;
            }
            case pause: {
                pause();
                break;
            }

        }
    }

    /**
     * Perform all the actions associated with playing audio
     */
    private void play() {
        view.getPlayPauseButtonText().setText("Pause");
        audioPresenter.play();
        view.setNowPlayingText("White Noise", "White Noise");
    }

    /**
     * perform all the actions associated with pausing the audio
     */
    private void pause() {
        view.getPlayPauseButtonText().setText("Play");
        audioPresenter.pause();
    }

}
