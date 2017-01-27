package com.rookandpawn.whitenoise;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.rookandpawn.whitenoise.audio.AudioPresenter;
import com.rookandpawn.whitenoise.ui.HasClickHandlers;
import com.rookandpawn.whitenoise.ui.HasText;
import com.rookandpawn.whitenoise.ui.Presenter;

import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity
        implements com.rookandpawn.whitenoise.ui.View {

    private Button playPauseButton;
    private HasClickHandlers playButtonExt;
    private HasClickHandlers pauseButtonExt;
    private HasClickHandlers playPauseButtonExt;
    private HasText playPauseButtonText;

    private EventBus eventBus;
    private Presenter presenter;
    private AudioPresenter audioPresenter;
    private AudioView audioView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playButtonExt = new HasClickHandlers();
        pauseButtonExt = new HasClickHandlers();
        playPauseButtonExt = new HasClickHandlers();

        playPauseButton = (Button)findViewById(R.id.btnPlayPause);
        playPauseButton.setOnClickListener((view) -> playPauseButtonExt.click());
        playPauseButtonText = new HasText() {
            @Override
            public String getText() {
                return MainActivity.this.playPauseButton.getText().toString();
            }

            @Override
            public void setText(String s) {
                runOnUiThread(() -> MainActivity.this.playPauseButton.setText(s));
            }
        };

        audioView = new AudioView();
        audioPresenter = new AudioPresenter(audioView);

        audioPresenter.bind();

        eventBus = new AsyncEventBus(Executors.newSingleThreadExecutor());
        presenter = new Presenter(this, audioPresenter, eventBus);

        presenter.bind();

    }

    @Override
    public HasClickHandlers getPlayButton() {
        return playButtonExt;
    }

    @Override
    public HasClickHandlers getPauseButton() {
        return pauseButtonExt;
    }

    @Override
    public HasClickHandlers getPlayPauseButton() {
        return playPauseButtonExt;
    }

    @Override
    public HasText getPlayPauseButtonText() {
        return playPauseButtonText;
    }

    @Override
    public void setNowPlayingText(String s, String s1) {

    }

}
