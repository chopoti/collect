package org.fsr.collect.android.widgets.utilities;

import org.fsr.collect.audioclips.Clip;

import java.util.function.Consumer;

public interface AudioPlayer {

    void play(Clip clip);

    void pause();

    void setPosition(String clipId, Integer position);

    void onPlayingChanged(String clipID, Consumer<Boolean> playingConsumer);

    void onPositionChanged(String clipID, Consumer<Integer> positionConsumer);

    void stop();
}
