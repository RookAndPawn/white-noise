package com.rookandpawn.whitenoise.ui;

/**
 * Description of the methods needed to interact with the White Noise app
 * @author kguthrie
 */
public interface View {

    HasClickHandlers getPlayButton();
    HasClickHandlers getPauseButton();
    HasClickHandlers getPlayPauseButton();

    HasText getPlayPauseButtonText();

    void setNowPlayingText(String mainText, String subText);

}
