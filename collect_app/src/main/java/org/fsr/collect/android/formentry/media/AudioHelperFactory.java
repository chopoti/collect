package org.fsr.collect.android.formentry.media;

import android.content.Context;

import org.fsr.collect.android.audio.AudioHelper;

public interface AudioHelperFactory {

    AudioHelper create(Context context);
}
